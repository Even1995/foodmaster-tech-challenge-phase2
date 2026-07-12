package br.com.app.foodmaster.services;

import br.com.app.foodmaster.dtos.AlterarCardapioDto;
import br.com.app.foodmaster.dtos.SalvarCardapioDto;
import br.com.app.foodmaster.entities.Cardapio;
import br.com.app.foodmaster.entities.Restaurante;
import br.com.app.foodmaster.exceptions.CardapioNaoEncontradoException;
import br.com.app.foodmaster.repositories.CardapioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CardapioService Tests")
class CardapioServiceTest {

    private CardapioService cardapioService;

    @Mock
    private CardapioRepository cardapioRepository;

    private UUID cardapioId;
    private Cardapio cardapioMock;
    private Restaurante restauranteMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardapioService = new CardapioService(cardapioRepository);

        cardapioId = UUID.randomUUID();
        
        restauranteMock = new Restaurante();
        restauranteMock.setNome("Pizzaria");
        restauranteMock.setEndereco("Rua Principal");
        restauranteMock.setTipoCozinha("Italiana");
        
        cardapioMock = new Cardapio();
        cardapioMock.setNome("Pizza Margherita");
        cardapioMock.setDescricao("Pizza com queijo e tomate");
        cardapioMock.setPreco(35.50);
        cardapioMock.setApenasNoLocal(false);
        cardapioMock.setCaminhoFoto("/images/pizza.jpg");
    }

    @Test
    @DisplayName("Devera salvar o cardapio com sucesso")
    void testSalvarCardapio() {
        SalvarCardapioDto salvarCardapioDto = new SalvarCardapioDto(
            "Sushi",
            "Sushi de salmão fresco",
            45.00,
            false,
            null,
            "/images/sushi.jpg"
        );

        when(cardapioRepository.save(any(Cardapio.class))).thenReturn(cardapioMock);

        Cardapio resultado = cardapioService.salvarCardapio(salvarCardapioDto);

        assertNotNull(resultado);
        assertEquals("Pizza Margherita", resultado.getNome());
        verify(cardapioRepository, times(1)).save(any(Cardapio.class));
    }

    @Test
    @DisplayName("Devera alterar o cardapio com sucesso")
    void testAlterarCardapio() {
        AlterarCardapioDto alterarCardapioDto = new AlterarCardapioDto(
            cardapioId,
            "Pizza Especial",
            "Pizza com ingredients premium",
            55.00,
            true,
            restauranteMock,
            "/images/pizza_premium.jpg"
        );

        when(cardapioRepository.findById(cardapioId)).thenReturn(Optional.of(cardapioMock));
        when(cardapioRepository.save(any(Cardapio.class))).thenReturn(cardapioMock);

        cardapioService.alterarCardapio(alterarCardapioDto);

        verify(cardapioRepository, times(1)).findById(cardapioId);
        verify(cardapioRepository, times(1)).save(any(Cardapio.class));
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar atualizar um cardapio não encontrado")
    void testAlterarCardapioNaoEncontrado() {
        AlterarCardapioDto alterarCardapioDto = new AlterarCardapioDto(
            cardapioId,
            "Prato",
            "Descrição",
            20.00,
            false,
            restauranteMock,
            "/images/prato.jpg"
        );

        when(cardapioRepository.findById(cardapioId)).thenReturn(Optional.empty());

        assertThrows(CardapioNaoEncontradoException.class,
            () -> cardapioService.alterarCardapio(alterarCardapioDto));

        verify(cardapioRepository, times(1)).findById(cardapioId);
        verify(cardapioRepository, never()).save(any(Cardapio.class));
    }

    @Test
    @DisplayName("Devera deletar o cardapio com sucesso")
    void testDeleteCardapio() {
        when(cardapioRepository.findById(cardapioId)).thenReturn(Optional.of(cardapioMock));

        cardapioService.deleteCardapio(cardapioId);

        verify(cardapioRepository, times(1)).findById(cardapioId);
        verify(cardapioRepository, times(1)).delete(cardapioMock);
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar deletar um cardapio não encontrado")
    void testDeleteCardapioNaoEncontrado() {
        when(cardapioRepository.findById(cardapioId)).thenReturn(Optional.empty());

        assertThrows(CardapioNaoEncontradoException.class,
            () -> cardapioService.deleteCardapio(cardapioId));

        verify(cardapioRepository, times(1)).findById(cardapioId);
        verify(cardapioRepository, never()).delete(any(Cardapio.class));
    }

    @Test
    @DisplayName("Devera retornar o cardapio por ID")
    void testGetCardapio() {
        when(cardapioRepository.findById(cardapioId)).thenReturn(Optional.of(cardapioMock));

        Cardapio resultado = cardapioService.getCardapio(cardapioId);

        assertNotNull(resultado);
        assertEquals("Pizza Margherita", resultado.getNome());
        verify(cardapioRepository, times(1)).findById(cardapioId);
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar obter um cardapio não encontrado")
    void testGetCardapioNaoEncontrado() {
        when(cardapioRepository.findById(cardapioId)).thenReturn(Optional.empty());

        assertThrows(CardapioNaoEncontradoException.class,
            () -> cardapioService.getCardapio(cardapioId));

        verify(cardapioRepository, times(1)).findById(cardapioId);
    }

    @Test
    @DisplayName("Devera salvar o cardapio com o flag de apenas local")
    void testSalvarCardapioApenasNoLocal() {
        SalvarCardapioDto salvarCardapioDto = new SalvarCardapioDto(
            "Prato Executivo",
            "Prato do dia",
            25.00,
            true,
            null,
            "/images/prato_exec.jpg"
        );

        when(cardapioRepository.save(any(Cardapio.class))).thenReturn(cardapioMock);

        Cardapio resultado = cardapioService.salvarCardapio(salvarCardapioDto);

        assertNotNull(resultado);
        verify(cardapioRepository, times(1)).save(any(Cardapio.class));
    }

    @Test
    @DisplayName("Devera alterar o cardapio com um preço atualizado")
    void testAlterarCardapioComPrecoAtualizado() {
        AlterarCardapioDto alterarCardapioDto = new AlterarCardapioDto(
            cardapioId,
            "Pizza Margherita",
            "Pizza melhorada",
            40.00,
            false,
            restauranteMock,
            "/images/pizza_v2.jpg"
        );

        when(cardapioRepository.findById(cardapioId)).thenReturn(Optional.of(cardapioMock));
        when(cardapioRepository.save(any(Cardapio.class))).thenReturn(cardapioMock);

        cardapioService.alterarCardapio(alterarCardapioDto);

        verify(cardapioRepository, times(1)).findById(cardapioId);
        verify(cardapioRepository, times(1)).save(any(Cardapio.class));
    }

    @Test
    @DisplayName("Devera alterar o cardapio com o ID do restaurante")
    void testAlterarCardapioComRestaurante() {
        AlterarCardapioDto alterarCardapioDto = new AlterarCardapioDto(
            cardapioId,
            "Hambúrguer",
            "Hambúrguer artesanal",
            30.00,
            false,
            restauranteMock,
            "/images/burger.jpg"
        );

        when(cardapioRepository.findById(cardapioId)).thenReturn(Optional.of(cardapioMock));
        when(cardapioRepository.save(any(Cardapio.class))).thenReturn(cardapioMock);

        cardapioService.alterarCardapio(alterarCardapioDto);

        verify(cardapioRepository, times(1)).findById(cardapioId);
        verify(cardapioRepository, times(1)).save(any(Cardapio.class));
    }
}
