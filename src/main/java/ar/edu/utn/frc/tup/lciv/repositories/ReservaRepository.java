package ar.edu.utn.frc.tup.lciv.repositories;

import ar.edu.utn.frc.tup.lciv.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
}
