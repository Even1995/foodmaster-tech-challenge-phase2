package br.com.app.foodmaster.exceptions;

import java.util.UUID;

public class RestauranteNaoEncontradoException extends RuntimeException {
    public RestauranteNaoEncontradoException(UUID id) {
        super("Restaurante com ID " + id + " não encontrado.");
    }
}