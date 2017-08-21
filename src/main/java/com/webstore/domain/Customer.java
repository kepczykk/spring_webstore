package com.webstore.domain;

public class Customer {
    private String customerId;
    private String name;
    private String address;
    private int noOfOrdersMade;

    public Customer() {}

    public Customer(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNoOfOrdersMade() {
        return noOfOrdersMade;
    }

    public void setNoOfOrdersMade(int noOfOrdersMade) {
        this.noOfOrdersMade = noOfOrdersMade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (noOfOrdersMade != customer.noOfOrdersMade) return false;
        if (customerId != null ? !customerId.equals(customer.customerId) : customer.customerId != null) return false;
        if (name != null ? !name.equals(customer.name) : customer.name != null) return false;
        return address != null ? address.equals(customer.address) : customer.address == null;
    }

    @Override
    public int hashCode() {
        int result = customerId != null ? customerId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + noOfOrdersMade;
        return result;
    }
}
