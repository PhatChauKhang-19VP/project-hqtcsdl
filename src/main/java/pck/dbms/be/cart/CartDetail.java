package pck.dbms.be.cart;

import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.product.Product;

public class CartDetail {
    public Cart cart;

    public Product product;
    public PartnerBranch partnerBranch;
    public int quantity;
    public float subTotal;

    public CartDetail() {
        product = new Product();
        partnerBranch = new PartnerBranch();
        quantity = 0;
        subTotal = 0;
    }
}
