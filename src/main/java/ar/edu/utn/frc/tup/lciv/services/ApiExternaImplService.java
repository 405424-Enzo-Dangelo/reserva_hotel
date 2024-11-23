package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.disponibilidadDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.precioDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class ApiExternaImplService implements ApiExternaService{

    private final RestTemplate restTemplate;
    //private final String url = "http://localhost:8080/habitacion";
    private final String url = "http://hotel-server:8080/habitacion";

    private static final String RESILIENCE4J_INSTANCE_NAME = "microCircuitBreaker";
    private Integer counter = 0;

    @Autowired
    public ApiExternaImplService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = "fallbackGetDisponibilidad")
    public disponibilidadDTO getDisponibilidad(Integer idHotel, String tipoHabitacion, LocalDate fechaDesde, LocalDate fechaHasta) {
        counter++;
        System.out.println("Execution N: " + counter + " - Calling API");
        disponibilidadDTO disponibilidad = restTemplate.getForEntity(url + "/disponibilidad?hotel_id=" + idHotel + "&tipo_habitacion=" + tipoHabitacion + "&fecha_desde=" + fechaDesde + "&fecha_hasta=" + fechaHasta, disponibilidadDTO.class).getBody();
        return disponibilidad;
    }

    @Override
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = "fallbackGetPrecio")
    public precioDTO getPrecio(Integer idHotel, String tipoHabitacion) {
        counter++;
        System.out.println("Execution N: " + counter + " - Calling API");
        precioDTO precio = restTemplate.getForEntity(url + "/precio?hotel_id=" + idHotel + "&tipo_habitacion=" + tipoHabitacion, precioDTO.class).getBody();
        return precio;
    }

    public disponibilidadDTO fallbackGetDisponibilidad (Exception ex) {
        counter++;
        System.out.println("Execution N: " + counter+" - Fallback method");
        throw new RuntimeException("Error manejado en el Fallback method");
    }

    public precioDTO fallbackGetPrecio (Exception ex) {
        counter++;
        System.out.println("Execution N: " + counter+" - Fallback method");
        throw new RuntimeException("Error manejado en el Fallback method");
    }
}
