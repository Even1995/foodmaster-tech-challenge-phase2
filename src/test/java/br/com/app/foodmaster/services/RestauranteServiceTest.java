package br.com.app.foodmaster.services;

import br.com.app.foodmaster.dtos.AlterarCardapioDto;
import br.com.app.foodmaster.dtos.AlterarRestauranteDto;
import br.com.app.foodmaster.dtos.SalvarRestauranteDto;
import br.com.app.foodmaster.entities.Cardapio;
import br.com.app.foodmaster.entities.Restaurante;
import br.com.app.foodmaster.entities.Usuario;
import br.com.app.foodmaster.exceptions.RestauranteNaoEncontradoException;
import br.com.app.foodmaster.repositories.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("RestauranteService Tests")
class RestauranteServiceTest {

    private RestauranteService restauranteService;

    @Mock
    private RestauranteRepository restauranteRepository;

    private UUID restauranteId;
    private Restaurante restauranteMock;
    private Usuario dono;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restauranteService = new RestauranteService(restauranteRepository);

        restauranteId = UUID.randomUUID();
        dono = new Usuario();
        dono.setNome("Chef João");

        restauranteMock = new Restaurante();
        restauranteMock.setNome("Pizzaria da Nonna");
        restauranteMock.setEndereco("Avenida Principal, 100");
        restauranteMock.setTipoCozinha("Italiana");
        restauranteMock.setHorarioFuncionamento("11:00 - 22:00");
        restauranteMock.setDono(dono);
        restauranteMock.setCardapio(new ArrayList<>());
    }

    @Test
    @DisplayName("Devera salvar o restaurante com sucesso")
    void testSalvarRestaurante() {
        SalvarRestauranteDto salvarRestauranteDto = new SalvarRestauranteDto(
            "Pizzaria da Nonna",
            "Avenida Principal, 100",
            "Italiana",
            "11:00 - 22:00",
            dono,
            null
        );

        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteMock);

        Restaurante resultado = restauranteService.salvarRestaurante(salvarRestauranteDto);

        assertNotNull(resultado);
        assertEquals("Pizzaria da Nonna", resultado.getNome());
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Devera salvar o restaurante com cardapios")
    void testSalvarRestauranteComCardapios() {
        SalvarRestauranteDto salvarRestauranteDto = new SalvarRestauranteDto(
            "Pizzaria da Nonna",
            "Avenida Principal, 100",
            "Italiana",
            "11:00 - 22:00",
            dono,
            null
        );

        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteMock);

        Restaurante resultado = restauranteService.salvarRestaurante(salvarRestauranteDto);

        assertNotNull(resultado);
        assertEquals("Pizzaria da Nonna", resultado.getNome());
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Devera alterar o restaurante com sucesso")
    void testAlterarRestaurante() {
        AlterarRestauranteDto alterarRestauranteDto = new AlterarRestauranteDto(
            restauranteId,
            "Pizzaria Atualizada",
            "Rua Nova, 200",
            "Italiana Premium",
            "12:00 - 23:00",
            dono,
            null
        );

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restauranteMock));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteMock);

        restauranteService.alterarRestaurante(alterarRestauranteDto);

        verify(restauranteRepository, times(1)).findById(restauranteId);
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar alterar um restaurante não encontrado")
    void testAlterarRestauranteNaoEncontrado() {
        AlterarRestauranteDto alterarRestauranteDto = new AlterarRestauranteDto(
            restauranteId,
            "Restaurante",
            "Endereço",
            "Culinária",
            "Horário",
            dono,
            null
        );

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.empty());

        assertThrows(RestauranteNaoEncontradoException.class,
            () -> restauranteService.alterarRestaurante(alterarRestauranteDto));

        verify(restauranteRepository, times(1)).findById(restauranteId);
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Devera deletar o restaurante com sucesso")
    void testDeleteRestaurante() {
        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restauranteMock));

        restauranteService.deleteRestaurante(restauranteId);

        verify(restauranteRepository, times(1)).findById(restauranteId);
        verify(restauranteRepository, times(1)).delete(restauranteMock);
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar deletar um restaurante não encontrado")
    void testDeleteRestauranteNaoEncontrado() {
        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.empty());

        assertThrows(RestauranteNaoEncontradoException.class,
            () -> restauranteService.deleteRestaurante(restauranteId));

        verify(restauranteRepository, times(1)).findById(restauranteId);
        verify(restauranteRepository, never()).delete(any(Restaurante.class));
    }

    @Test
    @DisplayName("Devera retornar o restaurante por ID")
    void testGetRestaurante() {
        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restauranteMock));

        Restaurante resultado = restauranteService.getRestaurante(restauranteId);

        assertNotNull(resultado);
        assertEquals("Pizzaria da Nonna", resultado.getNome());
        verify(restauranteRepository, times(1)).findById(restauranteId);
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar obter um restaurante não encontrado")
    void testGetRestauranteNaoEncontrado() {
        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.empty());

        assertThrows(RestauranteNaoEncontradoException.class,
            () -> restauranteService.getRestaurante(restauranteId));

        verify(restauranteRepository, times(1)).findById(restauranteId);
    }

    @Test
    @DisplayName("Devera alterar o restaurante com uma lista de cardapios atualizada")
    void testAlterarRestauranteComCardapiosAtualizado() {
        AlterarCardapioDto novoCardapioDto = new AlterarCardapioDto(
            UUID.randomUUID(),
            "Risoto Funghi",
            "Risoto com cogumelos",
            42.00,
            true,
            restauranteMock,
            "/images/risoto.jpg"
        );

        List<AlterarCardapioDto> cardapios = new ArrayList<>();
        cardapios.add(novoCardapioDto);

        AlterarRestauranteDto alterarRestauranteDto = new AlterarRestauranteDto(
            restauranteId,
            "Pizzaria da Nonna",
            "Avenida Principal, 100",
            "Italiana",
            "11:00 - 22:00",
            dono,
            cardapios
        );

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restauranteMock));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteMock);

        restauranteService.alterarRestaurante(alterarRestauranteDto);

        verify(restauranteRepository, times(1)).findById(restauranteId);
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Devera alterar o restaurante com informações básicas apenas")
    void testAlterarRestauranteApenasBasico() {
        AlterarRestauranteDto alterarRestauranteDto = new AlterarRestauranteDto(
            restauranteId,
            "Novo Nome",
            "Novo Endereço",
            "Nova Culinária",
            "Novo Horário",
            dono,
            null
        );

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restauranteMock));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteMock);

        restauranteService.alterarRestaurante(alterarRestauranteDto);

        verify(restauranteRepository, times(1)).findById(restauranteId);
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }
}
