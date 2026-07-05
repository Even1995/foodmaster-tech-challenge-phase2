package br.com.app.foodmaster.services;

import br.com.app.foodmaster.dtos.*;
import br.com.app.foodmaster.entities.Cardapio;
import br.com.app.foodmaster.entities.Restaurante;
import br.com.app.foodmaster.exceptions.RestauranteNaoEncontradoException;
import br.com.app.foodmaster.repositories.RestauranteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;

    public RestauranteService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    private Restaurante criarRestaurante(SalvarRestauranteDto input) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(input.nome());
        restaurante.setEndereco(input.endereco());
        restaurante.setTipoCozinha(input.tipoCozinha());
        restaurante.setHorarioFuncionamento(input.horarioFuncionamento());
        restaurante.setDono(input.dono());

        if (input.cardapio() != null) {
            for (AlterarCardapioDto alterarCardapioDto : input.cardapio()) {
                Cardapio cardapio = criarCardapio(alterarCardapioDto);
                restaurante.getCardapio().add(cardapio);
            }
        }

        return restaurante;
    }

    private void atualizarRestaurante(Restaurante restaurante, AlterarRestauranteDto input) {
        restaurante.setNome(input.nome());
        restaurante.setEndereco(input.endereco());
        restaurante.setTipoCozinha(input.tipoCozinha());
        restaurante.setHorarioFuncionamento(input.horarioFuncionamento());
        restaurante.setDono(input.dono());

        if (input.cardapio() != null) {
            restaurante.getCardapio().clear();
            for (AlterarCardapioDto alterarCardapioDto : input.cardapio()) {
                Cardapio cardapio = criarCardapio(alterarCardapioDto);
                restaurante.getCardapio().add(cardapio);
            }
        }
    }

    private Cardapio criarCardapio(AlterarCardapioDto alterarCardapioDto) {
        Cardapio cardapio = new Cardapio();
        cardapio.setNome(alterarCardapioDto.nome());
        cardapio.setDescricao(alterarCardapioDto.descricao());
        cardapio.setPreco(alterarCardapioDto.preco());
        cardapio.setApenasNoLocal(alterarCardapioDto.apenasNoLocal());
        cardapio.setCaminhoFoto(alterarCardapioDto.caminhoFoto());
        return cardapio;
    }

    public Restaurante salvarRestaurante(SalvarRestauranteDto input) {
        Restaurante restaurante = criarRestaurante(input);
        return restauranteRepository.save(restaurante);
    }

    public void alterarRestaurante(AlterarRestauranteDto input) {
        Restaurante restaurante = restauranteRepository.findById(input.id())
                .orElseThrow(() -> new RestauranteNaoEncontradoException(input.id()));
        atualizarRestaurante(restaurante, input);
        restauranteRepository.save(restaurante);
    }

    public void deleteRestaurante(UUID id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(id));
        restauranteRepository.delete(restaurante);
    }

    public Restaurante getRestaurante(UUID id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(id));
    }
}
