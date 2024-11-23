package ar.edu.utn.frc.tup.lciv.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer id_hotel;

    private String id_cliente;

    private String tipo_habitacion;

    private LocalDate fecha_ingreso;

    private LocalDate fecha_salida;

    private String medio_pago;

    private Double precio;

    private String estado_reserva;
}
