package br.com.app.foodmaster.controllers;

import br.com.app.foodmaster.dtos.SalvarRestauranteDto;
import br.com.app.foodmaster.dtos.AlterarRestauranteDto;
import br.com.app.foodmaster.entities.Restaurante;
import br.com.app.foodmaster.services.RestauranteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @PostMapping
    public ResponseEntity<Restaurante> salvarRestaurante(@RequestBody SalvarRestauranteDto input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteService.salvarRestaurante(input));
    }

    @PutMapping
    public ResponseEntity<Void> alterarRestaurante(@RequestBody AlterarRestauranteDto input) {
        restauranteService.alterarRestaurante(input);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurante(@PathVariable UUID id) {
        restauranteService.deleteRestaurante(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> getRestauranteById(@PathVariable UUID id) {
        return ResponseEntity.ok(restauranteService.getRestaurante(id));
    }
}
