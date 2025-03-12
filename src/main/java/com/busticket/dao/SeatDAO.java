package com.busticket.dao;

import com.busticket.config.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

public class SeatDAO {
    private Connection connection;

    public SeatDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findBySeatId(int seatId) throws SQLException {
        String sql = "SELECT seatID FROM seats WHERE seatID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, seatId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("seatID");
                } else {
                    throw new SQLException("Invalid seat number: " + seatId);
                }
            }
        }
    }

    public String findBySeatCode(String seatCode) throws SQLException {
        String sql = "SELECT seatCode FROM seats WHERE seatCode = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, seatCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("seatCode");
                } else {
                    throw new SQLException("Invalid seat number: " + seatCode);
                }
            }
        }
    }

    public int getSeatIdByCode(String seatCode) throws SQLException {
        String sql = "SELECT seatID FROM seats WHERE seatCode = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, seatCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("seatID");
                } else {
                    throw new SQLException("Invalid seat number: " + seatCode);
                }
            }
        }
    }


    public boolean isSeatAvailable(int scheduleId, int seatId, LocalDate travelDate) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE scheduleID = ? AND seatID = ? AND travelDate = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            stmt.setInt(2, seatId);
            stmt.setDate(3, Date.valueOf(travelDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Return true if no booking exists
                }
            }
        }
        return false;
    }

}
