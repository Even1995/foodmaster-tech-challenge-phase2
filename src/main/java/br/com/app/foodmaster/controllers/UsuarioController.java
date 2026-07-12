package br.com.app.foodmaster.controllers;

import br.com.app.foodmaster.dtos.AlterarUsuarioDto;
import br.com.app.foodmaster.dtos.SalvarUsuarioDto;
import br.com.app.foodmaster.entities.Usuario;
import br.com.app.foodmaster.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @PostMapping
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody SalvarUsuarioDto input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvarUsuario(input));}

    @PutMapping
    public ResponseEntity<Void> alterarUsuario(@RequestBody AlterarUsuarioDto input) {
        usuarioService.alterarUsuario(input);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable UUID id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.getUsuario(id));
    }
}
