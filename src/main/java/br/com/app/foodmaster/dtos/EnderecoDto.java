package br.com.app.foodmaster.dtos;

public record EnderecoDto(
        String rua,
        String numero,
        String cidade,
        String estado,
        String cep
) {}

