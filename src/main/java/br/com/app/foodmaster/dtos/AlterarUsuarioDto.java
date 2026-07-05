package br.com.app.foodmaster.dtos;

import br.com.app.foodmaster.entities.TipoUsuario;

import java.util.UUID;

public record AlterarUsuarioDto(UUID id, String nome, String email, String login, String senha, TipoUsuario tipoUsuario, EnderecoDto endereco) {
}
