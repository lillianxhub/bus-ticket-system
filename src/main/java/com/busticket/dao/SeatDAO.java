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

    public int getSeatById(int seatID) {
        return seatID;
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
