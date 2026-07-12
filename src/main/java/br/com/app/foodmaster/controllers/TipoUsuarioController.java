package br.com.app.foodmaster.controllers;

import br.com.app.foodmaster.dtos.SalvarTipoUsuarioDto;
import br.com.app.foodmaster.dtos.AlterarTipoUsuarioDto;
import br.com.app.foodmaster.entities.TipoUsuario;
import br.com.app.foodmaster.services.TipoUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tipos-usuario")
public class TipoUsuarioController {

    private final TipoUsuarioService tipoUsuarioService;

    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService) {
        this.tipoUsuarioService = tipoUsuarioService;
    }

    @PostMapping
    public ResponseEntity<TipoUsuario> salvarTipoUsuario(@RequestBody SalvarTipoUsuarioDto input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoUsuarioService.salvarTipoUsuario(input));
    }

    @PutMapping
    public ResponseEntity<Void> alterarTipoUsuario(@RequestBody AlterarTipoUsuarioDto input) {
        tipoUsuarioService.alterarTipoUsuario(input);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoUsuario(@PathVariable UUID id) {
        tipoUsuarioService.deleteTipoUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuario> getTipoUsuarioById(@PathVariable UUID id) {
        return ResponseEntity.ok(tipoUsuarioService.getTipoUsuario(id));
    }
}
