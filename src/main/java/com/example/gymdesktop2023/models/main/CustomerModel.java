package com.example.gymdesktop2023.models.main;

import com.example.gymdesktop2023.dto.main.PaymentService;
import com.example.gymdesktop2023.entity.main.CustomerBuilder;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.main.Payments;
import com.example.gymdesktop2023.entity.service.Users;
import com.example.gymdesktop2023.helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerModel {

    private static final Connection connection = DbConnection.getConnection();

    public void insert(Customers customer) throws SQLException {
        String insertQuery = "INSERT INTO customers(first_name, middle_name, last_name, phone, gander, shift, address, image, weight, who_added)\n" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        insertOrUpdateStatement(customer, insertQuery, true);
    }

    public void update(Customers customer) throws SQLException {
        String updateQuery = "UPDATE customers SET first_name=?,middle_name=?,last_name=?,phone=?,gander=?,shift=?, " +
                "address=?,image=?,weight=? WHERE customer_id=" + customer.getCustomerId();
        insertOrUpdateStatement(customer, updateQuery, false);
    }

    public ObservableList<Customers> fetchAllCustomers(Users activeUser) throws SQLException {
        System.out.println("Called customers");

        ObservableList<Customers> customers = FXCollections.observableArrayList();

        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments = PaymentService.fetchAllCustomersPayments(customerPhone);
            getCustomers(customers, rs, payments);
        }

        rs.close();
        statement.close();
        return customers;
    }

    public ObservableList<Customers> fetchOfflineCustomers(Users activeUser) throws SQLException {
        System.out.println("Called offline customers");
        ObservableList<Customers> customers = FXCollections.observableArrayList();

        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments = PaymentService.fetchCustomersOfflinePayment(customerPhone);
            if (payments == null || payments.isEmpty()) {
                continue;
            }
            getCustomers(customers, rs, payments);
        }
        rs.close();
        statement.close();
        return customers;
    }

    public ObservableList<Customers> fetchOnlineCustomers(Users activeUser) throws SQLException {
        System.out.println("Called offline customers");
        ObservableList<Customers> customers = FXCollections.observableArrayList();

        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments = PaymentService.fetchCustomersOnlinePayment(customerPhone);
            if (payments == null || payments.isEmpty()) {
                continue;
            }
            getCustomers(customers, rs, payments);
        }
        rs.close();
        statement.close();
        return customers;
    }

    public ObservableList<Customers> fetchQualifiedOfflineCustomers(String customerQuery, String fromDate, String toDate) throws SQLException {

        ObservableList<Customers> customers = FXCollections.observableArrayList();

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(customerQuery);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments = PaymentService.fetchQualifiedOfflinePayment(customerPhone, fromDate, toDate);

            if (payments == null || payments.isEmpty()) {
                continue;
            }
            Customers customer = new CustomerBuilder()
                    .setCustomerId(rs.getInt("customer_id"))
                    .setFirstName(rs.getString("first_name"))
                    .setMiddleName(rs.getString("middle_name"))
                    .setLastName(rs.getString("last_name"))
                    .setGander(rs.getString("gander"))
                    .setAddress(rs.getString("address"))
                    .setPhone(rs.getString("phone"))
                    .setImage(rs.getString("image"))
                    .setWhoAdded(rs.getString("who_added"))
                    .setShift(rs.getString("shift"))
                    .setWeight(rs.getDouble("weight")).build();

            customer.setPayments(payments);
            customers.add(customer);

        }

        return customers;

    }

    //---------------––Helpers---------------------
    private void insertOrUpdateStatement(Customers customer, String query, boolean insert) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, customer.getFirstName());
        ps.setString(2, customer.getMiddleName());
        ps.setString(3, customer.getLastName());
        ps.setString(4, customer.getPhone());
        ps.setString(5, customer.getGander());
        ps.setString(6, customer.getShift());
        ps.setString(7, customer.getAddress());
        ps.setString(8, customer.getImage());
        ps.setDouble(9, customer.getWeight());
        if (insert) {
            ps.setString(10, customer.getWhoAdded());
            System.out.println("Customer added");
        } else {
            System.out.println("Customer Updated..");
        }
        ps.executeUpdate();
        ps.close();
    }

    private String fetchByRoleAndGander(String gander, String role) {

        String fetchQuery = "SELECT * FROM customers WHERE gander='" + gander + "' ORDER BY customer_id ";

        if (role.equals("super_admin")) {
            System.out.println("Active customer is " + role);
            fetchQuery = "SELECT * FROM customers ORDER BY customer_id ";
        }
        System.out.println(fetchQuery);
        return fetchQuery;
    }

    private void getCustomers(ObservableList<Customers> customers, ResultSet rs, ObservableList<Payments> payments) throws SQLException {


        Customers customer = new CustomerBuilder()
                .setCustomerId(rs.getInt("customer_id"))
                .setFirstName(rs.getString("first_name"))
                .setMiddleName(rs.getString("middle_name"))
                .setLastName(rs.getString("last_name"))
                .setGander(rs.getString("gander"))
                .setAddress(rs.getString("address"))
                .setPhone(rs.getString("phone"))
                .setImage(rs.getString("image"))
                .setWhoAdded(rs.getString("who_added"))
                .setShift(rs.getString("shift"))
                .setWeight(rs.getDouble("weight")).build();

        customer.setPayments(payments);
        customers.add(customer);
    }
}
