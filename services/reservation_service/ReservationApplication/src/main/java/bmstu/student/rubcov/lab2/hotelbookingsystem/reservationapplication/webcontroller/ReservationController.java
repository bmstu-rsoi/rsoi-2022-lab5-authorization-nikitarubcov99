package bmstu.student.rubcov.lab2.hotelbookingsystem.reservationapplication.webcontroller;

import bmstu.student.rubcov.lab2.hotelbookingsystem.reservationapplication.models.*;
import bmstu.student.rubcov.lab2.hotelbookingsystem.reservationapplication.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services/reservation")
@Tag(name = "Reservation REST API operations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {

        this.reservationService = reservationService;

    }

    @Operation(summary = "Get Hotels on page", operationId = "getHotels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotels on page",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Page out of range",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("/hotels")
    PaginationResponse getHotels(@RequestParam Integer page, @RequestParam Integer size) {

        if ((page < 1) || (size < 1))
            throw new IllegalStateException("Bad request params: page and size must be greater than 0");
        return reservationService.getHotels(page, size);

    }

    //    UserInfoResponse getMe(String username);

    @Operation(summary = "Get all Reservations for user", operationId = "getReservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations for user",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class))) })
    })
    @GetMapping("/reservations")
    List<ReservationResponse> getReservations(@RequestHeader("X-User-Name") String username,
                                              @RequestHeader("Authorization") String bearer) {

        return reservationService.getReservations(username, bearer);

    }

    @Operation(summary = "Get Reservation for user by UUID", operationId = "getReservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation with UUID",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Reservation with UUID not found for user",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("/reservations/{reservationUid}")
    ReservationResponse getReservation(@PathVariable UUID reservationUid,
                                       @RequestHeader("X-User-Name") String username,
                                       @RequestHeader("Authorization") String bearer) {

        return reservationService.getReservation(reservationUid, username, bearer);

    }

    @Operation(summary = "Create reservation for user", operationId = "postReservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation details",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ValidationErrorResponse.class)) })
    })
    @PostMapping("/reservations")
    CreateReservationResponse postReservation(@RequestHeader("X-User-Name") String username,
                                              @RequestHeader("Authorization") String bearer,
                                              @Valid @RequestBody CreateReservationRequest createReservationRequest) {

        return reservationService.postReservation(username, createReservationRequest, bearer);

    }

    @Operation(summary = "Cancel Reservation for user by UUID", operationId = "cancelReservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation with UUID was canceled",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Reservation with UUID not found for user",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/reservations/{reservationUid}")
    void cancelReservation(@PathVariable UUID reservationUid,
                           @RequestHeader("X-User-Name") String username,
                           @RequestHeader("Authorization") String bearer) {

        reservationService.cancelReservation(reservationUid, username, bearer);

    }

}

