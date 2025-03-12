package com.busticket.services;

import com.busticket.dao.SeatDAO;
import java.sql.SQLException;

public class SeatService {
    private final SeatDAO seatDAO;

    public SeatService() {
        this.seatDAO = new SeatDAO();
    }

    public int getSeatCodeByID(String seatCode) throws SQLException {
        return seatDAO.getSeatIdByCode(seatCode);
    }
}