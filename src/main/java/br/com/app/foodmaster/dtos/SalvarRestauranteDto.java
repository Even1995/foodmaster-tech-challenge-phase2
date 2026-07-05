package br.com.app.foodmaster.dtos;

import br.com.app.foodmaster.entities.Usuario;

import java.util.List;

public record SalvarRestauranteDto(
        String nome,
        String endereco,
        String tipoCozinha,
        String horarioFuncionamento,
        Usuario dono,
        List<AlterarCardapioDto> cardapio
) {}

