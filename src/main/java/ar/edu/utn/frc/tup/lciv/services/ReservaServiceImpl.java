package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.disponibilidadDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.precioDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.requestPostDTO;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import ar.edu.utn.frc.tup.lciv.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ApiExternaService apiExternaService;
    private final ReservaRepository reservaRepository;

    public ReservaServiceImpl(ApiExternaService apiExternaService, ReservaRepository reservaRepository) {
        this.apiExternaService = apiExternaService;
        this.reservaRepository = reservaRepository;
    }

    @Override
    @Transactional
    public Reserva createReserva(requestPostDTO requestPostDTO) {

        validarEntradas(requestPostDTO);

        String estadoReserva = "EXITOSA";
        try {
            disponibilidadDTO disponibilidad = apiExternaService.getDisponibilidad(requestPostDTO.getId_hotel(), requestPostDTO.getTipo_habitacion(), requestPostDTO.getFecha_ingreso(), requestPostDTO.getFecha_salida());
            if (!disponibilidad.getDisponible()) {
                throw new RuntimeException("No hay disponibilidad para la fecha seleccionada");
            }
        } catch (NullPointerException e) {
            estadoReserva = "PENDIENTE";

        }

        precioDTO precio = apiExternaService.getPrecio(requestPostDTO.getId_hotel(), requestPostDTO.getTipo_habitacion());

        double precioEstadia = calcularPrecio(requestPostDTO.getFecha_ingreso(), requestPostDTO.getFecha_salida(), precio.getPrecio_lista());
        double precioConDescuento = aplicarDescuentoPorMedioPago(precioEstadia, requestPostDTO.getMedio_pago());

        double precioFinal = aplicarDescuentoPorTipoCliente(precioConDescuento, requestPostDTO.getId_cliente(), requestPostDTO.getFecha_ingreso(), requestPostDTO.getFecha_salida());

        Reserva reserva = new Reserva();
        reserva.setId_hotel(requestPostDTO.getId_hotel());
        reserva.setId_cliente(requestPostDTO.getId_cliente());
        reserva.setTipo_habitacion(requestPostDTO.getTipo_habitacion());
        reserva.setFecha_ingreso(requestPostDTO.getFecha_ingreso());
        reserva.setFecha_salida(requestPostDTO.getFecha_salida());
        reserva.setMedio_pago(requestPostDTO.getMedio_pago());
        reserva.setPrecio(precioFinal);
        reserva.setEstado_reserva(estadoReserva);

        return reservaRepository.save(reserva);

    }

    @Override
    public Reserva getReservaById(Integer id_reserva) {
        Reserva reserva = reservaRepository.findById(id_reserva).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));
        return reserva;
    }

    public double calcularPrecio(LocalDate fechaIngreso, LocalDate fechaSalida, double precioBasePorNoche) {
        long diasEstadia = ChronoUnit.DAYS.between(fechaIngreso, fechaSalida) + 1;

        double precioTotal = 0;

        for (LocalDate fecha = fechaIngreso; !fecha.isAfter(fechaSalida); fecha = fecha.plusDays(1)) {
            double precioPorDia = calcularPrecioPorTemporada(fecha, precioBasePorNoche);
            precioTotal += precioPorDia;
        }

        return precioTotal;
    }

    public double calcularPrecioPorTemporada(LocalDate fecha, double precioBase) {
        Month mes = fecha.getMonth();

        if (mes == Month.JANUARY || mes == Month.FEBRUARY || mes == Month.JULY || mes == Month.AUGUST) {
            return precioBase * 1.30;
        } else if (mes == Month.MARCH || mes == Month.APRIL || mes == Month.OCTOBER || mes == Month.NOVEMBER) {
            return precioBase * 0.90;
        } else {
            return precioBase;
        }
    }


    public double aplicarDescuentoPorMedioPago(double precio, String medioPago) {
        switch (medioPago.toUpperCase()) {
            case "EFECTIVO":
                return precio * 0.75;
            case "TARJETA_DEBITO":
                return precio * 0.90;
            case "TARJETA_CREDITO":
            default:
                return precio;
        }
    }

    public double aplicarDescuentoPorTipoCliente(double precio, String idCliente, LocalDate fechaIngreso, LocalDate fechaSalida) {
        boolean todasFechasTemporadaBaja = true;
        for (LocalDate fecha = fechaIngreso; !fecha.isAfter(fechaSalida); fecha = fecha.plusDays(1)) {
            Month mes = fecha.getMonth();
            if (mes != Month.MARCH && mes != Month.APRIL && mes != Month.OCTOBER && mes != Month.NOVEMBER) {
                todasFechasTemporadaBaja = false;
                break;
            }
        }

        if ("CUIT".equalsIgnoreCase(idCliente)) {
            return todasFechasTemporadaBaja ? precio * 0.85 : precio * 0.90;
        } else {
            return todasFechasTemporadaBaja ? precio * 0.90 : precio;
        }
    }

    public void validarEntradas(requestPostDTO requestPostDTO) {
        List<String> TIPOS_HABITACION = Arrays.asList("SIMPLE", "DOBLE", "TRIPLE");
        List<String> TIPOS_IDENTIFICACION = Arrays.asList("DNI", "PASAPORTE", "CUIT");
        List<String> MEDIOS_PAGO = Arrays.asList("EFECTIVO", "TARJETA_DEBITO", "TARJETA_CREDITO");

        if (requestPostDTO.getFecha_ingreso().isBefore(LocalDate.now()) ||
                requestPostDTO.getFecha_ingreso().isAfter(requestPostDTO.getFecha_salida())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fechas inválidas");
        }
        if (!TIPOS_HABITACION.contains(requestPostDTO.getTipo_habitacion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de habitación inválido");
        }
        if (!TIPOS_IDENTIFICACION.contains(requestPostDTO.getId_cliente())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de identificación inválido");
        }
        if (!MEDIOS_PAGO.contains(requestPostDTO.getMedio_pago())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medio de pago inválido");
        }
    }


}
