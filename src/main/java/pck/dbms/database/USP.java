package pck.dbms.database;

import pck.dbms.be.cart.Cart;
import pck.dbms.be.cart.CartDetail;
import pck.dbms.be.customer.Customer;
import pck.dbms.be.driver.Driver;
import pck.dbms.be.driver.DriverRegistration;
import pck.dbms.be.order.Order;
import pck.dbms.be.order.OrderDetail;
import pck.dbms.be.partner.Contract;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.product.Product;
import pck.dbms.be.user.LoginInfo;

public class USP {
    public static final String get_provinces = "exec dbo.usp_get_provinces", get_districts = "exec dbo.usp_get_districts", get_wards = "exec dbo.usp_get_wards";

    public static class partner {
        public static String registration(Partner partner) {
            String query = "exec dbo.usp_partner_registation ";
            LoginInfo loginInfo = partner.getLogin();

            query += "'" + loginInfo.getUsername() + "', ";
            query += "'" + loginInfo.getPassword() + "', ";
            query += "N'" + partner.getName() + "', ";
            query += "N'" + partner.getRepresentativeName() + "', ";
            query += "N'" + partner.getAddress().getProvince().getCode() + "', ";
            query += "N'" + partner.getAddress().getDistrict().getCode() + "', ";
            query += "N'" + partner.getAddress().getWard().getCode() + "', ";
            query += "N'" + partner.getAddress().getAddressLine() + "', ";
            query += partner.getBranchNumber() + ", ";
            query += partner.getOrderNumber() + ", ";
            query += "N'" + partner.getProductType() + "', ";
            query += "'" + partner.getPhone() + "', ";
            query += "'" + partner.getMail() + "'";

            System.out.println(query);
            return query;
        }

        public static String registerContract(Partner partner, Contract contract) {
            String query = "exec dbo.usp_partner_register_contract ";

            query += "'" + contract.getCID() + "', ";
            query += "'" + partner.getLogin().getUsername() + "', ";
            query += "'" + contract.getTIN() + "', ";
            query += contract.getContractTime() + ", ";
            query += contract.getCommission();
            return query;
        }

        public static String registerContractWithStartTime(Partner partner, Contract contract) {
            String query = "exec dbo.usp_partner_register_contract ";

            query += "'" + contract.getCID() + "', ";
            query += "'" + partner.getLogin().getUsername() + "', ";
            query += "'" + contract.getTIN() + "', ";
            query += "'" + contract.getCreatedAt() + "', ";
            query += contract.getContractTime() + ", ";
            query += contract.getCommission();

            return query;
        }

        public static String getAcceptedContracts(Partner partner) {
            String query = "exec dbo.usp_partner_get_accepted_contracts  ";

            query += "'" + partner.getLogin().getUsername() + "'";

            return query;
        }

        public static String getExpiredContracts(Partner partner) {
            String query = "exec dbo.usp_partner_get_expired_contracts  ";

            query += "'" + partner.getLogin().getUsername() + "'";

            return query;
        }

        public static String getBranches(Partner p) {
            String query = "exec dbo.usp_partner_get_branches ";

            query += "'" + p.getLogin().getUsername() + "'";

            return query;
        }

        public static String addBranch(PartnerBranch pb) {
            String query = "exec dbo.usp_partner_add_branch ";

            query += "'" + pb.getPBID() + "', ";
            query += "'" + pb.getPartner().getLogin().getUsername() + "', ";
            query += "N'" + pb.getName() + "', ";
            query += "N'" + pb.getAddress().getProvince().getCode() + "', ";
            query += "N'" + pb.getAddress().getDistrict().getCode() + "', ";
            query += "N'" + pb.getAddress().getWard().getCode() + "', ";
            query += "N'" + pb.getAddress().getAddressLine() + "'";

            return query;
        }

        public static String deleteBranch(PartnerBranch pb) {
            String query = "exec dbo.usp_partner_delete_branch ";

            query += "'" + pb.getPartner().getLogin().getUsername() + "', ";
            query += "'" + pb.getPBID() + "'";

            return query;
        }

