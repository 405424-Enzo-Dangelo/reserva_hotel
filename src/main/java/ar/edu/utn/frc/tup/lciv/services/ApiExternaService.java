package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.disponibilidadDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.precioDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface ApiExternaService {
    disponibilidadDTO getDisponibilidad(Integer idHotel, String tipoHabitacion, LocalDate fechaDesde, LocalDate fechaHasta);
    precioDTO getPrecio(Integer idHotel, String tipoHabitacion);
}
