package bmstu.student.rubcov.lab2.hotelbookingsystem.reservationapplication.service;

import bmstu.student.rubcov.lab2.hotelbookingsystem.reservationapplication.models.*;

import java.util.List;
import java.util.UUID;

public interface ReservationService {

    PaginationResponse getHotels(Integer page, Integer size);
    //    UserInfoResponse getMe(String username);
    List<ReservationResponse> getReservations(String username, String bearer);
    ReservationResponse getReservation(UUID reservationUid, String username, String bearer);
    CreateReservationResponse postReservation(String username, CreateReservationRequest createReservationRequest,
                                              String bearer);
    void cancelReservation(UUID reservationUid, String username, String bearer);

}
