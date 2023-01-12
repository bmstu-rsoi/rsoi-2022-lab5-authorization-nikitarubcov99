package bmstu.student.rubcov.lab2.hotelbookingsystem.reservationapplication.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HotelsRepo extends JpaRepository<Hotels, Long> {

    Optional<Hotels> findByHotelUid(UUID hotelUid);
    //List<Hotels> findAll(Pageable pageable);

}
