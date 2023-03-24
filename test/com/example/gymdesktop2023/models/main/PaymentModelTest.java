package com.example.gymdesktop2023.models.main;

import com.example.gymdesktop2023.dao.main.CustomerService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class PaymentModelTest {

    @Test
    void fetchQualifiedOfflinePayment() throws SQLException {

        System.out.println(CustomerService.fetchQualifiedOfflineCustomers("SELECT * FROM customers",
                LocalDate.now().minusDays(50), LocalDate.now().plusDays(50)));


    }
}