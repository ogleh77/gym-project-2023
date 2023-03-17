package com.example.gymdesktop2023.dto.main;

import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.service.Users;
import com.example.gymdesktop2023.helpers.CustomException;
import com.example.gymdesktop2023.models.main.CustomerModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;

public class CustomerService {
    private static final CustomerModel customerModel = new CustomerModel();
    private static ObservableList<Customers> allCustomersList;
    private static ObservableList<Customers> offlineCustomers;
    private static ObservableList<Customers> onlineCustomers;

    public static void insertOrUpdateCustomer(Customers customer, boolean newCustomer) throws SQLException {
        try {
            if (newCustomer) {
                insertCustomer(customer);
            } else {
                updateCustomer(customer);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: customers.phone)")) {
                throw new CustomException("Lanbarka " + customer.getPhone() + " hore ayaa loo diwaan geshay fadlan dooro lanbarkale");
            } else {
                throw new CustomException("Khalad ayaaa ka dhacay " + e.getMessage());
            }
        }
    }

    private static void insertCustomer(Customers customer) throws SQLException {
        customerModel.insert(customer);
    }

    private static void updateCustomer(Customers customer) throws SQLException {
        customerModel.update(customer);
    }

    public static ObservableList<Customers> fetchAllCustomer(Users activeUser) throws SQLException {
        if (allCustomersList == null) {
            allCustomersList = customerModel.fetchAllCustomers(activeUser);
        }
        return allCustomersList;
    }

    public static ObservableList<Customers> fetchOfflineCustomer(Users activeUser) throws SQLException {
        if (offlineCustomers == null) {
            offlineCustomers = customerModel.fetchOfflineCustomers(activeUser);
        }
        return offlineCustomers;
    }

    public static ObservableList<Customers> fetchOnlineCustomer(Users activeUser) throws SQLException {
        System.out.println("Online customers Called");
        if (onlineCustomers == null) {
            onlineCustomers = customerModel.fetchOnlineCustomers(activeUser);
            // Collections.sort(allCustomersList);
        }
        return onlineCustomers;
    }

    public static ObservableList<Customers> fetchQualifiedOfflineCustomers(String customerQuery, LocalDate fromDate, LocalDate toDate) throws SQLException {
        String from = fromDate.toString();
        String to = toDate.toString();

        return customerModel.fetchQualifiedOfflineCustomers(customerQuery, from, to);
    }


}
