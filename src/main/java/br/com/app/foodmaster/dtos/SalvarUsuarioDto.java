package br.com.app.foodmaster.dtos;

import br.com.app.foodmaster.entities.TipoUsuario;

public record SalvarUsuarioDto(String nome, String email, String login, String senha, TipoUsuario tipoUsuario, EnderecoDto enderecoDto) {
}
