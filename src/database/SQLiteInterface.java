package database;

import Person.Manager;
import Person.Member;
import Person.Provider;
import Person.Service;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Kai Jiang on 12/2/2014.
 */
public class SQLiteInterface {
    private int VECTOR_ALLOC_SIZE = 5000;

    private String driverName;
    private String dbName;
    private String urlPrefix;

    private Driver driver;
    private Connection con = null;
    private Statement stmt;
    private ResultSet rs;

    public SQLiteInterface() {
        this("org.sqlite.JDBC", "ChocAn.db", "jdbc:sqlite:");
    }

    public SQLiteInterface(String driverName, String dbName, String urlPrefix) {
        this.driverName = driverName;
        this.dbName = dbName;
        this.urlPrefix = urlPrefix;
        this.connect();
    }

    //Get connect to ChocAn.db
    private void connect() {
        try {
            driver = (Driver) Class.forName(driverName).newInstance();
            DriverManager.registerDriver(driver);
            System.out.println("register drive: fine!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error loading database driver:", JOptionPane.ERROR_MESSAGE);
        }

        try {
            String url = urlPrefix + dbName;
            con = DriverManager.getConnection(url);
            System.out.println("connection: fine!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error creating connection:", JOptionPane.ERROR_MESSAGE);
        }
    }

