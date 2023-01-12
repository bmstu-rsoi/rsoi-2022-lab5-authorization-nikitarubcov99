package bmstu.student.rubcov.lab2.hotelbookingsystem.paymentapplication.webcontroller;

import bmstu.student.rubcov.lab2.hotelbookingsystem.paymentapplication.models.ErrorResponse;
import bmstu.student.rubcov.lab2.hotelbookingsystem.paymentapplication.models.PaymentInfo;
import bmstu.student.rubcov.lab2.hotelbookingsystem.paymentapplication.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services/payment")
@Tag(name = "Payment REST API operations")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {

        this.paymentService = paymentService;

    }


    @Operation(summary = "Post new payment", operationId = "postPayment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Payment",
                    headers = { @Header(name = "Location", description = "Path to new Payment") }),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping
    public ResponseEntity<String> postPayment(@RequestParam UUID paymentUid, @RequestParam Integer price) {

        UUID createdUid = paymentService.postPayment(paymentUid, price);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{createdUid}").buildAndExpand(createdUid).toUri()
        ).build();

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
    @GetMapping("/{paymentUid}")
    public PaymentInfo getPayment(@PathVariable UUID paymentUid) {

        return  paymentService.getPayment(paymentUid);

    }

    @Operation(summary = "Cancel Payment by UUID", operationId = "cancelPayment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment for UUID was canceled"),
            @ApiResponse(responseCode = "404", description = "Not found uncanceled payment for UUID",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{paymentUid}")
    public PaymentInfo cancelPayment(@PathVariable UUID paymentUid) {

        return paymentService.cancelPayment(paymentUid);

    }

}
