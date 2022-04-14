package pck.dbms.database;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import pck.dbms.be.administrativeDivision.*;
import pck.dbms.be.cart.Cart;
import pck.dbms.be.cart.CartDetail;
import pck.dbms.be.order.Order;
import pck.dbms.be.order.OrderDetail;
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

    public boolean partnerRegistration(LoginInfo loginInfo, Partner partner) {
        try {
            this.currentSQLLogin = sqlLogins.get("PARTNER");

            open();

            PreparedStatement pstmt = conn.prepareStatement(USP.partner.registration(partner));
            pstmt.execute();
            close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean partnerGetAcceptedContracts() {
        try {
            this.currentSQLLogin = sqlLogins.get("PARTNER");

            open();

            PreparedStatement pstmt = conn.prepareStatement("exec dbo.usp_partner_get_accepted_contracts 'phatnm.partner1'");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("CID"));
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

    public static class employee {
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

                String query = USP.customer.createOrder(order);
                System.out.println("O:" + query);
                Statement statement = getInstance().conn.createStatement();
                statement.execute(query);

                for (OrderDetail od : order.getOrderDetails()) {
                    query = USP.customer.addProductToOrder(od);
                    System.out.println("OD:" + query);
                    statement.execute(query);
                }

                query = USP.customer.deleteCart(cart);
                System.out.println("DELETE C: " + query);

                statement.execute(query);

                getInstance().close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();
                return false;
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
}
