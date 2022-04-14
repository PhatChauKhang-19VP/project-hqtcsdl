package pck.dbms.be.order;

import pck.dbms.be.cart.CartDetail;
import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.product.Product;

public class OrderDetail {
    private Order order;
    private Product product;
    private PartnerBranch partnerBranch;
    private double pricePerProduct;
    private int quantity;
    private double subTotal;

    public OrderDetail() {
    }

    public OrderDetail(CartDetail cartDetail, Order order){
        this.order = order;
        this.product = cartDetail.product;
        this.partnerBranch = cartDetail.partnerBranch;
        this.pricePerProduct = cartDetail.product.getPrice();
        this.quantity = cartDetail.quantity;
        this.subTotal = cartDetail.subTotal;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PartnerBranch getPartnerBranch() {
        return partnerBranch;
    }

    public void setPartnerBranch(PartnerBranch partnerBranch) {
        this.partnerBranch = partnerBranch;
    }

    public double getPricePerProduct() {
        return pricePerProduct;
    }

    public void setPricePerProduct(double pricePerProduct) {
        this.pricePerProduct = pricePerProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
