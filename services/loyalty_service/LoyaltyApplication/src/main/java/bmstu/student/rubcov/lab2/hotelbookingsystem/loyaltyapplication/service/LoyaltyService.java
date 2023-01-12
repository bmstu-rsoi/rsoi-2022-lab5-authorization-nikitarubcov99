package bmstu.student.rubcov.lab2.hotelbookingsystem.loyaltyapplication.service;

import bmstu.student.rubcov.lab2.hotelbookingsystem.loyaltyapplication.models.LoyaltyInfoResponse;

public interface LoyaltyService {

    LoyaltyInfoResponse getLoyaltyInfo(String username);
    LoyaltyInfoResponse increaseLoyalty(String username);
    LoyaltyInfoResponse decreaseLoyalty(String username);

}
