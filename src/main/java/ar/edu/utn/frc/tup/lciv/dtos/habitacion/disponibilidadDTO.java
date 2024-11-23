package ar.edu.utn.frc.tup.lciv.dtos.habitacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class disponibilidadDTO {

    private Integer id_hotel;
    private String tipo_habitacion;
    private LocalDate fecha_desde;
    private LocalDate fecha_hasta;
    private Boolean disponible;
}
