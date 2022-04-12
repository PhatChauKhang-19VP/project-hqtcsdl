package pck.dbms.database;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import pck.dbms.be.Container;
import pck.dbms.be.administrativeDivision.*;
import pck.dbms.be.order.Order;
import pck.dbms.be.partner.Partner;
import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.product.Product;
import pck.dbms.be.user.Login;
import pck.dbms.be.utils.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseCommunication {
    private static DatabaseCommunication instance;
    private Pair<String, String> currLogin;
    private String databaseName;
    private HashMap<String, Pair<String, String>> logins;
    private Connection conn;
    private Statement stmt;

    private DatabaseCommunication() {
        logins = new HashMap<>();

        logins.put("SA", new Pair<>("sa", "Thoai1234"));
        logins.put("APP", new Pair<>("_app", "1"));
        logins.put("ADMIN", new Pair<>("_admin", "1"));
        logins.put("PARTNER", new Pair<>("_partner", "1"));
        logins.put("CUSTOMER", new Pair<>("_customer", "1"));
        logins.put("EMPLOYEE", new Pair<>("_employee", "1"));
        logins.put("DRIVER", new Pair<>("_driver", "1"));

        databaseName = new String("db_19vp_delivery");
        currLogin = logins.get("SA");
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

            sqlDs.setUser(currLogin.getFirst());
            sqlDs.setPassword(currLogin.getSecond());

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

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = null;
        ResultSetMetaData metaData = rs.getMetaData();
        Integer columnCount = metaData.getColumnCount();

        while (rs.next()) {
            row = new HashMap<String, Object>();
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

    public boolean partnerRegistration(Login login, Partner partner) {
        try {
            this.currLogin = logins.get("PARTNER");

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
            this.currLogin = logins.get("PARTNER");

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

        CallableStatement cstmt = conn.prepareCall(
                "{call dbo.usp_partner_get_accepted_contracts(?)}");
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
                getInstance().currLogin = getInstance().logins.get("EMPLOYEE");

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
        public static boolean getListPartner() {
            try {
                getInstance().currLogin = getInstance().logins.get("CUSTOMER");

                getInstance().open();

                String query = USP.customer.getPartner();

                CallableStatement cstmt = getInstance().conn.prepareCall(query);
                System.out.println(query);

                // Execute the CALL statement and expecting multiple result sets
                boolean isResultSet = cstmt.execute();

                ResultSet rs = cstmt.getResultSet();

                Container.partners.clear();

                while (rs.next()) {
                    Login login = new Login();
                    Partner partner = new Partner();

                    partner.setLogin(login);

                    login.setUsername(rs.getString("username"));
                    partner.setName(rs.getString("name"));
                    partner.setProductType(rs.getString("product_type"));
                    partner.setRepresentativeName(rs.getString("representative_name"));
                    partner.setAddress(new Address(
                            AdministrativeDivision.getInstance().getProvinceList().get(rs.getString("address_province_code")),
                            AdministrativeDivision.getInstance().getDistrictList().get(rs.getString("address_district_code")),
                            AdministrativeDivision.getInstance().getWardList().get(rs.getString("address_ward_code")),
                            rs.getString("address_line")
                    ));
                    partner.setPhone(rs.getString("phone"));
                    partner.setMail(rs.getString("mail"));

                    Container.partners.put(partner.getLogin().getUsername(), partner);
                }

                System.out.println(Container.partners.size());

                getInstance().close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                getInstance().close();

                return false;
            }
        }

        public static boolean getListProduct(Partner partner) {
            try {
                getInstance().currLogin = getInstance().logins.get("CUSTOMER");

                getInstance().open();

                System.out.println(USP.customer.getProducts(partner));

                CallableStatement cstmt = getInstance().conn.prepareCall(USP.customer.getProducts(partner));

                Boolean isResultSet = cstmt.execute();

                ResultSet rs = cstmt.getResultSet();
                Container.products.clear();

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

                    Container.products.put(product.getPID(), product);
                }
                rs.close();

                isResultSet = cstmt.getMoreResults();

                if (!isResultSet) {
                    System.out.println("The next result is not a ResultSet.");
                } else {
                    rs = cstmt.getResultSet();
                }
                // partner branches
                Container.partnerBranches.clear();
                while (rs.next()) {
                    PartnerBranch pb = new PartnerBranch();

                    pb.setPBID(rs.getString("PBID"));
                    pb.setName(rs.getString("name"));
                    pb.setPartner(Container.partners.get(rs.getString("username")));
                    pb.setAddress(new Address(
                            AdministrativeDivision.getInstance().getProvinceList().get(rs.getString("address_province_code")),
                            AdministrativeDivision.getInstance().getDistrictList().get(rs.getString("address_district_code")),
                            AdministrativeDivision.getInstance().getWardList().get(rs.getString("address_ward_code")),
                            rs.getString("address_line")
                    ));
                    pb.setDeleted(rs.getBoolean("is_deleted"));

                    Container.partnerBranches.put(pb.getPBID(), pb);
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
                    String PBID = rs.getString("PBID"),
                            PID = rs.getString("PID");

                    if (PID.equals("product23")){
                        System.out.println(PBID);
                    }

                    int stock = rs.getInt("stock");
                    Container.partnerBranches
                            .get(PBID)
                            .addProductToBranch(Container.products.get(PID), stock);
                    Container.products
                            .get(PID)
                            .addInBranch(Container.partnerBranches.get(PBID), stock);
                }

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
                getInstance().currLogin = getInstance().logins.get("CUSTOMER");

                getInstance().open();

                Order order = new Order();
                order.setOrderID("ORDER001");
                String query = USP.customer.getOrderDetails(order);

                CallableStatement cstmt = getInstance().conn.prepareCall(query);
                System.out.println(query);

                // Execute the CALL statement and expecting multiple result sets
                boolean isResultSet = cstmt.execute();

                ResultSet rs = cstmt.getResultSet();

                // First ReulstSet object
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
