package br.com.app.foodmaster.dtos;

import java.util.UUID;

public record AlterarTipoUsuarioDto(
        UUID id,
        String nomeTipo
) {}