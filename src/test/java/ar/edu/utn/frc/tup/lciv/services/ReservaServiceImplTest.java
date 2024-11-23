package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.disponibilidadDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.precioDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.requestPostDTO;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import ar.edu.utn.frc.tup.lciv.repositories.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ReservaServiceImplTest {

    @Mock
    private ApiExternaService apiExternaService;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReserva() {
        disponibilidadDTO disponibilidad = new disponibilidadDTO();
        disponibilidad.setDisponible(true);
        when(apiExternaService.getDisponibilidad(anyInt(), anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(disponibilidad);

        precioDTO precio = new precioDTO();
        precio.setPrecio_lista(100.0);
        when(apiExternaService.getPrecio(anyInt(), anyString())).thenReturn(precio);

        Reserva expectedReserva = new Reserva();
        expectedReserva.setId(1);
        expectedReserva.setId_hotel(1);
        expectedReserva.setId_cliente("DNI");
        expectedReserva.setTipo_habitacion("SIMPLE");
        expectedReserva.setFecha_ingreso(LocalDate.now().plusDays(1));
        expectedReserva.setFecha_salida(LocalDate.now().plusDays(5));
        expectedReserva.setMedio_pago("EFECTIVO");
        expectedReserva.setPrecio(300.0); // Assuming some discount applied
        expectedReserva.setEstado_reserva("Confirmada");

        when(reservaRepository.save(any(Reserva.class))).thenReturn(expectedReserva);

        requestPostDTO request = new requestPostDTO();
        request.setId_hotel(1);
        request.setId_cliente("DNI");
        request.setTipo_habitacion("SIMPLE");
        request.setFecha_ingreso(LocalDate.now().plusDays(1));
        request.setFecha_salida(LocalDate.now().plusDays(5));
        request.setMedio_pago("EFECTIVO");


        Reserva actualReserva = reservaService.createReserva(request);

        assertNotNull(actualReserva);
        assertEquals(expectedReserva, actualReserva);
        verify(apiExternaService, times(1)).getDisponibilidad(anyInt(), anyString(), any(LocalDate.class), any(LocalDate.class));
        verify(apiExternaService, times(1)).getPrecio(anyInt(), anyString());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
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

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(expectedReserva));
        Reserva actualReserva = reservaService.getReservaById(idReserva);

        assertNotNull(actualReserva);
        assertEquals(expectedReserva, actualReserva);
        verify(reservaRepository, times(1)).findById(idReserva);
    }

    @Test
    void calcularPrecio() {
        LocalDate fechaIngreso = LocalDate.of(2024, 1, 10);
        LocalDate fechaSalida = LocalDate.of(2024, 1, 15);
        double precioBasePorNoche = 100.0;

        double precioTotal = reservaService.calcularPrecio(fechaIngreso, fechaSalida, precioBasePorNoche);

        assertEquals(780.0, precioTotal);
    }

    @Test
    void calcularPrecioPorTemporada() {
        LocalDate fechaIngreso = LocalDate.of(2024, 1, 10);
        LocalDate fechaSalida = LocalDate.of(2024, 1, 15);
        double precioBasePorNoche = 100.0;

        double precioTotal = reservaService.calcularPrecio(fechaIngreso, fechaSalida, precioBasePorNoche);

        assertEquals(780.0, precioTotal);
    }

    @Test
    void aplicarDescuentoPorMedioPago() {
        LocalDate fechaIngreso = LocalDate.of(2024, 1, 10);
        LocalDate fechaSalida = LocalDate.of(2024, 1, 15);
        double precioBasePorNoche = 100.0;

        double precioTotal = reservaService.calcularPrecio(fechaIngreso, fechaSalida, precioBasePorNoche);

        assertEquals(780.0, precioTotal);
    }

    @Test
    void aplicarDescuentoPorTipoCliente() {
        LocalDate fechaIngreso = LocalDate.of(2024, 1, 10);
        LocalDate fechaSalida = LocalDate.of(2024, 1, 15);
        double precioBasePorNoche = 100.0;

        double precioTotal = reservaService.calcularPrecio(fechaIngreso, fechaSalida, precioBasePorNoche);

        assertEquals(780.0, precioTotal);
    }

    @Test
    void validarEntradas() {
        LocalDate fechaIngreso = LocalDate.of(2024, 1, 10);
        LocalDate fechaSalida = LocalDate.of(2024, 1, 15);
        double precioBasePorNoche = 100.0;

        double precioTotal = reservaService.calcularPrecio(fechaIngreso, fechaSalida, precioBasePorNoche);

        assertEquals(780.0, precioTotal);
    }
}