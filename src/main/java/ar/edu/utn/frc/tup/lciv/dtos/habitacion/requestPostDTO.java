package ar.edu.utn.frc.tup.lciv.dtos.habitacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class requestPostDTO {
    private Integer id_hotel;
    private String id_cliente;
    private String tipo_habitacion;
    private LocalDate fecha_ingreso;
    private LocalDate fecha_salida;
    private String medio_pago;

}
