package br.com.app.foodmaster.exceptions;

import java.util.UUID;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(UUID id) {
        super("Usuário com ID " + id + " não encontrado.");
    }
}
