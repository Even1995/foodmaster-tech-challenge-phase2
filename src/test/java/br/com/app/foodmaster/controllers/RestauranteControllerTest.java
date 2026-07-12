package br.com.app.foodmaster.controllers;

import br.com.app.foodmaster.dtos.AlterarRestauranteDto;
import br.com.app.foodmaster.dtos.SalvarRestauranteDto;
import br.com.app.foodmaster.entities.Restaurante;
import br.com.app.foodmaster.entities.Usuario;
import br.com.app.foodmaster.exceptions.RestauranteNaoEncontradoException;
import br.com.app.foodmaster.services.RestauranteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("RestauranteController Tests")
class RestauranteControllerTest {

    private RestauranteController restauranteController;

    @Mock
    private RestauranteService restauranteService;

    private UUID restauranteId;
    private Restaurante restaurante;
    private Usuario dono;
    private SalvarRestauranteDto salvarRestauranteDto;
    private AlterarRestauranteDto alterarRestauranteDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restauranteController = new RestauranteController(restauranteService);

        restauranteId = UUID.randomUUID();

        dono = new Usuario();
        dono.setNome("Chef João");

        restaurante = new Restaurante();
        restaurante.setNome("Pizzaria da Nonna");
        restaurante.setEndereco("Avenida Principal, 100");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioFuncionamento("11:00 - 22:00");
        restaurante.setDono(dono);
        restaurante.setCardapio(new ArrayList<>());

        salvarRestauranteDto = new SalvarRestauranteDto(
            "Pizzaria da Nonna",
            "Avenida Principal, 100",
            "Italiana",
            "11:00 - 22:00",
            dono,
            null
        );

        alterarRestauranteDto = new AlterarRestauranteDto(
            restauranteId,
            "Pizzaria da Nonna",
            "Avenida Principal, 100",
            "Italiana",
            "11:00 - 22:00",
            dono,
            null
        );
    }

    @Test
    @DisplayName("POST /restaurantes -Devera criar e retornar o status CREATED")
    void testSalvarRestauranteSucesso() {
        when(restauranteService.salvarRestaurante(any(SalvarRestauranteDto.class)))
            .thenReturn(restaurante);

        var resultado = restauranteController.salvarRestaurante(salvarRestauranteDto);

        assertNotNull(resultado);
        assertEquals(201, resultado.getStatusCode().value());
        assertEquals("Pizzaria da Nonna", resultado.getBody().getNome());
        assertEquals("Avenida Principal, 100", resultado.getBody().getEndereco());
        assertEquals("Italiana", resultado.getBody().getTipoCozinha());
        verify(restauranteService, times(1)).salvarRestaurante(any(SalvarRestauranteDto.class));
    }

    @Test
    @DisplayName("PUT /restaurantes - Devera alterar e retornar NO_CONTENT")
    void testAlterarRestauranteSucesso() {
        doNothing().when(restauranteService).alterarRestaurante(any(AlterarRestauranteDto.class));

        var resultado = restauranteController.alterarRestaurante(alterarRestauranteDto);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(restauranteService, times(1)).alterarRestaurante(any(AlterarRestauranteDto.class));
    }

    @Test
    @DisplayName("DELETE /restaurantes/{id} - Devera deletar e retornar NO_CONTENT")
    void testDeleteRestauranteSucesso() {
        doNothing().when(restauranteService).deleteRestaurante(restauranteId);

        var resultado = restauranteController.deleteRestaurante(restauranteId);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(restauranteService, times(1)).deleteRestaurante(restauranteId);
    }

    @Test
    @DisplayName("GET /restaurantes/{id} - Devera retorna OK")
    void testGetRestauranteSucesso() {
        when(restauranteService.getRestaurante(restauranteId)).thenReturn(restaurante);

        var resultado = restauranteController.getRestauranteById(restauranteId);

        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCode().value());
        assertEquals("Pizzaria da Nonna", resultado.getBody().getNome());
        assertEquals("Italiana", resultado.getBody().getTipoCozinha());
        verify(restauranteService, times(1)).getRestaurante(restauranteId);
    }

    @Test
    @DisplayName("DELETE /restaurantes/{id} - Devera lançar uma exceção quando não encontrar o restaurante")
    void testDeleteRestauranteNaoEncontrado() {
        doThrow(new RestauranteNaoEncontradoException(restauranteId))
            .when(restauranteService).deleteRestaurante(restauranteId);

        assertThrows(RestauranteNaoEncontradoException.class,
            () -> restauranteController.deleteRestaurante(restauranteId));

        verify(restauranteService, times(1)).deleteRestaurante(restauranteId);
    }

    @Test
    @DisplayName("GET /restaurantes/{id} - Devera lançar uma exceção quando não encontrar o restaurante")
    void testGetRestauranteNaoEncontrado() {
        when(restauranteService.getRestaurante(restauranteId))
            .thenThrow(new RestauranteNaoEncontradoException(restauranteId));

        assertThrows(RestauranteNaoEncontradoException.class,
            () -> restauranteController.getRestauranteById(restauranteId));

        verify(restauranteService, times(1)).getRestaurante(restauranteId);
    }

    @Test
    @DisplayName("POST /restaurantes - Devera salvar restaurante com todos os campos e retornar CREATED")
    void testSalvarRestauranteComTodosCampos() {
        when(restauranteService.salvarRestaurante(any(SalvarRestauranteDto.class)))
            .thenReturn(restaurante);

        var resultado = restauranteController.salvarRestaurante(salvarRestauranteDto);

        assertNotNull(resultado);
        assertEquals(201, resultado.getStatusCode().value());
        assertEquals("11:00 - 22:00", resultado.getBody().getHorarioFuncionamento());
        assertNotNull(resultado.getBody());
        verify(restauranteService, times(1)).salvarRestaurante(any(SalvarRestauranteDto.class));
    }

    @Test
    @DisplayName("PUT /restaurantes - Devera alterar restaurante com novos dados e retornar NO_CONTENT")
    void testAlterarRestauranteComNovosDados() {
        doNothing().when(restauranteService).alterarRestaurante(any(AlterarRestauranteDto.class));

        var resultado = restauranteController.alterarRestaurante(alterarRestauranteDto);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(restauranteService, times(1)).alterarRestaurante(any(AlterarRestauranteDto.class));
    }

    @Test
    @DisplayName("GET /restaurantes/{id} - Devera retornar restaurante com todos os campos preenchidos")
    void testGetRestauranteComTodosCampos() {
        when(restauranteService.getRestaurante(restauranteId)).thenReturn(restaurante);

        var resultado = restauranteController.getRestauranteById(restauranteId);

        assertNotNull(resultado);
        assertNotNull(resultado.getBody().getNome());
        assertNotNull(resultado.getBody().getEndereco());
        assertNotNull(resultado.getBody().getTipoCozinha());
        assertNotNull(resultado.getBody().getHorarioFuncionamento());
        verify(restauranteService, times(1)).getRestaurante(restauranteId);
    }
}