        public static String getProducts(Partner p) {
            String query = "exec dbo.usp_partner_get_products ";

            query += "'" + p.getLogin().getUsername() + "'";

            return query;
        }

        public static String addProduct(Partner pn, Product pd) {
            String query = "exec dbo.usp_partner_add_product ";

            query += "'" + pd.getPID() + ", ";
            query += "N'" + pd.getProductType() + "', ";
            query += "'" + pn.getLogin().getUsername() + "', ";
            query += "N'" + pd.getImgSrc() + "', ";
            query += "N'" + pd.getName() + "'";

            return query;
        }

        public static String updateProduct(Partner pn, Product pd) {
            String query = "exec dbo.usp_partner_update_product ";

            query += "'" + pd.getPID() + ", ";
            query += "N'" + pd.getProductType() + "', ";
            query += "'" + pn.getLogin().getUsername() + "', ";
            query += "N'" + pd.getImgSrc() + "', ";
            query += "N'" + pd.getName() + "', ";
            query += "N'" + pd.getDescription() + "', ";
            query += pd.getPrice();

            return query;
        }

        public static String getProductsInBranches(Partner partner) {
            String query = "exec dbo.usp_partner_get_products_in_braches ";

            query += "'" + partner.getLogin().getUsername() + "'";

            return query;
        }

        public static String addProductToBranch(Product p, PartnerBranch pb, int stock) {
            String query = "dbo.usp_partner_add_product_to_branch ";

            query += "'" + pb.getPBID() + "', ";
            query += "'" + p.getPID() + "', ";
            query += stock;

            return query;
        }

        public static String deleteProductFromBranch(Product p, PartnerBranch pb) {
            String query = "exec dbo.usp_partner_delete_product_from_branch ";

            query += "'" + pb.getPBID() + "', ";
            query += "'" + p.getPID() + "'";

            return query;
        }

        public static String getOrders(Partner partner) {
            String query = "exec dbo.usp_partner_get_orders ";

            query += "'" + partner.getLogin().getUsername() + "'";

            return query;
        }

        public static String updateDeliveryOrder(Order order) {
            String query = "exec dbo.usp_partner_or_driver_update_delivery_status ";

            query += "'" + order.getOrderID() + "', ";
            query += "'" + order.getDeliveryStatus() + "'";

            return query;
        }
    }

    /**
     * driver class to return query to exec all driver's usp
     */
    public static class driver {
        public static String updateDeliveryOrder(Order order) {
            String query = "exec dbo.usp_partner_or_driver_update_delivery_status ";

            query += "'" + order.getOrderID() + "', ";
            query += "'" + order.getDeliveryStatus() + "'";

            return query;
        }

        public static String registration(DriverRegistration dr) {
            String query = "exec dbo.usp_driver_registration ";

            Driver d = dr.getDriver();

            query += "'" + d.getLogin().getUsername() + "', ";
            query += "'" + d.getLogin().getPassword() + "', ";
            query += "N'" + d.getName() + "', ";
            query += "'" + d.getNIN() + "', ";
            query += "N'" + d.getAddress().getProvince().getCode() + "', ";
            query += "N'" + d.getAddress().getDistrict().getCode() + "', ";
            query += "N'" + d.getAddress().getWard().getCode() + "', ";
            query += "N'" + d.getAddress().getAddressLine() + "', ";
            query += "'" + d.getMail() + "', ";
            query += "'" + d.getBank().getBID() + "', ";
            query += "N'" + d.getBank().getName() + "', ";
            query += "N'" + d.getBank().getBranch() + "', ";
            query += "'" + dr.getVIN() + "'";

            System.out.println(query);
            return query;
        }

        public static String getOrdersInActiveArea(Driver driver) {
            String query = "exec dbo.usp_driver_get_orders_in_active_area ";

            query += "'" + driver.getLogin().getUsername() + "'";

            return query;
        }

        public static String receiveOrder(Driver d, Order o) {
            String query = "exec dbo.usp_driver_receive_order ";

            query += "'" + d.getLogin().getUsername() + "', ";
            query += "'" + o.getOrderID() + "'";

            return query;
        }

