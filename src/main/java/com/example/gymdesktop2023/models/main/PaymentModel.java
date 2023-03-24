package com.example.gymdesktop2023.models.main;

import com.example.gymdesktop2023.dao.BoxService;
import com.example.gymdesktop2023.entity.Box;
import com.example.gymdesktop2023.entity.main.PaymentBuilder;
import com.example.gymdesktop2023.entity.main.Payments;
import com.example.gymdesktop2023.helpers.CustomException;
import com.example.gymdesktop2023.helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class PaymentModel {
    private static final Connection connection = DbConnection.getConnection();

    public void insertPayment(String customerPhone, String customerGender, Payments payment) throws SQLException {
        connection.setAutoCommit(false);

        try {
            String insertPaymentQuery = "INSERT INTO payments(exp_date, amount_paid, paid_by," + "discount,poxing,box_fk, customer_phone_fk,month) VALUES (?,?,?,?,?,?,?,?)";
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
            ps.setString(8, payment.getMonth());
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
            } else {
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

    public void unHold(Payments payment) throws SQLException {
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        try {

            int daysRemain = daysRemain(payment.getPaymentID());

            if (daysRemain != 0) {

                LocalDate remainedDays = LocalDate.now().plusDays(daysRemain);

                String deleteQuery = "DELETE FROM pending WHERE payment_fk=" + payment.getPaymentID();

                String unPendPayment = "UPDATE payments SET is_online=true, pending=false," + "exp_date='" + remainedDays + "' WHERE payment_id=" + payment.getPaymentID();

                statement.addBatch(deleteQuery);
                statement.addBatch(unPendPayment);
                statement.executeBatch();

                connection.commit();
                payment.setExpDate(LocalDate.now().plusDays(daysRemain));
            } else {
                throw new CustomException("Paymentkan lama xayarin fadlan iska hubi");
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }


    }


    //Fetch Payments---------------------

    public ObservableList<Payments> fetchAllCustomersPayments(String phone) throws SQLException {
        //-------Fetch payments according to customer that belongs--------tested......

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        Payments payment = null;
        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk=" + phone + " ORDER BY exp_date DESC");

        return getPayments(payments, statement, rs);
    }

    public ObservableList<Payments> fetchCustomersOnlinePayment(String customerPhone) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        Payments payment = null;
        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk=" + customerPhone + "  AND pending=false AND is_online=true");

        return getPayments(payments, statement, rs);
    }

    public ObservableList<Payments> fetchCustomersOfflinePayment(String customerPhone) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk=" + customerPhone + "  AND pending=false AND is_online=false");


        return getPayments(payments, statement, rs);

    }

    public ObservableList<Payments> fetchQualifiedOfflinePayment(String customerPhone, String fromDate, String toDate) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk='" + customerPhone + "'" + " AND is_online=false AND pending=false " + "AND exp_date between '" + fromDate + "' AND '" + toDate + "';";

        ResultSet rs = statement.executeQuery(query);

        getPayments(payments, statement, rs);
        statement.close();
        rs.close();
        return payments;

    }
    //----------------------------Helpers----------------––--------

    private ObservableList<Payments> getPayments(ObservableList<Payments> payments, Statement statement, ResultSet rs) throws SQLException {
        Payments payment;
        while (rs.next()) {

            Box box = null;
            if (rs.getString("box_fk") != null) {
                box = new Box(rs.getInt("box_id"), rs.getString("box_name"), rs.getBoolean("is_ready"));
            }
            payment = getPayments(rs);
            payment.setBox(box);
            payments.add(payment);

        }
        statement.close();
        rs.close();
        return payments;
    }

    private Payments getPayments(ResultSet rs) throws SQLException {
        Payments payment;
        payment = new PaymentBuilder().setPaymentID(rs.getInt("payment_id")).setPaymentDate(rs.getString("payment_date")).setExpDate(LocalDate.parse(rs.getString("exp_date"))).setAmountPaid(rs.getDouble("amount_paid")).setPaidBy(rs.getString("paid_by")).setPoxing(rs.getBoolean("poxing")).setDiscount(rs.getDouble("discount")).setCustomerFK(rs.getString("customer_phone_fk")).setOnline(rs.getBoolean("is_online")).setYear(rs.getString("year")).setPending(rs.getBoolean("pending")).setMonth(rs.getString("month")).build();
        return payment;
    }

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

    public void offPayment(Payments payment) throws SQLException {
        connection.setAutoCommit(false);
        try {
            Statement statement = connection.createStatement();
            String query = "UPDATE payments SET is_online=false WHERE payment_id=" + payment.getPaymentID();
            if (payment.getBox() != null) {
                BoxService.updateBox(payment.getBox());
            }
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            throw new CustomException("Khalad ayaa dhacay mmarka lama off garayn paymentkan");
        }


        // TODO: 09/03/2023 Make paymenr off insha Allah
    }
}
