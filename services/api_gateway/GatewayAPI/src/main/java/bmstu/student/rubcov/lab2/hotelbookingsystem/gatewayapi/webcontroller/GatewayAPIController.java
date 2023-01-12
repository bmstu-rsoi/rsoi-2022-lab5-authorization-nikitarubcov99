package bmstu.student.rubcov.lab2.hotelbookingsystem.gatewayapi.webcontroller;

import bmstu.student.rubcov.lab2.hotelbookingsystem.gatewayapi.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "@Gateway API")
public class GatewayAPIController {

    private static final String LOYALTYBASEURL = "http://loyalty-service:8050/api/v1/services/loyalty";
    private static final String PAYMENTBASEURL = "http://payment-service:8060/api/v1/services/payment";
    private static final String RESERVATIONBASEURL = "http://reservation-service:8070/api/v1/services/reservation";
    private static final String X_USER_NAME = "X-User-Name";


    @Operation(summary = "Login", operationId = "login")
    @GetMapping("/authorize")
    public ResponseEntity<String> authorize() {

        return null;

    }

    @Operation(summary = "Callback", operationId = "callback")
    @GetMapping("/callback")
    public ResponseEntity<String> callback() {

        return null;

    }

    //------------------------- Loyalty Service --------------------------------------------

    @Operation(summary = "Get Loyalty Info", operationId = "getLoyaltyInfo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loyalty Info",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoyaltyInfoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found Loyalty for Username",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/loyalty")
    public ResponseEntity<String> getLoyaltyInfo(Authentication authentication) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = LOYALTYBASEURL + "/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_USER_NAME, authentication.getName());
        HttpEntity<?> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.GET,
                request,
                String.class
        );

    }

    @Operation(summary = "Increment reservation count by 1", operationId = "incrementLoyalty")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loyalty Info",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoyaltyInfoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found Loyalty for Username",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/loyalty")
    public ResponseEntity<String> incrementLoyalty(Authentication authentication) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = LOYALTYBASEURL + "/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_USER_NAME, authentication.getName());
        HttpEntity<?> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.POST,
                request,
                String.class
        );

    }

    @Operation(summary = "Decrement reservation count by 1", operationId = "decrementLoyalty")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Loyalty Info"),
            @ApiResponse(responseCode = "404", description = "Not found Loyalty for Username",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Illegal modification",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/loyalty")
    public ResponseEntity<String> decrementLoyalty(Authentication authentication) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = LOYALTYBASEURL + "/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_USER_NAME, authentication.getName());
        headers.set("Authorization", authentication.toString());
        HttpEntity<?> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.DELETE,
                request,
                String.class
        );

    }


    //------------------------- Payment Service --------------------------------------------

    @Operation(summary = "Post new payment", operationId = "postPayment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Payment",
                    headers = { @Header(name = "Location", description = "Path to new Payment") }),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping("/payment")
    public ResponseEntity<String> postPayment(@RequestParam UUID paymentUid, @RequestParam Integer price) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = PAYMENTBASEURL +
                String.format("?paymentUid=%s&price=%d", paymentUid.toString(), price);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.POST,
                request,
                String.class
        );

    }

    @Operation(summary = "Get Payment by UUID", operationId = "getPayment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment for UUID",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaymentInfo.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found payment for UUID",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("/payment/{paymentUid}")
    public ResponseEntity<String> getPayment(@PathVariable UUID paymentUid) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = PAYMENTBASEURL +
                String.format("/%s", paymentUid.toString());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.GET,
                request,
                String.class
        );

    }

    @Operation(summary = "Cancel Payment by UUID", operationId = "cancelPayment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment for UUID was canceled"),
            @ApiResponse(responseCode = "404", description = "Not found uncanceled payment for UUID",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @DeleteMapping("/payment/{paymentUid}")
    public ResponseEntity<String> cancelPayment(@PathVariable UUID paymentUid, Authentication authentication) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = PAYMENTBASEURL +
                String.format("/%s", paymentUid.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authentication.toString());
        HttpEntity<?> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.DELETE,
                request,
                String.class
        );

    }


    //------------------------- Reservation Service ----------------------------------------

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
    ResponseEntity<String> getHotels(@RequestParam Integer page, @RequestParam Integer size) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = RESERVATIONBASEURL + "/hotels" +
                String.format("?page=%d&size=%d", page, size);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.GET,
                request,
                String.class
        );

    }

    @Operation(summary = "Get all Reservations for user", operationId = "getReservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations for user",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class))) })
    })
    @GetMapping("/reservations")
    ResponseEntity<String> getReservations(Authentication authentication,
                                           @RequestHeader("Authorization") String bearer) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = RESERVATIONBASEURL + "/reservations";
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_USER_NAME, authentication.getName());
        headers.set("Authorization", bearer);
        HttpEntity<?> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.GET,
                request,
                String.class
        );

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
    ResponseEntity<String> getReservation(@PathVariable UUID reservationUid,
                                          Authentication authentication,
                                          @RequestHeader("Authorization") String bearer) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = RESERVATIONBASEURL + "/reservations" +
                String.format("/%s", reservationUid.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_USER_NAME, authentication.getName());
        headers.set("Authorization", bearer);
        HttpEntity<?> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.GET,
                request,
                String.class
        );

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
    ResponseEntity<String> postReservation(Authentication authentication,
                                           @RequestHeader("Authorization") String bearer,
                                           @Valid @RequestBody CreateReservationRequest createReservationRequest) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = RESERVATIONBASEURL + "/reservations";
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_USER_NAME, authentication.getName());
        headers.set("Authorization", bearer);
        HttpEntity<CreateReservationRequest> request = new HttpEntity<>(createReservationRequest, headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.POST,
                request,
                String.class
        );

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
    ResponseEntity<String> cancelReservation(@PathVariable UUID reservationUid,
                                             @RequestHeader("Authorization") String bearer,
                                             Authentication authentication) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = RESERVATIONBASEURL + "/reservations" +
                String.format("/%s", reservationUid.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_USER_NAME, authentication.getName());
        headers.set("Authorization", bearer);
        HttpEntity<?> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                resourceUrl,
                HttpMethod.DELETE,
                request,
                String.class
        );

    }

    //------------------------------- Aggregate --------------------------------------------

    @Operation(summary = "Get all information about user", operationId = "getMe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations for user",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserInfoResponse.class)) })
    })
    @GetMapping("/me")
    UserInfoResponse getMe(Authentication authentication,
                           @RequestHeader("Authorization") String bearer) {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = RESERVATIONBASEURL + "/reservations";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearer);
        headers.set(X_USER_NAME, authentication.getName());
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> reservations = restTemplate.exchange(
                resourceUrl,
                HttpMethod.GET,
                request,
                String.class
        );

        if (reservations.getStatusCode() != HttpStatus.OK)
            throw new InternalError(String.format("getReservations returned code: %s", reservations.getStatusCode()));

        ObjectMapper objectMapper = new ObjectMapper();
        List<ReservationResponse> reservationResponses;
        try {
            reservationResponses = objectMapper.readValue(reservations.getBody(),
                    objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, ReservationResponse.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        RestTemplate loyaltyRestTemplate = new RestTemplate();
        resourceUrl = LOYALTYBASEURL + "/me";
        headers = new HttpHeaders();
        headers.set(X_USER_NAME, authentication.getName());
        request = new HttpEntity<>(headers);
        ResponseEntity<String> loyalty = restTemplate.exchange(
                resourceUrl,
                HttpMethod.GET,
                request,
                String.class
        );

        if (loyalty.getStatusCode() != HttpStatus.OK)
            throw new InternalError(String.format("getLoyalty returned code: %s", loyalty.getStatusCode()));

        objectMapper = new ObjectMapper();
        LoyaltyInfoResponse loyaltyInfoResponse;
        try {
            loyaltyInfoResponse = objectMapper.readValue(loyalty.getBody(), LoyaltyInfoResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return new UserInfoResponse(reservationResponses, loyaltyInfoResponse);

    }

}
