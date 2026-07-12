package br.com.app.foodmaster.exceptions;

import java.util.UUID;

public class CardapioNaoEncontradoException extends RuntimeException {
    public CardapioNaoEncontradoException(UUID id) {
        super("Cardápio com ID " + id + " não encontrado.");
    }
}