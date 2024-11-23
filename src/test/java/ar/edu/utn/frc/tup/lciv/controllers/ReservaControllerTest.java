package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.requestPostDTO;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import ar.edu.utn.frc.tup.lciv.services.ReservaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ReservaControllerTest {

    @Mock
    private ReservaServiceImpl reservaService;

    @InjectMocks
    private ReservaController reservaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReserva() {
        Reserva expectedReserva = new Reserva();
        expectedReserva.setId(1);
        expectedReserva.setId_hotel(1);
        expectedReserva.setId_cliente("DNI");
        expectedReserva.setTipo_habitacion("SIMPLE");
        expectedReserva.setFecha_ingreso(LocalDate.now().plusDays(1));
        expectedReserva.setFecha_salida(LocalDate.now().plusDays(5));
        expectedReserva.setMedio_pago("EFECTIVO");
        expectedReserva.setPrecio(300.0);
        expectedReserva.setEstado_reserva("Confirmada");

        when(reservaService.createReserva(any())).thenReturn(expectedReserva);

        ResponseEntity<Reserva> response = reservaController.createReserva(new requestPostDTO());

        assertNotNull(response);
        assertEquals(expectedReserva, response.getBody());
    }

    @Test
    void getReservaById() {
        Integer idReserva = 1;
        Reserva expectedReserva = new Reserva();
        expectedReserva.setId(idReserva);
        expectedReserva.setId_hotel(100);
        expectedReserva.setId_cliente("cliente123");
        expectedReserva.setTipo_habitacion("Suite");
        expectedReserva.setFecha_ingreso(LocalDate.of(2024, 1, 10));
        expectedReserva.setFecha_salida(LocalDate.of(2024, 1, 15));
        expectedReserva.setMedio_pago("Tarjeta de crédito");
        expectedReserva.setPrecio(500.0);
        expectedReserva.setEstado_reserva("Confirmada");

        when(reservaService.getReservaById(anyInt())).thenReturn(expectedReserva);

        ResponseEntity<Reserva> response = reservaController.getReservaById(idReserva);

        assertNotNull(response);
        assertEquals(expectedReserva, response.getBody());
    }
}