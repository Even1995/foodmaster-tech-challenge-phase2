package br.com.app.foodmaster.controllers;

import br.com.app.foodmaster.dtos.SalvarCardapioDto;
import br.com.app.foodmaster.dtos.AlterarCardapioDto;
import br.com.app.foodmaster.entities.Cardapio;
import br.com.app.foodmaster.services.CardapioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cardapios")
public class CardapioController {

    private final CardapioService cardapioService;

    public CardapioController(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    @PostMapping
    public ResponseEntity<Cardapio> salvarCardapio(@RequestBody SalvarCardapioDto input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardapioService.salvarCardapio(input));
    }

    @PutMapping
    public ResponseEntity<Void> alterarCardapio(@RequestBody AlterarCardapioDto input) {
        cardapioService.alterarCardapio(input);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardapio(@PathVariable UUID id) {
        cardapioService.deleteCardapio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cardapio> getCardapioById(@PathVariable UUID id) {
        return ResponseEntity.ok(cardapioService.getCardapio(id));
    }
}
