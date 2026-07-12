package br.com.app.foodmaster.exceptions;

import java.util.UUID;

public class TipoUsuarioNaoEncontradoException extends RuntimeException {
    public TipoUsuarioNaoEncontradoException(UUID id) {
        super("TipoUsuario com ID " + id + " não encontrado.");
    }
}
