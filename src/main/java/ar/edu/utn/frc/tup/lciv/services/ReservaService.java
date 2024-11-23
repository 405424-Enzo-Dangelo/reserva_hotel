package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.requestPostDTO;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import org.springframework.stereotype.Service;

@Service
public interface ReservaService {
    Reserva createReserva(requestPostDTO requestPostDTO);
    Reserva getReservaById(Integer id_reserva);
}
