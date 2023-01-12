package bmstu.student.rubcov.lab2.hotelbookingsystem.paymentapplication.service;

import bmstu.student.rubcov.lab2.hotelbookingsystem.paymentapplication.models.PaymentInfo;

import java.util.UUID;

public interface PaymentService {

    UUID postPayment(UUID paymentUid, Integer price);
    PaymentInfo getPayment(UUID paymentUid);
    PaymentInfo cancelPayment(UUID paymentUid);

}
