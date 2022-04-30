package pck.dbms.be.order;

import pck.dbms.be.cart.Cart;
import pck.dbms.be.cart.CartDetail;
import pck.dbms.be.customer.Customer;
import pck.dbms.be.partner.Partner;

import java.util.ArrayList;
import java.util.Random;

public class Order {
    private String orderID;
    private Partner partner;
    private Customer customer;
    private String paymentMethod, deliveryStatus, paidStatus;
    private double shippingFee, total;

    // list product
    private ArrayList<OrderDetail> orderDetails;

    public Order() {
        partner = new Partner();
        customer = new Customer();

        orderDetails = new ArrayList<>();

    }

    public Order(Cart cart, String paymentMethod) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        orderID = generatedString;
        partner = cart.partner;
        customer = cart.customer;
        this.paymentMethod = paymentMethod;

        orderDetails = new ArrayList<>();
        for (CartDetail cartDetail : cart.cartDetails){
            orderDetails.add(new OrderDetail(cartDetail, this));
        }
    }

    public Order(String orderID, Partner partner, Customer customer, String paymentMethod, String deliveryStatus, String paidStatus, double shippingFee, double total) {
        this.orderID = orderID;
        this.partner = partner;
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.deliveryStatus = deliveryStatus;
        this.paidStatus = paidStatus;
        this.shippingFee = shippingFee;
        this.total = total;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Partner getPartner() {

        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCustomerName(){return customer.getName();};

    public String getCustomerAddress(){
        return customer.getAddress().getAddressLine() + "\n"
                + customer.getAddress().getWard().getFullName() + "\n"
                + customer.getAddress().getDistrict().getFullName() + "\n"
                + customer.getAddress().getProvince().getFullName() + "\n";
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void addOrderDetailToOrder(OrderDetail orderDetail) {
        if (orderDetails == null) {
            orderDetails = new ArrayList<>();
        }

        orderDetails.add(orderDetail);
    }
}
