package com.example.gymdesktop2023.dto.main;

import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.main.Payments;
import com.example.gymdesktop2023.helpers.CustomException;
import com.example.gymdesktop2023.models.main.PaymentModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class PaymentService {
    private static final PaymentModel paymentModel = new PaymentModel();

    public static void insertPayment(Customers customer) throws SQLException {
        try {
            String customerGander = customer.getGander();
            paymentModel.insertPayment(customer.getPhone(), customerGander, customer.getPayments().get(0));
        } catch (SQLException e) {
            throw new CustomException("Khalad ayaaa ka dhacay " + e.getMessage() + " " +
                    "\n fadlan dib u search garee customerka kadibna payment usamee");
        }

    }

    public static void holdPayment(Payments payment, int allowedDays) throws SQLException {
        try {
            LocalDate exp = payment.getExpDate();
            LocalDate pendingDate = LocalDate.now();
            int daysRemind = Period.between(pendingDate, exp).getDays();

            if (daysRemind <= allowedDays) {
                throw new CustomException("Fadlan lama xidhi karo event kan waayo\n" +
                        " wuxu ka hoseya wakhtiga loo asteyey oo ah " + allowedDays + " malmood " +
                        "Payment kana waxa u hadhay " + daysRemind + " Malmood");
            }
            paymentModel.holdPayment(payment, daysRemind);
        } catch (SQLException e) {
            throw new CustomException("Khalad ayaaa ka dhacay " + e.getMessage());
        }
    }

    public static void unHoldPayment(Payments payment) throws SQLException {
        try {
            paymentModel.unHold(payment.getPaymentID());
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
    }

    public static ObservableList<Payments> fetchAllCustomersPayments(String customerPhone) throws SQLException {
        return paymentModel.fetchAllCustomersPayments(customerPhone);
    }

    public static ObservableList<Payments> fetchCustomersOnlinePayment(String customerPhone) throws SQLException {
        return paymentModel.fetchCustomersOnlinePayment(customerPhone);
    }

    public static ObservableList<Payments> fetchCustomersOfflinePayment(String customerPhone) throws SQLException {
        return paymentModel.fetchCustomersOfflinePayment(customerPhone);
    }
    public static ObservableList<Payments> fetchQualifiedOfflinePayment(String customerPhone, String fromDate, String toDate) throws SQLException {
        return paymentModel.fetchQualifiedOfflinePayment(customerPhone, fromDate, toDate);
    }
}
