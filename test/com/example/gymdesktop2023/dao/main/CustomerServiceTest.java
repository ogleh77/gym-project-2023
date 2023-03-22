package com.example.gymdesktop2023.dao.main;

import com.example.gymdesktop2023.dao.UserService;
import com.example.gymdesktop2023.entity.main.Customers;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class CustomerServiceTest {

    @Test
    void binarySearch() throws SQLException {
        ObservableList<Customers> customers = CustomerService.fetchAllCustomer(UserService.users().get(0));
        int index = CustomerService.binarySearch(customers, 0, customers.size() - 1, 11);
        System.out.println(index);
    }
}