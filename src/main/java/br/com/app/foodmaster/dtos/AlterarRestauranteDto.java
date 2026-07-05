package br.com.app.foodmaster.dtos;

import br.com.app.foodmaster.entities.Usuario;

import java.util.List;
import java.util.UUID;

public record AlterarRestauranteDto(
        UUID id,
        String nome,
        String endereco,
        String tipoCozinha,
        String horarioFuncionamento,
        Usuario dono,
        List<AlterarCardapioDto> cardapio
) {}