        public static String getHistoryIncomes(Driver d) {
            String query = "exec dbo.usp_driver_history_incomes ";

            query += "'" + d.getLogin().getUsername() + "'";

            return query;
        }
    }

    public static class customer {

        public static String registration(Customer c) {
            String query = "exec dbo.usp_customer_registration ";

            LoginInfo loginInfo = c.getLogin();

            query += "'" + loginInfo.getUsername() + "', ";
            query += "'" + loginInfo.getPassword() + "', ";
            query += "N'" + c.getName() + "', ";
            query += "N'" + c.getAddress().getProvince().getCode() + "', ";
            query += "N'" + c.getAddress().getDistrict().getCode() + "', ";
            query += "N'" + c.getAddress().getWard().getCode() + "', ";
            query += "N'" + c.getAddress().getAddressLine() + "', ";
            query += "'" + c.getPhone() + "', ";
            query += "'" + c.getMail() + "'";

            return query;
        }

        public static String getPartner() {
            return "exec usp_customer_get_partner";
        }

        public static String getProducts(Partner partner) {

            return "exec usp_customer_get_products '" + partner.getLogin().getUsername() + "'";
        }

        public static String addProductToCart(CartDetail cd) {
            String query = "exec dbo.usp_customer_change_cart_detail ";

            query += "'" + cd.cart.partner.getLogin().getUsername() + "', ";
            query += "'" + cd.cart.customer.getLogin().getUsername() + "', ";
            query += "'" + cd.product.getPID() + "', ";
            query += "'" + cd.partnerBranch.getPBID() + "', ";
            query += cd.quantity;

            return query;
        }

        public static String getCartDetails(Cart cart){
            String query = "exec dbo.usp_customer_get_cart_details ";
            query += "'" + cart.partner.getLogin().getUsername() + "', ";
            query += "'" + cart.customer.getLogin().getUsername() + "'";

            return query;
        }

        public static String deleteCart(Cart cart){
            String query = "exec dbo.usp_customer_delete_cart_details ";
            query += "'" + cart.partner.getLogin().getUsername() + "', ";
            query += "'" + cart.customer.getLogin().getUsername() + "'";

            return query;
        }

        public static String createOrder(Order o) {
            String query = "exec dbo.usp_customer_create_order ";

            query += "'" + o.getOrderID() + "', ";
            query += "'" + o.getPartner().getLogin().getUsername() + "', ";
            query += "'" + o.getCustomer().getLogin().getUsername() + "', ";
            query += "'" + o.getPaymentMethod() + "'";

            return query;
        }

        public static String addProductToOrder(OrderDetail od) {
            String query = "exec dbo.usp_customer_add_product_to_order ";

            query += "'" + od.getOrder().getOrderID() + "', ";
            query += "'" + od.getProduct().getPID() + "', ";
            query += "'" + od.getPartnerBranch().getPBID() + "', ";
            query += od.getQuantity();

            return query;
        }

        public static String removeProductFromOrder(OrderDetail od) {
            String query = "exec dbo.usp_customer_remove_product_to_order ";

            query += "'" + od.getOrder().getOrderID() + "', ";
            query += "'" + od.getProduct().getPID() + "', ";
            query += "'" + od.getPartnerBranch() + "', ";
            query += od.getQuantity();

            return query;
        }

        public static String payOrder(Order o) {
            String query = "exec dbo.usp_customer_pay_order ";

            query += "'" + o.getOrderID() + "'";

            return query;
        }

        public static String getOrderDetails(Order o) {
            String query = "exec dbo.usp_user_get_order_details ";

            query += "'" + o.getOrderID() + "'";

            return query;
        }


    }

    public static class employee {
        public static String getContracts(String contractStatus) {
            String query = "exec dbo.usp_employee_get_contracts ";

            query += "'" + contractStatus + "'";

            return query;
        }

        public static String acceptAllContract() {
            return "{call dbo.usp_employee_accept_all_contracts(?)}";
        }
    }
}
