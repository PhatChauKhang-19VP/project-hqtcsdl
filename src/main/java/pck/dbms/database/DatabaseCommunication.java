package pck.dbms.database;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import pck.dbms.be.administrativeDivision.*;
import pck.dbms.be.cart.Cart;
import pck.dbms.be.cart.CartDetail;
import pck.dbms.be.customer.Customer;
import pck.dbms.be.driver.Driver;
import pck.dbms.be.order.Order;
import pck.dbms.be.partner.Contract;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.product.Product;
import pck.dbms.be.user.LoginInfo;
import pck.dbms.be.utils.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseCommunication {
    private static DatabaseCommunication instance;
    private final String databaseName;
    private final HashMap<String, Pair<String, String>> sqlLogins;
    private Pair<String, String> currentSQLLogin;
    private Connection conn;

    private DatabaseCommunication() {
        sqlLogins = new HashMap<>();

        sqlLogins.put("SA", new Pair<>("sa", "Thoai1234"));
        sqlLogins.put("APP", new Pair<>("_app", "1"));
        sqlLogins.put("ADMIN", new Pair<>("_admin", "1"));
        sqlLogins.put("PARTNER", new Pair<>("_partner", "1"));
        sqlLogins.put("CUSTOMER", new Pair<>("_customer", "1"));
        sqlLogins.put("EMPLOYEE", new Pair<>("_employee", "1"));
        sqlLogins.put("DRIVER", new Pair<>("_driver", "1"));

        databaseName = "db_19vp_delivery";
        currentSQLLogin = sqlLogins.get("SA");
    }

    public static DatabaseCommunication getInstance() {
        if (instance == null) {
            instance = new DatabaseCommunication();
            getInstance().loadAdministrativeDivision();
        }

        return instance;
    }

    private void open() {
        try {
            SQLServerDataSource sqlDs = new SQLServerDataSource();

            sqlDs.setIntegratedSecurity(false);
            sqlDs.setEncrypt(false);

            sqlDs.setUser(currentSQLLogin.getFirst());
            sqlDs.setPassword(currentSQLLogin.getSecond());

            sqlDs.setServerName("localhost");
            sqlDs.setPortNumber(1433);
            sqlDs.setDatabaseName(databaseName);

            conn = sqlDs.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection.");
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        open();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> row;
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            resultList.add(row);
        }
        rs.close();
        stmt.close();
        return resultList;
    }

    public void execute(String sql) throws SQLException {
        open();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
        close();
    }

    public boolean loadAdministrativeDivision() {

        AdministrativeDivision ad = AdministrativeDivision.getInstance();
        try {
            open();
            PreparedStatement pstmt = conn.prepareStatement(USP.get_provinces);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Province province = new Province();

                province.setCode(rs.getString("code"));
                province.setName(rs.getString("name"));
                province.setFullName(rs.getString("full_name"));

                ad.getProvinceList().put(province.getCode(), province);
            }

            pstmt = conn.prepareStatement(USP.get_districts);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                District district = new District();

                district.setCode(rs.getString("code"));
                district.setName(rs.getString("name"));
                district.setFullName(rs.getString("full_name"));
                district.setBelong_to(ad.getProvinceList().get(rs.getString("province_code")));

                ad.getDistrictList().put(district.getCode(), district);
            }

            pstmt = conn.prepareStatement(USP.get_wards);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Ward ward = new Ward();

                ward.setCode(rs.getString("code"));
                ward.setName(rs.getString("name"));
                ward.setFullName(rs.getString("full_name"));
                ward.setBelong_to(ad.getDistrictList().get(rs.getString("district_code")));

                ad.getWardList().put(ward.getCode(), ward);
            }
            close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return false;
        }

    }

    public void callStoreProcedure() throws SQLException {
        open();

        CallableStatement cstmt = conn.prepareCall("{call dbo.usp_partner_get_accepted_contracts(?)}");
        cstmt.setString(1, "phatnm.partner1");
        cstmt.execute();
        ResultSet rs = cstmt.getResultSet();
        while (rs.next()) {
            System.out.println(rs.getString("CID"));
        }
        rs.close();
        close();
    }

    public String login(String username, String password) {
        try {
            open();
            CallableStatement callSt = conn.prepareCall("{?= call dbo.fn_app_log_in(?,?)}");
            callSt.setString(2, username);
            callSt.setString(3, password);
            //below method used to register data type of the out parameter
            callSt.registerOutParameter(1, Types.VARCHAR);
            callSt.execute();
            String role = callSt.getString(1);

            System.out.println("ROLE: " + role);

            close();
            return role;
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public void printResult(List<Map<String, Object>> rs) throws SQLException {
        // Prepare metadata object and get the number of columns.
        if (rs.size() == 0) {
            return;
        }

        for (String key : rs.get(0).keySet()) {
            System.out.print(key + " | ");
        }

        System.out.println();
        for (Map<String, Object> item : rs) {
            item.entrySet().forEach(entry -> {
                System.out.print(entry.getValue() + " | ");
            });
            System.out.println();
        }
    }

    public void testTransaction() {
        try {
            open();

            String query = """
                    declare @phat varchar(20) = 'Ngo Minh Phat'\n
                    begin try
                    	begin tran
                    		print 'ABC'
                                        
                    		if '1' = '1'
                    			throw 52000, @phat, 1
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
            Statement stmt = conn.createStatement();
            stmt.execute(query);

            close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static class partner {
        public static boolean partnerRegistration(LoginInfo loginInfo, Partner partner) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("PARTNER");

                getInstance().open();

                PreparedStatement pstmt = getInstance().conn.prepareStatement(USP.partner.registration(partner));
                pstmt.execute();
                getInstance().close();

                return true;
            } catch (SQLException e) {
                e.printStackTrace();

                getInstance().close();
                return false;
            }
        }

        public static boolean getOrders(Partner partner, HashMap<String, Order> orderHashMap) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("PARTNER");

                getInstance().open();

                Statement stmt = getInstance().conn.createStatement();
                ResultSet rs = stmt.executeQuery(USP.partner.getOrders(partner));

                while (rs.next()) {
                    Order o = new Order();

                    o.setOrderID(rs.getString("order_id"));

                    Customer c = new Customer();
                    c.setName(rs.getString("name"));
                    c.setAddress(new Address(AdministrativeDivision.getInstance().getProvinceList().get(rs.getString("pv_code")), AdministrativeDivision.getInstance().getDistrictList().get(rs.getString("dt_code")), AdministrativeDivision.getInstance().getWardList().get(rs.getString("w_code")), rs.getString("address_line")));
                    o.setCustomer(c);

                    o.setTotal(rs.getFloat("total"));
                    o.setDeliveryStatus(rs.getString("delivery_status"));
                    o.setPaidStatus(rs.getString("paid_status"));

                    orderHashMap.put(o.getOrderID(), o);
                }
                getInstance().close();

                return true;
            } catch (SQLException e) {
                e.printStackTrace();

                getInstance().close();
                return false;
            }
        }

        public static boolean getOrdersDirtyRead1Error(Partner partner, HashMap<String, Order> orderHashMap) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                Statement stmt = getInstance().conn.createStatement();
                ResultSet rs = stmt.executeQuery(USP.partner.partnerGetOrderListDirtyRead1Error(partner));

                while (rs.next()) {
                    Order o = new Order();

                    o.setOrderID(rs.getString("order_id"));

                    Customer c = new Customer();
                    c.setName(rs.getString("name"));
                    c.setAddress(new Address(AdministrativeDivision.getInstance().getProvinceList().get(rs.getString("pv_code")), AdministrativeDivision.getInstance().getDistrictList().get(rs.getString("dt_code")), AdministrativeDivision.getInstance().getWardList().get(rs.getString("w_code")), rs.getString("address_line")));
                    o.setCustomer(c);

                    o.setTotal(rs.getFloat("total"));
                    o.setDeliveryStatus(rs.getString("delivery_status"));
                    o.setPaidStatus(rs.getString("paid_status"));

                    orderHashMap.put(o.getOrderID(), o);
                }
                getInstance().close();

                System.out.println("demo1 trigger");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();

                getInstance().close();
                return false;
            }
        }

        public static boolean getOrdersDirtyRead1Fixed(Partner partner, HashMap<String, Order> orderHashMap) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                Statement stmt = getInstance().conn.createStatement();
                ResultSet rs = stmt.executeQuery(USP.partner.partnerGetOrderListDirtyRead1Fixed(partner));

                while (rs.next()) {
                    Order o = new Order();

                    o.setOrderID(rs.getString("order_id"));

                    Customer c = new Customer();
                    c.setName(rs.getString("name"));
                    c.setAddress(new Address(AdministrativeDivision.getInstance().getProvinceList().get(rs.getString("pv_code")), AdministrativeDivision.getInstance().getDistrictList().get(rs.getString("dt_code")), AdministrativeDivision.getInstance().getWardList().get(rs.getString("w_code")), rs.getString("address_line")));
                    o.setCustomer(c);

                    o.setTotal(rs.getFloat("total"));
                    o.setDeliveryStatus(rs.getString("delivery_status"));
                    o.setPaidStatus(rs.getString("paid_status"));

                    orderHashMap.put(o.getOrderID(), o);
                }
                getInstance().close();

                System.out.println("demo1 trigger");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();

                getInstance().close();
                return false;
            }
        }

        public static boolean getAcceptedContracts() {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("PARTNER");

                getInstance().open();

                PreparedStatement pstmt = getInstance().conn.prepareStatement("exec dbo.usp_partner_get_accepted_contracts 'phatnm.partner1'");
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    System.out.println(rs.getString("CID"));

                    Contract c = new Contract();
                    c.setCID(rs.getString("CID"));
                }
                getInstance().close();

                return true;
            } catch (SQLException e) {
                e.printStackTrace();

                getInstance().close();
                return false;
            }
        }

        public static boolean getContractsDirtyRead2Error(HashMap<String, Contract> hm, String status, Partner partner) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                String query = USP.partner.getContractDirtyRead2Error(status, partner);

                Statement stmt = getInstance().conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                hm.clear();
                while (rs.next()) {
                    Partner p = new Partner();
                    p.setName(rs.getString("name"));
                    p.setRepresentativeName(rs.getString("representative_name"));
                    p.setAddress(new Address(AdministrativeDivision.getInstance().getProvinceList().get(rs.getString("address_province_code")), AdministrativeDivision.getInstance().getDistrictList().get(rs.getString("address_district_code")), AdministrativeDivision.getInstance().getWardList().get(rs.getString("address_ward_code")), rs.getString("address_line")));
                    p.setBranchNumber(rs.getInt("branch_number"));
                    p.setOrderNumber(rs.getInt("order_number"));
                    p.setProductType(rs.getString("product_type"));
                    p.setPhone(rs.getString("phone"));
                    p.setMail(rs.getString("mail"));

                    Contract c = new Contract();
                    c.setPartner(p);
                    c.setTIN(rs.getString("TIN"));
                    c.setCID(rs.getString("CID"));
                    c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    c.setExpiredAt(rs.getTimestamp("expired_at").toLocalDateTime());
                    c.setCommission(rs.getFloat("commission"));
                    c.setStatus(rs.getString("status"));
                    c.setExpired(rs.getBoolean("is_expired"));

                    hm.put(c.getCID(), c);
                }

                getInstance().close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();
                return false;
            }
        }

        public static boolean getContractsDirtyRead2Fixed(HashMap<String, Contract> hm, String status, Partner partner) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                String query = USP.partner.getContractDirtyRead2Fixed(status, partner);

                Statement stmt = getInstance().conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                hm.clear();
                while (rs.next()) {
                    Partner p = new Partner();
                    p.setName(rs.getString("name"));
                    p.setRepresentativeName(rs.getString("representative_name"));
                    p.setAddress(new Address(AdministrativeDivision.getInstance().getProvinceList().get(rs.getString("address_province_code")), AdministrativeDivision.getInstance().getDistrictList().get(rs.getString("address_district_code")), AdministrativeDivision.getInstance().getWardList().get(rs.getString("address_ward_code")), rs.getString("address_line")));
                    p.setBranchNumber(rs.getInt("branch_number"));
                    p.setOrderNumber(rs.getInt("order_number"));
                    p.setProductType(rs.getString("product_type"));
                    p.setPhone(rs.getString("phone"));
                    p.setMail(rs.getString("mail"));

                    Contract c = new Contract();
                    c.setPartner(p);
                    c.setTIN(rs.getString("TIN"));
                    c.setCID(rs.getString("CID"));
                    c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    c.setExpiredAt(rs.getTimestamp("expired_at").toLocalDateTime());
                    c.setCommission(rs.getFloat("commission"));
                    c.setStatus(rs.getString("status"));
                    c.setExpired(rs.getBoolean("is_expired"));

                    hm.put(c.getCID(), c);
                }

                getInstance().close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();
                return false;
            }
        }

        public static boolean registerContractPhantom2Error(Contract c, int month) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                String query = USP.partner.registerContractPhantom2Error(c, month);

                Statement stmt = getInstance().conn.createStatement();

                stmt.execute(query);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean registerContractPhantom2Fixed(Contract c, int month) {
            try {

                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                String query = USP.partner.registerContractPhantom2Fixed(c, month);

                System.out.println(query);

                Statement stmt = getInstance().conn.createStatement();
                stmt.execute(query);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                getInstance().close();
            }
        }
    }

    public static class employee {
        public static boolean getContracts(String status, HashMap<String, Contract> hm) {
            hm.clear();
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("EMPLOYEE");

                getInstance().open();

                String query = USP.employee.getContracts(status);

                Statement stmt = getInstance().conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    Partner p = new Partner();
                    p.setName(rs.getString("name"));
                    p.setRepresentativeName(rs.getString("representative_name"));
                    p.setAddress(new Address(AdministrativeDivision.getInstance().getProvinceList().get(rs.getString("address_province_code")), AdministrativeDivision.getInstance().getDistrictList().get(rs.getString("address_district_code")), AdministrativeDivision.getInstance().getWardList().get(rs.getString("address_ward_code")), rs.getString("address_line")));
                    p.setBranchNumber(rs.getInt("branch_number"));
                    p.setOrderNumber(rs.getInt("order_number"));
                    p.setProductType(rs.getString("product_type"));
                    p.setPhone(rs.getString("phone"));
                    p.setMail(rs.getString("mail"));

                    Contract c = new Contract();
                    c.setPartner(p);
                    c.setTIN(rs.getString("TIN"));
                    c.setCID(rs.getString("CID"));
                    c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    c.setExpiredAt(rs.getTimestamp("expired_at").toLocalDateTime());
                    c.setCommission(rs.getFloat("commission"));
                    c.setStatus(rs.getString("status"));
                    c.setExpired(rs.getBoolean("is_expired"));

                    hm.put(c.getCID(), c);
                }

                getInstance().close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();
                return false;
            }
        }

        public static boolean acceptContractButFailDirtyRead2Error(Contract c) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                String query = USP.employee.acceptContractButFailDirtyRead2Error(c);

                Statement stmt = getInstance().conn.createStatement();
                stmt.execute(query);

                //throw new SQLException("ERROR: Accept fail !!!!!");
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();
                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean acceptContractButFailDirtyRead2Fixed(Contract c) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                String query = USP.employee.acceptContractButFailDirtyRead2Fixed(c);

                Statement stmt = getInstance().conn.createStatement();
                stmt.execute(query);

                //throw new SQLException("FIXED: Accept fail !!!!!");
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();
                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean acceptAllContract() {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("EMPLOYEE");

                getInstance().open();

                CallableStatement cstmt = getInstance().conn.prepareCall(USP.employee.acceptAllContract());
                cstmt.registerOutParameter(1, Types.INTEGER);
                cstmt.executeUpdate();

                int numberOfContractAccepted = cstmt.getInt(1);

                System.out.println("So hop dong da duyet: " + numberOfContractAccepted);

                getInstance().close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();
                return false;
            }
        }

        public static boolean resetToAllContractToPending() {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                String query = """
                        update dbo.CONTRACTS
                            set status = 'PENDING'
                            where is_expired = 0""";

                getInstance().conn.createStatement().execute(query);
                return true;
            } catch (Exception e) {
                e.printStackTrace();

                return false;
            } finally {
                getInstance().close();
            }
        }

        public static String acceptAllContractPhantom2Error(HashMap<String, Contract> hm) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                String query = USP.employee.acceptAllContractPhantom2Error();

                CallableStatement cstmt = getInstance().conn.prepareCall(query);
                System.out.println(query);

                // Execute the CALL statement and expecting multiple result sets

                int before = 0;

                boolean isResultSet = cstmt.execute();

                // get before
                if (isResultSet) {
                    ResultSet rs = cstmt.getResultSet();

                    while (rs.next()) {
                        before = rs.getInt("number_contract_accepted");
                    }
                    rs.close();
                }

                isResultSet = cstmt.getMoreResults();
                int counter = 0;
                isResultSet = cstmt.getMoreResults();
                // get rs
                if (isResultSet) {
                    ResultSet rs = cstmt.getResultSet();
                    while (rs.next()) {
                        String CID = rs.getString("CID");
                        Contract c = new Contract();
                        c.setCID(CID);

                        hm.put(CID, c);
                    }
                    int after = hm.size();
                    if (after > before) {
                        throw new IndexOutOfBoundsException("Có lỗi phantom.\nBắt đầu duyệt: " + before + ", Đã duyệt: " + after);
                    }
                    rs.close();
                }


                return "success";
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();

                return e.getMessage();
            } catch (SQLException e) {
                e.printStackTrace();

                return "sqlErr";
            } finally {
                getInstance().close();
            }
        }

        public static String acceptAllContractPhantom2Fixed(HashMap<String, Contract> hm) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                String query = USP.employee.acceptAllContractPhantom2Fixed();

                CallableStatement cstmt = getInstance().conn.prepareCall(query);
                System.out.println(query);

                // Execute the CALL statement and expecting multiple result sets

                int before = 0;

                boolean isResultSet = cstmt.execute();

                // get before
                if (isResultSet) {
                    ResultSet rs = cstmt.getResultSet();

                    while (rs.next()) {
                        before = rs.getInt("number_contract_accepted");
                    }
                    rs.close();
                }

                isResultSet = cstmt.getMoreResults();
                int counter = 0;
                isResultSet = cstmt.getMoreResults();
                // get rs
                if (isResultSet) {
                    ResultSet rs = cstmt.getResultSet();
                    while (rs.next()) {
                        String CID = rs.getString("CID");
                        Contract c = new Contract();
                        c.setCID(CID);

                        hm.put(CID, c);
                    }
                    int after = hm.size();
                    if (after > before) {
                        throw new IndexOutOfBoundsException("Có lỗi phantom.\nBắt đầu duyệt: " + before + ", Đã duyệt: " + after);
                    }
                    rs.close();
                }


                return "success";
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();

                return e.getMessage();
            } catch (SQLException e) {
                e.printStackTrace();

                return "sqlErr";
            } finally {
                getInstance().close();
            }
        }
    }

    public static class customer {
        public static boolean getListPartner(HashMap<String, Partner> partners) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                String query = USP.customer.getPartner();

                CallableStatement cstmt = getInstance().conn.prepareCall(query);
                System.out.println(query);

                // Execute the CALL statement and expecting multiple result sets
                boolean isResultSet = cstmt.execute();

                ResultSet rs = cstmt.getResultSet();

                partners.clear();

                while (rs.next()) {
                    LoginInfo loginInfo = new LoginInfo();
                    Partner partner = new Partner();

                    partner.setLogin(loginInfo);

                    loginInfo.setUsername(rs.getString("username"));
                    partner.setName(rs.getString("name"));
                    partner.setProductType(rs.getString("product_type"));
                    partner.setRepresentativeName(rs.getString("representative_name"));
                    partner.setAddress(new Address(AdministrativeDivision.getInstance().getProvinceList().get(rs.getString("address_province_code")), AdministrativeDivision.getInstance().getDistrictList().get(rs.getString("address_district_code")), AdministrativeDivision.getInstance().getWardList().get(rs.getString("address_ward_code")), rs.getString("address_line")));
                    partner.setPhone(rs.getString("phone"));
                    partner.setMail(rs.getString("mail"));

                    partners.put(partner.getLogin().getUsername(), partner);
                }

                System.out.println(partners.size());

                getInstance().close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();

                return false;
            }
        }

        public static boolean addProductToCart(CartDetail c) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                Statement statement = getInstance().conn.createStatement();
                statement.execute(USP.customer.addProductToCart(c));

                getInstance().close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();
                return false;
            }
        }

        public static boolean getCartDetails(Cart cart) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                String query = USP.customer.getCartDetails(cart);

                System.out.println("[GET CART DETAILS]" + query);

                CallableStatement cstmt = getInstance().conn.prepareCall(query);


                boolean isResultSet = cstmt.execute();

                // CART info
                ResultSet rs = cstmt.getResultSet();

                if (!isResultSet) {
                    System.out.println("No result set");
                    return false;
                }

                while (rs.next()) {
                    cart.shippingFee = rs.getFloat("shipping_fee");
                    cart.total = rs.getFloat("total");
                }

                // CART_DETAILS
                isResultSet = cstmt.getMoreResults();

                if (!isResultSet) {
                    System.out.println("The next result is not a ResultSet.");
                } else {
                    rs = cstmt.getResultSet();
                }

                while (rs.next()) {
                    CartDetail cartDetail = new CartDetail();
                    Product product = new Product();
                    product.setName(rs.getString("p_name"));
                    product.setPrice(rs.getFloat("price_per_product"));
                    product.setImgSrc(rs.getString("img_src"));
                    product.setPID(rs.getString("PID"));
                    int quantity = rs.getInt("quantity");
                    float subTotal = rs.getFloat("sub_total");

                    cartDetail.cart = cart;
                    cartDetail.product = product;
                    cartDetail.quantity = quantity;
                    cartDetail.subTotal = subTotal;
                    cartDetail.partnerBranch.setPBID(rs.getString("PBID"));
                    cartDetail.partnerBranch.setName(rs.getString("pb_name"));

                    cart.cartDetails.add(cartDetail);

                }

                getInstance().close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();

                return false;
            }
        }

        public static boolean getListProduct(Partner partner, HashMap<String, PartnerBranch> partnerBranches, HashMap<String, Product> products) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                System.out.println(USP.customer.getProducts(partner));

                CallableStatement cstmt = getInstance().conn.prepareCall(USP.customer.getProducts(partner));

                boolean isResultSet = cstmt.execute();

                ResultSet rs = cstmt.getResultSet();
                products.clear();

                // get list product
                while (rs.next()) {
                    Product product = new Product();

                    product.setPID(rs.getString("PID"));
                    product.setPartner(partner);
                    product.setProductType(rs.getString("product_type"));
                    product.setImgSrc(rs.getString("img_src"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getFloat("price"));
                    product.setDeleted(rs.getBoolean("is_deleted"));

                    products.put(product.getPID(), product);
                }
                rs.close();

                isResultSet = cstmt.getMoreResults();

                if (!isResultSet) {
                    System.out.println("The next result is not a ResultSet.");
                } else {
                    rs = cstmt.getResultSet();
                }
                // partner branches
                partnerBranches.clear();
                while (rs.next()) {
                    PartnerBranch pb = new PartnerBranch();

                    pb.setPBID(rs.getString("PBID"));
                    pb.setName(rs.getString("name"));
                    partner.getLogin().setUsername(rs.getString("username"));
                    pb.setPartner(partner);
                    pb.setAddress(new Address(AdministrativeDivision.getInstance().getProvinceList().get(rs.getString("address_province_code")), AdministrativeDivision.getInstance().getDistrictList().get(rs.getString("address_district_code")), AdministrativeDivision.getInstance().getWardList().get(rs.getString("address_ward_code")), rs.getString("address_line")));
                    pb.setDeleted(rs.getBoolean("is_deleted"));

                    partnerBranches.put(pb.getPBID(), pb);
                }
                rs.close();

                isResultSet = cstmt.getMoreResults();

                if (!isResultSet) {
                    System.out.println("The next result is not a ResultSet.");
                } else {
                    rs = cstmt.getResultSet();
                }
                // get product in Branches

                while (rs.next()) {
                    String PBID = rs.getString("PBID"), PID = rs.getString("PID");

                    if (PID.equals("product23")) {
                        System.out.println(PBID);
                    }

                    int stock = rs.getInt("stock");
                    partnerBranches.get(PBID).addProductToBranch(products.get(PID), stock);
                    products.get(PID).addInBranch(partnerBranches.get(PBID), stock);
                }

                getInstance().close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();
                return false;
            }
        }

        public static boolean createOrder(Order order, Cart cart) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();
                String query = USP.customer.createOrderButFailDirtyRead1Error(order);

                Statement statement = getInstance().conn.createStatement();
                statement.execute(query);

                throw new SQLException("create failed");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean createOrderButFailError(Order order, Cart cart) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                String query = USP.customer.createOrderButFailDirtyRead1Error(order);

                Statement statement = getInstance().conn.createStatement();
                statement.execute(query);

                throw new SQLException("create failed");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean createOrderButFailFixed(Order order, Cart cart) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                String query = USP.customer.createOrderButFailDirtRead1Fixed(order);

                Statement statement = getInstance().conn.createStatement();
                statement.execute(query);

                throw new SQLException("create failed");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean createOrderLostUpdate1ErrorT1(Order order, Cart cart) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                String query = USP.customer.createOrderLostUpdate1ErrorT1(order);

                Statement statement = getInstance().conn.createStatement();
                statement.execute(query);

                query = USP.customer.deleteCart(cart);
                statement.execute(query);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean createOrderLostUpdate1ErrorT2(Order order, Cart cart) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                String query = USP.customer.createOrderLostUpdate1ErrorT2(order);

                Statement statement = getInstance().conn.createStatement();
                statement.execute(query);

                query = USP.customer.deleteCart(cart);
                statement.execute(query);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                getInstance().close();
            }
        }

        // ----------------------------------
        public static boolean createOrderLostUpdate1FixedT1(Order order, Cart cart) {
            try {
                Thread.sleep(5000);
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                String query = USP.customer.createOrderLostUpdate1FixedT1(order);

                Statement statement = getInstance().conn.createStatement();
                statement.execute(query);

                query = USP.customer.deleteCart(cart);
                statement.execute(query);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean createOrderLostUpdate1FixedT2(Order order, Cart cart) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                String query = USP.customer.createOrderLostUpdate1FixedT2(order);

                Statement statement = getInstance().conn.createStatement();
                statement.execute(query);

                query = USP.customer.deleteCart(cart);
                statement.execute(query);
                Thread.sleep(5000);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                getInstance().close();
            }
        }

        // ----------------------------------
        public static boolean getOrders(Customer c, HashMap<String, Order> hm) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                Statement stmt = getInstance().conn.createStatement();
                String query = USP.customer.getOrders(c);
                System.out.println(query);
                ResultSet rs = stmt.executeQuery(query);

                hm.clear();
                while (rs.next()) {
                    Order o = new Order();

                    o.setOrderID(rs.getString("order_id"));

                    c.setName(rs.getString("name"));
                    String pvCode = rs.getString("pv_code"), dtCode = rs.getString("dt_code"), wCode = rs.getString("w_code");
                    c.setAddress(new Address(
                            AdministrativeDivision.getInstance().getProvinceList().get(pvCode),
                            AdministrativeDivision.getInstance().getDistrictList().get(dtCode),
                            AdministrativeDivision.getInstance().getWardList().get(wCode),
                            rs.getString("address_line")));
                    o.setCustomer(c);

                    o.setTotal(rs.getFloat("total"));
                    o.setPaymentMethod(rs.getString("payment_method"));
                    o.setDeliveryStatus(rs.getString("delivery_status"));
                    o.setPaidStatus(rs.getString("paid_status"));

                    hm.put(o.getOrderID(), o);
                }

                return true;
            } catch (SQLException e) {
                e.printStackTrace();

                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean updateOrderPMNRR2Error(Order o, String newPm) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                Statement stmt = getInstance().conn.createStatement();
                String query = USP.customer.updateOrderPMNRR2Error(o, newPm);
                System.out.println(query);
                boolean res = stmt.execute(query);

                return true;

            } catch (SQLException e) {
                e.printStackTrace();

                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean updateOrderPMNRR2Fixed(Order o, String newPm) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                Statement stmt = getInstance().conn.createStatement();
                String query = USP.customer.updateOrderPMNRR2Fixed(o, newPm);
                System.out.println(query);
                boolean res = stmt.execute(query);

                return true;

            } catch (SQLException e) {
                e.printStackTrace();

                return false;
            } finally {
                getInstance().close();
            }
        }

        public static boolean getOrdersDetail() {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("CUSTOMER");

                getInstance().open();

                Order order = new Order();
                order.setOrderID("ORDER001");
                String query = USP.customer.getOrderDetails(order);

                CallableStatement cstmt = getInstance().conn.prepareCall(query);
                System.out.println(query);

                // Execute the CALL statement and expecting multiple result sets
                boolean isResultSet = cstmt.execute();

                ResultSet rs = cstmt.getResultSet();

                // First ResultSet object
                if (!isResultSet) {
                    System.out.println("The first result is not a ResultSet.");
                }


                List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                Map<String, Object> row = null;
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    row = new HashMap<String, Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    resultList.add(row);
                }
                rs.close();

                isResultSet = cstmt.getMoreResults();

                if (!isResultSet) {
                    System.out.println("The next result is not a ResultSet.");
                } else {
                    rs = cstmt.getResultSet();
                }

                metaData = rs.getMetaData();
                columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    row = new HashMap<String, Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    resultList.add(row);
                }

                rs.close();
                getInstance().close();

                System.out.println(resultList);

                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();

                return false;
            }
        }
    }

    public static class driver {
        public static boolean getOrdersInActiveArea(Driver driver, HashMap<String, Order> hm) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                Statement stmt = getInstance().conn.createStatement();
                String query = USP.driver.getOrdersInActiveArea(driver);
                System.out.println(query);
                ResultSet rs = stmt.executeQuery(query);

                hm.clear();
                while (rs.next()) {
                    Order o = new Order();

                    o.setOrderID(rs.getString("order_id"));

                    Customer c = new Customer();
                    c.setName(rs.getString("name"));
                    String pvCode = rs.getString("pv_code"), dtCode = rs.getString("dt_code"), wCode = rs.getString("w_code");
                    c.setAddress(new Address(
                            AdministrativeDivision.getInstance().getProvinceList().get(pvCode),
                            AdministrativeDivision.getInstance().getDistrictList().get(dtCode),
                            AdministrativeDivision.getInstance().getWardList().get(wCode),
                            rs.getString("address_line")));
                    o.setCustomer(c);

                    o.setTotal(rs.getFloat("total"));
                    o.setPaymentMethod(rs.getString("payment_method"));
                    o.setDeliveryStatus(rs.getString("delivery_status"));
                    o.setPaidStatus(rs.getString("paid_status"));

                    hm.put(o.getOrderID(), o);
                }

                return true;
            } catch (SQLException e) {
                e.printStackTrace();

                return false;
            } finally {
                getInstance().close();
            }
        }

        public static String receiveOrderNRR2Error(Driver d, Order in, Order out) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                Statement stmt = getInstance().conn.createStatement();
                String query = USP.driver.receiveOrderNRR2Error(d, in);

                CallableStatement cstmt = getInstance().conn.prepareCall(query);
                System.out.println(query);

                // Execute the CALL statement and expecting multiple result sets

                boolean isResultSet = cstmt.execute();

                // get before
                if (isResultSet) {
                    ResultSet rs = cstmt.getResultSet();

                    while (rs.next()) {
                        in.setOrderID(rs.getString("order_id"));
                        in.setPaymentMethod(rs.getString("payment_method"));
                    }
                    rs.close();
                }

                isResultSet = cstmt.getMoreResults();
                isResultSet = cstmt.getMoreResults();
                if (isResultSet) {
                    ResultSet rs = cstmt.getResultSet();

                    while (rs.next()) {
                        out.setOrderID(rs.getString("order_id"));
                        out.setPaymentMethod(rs.getString("payment_method"));
                    }
                    rs.close();
                }

                if (!in.getPaymentMethod().equals(out.getPaymentMethod())) {
                    throw new SQLException("nrr;Bắt đầu: " + in.getPaymentMethod() + " - Kết thúc: " + out.getPaymentMethod());
                }

                return "success";
            } catch (SQLException e) {
                e.printStackTrace();

                return e.getMessage();
            } finally {
                getInstance().close();
            }
        }

        public static String receiveOrderNRR2Fixed(Driver d, Order in, Order out) {
            try {
                getInstance().currentSQLLogin = getInstance().sqlLogins.get("SA");

                getInstance().open();

                Statement stmt = getInstance().conn.createStatement();
                String query = USP.driver.receiveOrderNRR2Fixed(d, in);

                CallableStatement cstmt = getInstance().conn.prepareCall(query);
                System.out.println(query);

                // Execute the CALL statement and expecting multiple result sets

                boolean isResultSet = cstmt.execute();

                // get before
                if (isResultSet) {
                    ResultSet rs = cstmt.getResultSet();

                    while (rs.next()) {
                        in.setOrderID(rs.getString("order_id"));
                        in.setPaymentMethod(rs.getString("payment_method"));
                    }
                    rs.close();
                }

                isResultSet = cstmt.getMoreResults();
                isResultSet = cstmt.getMoreResults();
                if (isResultSet) {
                    ResultSet rs = cstmt.getResultSet();

                    while (rs.next()) {
                        out.setOrderID(rs.getString("order_id"));
                        out.setPaymentMethod(rs.getString("payment_method"));
                    }
                    rs.close();
                }

                if (!in.getPaymentMethod().equals(out.getPaymentMethod())) {
                    throw new SQLException("nrr");
                }

                return "success";
            } catch (SQLException e) {
                e.printStackTrace();

                return e.getMessage();
            } finally {
                getInstance().close();
            }
        }
    }
}


