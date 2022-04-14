package pck.dbms.be.cart;

import pck.dbms.be.customer.Customer;
import pck.dbms.be.partner.Partner;

import java.util.ArrayList;

public class Cart {
    public Partner partner;
    public Customer customer;

    public ArrayList<CartDetail> cartDetails;
    public float shippingFee, total;

    public Cart(){
        partner = new Partner();
        customer = new Customer();
        cartDetails = new ArrayList<>();
    }
}
