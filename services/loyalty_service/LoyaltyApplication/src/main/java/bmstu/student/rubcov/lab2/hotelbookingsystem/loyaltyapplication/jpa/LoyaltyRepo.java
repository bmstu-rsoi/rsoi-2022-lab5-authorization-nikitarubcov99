package bmstu.student.rubcov.lab2.hotelbookingsystem.loyaltyapplication.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyRepo extends JpaRepository<Loyalty, Long> {

    Optional<Loyalty> findByUsername(String username);

}
