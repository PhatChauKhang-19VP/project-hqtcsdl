package pck.dbms.be.order;

import pck.dbms.be.customer.Customer;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.partner.PartnerBranch;

import java.util.ArrayList;

public class Order {
    private String orderID;
    private Partner partner;
    private Customer customer;
    private String paymentMethod, deliveryStatus, paidStatus;
    private double shippingFee, total;

    // list product
    private ArrayList<OrderDetail> orderDetails;

    public Order() {
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
