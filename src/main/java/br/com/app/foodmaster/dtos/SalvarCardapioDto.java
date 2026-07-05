package br.com.app.foodmaster.dtos;

import java.util.UUID;

public record SalvarCardapioDto(
        String nome,
        String descricao,
        Double preco,
        Boolean apenasNoLocal,
        UUID restaurante,
        String caminhoFoto
) {}


