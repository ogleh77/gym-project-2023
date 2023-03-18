package com.example.gymdesktop2023.dto;

import com.example.gymdesktop2023.dto.main.CustomerService;
import com.example.gymdesktop2023.entity.main.CustomerBuilder;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.service.Users;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class CustomerServiceTest {
    private final Users user = new Users(1, null, null, null, "Male",
            null, "ogleh", null, null, "super_admin");

    @Test
    void insertOrUpdateCustomer() throws SQLException {

        Customers customer = new CustomerBuilder()
                .setFirstName("Luul")
                .setMiddleName("Cusmaan")
                .setLastName("Maxamuud")
                .setAddress("Daalo")
                .setImage(null)
                .setPhone("4303923")
                .setWhoAdded("Lulka")
                .setGander("Female")
                .setShift("Afternoon")
                .build();

        CustomerService.insertOrUpdateCustomer(customer, true);
    }

    @Test
    void fetchAllCustomer() throws SQLException {

        System.out.println(CustomerService.fetchAllCustomer(user));
    }

    @Test
    void fetchOfflineCustomer() throws SQLException {
        System.out.println(CustomerService.fetchOfflineCustomer(user));
    }

    @Test
    void fetchOnlineCustomer() throws SQLException {

        System.out.println(CustomerService.fetchOnlineCustomer(user));
    }


    @Test
    void fetchQualifiedOfflineCustomers() throws SQLException {

        System.out.println(CustomerService.fetchQualifiedOfflineCustomers("SELECT * FROM customers",LocalDate.now().minusDays(200)
                ,LocalDate.now().plusDays(200)));
    }


}