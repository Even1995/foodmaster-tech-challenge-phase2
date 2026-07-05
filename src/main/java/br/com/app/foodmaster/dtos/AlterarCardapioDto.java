package br.com.app.foodmaster.dtos;

import br.com.app.foodmaster.entities.Restaurante;

import java.util.UUID;

public record AlterarCardapioDto(
        UUID id,
        String nome,
        String descricao,
        Double preco,
        Boolean apenasNoLocal,
        Restaurante restaurante,
        String caminhoFoto
) {}

