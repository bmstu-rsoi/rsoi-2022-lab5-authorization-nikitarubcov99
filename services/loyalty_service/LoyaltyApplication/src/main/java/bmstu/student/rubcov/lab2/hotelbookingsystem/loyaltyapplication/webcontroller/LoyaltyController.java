package bmstu.student.rubcov.lab2.hotelbookingsystem.loyaltyapplication.webcontroller;

import bmstu.student.rubcov.lab2.hotelbookingsystem.loyaltyapplication.models.ErrorResponse;
import bmstu.student.rubcov.lab2.hotelbookingsystem.loyaltyapplication.models.LoyaltyInfoResponse;
import bmstu.student.rubcov.lab2.hotelbookingsystem.loyaltyapplication.service.LoyaltyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services/loyalty")
@Tag(name = "Loyalty REST API operations")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    public LoyaltyController (LoyaltyService loyaltyService) {

        this.loyaltyService = loyaltyService;

    }

    @Operation(summary = "Get Loyalty Info", operationId = "getLoyaltyInfo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loyalty Info",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoyaltyInfoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found Loyalty for Username",
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/me")
    public LoyaltyInfoResponse getLoyaltyInfo(@RequestHeader("X-User-Name") String username) {

        return loyaltyService.getLoyaltyInfo(username);

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
    @PostMapping("/me")
    public LoyaltyInfoResponse incrementLoyalty(@RequestHeader("X-User-Name") String username) {

        return loyaltyService.increaseLoyalty(username);

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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/me")
    public LoyaltyInfoResponse decrementLoyalty(@RequestHeader("X-User-Name") String username) {

        return loyaltyService.decreaseLoyalty(username);

    }

}
