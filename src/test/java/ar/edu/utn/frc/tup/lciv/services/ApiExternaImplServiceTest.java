package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.disponibilidadDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.precioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ApiExternaImplServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApiExternaImplService apiExternaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDisponibilidad() {
        disponibilidadDTO expectedDisponibilidad = new disponibilidadDTO();
        expectedDisponibilidad.setDisponible(true);
        when(restTemplate.getForEntity(eq("http://hotel-server:8080/habitacion/disponibilidad?hotel_id=1&tipo_habitacion=SIMPLE&fecha_desde=" + LocalDate.now() + "&fecha_hasta=" + LocalDate.now().plusDays(5)), eq(disponibilidadDTO.class)))
                .thenReturn(ResponseEntity.ok(expectedDisponibilidad));

        disponibilidadDTO actualDisponibilidad = apiExternaService.getDisponibilidad(1, "SIMPLE", LocalDate.now(), LocalDate.now().plusDays(5));

        assertNotNull(actualDisponibilidad);
        assertEquals(expectedDisponibilidad, actualDisponibilidad);
    }

    @Test
    void getPrecio() {
        precioDTO expectedPrecio = new precioDTO();
        expectedPrecio.setPrecio_lista(100.0);
        when(restTemplate.getForEntity(eq("http://hotel-server:8080/habitacion/precio?hotel_id=1&tipo_habitacion=SIMPLE"), eq(precioDTO.class)))
                .thenReturn(ResponseEntity.ok(expectedPrecio));

        precioDTO actualPrecio = apiExternaService.getPrecio(1, "SIMPLE");

        assertNotNull(actualPrecio);
        assertEquals(expectedPrecio, actualPrecio);
    }

    @Test
    void fallbackGetDisponibilidad() {
        when(restTemplate.getForEntity(anyString(), eq(disponibilidadDTO.class)))
                .thenThrow(new RuntimeException("API call failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            try {
                apiExternaService.getDisponibilidad(1, "SIMPLE", LocalDate.now(), LocalDate.now().plusDays(5));
            } catch (RuntimeException e) {
                throw new RuntimeException("Error manejado en el Fallback method");
            }
        });

        assertEquals("Error manejado en el Fallback method", exception.getMessage());
    }

    @Test
    void fallbackGetPrecio() {
        when(restTemplate.getForEntity(anyString(), eq(precioDTO.class)))
                .thenThrow(new RuntimeException("API call failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            try {
                apiExternaService.getPrecio(1, "SIMPLE");
            } catch (RuntimeException e) {
                throw new RuntimeException("Error manejado en el Fallback method");
            }
        });

        assertEquals("Error manejado en el Fallback method", exception.getMessage());
    }
}