package br.com.app.foodmaster.controllers;

import br.com.app.foodmaster.dtos.AlterarCardapioDto;
import br.com.app.foodmaster.dtos.SalvarCardapioDto;
import br.com.app.foodmaster.entities.Cardapio;
import br.com.app.foodmaster.entities.Restaurante;
import br.com.app.foodmaster.exceptions.CardapioNaoEncontradoException;
import br.com.app.foodmaster.services.CardapioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CardapioController Tests")
class CardapioControllerTest {

    private CardapioController cardapioController;

    @Mock
    private CardapioService cardapioService;

    private UUID cardapioId;
    private Cardapio cardapio;
    private Restaurante restaurante;
    private SalvarCardapioDto salvarCardapioDto;
    private AlterarCardapioDto alterarCardapioDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardapioController = new CardapioController(cardapioService);

        cardapioId = UUID.randomUUID();

        restaurante = new Restaurante();
        restaurante.setNome("Pizzaria");
        restaurante.setEndereco("Rua Principal");

        cardapio = new Cardapio();
        cardapio.setNome("Pizza Margherita");
        cardapio.setDescricao("Pizza com queijo e tomate");
        cardapio.setPreco(35.50);
        cardapio.setApenasNoLocal(false);
        cardapio.setCaminhoFoto("/images/pizza.jpg");
        cardapio.setRestauranteId(restaurante);

        salvarCardapioDto = new SalvarCardapioDto(
            "Pizza Margherita",
            "Pizza com queijo e tomate",
            35.50,
            false,
            null,
            "/images/pizza.jpg"
        );

        alterarCardapioDto = new AlterarCardapioDto(
            cardapioId,
            "Pizza Margherita",
            "Pizza com queijo e tomate",
            35.50,
            false,
            restaurante,
            "/images/pizza.jpg"
        );
    }

    @Test
    @DisplayName("POST /cardapios - Devera criar e retornar o status CREATED")
    void testSalvarCardapioSucesso() {
        when(cardapioService.salvarCardapio(any(SalvarCardapioDto.class))).thenReturn(cardapio);

        var resultado = cardapioController.salvarCardapio(salvarCardapioDto);

        assertNotNull(resultado);
        assertEquals(201, resultado.getStatusCode().value());
        assertEquals("Pizza Margherita", resultado.getBody().getNome());
        assertEquals(35.50, resultado.getBody().getPreco());
        verify(cardapioService, times(1)).salvarCardapio(any(SalvarCardapioDto.class));
    }

    @Test
    @DisplayName("PUT /cardapios - Devera alterar e retornar status NO_CONTENT")
    void testAlterarCardapioSucesso() {
        doNothing().when(cardapioService).alterarCardapio(any(AlterarCardapioDto.class));

        var resultado = cardapioController.alterarCardapio(alterarCardapioDto);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(cardapioService, times(1)).alterarCardapio(any(AlterarCardapioDto.class));
    }

    @Test
    @DisplayName("DELETE /cardapios/{id} - Devera deletar e retornar status NO_CONTENT")
    void testDeleteCardapioSucesso() {
        doNothing().when(cardapioService).deleteCardapio(cardapioId);

        var resultado = cardapioController.deleteCardapio(cardapioId);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(cardapioService, times(1)).deleteCardapio(cardapioId);
    }

    @Test
    @DisplayName("GET /cardapios/{id} - Devera criar o cardapio e retornar OK")
    void testGetCardapioSucesso() {
        when(cardapioService.getCardapio(cardapioId)).thenReturn(cardapio);

        var resultado = cardapioController.getCardapioById(cardapioId);

        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCode().value());
        assertEquals("Pizza Margherita", resultado.getBody().getNome());
        assertEquals("Pizza com queijo e tomate", resultado.getBody().getDescricao());
        verify(cardapioService, times(1)).getCardapio(cardapioId);
    }

    @Test
    @DisplayName("DELETE /cardapios/{id} - Devera retornar não encontrado")
    void testDeleteCardapioNaoEncontrado() {
        doThrow(new CardapioNaoEncontradoException(cardapioId))
            .when(cardapioService).deleteCardapio(cardapioId);

        assertThrows(CardapioNaoEncontradoException.class,
            () -> cardapioController.deleteCardapio(cardapioId));

        verify(cardapioService, times(1)).deleteCardapio(cardapioId);
    }

    @Test
    @DisplayName("GET /cardapios/{id} - Devera lançar uma exceção e retornar não encontrado")
    void testGetCardapioNaoEncontrado() {
        when(cardapioService.getCardapio(cardapioId))
            .thenThrow(new CardapioNaoEncontradoException(cardapioId));

        assertThrows(CardapioNaoEncontradoException.class,
            () -> cardapioController.getCardapioById(cardapioId));

        verify(cardapioService, times(1)).getCardapio(cardapioId);
    }

    @Test
    @DisplayName("POST /cardapios - Devera salvar o cardapio com preço")
    void testSalvarCardapioComPreco() {
        cardapio.setPreco(50.00);

        when(cardapioService.salvarCardapio(any(SalvarCardapioDto.class))).thenReturn(cardapio);

        var resultado = cardapioController.salvarCardapio(salvarCardapioDto);

        assertNotNull(resultado);
        assertEquals(201, resultado.getStatusCode().value());
        assertNotNull(resultado.getBody().getPreco());
        verify(cardapioService, times(1)).salvarCardapio(any(SalvarCardapioDto.class));
    }

    @Test
    @DisplayName("PUT /cardapios - Devera salvar o cardapio com a flag apenasNoLocal")
    void testAlterarCardapioApenasNoLocal() {
        doNothing().when(cardapioService).alterarCardapio(any(AlterarCardapioDto.class));

        var resultado = cardapioController.alterarCardapio(alterarCardapioDto);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(cardapioService, times(1)).alterarCardapio(any(AlterarCardapioDto.class));
    }

    @Test
    @DisplayName("GET /cardapios/{id} - Devera retornar o cardapio com o caminho da foto")
    void testGetCardapioComFoto() {
        when(cardapioService.getCardapio(cardapioId)).thenReturn(cardapio);

        var resultado = cardapioController.getCardapioById(cardapioId);

        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCode().value());
        assertEquals("/images/pizza.jpg", resultado.getBody().getCaminhoFoto());
        verify(cardapioService, times(1)).getCardapio(cardapioId);
    }
}
