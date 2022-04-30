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

            System.out.println(query);

            return query;
        }

        public static String updateDeliveryOrder(Order order) {
            String query = "exec dbo.usp_partner_or_driver_update_delivery_status ";

            query += "'" + order.getOrderID() + "', ";
            query += "'" + order.getDeliveryStatus() + "'";

            return query;
        }

        public static String partnerGetOrderListDirtyRead1Error(Partner partner) {
            String query = "declare @partner_username varchar(50) = '" + partner.getLogin().getUsername() + "'\n";

            query += """
                    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
                    begin try
                        begin tran
                        select o.*, p.username, c.name, c.address_line, w.full_name, dt.full_name, pv.full_name, w.code as w_code, dt.code as dt_code, pv.code as pv_code
                            from dbo.ORDERS as o
                                join dbo.PARTNERS as p on p.username = o.partner_username
                                join dbo.CUSTOMERS as c on o.customer_username = c.username
                                join dbo.PROVINCES as pv on c.address_province_code = pv.code
                                join dbo.DISTRICTS as dt on c.address_district_code = dt.code
                                join dbo.WARDS as w on c.address_ward_code = w.code
                            where p.username = @partner_username
                        commit tran
                    end try
                    begin catch
                        declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                    select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                    raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                    if (@@TRANCOUNT > 0)
                        rollback tran
                    end catch""";

            System.out.println("[partner]\n" + query);
            return query;
        }

        public static String partnerGetOrderListDirtyRead1Fixed(Partner partner) {
            String query = "declare @partner_username varchar(50) = '" + partner.getLogin().getUsername() + "'\n";

            query += """
                    begin try
                        begin tran
                        select o.*, p.username, c.name, c.address_line, w.full_name, dt.full_name, pv.full_name, w.code as w_code, dt.code as dt_code, pv.code as pv_code
                            from dbo.ORDERS as o
                                join dbo.PARTNERS as p on p.username = o.partner_username
                                join dbo.CUSTOMERS as c on o.customer_username = c.username
                                join dbo.PROVINCES as pv on c.address_province_code = pv.code
                                join dbo.DISTRICTS as dt on c.address_district_code = dt.code
                                join dbo.WARDS as w on c.address_ward_code = w.code
                            where p.username = @partner_username
                        commit tran
                    end try
                    begin catch
                        declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                    select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                    raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                    if (@@TRANCOUNT > 0)
                        rollback tran
                    end catch""";

            System.out.println("[partner]\n" + query);
            return query;
        }

        public static String getContractDirtyRead2Error(String status, Partner p) {
            String query = "declare @contract_status varchar(20) = '" + status + "'\n";

            query += "declare @partner_username varchar(20) = '" + p.getLogin().getUsername() + "'\n";
            query += """
                    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
                    begin try
                    		begin tran
                    			if not @contract_status = '*'
                    			begin
                    				select * from dbo.CONTRACTS as c
                    				join dbo.PARTNERS as p on c.username = p.username
                    				where c.status=@contract_status AND c.username = @partner_username
                    			end
                    			else
                    				select * from dbo.CONTRACTS as c
                    				join dbo.PARTNERS as p on c.username = p.username
                    				where c.username = @partner_username
                    		commit tran
                    	end try
                    	begin catch
                    		declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                    		select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                    		raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                    		if (@@TRANCOUNT > 0)
                    			rollback tran
                    	end catch""";

            System.out.println(query);
            return query;
        }

        public static String getContractDirtyRead2Fixed(String status, Partner p) {
            String query = "declare @contract_status varchar(20) = '" + status + "'\n";

            query += "declare @partner_username varchar(20) = '" + p.getLogin().getUsername() + "'\n";
            query += """
                    begin try
                    		begin tran
                    			if not @contract_status = '*'
                    			begin
                    				select * from dbo.CONTRACTS as c
                    				join dbo.PARTNERS as p on c.username = p.username
                    				where c.status=@contract_status AND c.username = @partner_username
                    			end
                    			else
                    				select * from dbo.CONTRACTS as c
                    				join dbo.PARTNERS as p on c.username = p.username
                    				where c.username = @partner_username
                    		commit tran
                    	end try
                    	begin catch
                    		declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                    		select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                    		raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                    		if (@@TRANCOUNT > 0)
                    			rollback tran
                    	end catch""";
            System.out.println(query);
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

        public static String getCartDetails(Cart cart) {
            String query = "exec dbo.usp_customer_get_cart_details ";
            query += "'" + cart.partner.getLogin().getUsername() + "', ";
            query += "'" + cart.customer.getLogin().getUsername() + "'";

            return query;
        }

        public static String deleteCart(Cart cart) {
            String query = "exec dbo.usp_customer_delete_cart_details ";
            query += "'" + cart.partner.getLogin().getUsername() + "', ";
            query += "'" + cart.customer.getLogin().getUsername() + "'";

            return query;
        }

        private static String insertOrder(Order o) {
            String query = "insert into dbo.ORDERS(order_id, partner_username, customer_username, payment_method, delivery_status, paid_status, shipping_fee) values(";

            query += "'" + o.getOrderID() + "', ";
            query += "'" + o.getPartner().getLogin().getUsername() + "', ";
            query += "'" + o.getCustomer().getLogin().getUsername() + "', ";
            query += "'" + o.getPaymentMethod() + "'";
            query += "'PENDING', 'UNPAID', 10)";

            return query;
        }

        private static String insertSingleOrderDetail(OrderDetail od) {
            String query = "\n";
            query += "set @PID = " + "'" + od.getProduct().getPID() + "'\n";
            query += "set @PBID = " + "'" + od.getPartnerBranch().getPBID() + "'\n";
            query += "set @quantity = " + od.getQuantity() + "\n";

            query += """
                    -- kiểm tra số lượng có > 0
                    if @quantity <= 0
                        throw 52000, --Error number must be between 50000 and  2147483647.
                            'Quantity <= 0 : NOT VALID', --Message
                            1; --State
                             
                    -- kiểm tra sản phầm có tồn tại trong branch/stock > 0 không
                    if not exists (select stock from dbo.PRODUCT_IN_BRANCHES as pib where pib.PID = @PID and pib.PBID = @PBID)\s
                        or (select stock from dbo.PRODUCT_IN_BRANCHES as pib where pib.PID = @PID and pib.PBID = @PBID) = 0
                             
                        throw 52000, --Error number must be between 50000 and  2147483647.
                            'Product not in branch or out of stock', --Message
                            1; --State
                             
                    -- thêm sản phẩm vào ORDER_DETAILS
                             
                    ---- Kiểm tra sản phẩm đã tồn tại hay chưa, nếu không tạo mới, nếu có, tăng thêm số lượng
                    if not exists (select * from dbo.ORDERS_DETAILS as od where od.order_id = @order_id and od.PID = @PID)
                        begin
                            insert into dbo.ORDERS_DETAILS (order_id, PID, PBID, quantity)
                                values (@order_id, @PID, @PBID,  @quantity)
                        end
                    else
                        begin
                            set @curr_quantity = (select quantity from dbo.ORDERS_DETAILS as od where od.order_id = @order_id and od.PID = @PID)
                            
                            update dbo.ORDERS_DETAILS
                                set\s
                                    quantity = @curr_quantity + @quantity
                                where order_id = @order_id and PID = @PID
                        end
                             
                    -- giảm số lượng sản phẩm trong chi nhánh đi `quantity`
                    set @curr_stock = (select pib.stock
                                                    from dbo.PRODUCT_IN_BRANCHES as pib
                                                    where pib.PID = @PID and pib.PBID = @PBID)
                    set @new_stock = @curr_stock - @quantity
                    update dbo.PRODUCT_IN_BRANCHES
                        set stock = @new_stock
                        where PID = @PID and PBID = @PBID
                    """;

            return query;
        }

        private static String insertOrderDetails(Order o) {
            StringBuilder query = new StringBuilder("\n");

            for (OrderDetail od : o.getOrderDetails()) {
                query.append(insertSingleOrderDetail(od));
            }

            return query.toString();
        }

        public static String createOrder(Order o) {
            String query = "";
            query += "declare @order_id varchar(20) = " + "'" + o.getOrderID() + "'\n";
            query += "declare @partner_username varchar(50) = " + "'" + o.getPartner().getLogin().getUsername() + "'\n";
            query += "declare @customer_username varchar(50) = " + "'" + o.getCustomer().getLogin().getUsername() + "'\n";
            query += "declare @payment_method varchar(20) = " + "'" + o.getPaymentMethod() + "'\n";
            query += "declare @PID varchar(20)\n";
            query += "declare @PBID varchar(20)\n";
            query += "declare @quantity int\n";
            query += "declare @curr_quantity int\n";
            query += "declare @curr_stock int\n";
            query += "declare @new_stock int\n";
            query +=
                    """
                            begin try
                            	begin tran
                            """;
            query +=
                    """
                            insert into dbo.ORDERS(order_id, partner_username, customer_username, payment_method, delivery_status, paid_status, shipping_fee)
                                values (@order_id, @partner_username, @customer_username, @payment_method, 'PENDING', 'UNPAID', 10)
                                \n
                            """;
            query += insertOrderDetails(o);

            query +=
                    """
                            commit tran
                            end try
                            begin catch
                                declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                            select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                            raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                            if (@@TRANCOUNT > 0)
                                rollback tran
                            end catch
                            """;

            System.out.println(query);

            return query;
        }

        public static String createOrderButFailDirtyRead1Error(Order o) {
            String query = "";
            query += "declare @order_id varchar(20) = " + "'" + o.getOrderID() + "'\n";
            query += "declare @partner_username varchar(50) = " + "'" + o.getPartner().getLogin().getUsername() + "'\n";
            query += "declare @customer_username varchar(50) = " + "'" + o.getCustomer().getLogin().getUsername() + "'\n";
            query += "declare @payment_method varchar(20) = " + "'" + o.getPaymentMethod() + "'\n";
            query += "declare @PID varchar(20)\n";
            query += "declare @PBID varchar(20)\n";
            query += "declare @quantity int\n";
            query += "declare @curr_quantity int\n";
            query += "declare @curr_stock int\n";
            query += "declare @new_stock int\n";
            query +=
                    """
                            SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
                                                        
                            begin try
                            	begin tran
                            """;
            query +=
                    """
                            insert into dbo.ORDERS(order_id, partner_username, customer_username, payment_method, delivery_status, paid_status, shipping_fee)
                                values (@order_id, @partner_username, @customer_username, @payment_method, 'PENDING', 'UNPAID', 10)
                                \n
                            """;
            query += insertOrderDetails(o);

            query +=
                    """
                            waitfor delay '00:00:05';
                            throw 52000, 'Some Error failed to create order !!!', 1
                            commit tran
                            end try
                            begin catch
                                declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                            select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                            raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                            if (@@TRANCOUNT > 0)
                                rollback tran
                            end catch
                            """;

            System.out.println(query);

            return query;
        }

        public static String createOrderButFailDirtRead1Fixed(Order o) {
            String query = "";
            query += "declare @order_id varchar(20) = " + "'" + o.getOrderID() + "'\n";
            query += "declare @partner_username varchar(50) = " + "'" + o.getPartner().getLogin().getUsername() + "'\n";
            query += "declare @customer_username varchar(50) = " + "'" + o.getCustomer().getLogin().getUsername() + "'\n";
            query += "declare @payment_method varchar(20) = " + "'" + o.getPaymentMethod() + "'\n";
            query += "declare @PID varchar(20)\n";
            query += "declare @PBID varchar(20)\n";
            query += "declare @quantity int\n";
            query += "declare @curr_quantity int\n";
            query += "declare @curr_stock int\n";
            query += "declare @new_stock int\n";
            query +=
                    """
                            begin try
                            	begin tran
                            """;
            query +=
                    """
                            insert into dbo.ORDERS(order_id, partner_username, customer_username, payment_method, delivery_status, paid_status, shipping_fee)
                                values (@order_id, @partner_username, @customer_username, @payment_method, 'PENDING', 'UNPAID', 10)
                                \n
                            """;
            query += insertOrderDetails(o);

            query +=
                    """
                            waitfor delay '00:00:05';
                            throw 52000, 'Some Error failed to create order !!!', 1
                            commit tran
                            end try
                            begin catch
                                declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                            select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                            raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                            if (@@TRANCOUNT > 0)
                                rollback tran
                            end catch
                            """;

            System.out.println(query);

            return query;
        }

        // -----------------------------

        private static String insertSingleOrderDetailLostUpdate1ErrorT1(OrderDetail od) {
            String query = "\n";
            query += "set @PID = " + "'" + od.getProduct().getPID() + "'\n";
            query += "set @PBID = " + "'" + od.getPartnerBranch().getPBID() + "'\n";
            query += "set @quantity = " + od.getQuantity() + "\n";

            query += """
                    -- kiểm tra số lượng có > 0
                    if @quantity <= 0
                        throw 52000, --Error number must be between 50000 and  2147483647.
                            'Quantity <= 0 : NOT VALID', --Message
                            1; --State
                             
                    -- kiểm tra sản phầm có tồn tại trong branch/stock > 0 không
                    if not exists (select stock from dbo.PRODUCT_IN_BRANCHES as pib where pib.PID = @PID and pib.PBID = @PBID)\s
                        or (select stock from dbo.PRODUCT_IN_BRANCHES as pib where pib.PID = @PID and pib.PBID = @PBID) = 0
                             
                        throw 52000, --Error number must be between 50000 and  2147483647.
                            'Product not in branch or out of stock', --Message
                            1; --State
                             
                    -- thêm sản phẩm vào ORDER_DETAILS
                             
                    ---- Kiểm tra sản phẩm đã tồn tại hay chưa, nếu không tạo mới, nếu có, tăng thêm số lượng
                    if not exists (select * from dbo.ORDERS_DETAILS as od where od.order_id = @order_id and od.PID = @PID)
                        begin
                            insert into dbo.ORDERS_DETAILS (order_id, PID, PBID, quantity)
                                values (@order_id, @PID, @PBID,  @quantity)
                        end
                    else
                        begin
                            set @curr_quantity = (select quantity from dbo.ORDERS_DETAILS as od where od.order_id = @order_id and od.PID = @PID)
                            
                            update dbo.ORDERS_DETAILS
                                set\s
                                    quantity = @curr_quantity + @quantity
                                where order_id = @order_id and PID = @PID
                        end
                             
                    -- giảm số lượng sản phẩm trong chi nhánh đi `quantity`
                    set @curr_stock = (select pib.stock
                                                    from dbo.PRODUCT_IN_BRANCHES as pib
                                                    where pib.PID = @PID and pib.PBID = @PBID)
                    
                    waitfor delay '00:00:05'
                    
                    set @new_stock = @curr_stock - @quantity
                    update dbo.PRODUCT_IN_BRANCHES
                        set stock = @new_stock
                        where PID = @PID and PBID = @PBID
                    """;

            return query;
        }

        private static String insertOrderDetailsLostUpdate1ErrorT1(Order o) {
            StringBuilder query = new StringBuilder("\n");

            for (OrderDetail od : o.getOrderDetails()) {
                query.append(insertSingleOrderDetailLostUpdate1ErrorT1(od));
            }

            return query.toString();
        }

        public static String createOrderLostUpdate1ErrorT1(Order o) {
            String query = "";
            query += "declare @order_id varchar(20) = " + "'" + o.getOrderID() + "'\n";
            query += "declare @partner_username varchar(50) = " + "'" + o.getPartner().getLogin().getUsername() + "'\n";
            query += "declare @customer_username varchar(50) = " + "'" + o.getCustomer().getLogin().getUsername() + "'\n";
            query += "declare @payment_method varchar(20) = " + "'" + o.getPaymentMethod() + "'\n";
            query += "declare @PID varchar(20)\n";
            query += "declare @PBID varchar(20)\n";
            query += "declare @quantity int\n";
            query += "declare @curr_quantity int\n";
            query += "declare @curr_stock int\n";
            query += "declare @new_stock int\n";
            query +=
                    """
                            begin try
                            	begin tran
                            """;
            query +=
                    """
                            insert into dbo.ORDERS(order_id, partner_username, customer_username, payment_method, delivery_status, paid_status, shipping_fee)
                                values (@order_id, @partner_username, @customer_username, @payment_method, 'PENDING', 'UNPAID', 10)
                                \n
                            """;
            query += insertOrderDetailsLostUpdate1ErrorT1(o);

            query +=
                    """
                            commit tran
                            end try
                            begin catch
                                declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                            select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                            raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                            if (@@TRANCOUNT > 0)
                                rollback tran
                            end catch
                            """;

            System.out.println(query);

            return query;
        }

        // - - - - - -

        private static String insertSingleOrderDetailLostUpdate1ErrorT2(OrderDetail od) {
            String query = "\n";
            query += "set @PID = " + "'" + od.getProduct().getPID() + "'\n";
            query += "set @PBID = " + "'" + od.getPartnerBranch().getPBID() + "'\n";
            query += "set @quantity = " + od.getQuantity() + "\n";

            query += """
                    -- kiểm tra số lượng có > 0
                    if @quantity <= 0
                        throw 52000, --Error number must be between 50000 and  2147483647.
                            'Quantity <= 0 : NOT VALID', --Message
                            1; --State
                             
                    -- kiểm tra sản phầm có tồn tại trong branch/stock > 0 không
                    if not exists (select stock from dbo.PRODUCT_IN_BRANCHES as pib where pib.PID = @PID and pib.PBID = @PBID)\s
                        or (select stock from dbo.PRODUCT_IN_BRANCHES as pib where pib.PID = @PID and pib.PBID = @PBID) = 0
                             
                        throw 52000, --Error number must be between 50000 and  2147483647.
                            'Product not in branch or out of stock', --Message
                            1; --State
                             
                    -- thêm sản phẩm vào ORDER_DETAILS
                             
                    ---- Kiểm tra sản phẩm đã tồn tại hay chưa, nếu không tạo mới, nếu có, tăng thêm số lượng
                    if not exists (select * from dbo.ORDERS_DETAILS as od where od.order_id = @order_id and od.PID = @PID)
                        begin
                            insert into dbo.ORDERS_DETAILS (order_id, PID, PBID, quantity)
                                values (@order_id, @PID, @PBID,  @quantity)
                        end
                    else
                        begin
                            set @curr_quantity = (select quantity from dbo.ORDERS_DETAILS as od where od.order_id = @order_id and od.PID = @PID)
                            
                            update dbo.ORDERS_DETAILS
                                set\s
                                    quantity = @curr_quantity + @quantity
                                where order_id = @order_id and PID = @PID
                        end
                             
                    -- giảm số lượng sản phẩm trong chi nhánh đi `quantity`
                    set @curr_stock = (select pib.stock
                                                    from dbo.PRODUCT_IN_BRANCHES as pib
                                                    where pib.PID = @PID and pib.PBID = @PBID)                                   
                    set @new_stock = @curr_stock - @quantity
                    update dbo.PRODUCT_IN_BRANCHES
                        set stock = @new_stock
                        where PID = @PID and PBID = @PBID
                    """;

            return query;
        }

        private static String insertOrderDetailsLostUpdate1ErrorT2(Order o) {
            StringBuilder query = new StringBuilder("\n");

            for (OrderDetail od : o.getOrderDetails()) {
                query.append(insertSingleOrderDetailLostUpdate1ErrorT2(od));
            }

            return query.toString();
        }

        public static String createOrderLostUpdate1ErrorT2(Order o) {
            String query = "";
            query += "declare @order_id varchar(20) = " + "'" + o.getOrderID() + "'\n";
            query += "declare @partner_username varchar(50) = " + "'" + o.getPartner().getLogin().getUsername() + "'\n";
            query += "declare @customer_username varchar(50) = " + "'" + o.getCustomer().getLogin().getUsername() + "'\n";
            query += "declare @payment_method varchar(20) = " + "'" + o.getPaymentMethod() + "'\n";
            query += "declare @PID varchar(20)\n";
            query += "declare @PBID varchar(20)\n";
            query += "declare @quantity int\n";
            query += "declare @curr_quantity int\n";
            query += "declare @curr_stock int\n";
            query += "declare @new_stock int\n";
            query +=
                    """
                            begin try
                            	begin tran
                            """;
            query +=
                    """
                            insert into dbo.ORDERS(order_id, partner_username, customer_username, payment_method, delivery_status, paid_status, shipping_fee)
                                values (@order_id, @partner_username, @customer_username, @payment_method, 'PENDING', 'UNPAID', 10)
                                \n
                            """;
            query += insertOrderDetailsLostUpdate1ErrorT2(o);

            query +=
                    """
                            commit tran
                            end try
                            begin catch
                                declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                            select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                            raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                            if (@@TRANCOUNT > 0)
                                rollback tran
                            end catch
                            """;

            System.out.println(query);

            return query;
        }

        // -----------------------------

        private static String insertSingleOrderDetailLostUpdate1FixedT1(OrderDetail od) {
            String query = "\n";
            query += "set @PID = " + "'" + od.getProduct().getPID() + "'\n";
            query += "set @PBID = " + "'" + od.getPartnerBranch().getPBID() + "'\n";
            query += "set @quantity = " + od.getQuantity() + "\n";

            query += """
                    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
                    
                    -- kiểm tra số lượng có > 0
                    if @quantity <= 0
                        throw 52000, --Error number must be between 50000 and  2147483647.
                            'Quantity <= 0 : NOT VALID', --Message
                            1; --State
                             
                    -- kiểm tra sản phầm có tồn tại trong branch/stock > 0 không
                    if not exists (select stock from dbo.PRODUCT_IN_BRANCHES as pib where pib.PID = @PID and pib.PBID = @PBID)\s
                        or (select stock from dbo.PRODUCT_IN_BRANCHES as pib where pib.PID = @PID and pib.PBID = @PBID) = 0
                             
                        throw 52000, --Error number must be between 50000 and  2147483647.
                            'Product not in branch or out of stock', --Message
                            1; --State
                             
                    -- thêm sản phẩm vào ORDER_DETAILS
                             
                    ---- Kiểm tra sản phẩm đã tồn tại hay chưa, nếu không tạo mới, nếu có, tăng thêm số lượng
                    if not exists (select * from dbo.ORDERS_DETAILS as od where od.order_id = @order_id and od.PID = @PID)
                        begin
                            insert into dbo.ORDERS_DETAILS (order_id, PID, PBID, quantity)
                                values (@order_id, @PID, @PBID,  @quantity)
                        end
                    else
                        begin
                            set @curr_quantity = (select quantity from dbo.ORDERS_DETAILS as od where od.order_id = @order_id and od.PID = @PID)
                            
                            update dbo.ORDERS_DETAILS
                                set\s
                                    quantity = @curr_quantity + @quantity
                                where order_id = @order_id and PID = @PID
                        end
                             
                    -- giảm số lượng sản phẩm trong chi nhánh đi `quantity`
                    set @curr_stock = (select pib.stock
                                                    from dbo.PRODUCT_IN_BRANCHES as pib
                                                    where pib.PID = @PID and pib.PBID = @PBID)
                                        
                    waitfor delay '00:00:05'
                                        
                    set @new_stock = @curr_stock - @quantity
                    update dbo.PRODUCT_IN_BRANCHES
                        set stock = @new_stock
                        where PID = @PID and PBID = @PBID
                    """;

            return query;
        }

        private static String insertOrderDetailsLostUpdate1FixedT1(Order o) {
            StringBuilder query = new StringBuilder("\n");

            for (OrderDetail od : o.getOrderDetails()) {
                query.append(insertSingleOrderDetailLostUpdate1FixedT1(od));
            }

            return query.toString();
        }

        public static String createOrderLostUpdate1FixedT1(Order o) {
            String query = "";
            query += "declare @order_id varchar(20) = " + "'" + o.getOrderID() + "'\n";
            query += "declare @partner_username varchar(50) = " + "'" + o.getPartner().getLogin().getUsername() + "'\n";
            query += "declare @customer_username varchar(50) = " + "'" + o.getCustomer().getLogin().getUsername() + "'\n";
            query += "declare @payment_method varchar(20) = " + "'" + o.getPaymentMethod() + "'\n";
            query += "declare @PID varchar(20)\n";
            query += "declare @PBID varchar(20)\n";
            query += "declare @quantity int\n";
            query += "declare @curr_quantity int\n";
            query += "declare @curr_stock int\n";
            query += "declare @new_stock int\n";
            query +=
                    """
                            begin try
                            	begin tran
                            """;
            query +=
                    """
                            insert into dbo.ORDERS(order_id, partner_username, customer_username, payment_method, delivery_status, paid_status, shipping_fee)
                                values (@order_id, @partner_username, @customer_username, @payment_method, 'PENDING', 'UNPAID', 10)
                                \n
                            """;
            query += insertOrderDetailsLostUpdate1FixedT1(o);

            query +=
                    """
                            commit tran
                            end try
                            begin catch
                                declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                            select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                            raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                            if (@@TRANCOUNT > 0)
                                rollback tran
                            end catch
                            """;

            System.out.println(query);

            return query;
        }

        // - - - - - -

        private static String insertSingleOrderDetailLostUpdate1FixedT2(OrderDetail od) {
            String query = "\n";
            query += "set @PID = " + "'" + od.getProduct().getPID() + "'\n";
            query += "set @PBID = " + "'" + od.getPartnerBranch().getPBID() + "'\n";
            query += "set @quantity = " + od.getQuantity() + "\n";

            query += """
                    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
                    
                    -- kiểm tra số lượng có > 0
                    if @quantity <= 0
                        throw 52000, --Error number must be between 50000 and  2147483647.
                            'Quantity <= 0 : NOT VALID', --Message
                            1; --State
                             
                    -- kiểm tra sản phầm có tồn tại trong branch/stock > 0 không
                    if not exists (select stock from dbo.PRODUCT_IN_BRANCHES as pib where pib.PID = @PID and pib.PBID = @PBID)\s
                        or (select stock from dbo.PRODUCT_IN_BRANCHES as pib where pib.PID = @PID and pib.PBID = @PBID) = 0
                             
                        throw 52000, --Error number must be between 50000 and  2147483647.
                            'Product not in branch or out of stock', --Message
                            1; --State
                             
                    -- thêm sản phẩm vào ORDER_DETAILS
                             
                    ---- Kiểm tra sản phẩm đã tồn tại hay chưa, nếu không tạo mới, nếu có, tăng thêm số lượng
                    if not exists (select * from dbo.ORDERS_DETAILS as od where od.order_id = @order_id and od.PID = @PID)
                        begin
                            insert into dbo.ORDERS_DETAILS (order_id, PID, PBID, quantity)
                                values (@order_id, @PID, @PBID,  @quantity)
                        end
                    else
                        begin
                            set @curr_quantity = (select quantity from dbo.ORDERS_DETAILS as od where od.order_id = @order_id and od.PID = @PID)
                            
                            update dbo.ORDERS_DETAILS
                                set\s
                                    quantity = @curr_quantity + @quantity
                                where order_id = @order_id and PID = @PID
                        end
                             
                    -- giảm số lượng sản phẩm trong chi nhánh đi `quantity`
                    set @curr_stock = (select pib.stock
                                                    from dbo.PRODUCT_IN_BRANCHES as pib
                                                    where pib.PID = @PID and pib.PBID = @PBID)
                                        
                    set @new_stock = @curr_stock - @quantity
                    update dbo.PRODUCT_IN_BRANCHES
                        set stock = @new_stock
                        where PID = @PID and PBID = @PBID
                    """;

            return query;
        }

        private static String insertOrderDetailsLostUpdate1FixedT2(Order o) {
            StringBuilder query = new StringBuilder("\n");

            for (OrderDetail od : o.getOrderDetails()) {
                query.append(insertSingleOrderDetailLostUpdate1FixedT2(od));
            }

            return query.toString();
        }

        public static String createOrderLostUpdate1FixedT2(Order o) {
            String query = "";
            query += "declare @order_id varchar(20) = " + "'" + o.getOrderID() + "'\n";
            query += "declare @partner_username varchar(50) = " + "'" + o.getPartner().getLogin().getUsername() + "'\n";
            query += "declare @customer_username varchar(50) = " + "'" + o.getCustomer().getLogin().getUsername() + "'\n";
            query += "declare @payment_method varchar(20) = " + "'" + o.getPaymentMethod() + "'\n";
            query += "declare @PID varchar(20)\n";
            query += "declare @PBID varchar(20)\n";
            query += "declare @quantity int\n";
            query += "declare @curr_quantity int\n";
            query += "declare @curr_stock int\n";
            query += "declare @new_stock int\n";
            query +=
                    """
                            begin try
                            	begin tran
                            """;
            query +=
                    """
                            insert into dbo.ORDERS(order_id, partner_username, customer_username, payment_method, delivery_status, paid_status, shipping_fee)
                                values (@order_id, @partner_username, @customer_username, @payment_method, 'PENDING', 'UNPAID', 10)
                                \n
                            """;
            query += insertOrderDetailsLostUpdate1FixedT2(o);

            query +=
                    """
                            commit tran
                            end try
                            begin catch
                                declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                            select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                            raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                            if (@@TRANCOUNT > 0)
                                rollback tran
                            end catch
                            """;

            System.out.println(query);

            return query;
        }

        // -----------------------------

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
        public static String acceptContractButFailDirtyRead2Error(Contract c) {
            String query = "declare @CID varchar(20) = '" + c.getCID() + "'\n";

            query += """
                    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
                                        
                    begin try
                        begin tran
                    update CONTRACTS
                        set status='ACCEPTED'
                        where CID = @CID
                        waitfor delay '00:00:05';
                        throw 52000, 'Accept failed !!!', 1            
                        commit tran
                    end try
                    begin catch
                        declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                    select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                    raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                    if (@@TRANCOUNT > 0)
                        rollback tran
                    end catch""";

            System.out.println(query);
            return query;

        }

        public static String acceptContractButFailDirtyRead2Fixed(Contract c) {
            String query = "declare @CID varchar(20) = '" + c.getCID() + "'\n";

            query += """
                    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
                                        
                    begin try
                        begin tran
                    update CONTRACTS
                        set status='ACCEPTED'
                        where CID = @CID
                        waitfor delay '00:00:05';
                        
                        throw 52000, 'Accept failed !!!', 1;               
                        commit tran
                    end try
                    begin catch
                        declare @ErrorMessage nvarchar(4000), @ErrorSeverity int, @ErrorState int;
                    select @ErrorMessage = ERROR_MESSAGE(), @ErrorSeverity = ERROR_SEVERITY(), @ErrorState = ERROR_STATE();
                    raiserror (@ErrorMessage, @ErrorSeverity, @ErrorState);
                    if (@@TRANCOUNT > 0)
                        rollback tran
                    end catch""";


            return query;

        }

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
