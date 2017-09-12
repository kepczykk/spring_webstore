package com.webstore.controller;

import com.webstore.domain.Product;
import com.webstore.exception.NoProductsFoundUnderCategoryException;
import com.webstore.exception.ProductNotFoundException;
import com.webstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @RequestMapping("/all")
    public String allProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @RequestMapping("/{category}")
    public String getProductsByCategory(Model model, @PathVariable("category") String category) {
        List<Product> products = productService.getProductsByCategory(category);
        if (products == null || products.isEmpty()) {
            throw new NoProductsFoundUnderCategoryException();
        }
        model.addAttribute("products", products);
        return "products";
    }

    @RequestMapping("/filter/{ByCriteria}")
    public String getProductsByFilter(@MatrixVariable(pathVar="ByCriteria") Map<String,List<String>> filterParams, Model model) {
        model.addAttribute("products", productService.getProductsByFilter(filterParams));
        return "products";
    }

    @RequestMapping("/product")
    public String getProductById(@RequestParam("id") String productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        return "product";
    }

    @RequestMapping("/{category}/{price}")
    public String filterProducts(Model model, @PathVariable("category") String category,
                                 @MatrixVariable(pathVar="price") Map<String, List<String>> filterParams,
                                 @RequestParam("manufacturer") String manufacturer){
        Set<Product> products = new HashSet<>();
        products.addAll(productService.getProductsByManufacturer(manufacturer));
        products.retainAll(productService.getProductsByCategory(category));
        products.retainAll(productService.getProductsByPriceFilter(filterParams));
        model.addAttribute("products", products);
        return "products";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAddNewProductForm(Model model) {
        Product newProduct = new Product();
        model.addAttribute("newProduct", newProduct);
        return "addProduct";
    }

//    @RequestMapping(value = "/add", method = RequestMethod.GET)
//    public String getAddNewProductForm(@ModelAttribute("newProduct") Product newProduct) {
//        return "addProduct";
//    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddNewProductForm(@ModelAttribute("newProduct") Product newProduct,
                                           BindingResult result, HttpServletRequest request) {
        String[] suppressedFields = result.getSuppressedFields();
        if (suppressedFields.length > 0) {
            throw new RuntimeException("Próba wiązania niedozwolonych pól: " +
                    StringUtils.arrayToCommaDelimitedString(suppressedFields));
        }
        MultipartFile productImage = newProduct.getProductImage();
        MultipartFile productManual = newProduct.getProductManual();
        String rootDirectory = request.getSession().getServletContext().getRealPath("/");
        if (productImage!=null && !productImage.isEmpty()) {
            try {
                productImage.transferTo(new File(rootDirectory+"resources\\images\\"+ newProduct.getProductId() + ".png"));
            } catch (Exception e) {
                throw new RuntimeException("Niepowodzenie podczas próby zapisu obrazka produktu", e);
            }
        }
        if (productManual!=null && !productManual.isEmpty()) {
            try {
                productManual.transferTo(new File(rootDirectory+"resources\\pdf\\"+ newProduct.getProductId() + ".pdf"));
            } catch (Exception e) {
                throw new RuntimeException("Niepowodzenie podczas próby zapisu instrukcji produktu", e);
            }
        }
        productService.addProduct(newProduct);
        return "redirect:/products";
    }

    @InitBinder
    public void initialiseBinder(WebDataBinder binder) {
//        DateFormat dateFormat = new SimpleDateFormat("MMM d, YYYY");
//        CustomDateEditor orderDateEditor = new CustomDateEditor(dateFormat, true);
//        binder.registerCustomEditor(Date.class, orderDateEditor);
        binder.setDisallowedFields("unitsInOrder", "discontinued");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ModelAndView handleError(HttpServletRequest req, ProductNotFoundException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("invalidProductId", exception.getProductId());
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL()+"?"+req.getQueryString());
        mav.setViewName("productNotFound");
        return mav;
    }
}
