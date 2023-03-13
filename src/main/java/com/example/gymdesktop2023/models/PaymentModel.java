package com.example.gymdesktop2023.models;

import com.example.gymdesktop2023.dto.BoxService;
import com.example.gymdesktop2023.entity.main.Payments;
import com.example.gymdesktop2023.helpers.CustomException;
import com.example.gymdesktop2023.helpers.DbConnection;

import java.sql.*;

public class PaymentModel {
    private static final Connection connection = DbConnection.getConnection();

    public void insertPayment(String customerPhone, String customerGender, Payments payment) throws SQLException {
        connection.setAutoCommit(false);

        try {
            String insertPaymentQuery = "INSERT INTO payments(exp_date, amount_paid, paid_by,"
                    + "discount,poxing,box_fk, customer_phone_fk) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(insertPaymentQuery);
            ps.setString(1, payment.getExpDate().toString());
            ps.setDouble(2, payment.getAmountPaid());
            ps.setString(3, payment.getPaidBy());
            ps.setDouble(4, payment.getDiscount());
            ps.setBoolean(5, payment.isPoxing());

            if (payment.getBox() == null) {
                ps.setString(6, null);
            } else {
                ps.setInt(6, payment.getBox().getBoxId());
                BoxService.updateBox(payment.getBox());
            }

            ps.setString(7, customerPhone);
            ps.executeUpdate();
            connection.commit();


        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void holdPayment(Payments payment, int daysRemain) throws SQLException {
        connection.setAutoCommit(false);

        String holdQuery = "INSERT INTO pending(days_remain,payment_fk)" + "VALUES (" + daysRemain + "," + payment.getPaymentID() + ")";

        try (Statement statement = connection.createStatement()) {
            String paymentQuery = "UPDATE payments SET is_online=false,pending=true WHERE payment_id=" + payment.getPaymentID();
            if (payment.getBox() != null) {
                System.out.println("Payment has a box");
                paymentQuery = "UPDATE payments SET is_online=false,pending=true,box_fk=null WHERE payment_id=" + payment.getPaymentID();
                BoxService.updateBox(payment.getBox());
            }else{
                System.out.println("Payment dosnt have a box");
            }

            statement.addBatch(holdQuery);
            statement.addBatch(paymentQuery);
            statement.executeBatch();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }


    }

    public void unHold(int paymentID) throws SQLException {
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        try {

            int daysRemain = daysRemain(paymentID);

            if (daysRemain != 0) {

                String deleteQuery = "DELETE FROM pending WHERE payment_fk=" + paymentID;

                String unPendPayment = "UPDATE payments SET is_online=true, pending=false," +
                        "exp_date='" + daysRemain + "' WHERE payment_id=" + paymentID;

                statement.addBatch(deleteQuery);
                statement.addBatch(unPendPayment);
                statement.executeBatch();

                connection.commit();

            } else {
                throw new CustomException("Paymentkan lama xayarin fadlan iska hubi");
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }


    }


    //Fetch Payments---------------------


    private int daysRemain(int paymentID) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM pending WHERE payment_fk=" + paymentID);
        if (rs.next()) {
            int daysRemain = rs.getInt("days_remain");
            System.out.println("Founded");
            return daysRemain;
        } else {
            System.out.println("Not exist");
        }
        statement.close();
        rs.close();
        return 0;
    }
}
