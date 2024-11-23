package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.requestPostDTO;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import ar.edu.utn.frc.tup.lciv.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/reserva")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<Reserva> createReserva (@RequestBody requestPostDTO requestPostDTO) {
        try {
            Reserva reserva = reservaService.createReserva(requestPostDTO);
            return new ResponseEntity<>(reserva, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id_reserva}")
    public ResponseEntity<Reserva> getReservaById (@PathVariable Integer id_reserva) {
        try {
            Reserva reserva = reservaService.getReservaById(id_reserva);
            return new ResponseEntity<>(reserva, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
