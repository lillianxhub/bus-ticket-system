package com.busticket.services;

import com.busticket.dao.SeatDAO;
import java.sql.SQLException;
import java.time.LocalDate;

public class SeatService {
    private final SeatDAO seatDAO;

    public SeatService() {
        this.seatDAO = new SeatDAO();
    }

    public int getSeatCodeByID(String seatCode) throws SQLException {
        return seatDAO.getSeatIdByCode(seatCode);
    }

    public boolean isSeatAvailable(int scheduleId, int seatId, LocalDate travelDate) throws SQLException {
        return seatDAO.checkSeat(scheduleId, seatId, travelDate);
    }
}