    //execute SQL
    private void execute(String query) {
        try {
            if (con == null)
                this.connect();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL statement: " + ex.toString(), "Error creating or running statement:", JOptionPane.ERROR_MESSAGE);
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString(), "Could not close connection:", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //update database with SQL
    private void update(String query) {
        try {
            if (con == null)
                this.connect();
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL update: " + ex.toString(), "Error creating or running update:", JOptionPane.ERROR_MESSAGE);
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString(), "Could not close connection:", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //operation
    public String checkPassword(int id) {
        String query = "select Password from ACCOUNT where id = " + id + ";";
        this.execute(query);
        String password = "";
        try {
            if (rs.next()) {
                password = rs.getString(1);     //1st column;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Fetching Password: " + e.toString(), "Error Fetching Password", JOptionPane.ERROR_MESSAGE);
        }
        return password;
    }

    public String checkRole(int id) {
        String query = "select ROLE from ACCOUNT where id = " + id + ";";
        this.execute(query);
        String password = "";
        try {
            if (rs.next()) {
                password = rs.getString(1);     //1st column;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Fetching Password: " + e.toString(), "Error Fetching Password", JOptionPane.ERROR_MESSAGE);
        }
        return password;
    }

    public int updateServiceAndGetCode(int member_id, int provider_id) {
        String query1 = "INSERT INTO serviceinstance (service_code, member_id, provider_id, date_provided, time_stamp, visited, comments) VALUES (null," +
                String.valueOf(member_id) + "," + String.valueOf(provider_id) + ", current_date, current_timestamp,0,'');";
        this.update(query1);

        //last line's service code
        String query2 = "SELECT service_code FROM SERVICEINSTANCE ORDER BY service_code DESC LIMIT 1;";
        int serviceCode = 0;
        this.execute(query2);
        try {
            if (rs.next()) {
                serviceCode = rs.getInt(1);     //1st column;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Fetching Password: " + e.toString(), "Error Fetching Password", JOptionPane.ERROR_MESSAGE);
        }
        return serviceCode;
    }

    //return map <service code(integer), member_id>
    public Map<Integer, Integer> unVisitMembers(int providerID) {
        Map<Integer, Integer> results = null;
        String query = "SELECT SERVICEINSTANCE.service_code, SERVICEINSTANCE.member_id, MEMBER.name FROM SERVICEINSTANCE,MEMBER WHERE provider_id = " +
                String.valueOf(providerID) +
                " AND visited = 0 and SERVICEINSTANCE.member_id = MEMBER.member_id";
        this.execute(query);
        try {
            while (rs.next()) {
                if (results == null) {
                    results = new HashMap<Integer, Integer>(VECTOR_ALLOC_SIZE);
                    results.clear();
                }
                int sc = rs.getInt(1);
                int mID = rs.getInt(2);

                results.put(sc, mID);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Fetching unVisitMember Results: " + e.toString(), "Error Fetching unVisitMember Results: ", JOptionPane.ERROR_MESSAGE);
            try {
                rs.close();
                stmt.close();
                con.close();
                con = null;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error closing result set, statement and connection: " + e.toString(), "Error Fetching unVisitMember Results: ", JOptionPane.ERROR_MESSAGE);
            }
        }
        return results;
    }

    public void setVisitedAndComments(int sc, String comments) {
        String query = "UPDATE SERVICEINSTANCE SET visited = 1, date_provided = current_date, comments = '" + comments + "' WHERE service_code = " + String.valueOf(sc) + ";";
        this.update(query);
    }

    public void updateMemberFee(int sc, double fee) {
        String query = "UPDATE MEMBER SET fee = fee + " + String.valueOf(fee) + " WHERE MEMBER.member_id = (SELECT SERVICEINSTANCE.member_id FROM SERVICEINSTANCE WHERE service_code = "
                + String.valueOf(sc) + ");";
        this.update(query);
    }

    public int getLastIDinAccount() throws SQLException {
        String query1 = "SELECT id FROM ACCOUNT ORDER BY id DESC LIMIT 1;";
        this.execute(query1);
        int result = 0;
        try {
            if (rs.next()) {
                result = rs.getInt(1);     //1st column;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Fetching LastID: " + e.toString(), "Error Fetching LastID", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    //add to Account table and Role(three) tables
    //return id
    public int insertRoleToAccountAndRoleTable(Person.Member newMember, String Passwd) {
        //add to Account table with password
        String query = "INSERT INTO ACCOUNT (Name, Password, ROLE) VALUES ('" + newMember.name + "','" + Passwd + "','member');";
        this.update(query);

        int id = 0;
        try {
            id = this.getLastIDinAccount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        newMember.id = id;
        this.addMember(newMember);

        JOptionPane.showConfirmDialog(null, "Add Member Successfully \n" +
                "Name:   " + newMember.name + "\n" +
                "ID:     " + Format.formatNumber.ID(id) + "\n", "New Member: " + newMember.name, JOptionPane.OK_OPTION);

        return id;
    }

    public int insertRoleToAccountAndRoleTable(Person.Manager newManager, String Passwd) {
        String query = "INSERT INTO ACCOUNT (Name, Password, ROLE) VALUES ('" + newManager.name + "','" + Passwd + "','manager');";
        this.update(query);

        int id = 0;
        try {
            id = this.getLastIDinAccount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        newManager.id = id;
        this.addManager(newManager);

        JOptionPane.showConfirmDialog(null, "Add Manager Successfully \n" +
                "Name:   " + newManager.name + "\n" +
                "ID:     " + Format.formatNumber.ID(id) + "\n", "New Member: " + newManager.name, JOptionPane.CLOSED_OPTION);

        return id;
    }

    public int insertRoleToAccountAndRoleTable(Person.Provider newProvider, String Passwd) {
        String query = "INSERT INTO ACCOUNT (Name, Password, ROLE) VALUES ('" + newProvider.name + "','" + Passwd + "','provider');";
        this.update(query);

        int id = 0;
        try {
            id = this.getLastIDinAccount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        newProvider.id = id;
        this.addProvider(newProvider);

        JOptionPane.showConfirmDialog(null, "Add Provider Successfully \n" +
                "Name:   " + newProvider.name + "\n" +
                "ID:     " + Format.formatNumber.ID(id) + "\n", "New Member: " + newProvider.name, JOptionPane.OK_OPTION);

        return id;
    }
    //operation ending

    //everything about member
    private Vector<Member> fetchMemberResults() {
        Vector<Member> results = null;
        Member row = null;

        try {
            while (rs.next()) {
                if (results == null)
                    results = new Vector<Member>(VECTOR_ALLOC_SIZE);
                row = new Member();

                row.id = rs.getInt(1);
                row.name = rs.getString(2);
                row.address = rs.getString(3);
                row.city = rs.getString(4);
                row.state = rs.getString(5);
                row.zipCode = rs.getString(6);
                row.fee = rs.getDouble(7);
                results.add(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Fetching Member Results: " + e.toString(), "Error Fetching Member Results: ", JOptionPane.ERROR_MESSAGE);
            try {
                rs.close();
                stmt.close();
                con.close();
                con = null;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error closing result set, statement and connection: " + e.toString(), "Error Fetching Member Results: ", JOptionPane.ERROR_MESSAGE);
            }
        }
        return results;
    }

    public Member retrieveMember(int member_id) {
        String query = "SELECT * FROM member m WHERE m.member_id=" + member_id + ";";
        execute(query);
        Vector<Member> results = fetchMemberResults();
        if (results == null)
            return null;
        return results.firstElement();
    }

    public Vector<Member> retrieveMemberTable() {
        String query = "SELECT * FROM member;";
        this.execute(query);
        return this.fetchMemberResults();
    }


    public void addMember(Member newMember) {
        String query = "INSERT INTO member VALUES (" +
                String.valueOf(newMember.id) + ",'" +
                newMember.name + "','" +
                newMember.address + "','" +
                newMember.city + "','" +
                newMember.state + "','" +
                newMember.zipCode + "'," +
                String.valueOf(newMember.fee) + ");";
        this.update(query);
    }

    public void updateMember(Member rpManager) {
        String query = "UPDATE member SET " +
                "name='" + rpManager.name + "', " +
                "address='" + rpManager.address + "', " +
                "city='" + rpManager.city + "', " +
                "state='" + rpManager.state + "', " +
                "zipcode='" + rpManager.zipCode + "', " +
                "fee= " + String.valueOf(rpManager.fee) +
                " WHERE member.member_id = " +
                rpManager.id + ";";
        this.update(query);
    }

    public void deleteMember(int member_id) {
        String query = "DELETE FROM member WHERE member.member_id=" + member_id + ";";
        this.update(query);
    }

    //everything on Provider
    private Vector<Provider> fetchProviderResults() {
        Vector<Provider> results = null;
        Provider row = null;

        try {
            while (rs.next()) {
                if (results == null)
                    results = new Vector<Provider>(VECTOR_ALLOC_SIZE);
                row = new Provider();

                row.id = rs.getInt(1);
                row.name = rs.getString(2);
                row.providertype_id = rs.getInt(3);
                row.address = rs.getString(4);
                row.city = rs.getString(5);
                row.state = rs.getString(6);
                row.zipCode = rs.getString(7);

                results.add(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Fetching Provider Results: " + e.toString(), "Error Fetching Provider Results: ", JOptionPane.ERROR_MESSAGE);
            try {
                rs.close();
                stmt.close();
                con.close();
                con = null;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error closing result set, statement and connection: " + e.toString(), "Error Fetching Provider Results: ", JOptionPane.ERROR_MESSAGE);
            }
        }
        return results;
    }

    public Provider retrieveProvider(int provider_id) {
        String query = "SELECT * FROM provider p WHERE p.provider_id=" + provider_id + ";";
        this.execute(query);
        // Prevents null pointer exception when there is no corresponding provider
        Vector<Provider> vp = fetchProviderResults();
        if (vp != null) {
            return vp.firstElement();
        } else {
            return new Provider();
        }
    }

    public Vector<Provider> retrieveProviderTable() {
        String query = "SELECT * FROM provider;";
        this.execute(query);
        return this.fetchProviderResults();
    }

    public void addProvider(Provider np) {
        String query = "INSERT INTO provider VALUES (" + String.valueOf(np.id) + " , '" +
                np.name + "', " +
                np.providertype_id + ", '" +
                np.address + "', '" +
                np.city + "', '" +
                np.state + "', '" +
                np.zipCode + "');";
        this.update(query);
    }

    public void updateProvider(Provider up) {
        String query = "UPDATE provider SET " +
                "name='" + up.name + "', " +
                "providertype_id=" + up.providertype_id + ", " +
                "address='" + up.address + "', " +
                "city='" + up.city + "', " +
                "state='" + up.state + "', " +
                "zip='" + up.zipCode +
                "' WHERE provider.provider_id =" +
                up.id + ";";
        this.update(query);
    }

    public void deleteProvider(int provider_id) {
        String query = "DELETE FROM provider WHERE provider.provider_id=" + provider_id + ";";
        this.update(query);
    }

    //Everything on Manager
    private Vector<Manager> fetchManagerResults() {
        Vector<Manager> results = null;
        Manager row = null;

        try {
            while (rs.next()) {
                if (results == null)
                    results = new Vector<Manager>(VECTOR_ALLOC_SIZE);
                row = new Manager();

                row.id = rs.getInt(1);
                row.name = rs.getString(2);
                row.address = rs.getString(3);
                row.city = rs.getString(4);
                row.state = rs.getString(5);
                row.zipCode = rs.getString(6);
                results.add(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Fetching Manager Results: " + e.toString(), "Error Fetching Manager Results: ", JOptionPane.ERROR_MESSAGE);
            try {
                rs.close();
                stmt.close();
                con.close();
                con = null;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error closing result set, statement and connection: " + e.toString(), "Error Fetching Manager Results: ", JOptionPane.ERROR_MESSAGE);
            }
        }
        return results;
    }

    public Manager retrieveManager(int manager_id) {
        String query = "SELECT * FROM manager m WHERE m.manager_id=" + manager_id + ";";
        execute(query);
        Vector<Manager> results = fetchManagerResults();
        if (results == null)
            return null;
        return results.firstElement();
    }

    public Vector<Manager> retrieveManagerTable() {
        String query = "SELECT * FROM manager;";
        this.execute(query);
        return this.fetchManagerResults();
    }


    public void addManager(Manager newManager) {
        String query = "INSERT INTO manager VALUES (" +
                String.valueOf(newManager.id) + ",'" +
                newManager.name + "','" +
                newManager.address + "','" +
                newManager.city + "','" +
                newManager.state + "','" +
                newManager.zipCode + "');";
        this.update(query);
    }

    public void updateManager(Manager rpManager) {
        String query = "UPDATE manager SET " +
                "name='" + rpManager.name + "', " +
                "address='" + rpManager.address + "', " +
                "city='" + rpManager.city + "', " +
                "state='" + rpManager.state + "', " +
                "zipcode='" + rpManager.zipCode + "', '" +
                " WHERE manager.manager_id = " +
                rpManager.id + ";";
        this.update(query);
    }

    public void deleteManager(int manager_id) {
        String query = "DELETE FROM manager WHERE manager.manager_id=" + manager_id + ";";
        this.update(query);
    }

    //Everything on Service
    private Vector<Service> fetchServiceResults() {
        Vector<Service> results = null;
        Service row = null;

        try {
            while (rs.next()) {
                if (results == null)
                    results = new Vector<Service>(VECTOR_ALLOC_SIZE);
                row = new Service(rs.getInt(1), rs.getString(2), rs.getDouble(3));

                results.add(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Fetching Service Results: " + e.toString(), "Error Fetching Service Results: ", JOptionPane.ERROR_MESSAGE);
            try {
                rs.close();
                stmt.close();
                con.close();
                con = null;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error closing result set, statement and connection: " + e.toString(), "Error Fetching Service Results: ", JOptionPane.ERROR_MESSAGE);
            }
        }
        return results;
    }

    public Vector<Service> retrieveServieTable() {
        String query = "SELECT * FROM service;";
        this.execute(query);
        return this.fetchServiceResults();
    }

    public static void main(String[] args) {
        SQLiteInterface sql1 = new SQLiteInterface();
        sql1.execute("select * from ACCOUNT;");
    }
}
