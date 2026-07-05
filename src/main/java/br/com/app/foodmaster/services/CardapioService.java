package br.com.app.foodmaster.services;

import br.com.app.foodmaster.dtos.AlterarCardapioDto;
import br.com.app.foodmaster.dtos.SalvarCardapioDto;
import br.com.app.foodmaster.entities.Cardapio;
import br.com.app.foodmaster.exceptions.CardapioNaoEncontradoException;
import br.com.app.foodmaster.repositories.CardapioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CardapioService {

    private final CardapioRepository cardapioRepository;

    public CardapioService(CardapioRepository cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }

    private Cardapio criarCardapio(SalvarCardapioDto input) {
        Cardapio cardapio = new Cardapio();
        cardapio.setNome(input.nome());
        cardapio.setDescricao(input.descricao());
        cardapio.setPreco(input.preco());
        cardapio.setApenasNoLocal(input.apenasNoLocal());
        cardapio.setCaminhoFoto(input.caminhoFoto());

        return cardapio;
    }

    private void atualizarDadosCardapio(Cardapio cardapio, AlterarCardapioDto dto) {
        cardapio.setNome(dto.nome());
        cardapio.setDescricao(dto.descricao());
        cardapio.setPreco(dto.preco());
        cardapio.setApenasNoLocal(dto.apenasNoLocal());
        cardapio.setRestauranteId(dto.restaurante());
        cardapio.setCaminhoFoto(dto.caminhoFoto());
    }

    public Cardapio salvarCardapio(SalvarCardapioDto input) {
        Cardapio cardapio = criarCardapio(input);
        return cardapioRepository.save(cardapio);
    }

    public void alterarCardapio(AlterarCardapioDto dto) {
        Cardapio cardapio = cardapioRepository.findById(dto.id())
                .orElseThrow(() -> new CardapioNaoEncontradoException(dto.id()));
        atualizarDadosCardapio(cardapio, dto);
        cardapioRepository.save(cardapio);
    }

    public void deleteCardapio(UUID id) {
        Cardapio cardapio = cardapioRepository.findById(id)
                .orElseThrow(() -> new CardapioNaoEncontradoException(id));
        cardapioRepository.delete(cardapio);
    }

    public Cardapio getCardapio(UUID id) {
        return cardapioRepository.findById(id)
                .orElseThrow(() -> new CardapioNaoEncontradoException(id));
    }
}

