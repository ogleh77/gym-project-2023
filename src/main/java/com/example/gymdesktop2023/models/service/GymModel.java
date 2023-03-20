package com.example.gymdesktop2023.models.service;

import com.example.gymdesktop2023.dao.BoxService;
import com.example.gymdesktop2023.entity.Gym;
import com.example.gymdesktop2023.helpers.DbConnection;

import java.sql.*;

public class GymModel {
    public static Connection connection = DbConnection.getConnection();

    public void update(Gym gym) throws SQLException {
        String updateQuery = "UPDATE gym SET gym_name=?,fitness_cost=?,poxing_cost=?,vipBox=?," +
                "pending_date=?,max_discount=?,zaad_merchant=?," +
                "edahab_merchant=? WHERE gym_id=" + gym.getGymId();
        PreparedStatement ps = connection.prepareStatement(updateQuery);
        ps.setString(1, gym.getGymName());
        ps.setDouble(2, gym.getFitnessCost());
        ps.setDouble(3, gym.getPoxingCost());
        ps.setDouble(4, gym.getBoxCost());
        ps.setInt(5, gym.getPendingDate());
        ps.setDouble(6, gym.getMaxDiscount());
        ps.setInt(7, gym.getZaad());
        ps.setInt(8, gym.geteDahab());

        ps.executeUpdate();
        ps.close();
        System.out.println("Updated");
    }

    public Gym currentGym() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM gym");

        Gym currentGym = new Gym(rs.getInt("gym_id"), rs.getString("gym_name"),
                rs.getDouble("fitness_cost"), rs.getDouble("poxing_cost"),
                rs.getDouble("vipBox"), rs.getDouble("max_discount"), rs.getInt("pending_date"),
                rs.getInt("zaad_merchant"), rs.getInt("edahab_merchant"));

        currentGym.getVipBoxes().setAll(BoxService.fetchBoxes());
        rs.close();
        statement.close();
        return currentGym;

    }
}
