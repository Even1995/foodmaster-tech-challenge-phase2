package br.com.app.foodmaster.controllers;

import br.com.app.foodmaster.dtos.AlterarTipoUsuarioDto;
import br.com.app.foodmaster.dtos.SalvarTipoUsuarioDto;
import br.com.app.foodmaster.entities.TipoUsuario;
import br.com.app.foodmaster.exceptions.TipoUsuarioNaoEncontradoException;
import br.com.app.foodmaster.services.TipoUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("TipoUsuarioController Tests")
class TipoUsuarioControllerTest {

    private TipoUsuarioController tipoUsuarioController;

    @Mock
    private TipoUsuarioService tipoUsuarioService;

    private UUID tipoUsuarioId;
    private TipoUsuario tipoUsuario;
    private SalvarTipoUsuarioDto salvarTipoUsuarioDto;
    private AlterarTipoUsuarioDto alterarTipoUsuarioDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tipoUsuarioController = new TipoUsuarioController(tipoUsuarioService);

        tipoUsuarioId = UUID.randomUUID();

        tipoUsuario = new TipoUsuario();
        tipoUsuario.setNomeTipo("Cliente");

        salvarTipoUsuarioDto = new SalvarTipoUsuarioDto("Cliente");

        alterarTipoUsuarioDto = new AlterarTipoUsuarioDto(
            tipoUsuarioId,
            "Cliente Premium"
        );
    }

    @Test
    @DisplayName("POST /tipos-usuario - Devera salvar o tipo de Usuario e retornar CREATED")
    void testSalvarTipoUsuarioSucesso() {
        when(tipoUsuarioService.salvarTipoUsuario(any(SalvarTipoUsuarioDto.class)))
            .thenReturn(tipoUsuario);

        var resultado = tipoUsuarioController.salvarTipoUsuario(salvarTipoUsuarioDto);

        assertNotNull(resultado);
        assertEquals(201, resultado.getStatusCode().value());
        assertEquals("Cliente", resultado.getBody().getNomeTipo());
        verify(tipoUsuarioService, times(1)).salvarTipoUsuario(any(SalvarTipoUsuarioDto.class));
    }

    @Test
    @DisplayName("PUT /tipos-usuario - Devera alterar o tipo de Usuario e retornar NO_CONTENT")
    void testAlterarTipoUsuarioSucesso() {
        doNothing().when(tipoUsuarioService).alterarTipoUsuario(any(AlterarTipoUsuarioDto.class));

        var resultado = tipoUsuarioController.alterarTipoUsuario(alterarTipoUsuarioDto);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(tipoUsuarioService, times(1)).alterarTipoUsuario(any(AlterarTipoUsuarioDto.class));
    }

    @Test
    @DisplayName("DELETE /tipos-usuario/{id} - Devera deletar o tipo de Usuario e retornar NO_CONTENT")
    void testDeleteTipoUsuarioSucesso() {
        doNothing().when(tipoUsuarioService).deleteTipoUsuario(tipoUsuarioId);

        var resultado = tipoUsuarioController.deleteTipoUsuario(tipoUsuarioId);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(tipoUsuarioService, times(1)).deleteTipoUsuario(tipoUsuarioId);
    }

    @Test
    @DisplayName("GET /tipos-usuario/{id} - Devera retornar o tipo de Usuario com status OK")
    void testGetTipoUsuarioSucesso() {
        when(tipoUsuarioService.getTipoUsuario(tipoUsuarioId)).thenReturn(tipoUsuario);

        var resultado = tipoUsuarioController.getTipoUsuarioById(tipoUsuarioId);

        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCode().value());
        assertEquals("Cliente", resultado.getBody().getNomeTipo());
        verify(tipoUsuarioService, times(1)).getTipoUsuario(tipoUsuarioId);
    }

    @Test
    @DisplayName("DELETE /tipos-usuario/{id} - Devera lançar uma exceção quando não encontrar o tipo de Usuario")
    void testDeleteTipoUsuarioNaoEncontrado() {
        doThrow(new TipoUsuarioNaoEncontradoException(tipoUsuarioId))
            .when(tipoUsuarioService).deleteTipoUsuario(tipoUsuarioId);

        assertThrows(TipoUsuarioNaoEncontradoException.class,
            () -> tipoUsuarioController.deleteTipoUsuario(tipoUsuarioId));

        verify(tipoUsuarioService, times(1)).deleteTipoUsuario(tipoUsuarioId);
    }

    @Test
    @DisplayName("GET /tipos-usuario/{id} - Devera lançar uma exceção quando não encontrar o tipo de Usuario")
    void testGetTipoUsuarioNaoEncontrado() {
        when(tipoUsuarioService.getTipoUsuario(tipoUsuarioId))
            .thenThrow(new TipoUsuarioNaoEncontradoException(tipoUsuarioId));

        assertThrows(TipoUsuarioNaoEncontradoException.class,
            () -> tipoUsuarioController.getTipoUsuarioById(tipoUsuarioId));

        verify(tipoUsuarioService, times(1)).getTipoUsuario(tipoUsuarioId);
    }

    @Test
    @DisplayName("POST /tipos-usuario - Devera validar o nome do tipo de Usuario")
    void testSalvarTipoUsuarioComNome() {
        tipoUsuario.setNomeTipo("Administrador");

        when(tipoUsuarioService.salvarTipoUsuario(any(SalvarTipoUsuarioDto.class)))
            .thenReturn(tipoUsuario);

        var resultado = tipoUsuarioController.salvarTipoUsuario(
            new SalvarTipoUsuarioDto("Administrador"));

        assertNotNull(resultado);
        assertEquals(201, resultado.getStatusCode().value());
        assertNotNull(resultado.getBody());
        verify(tipoUsuarioService, times(1)).salvarTipoUsuario(any(SalvarTipoUsuarioDto.class));
    }

    @Test
    @DisplayName("PUT /tipos-usuario - Devera alterar o nome do tipo de Usuario e retornar NO_CONTENT")
    void testAlterarNomeTipoUsuario() {
        doNothing().when(tipoUsuarioService).alterarTipoUsuario(any(AlterarTipoUsuarioDto.class));

        var resultado = tipoUsuarioController.alterarTipoUsuario(alterarTipoUsuarioDto);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(tipoUsuarioService, times(1)).alterarTipoUsuario(any(AlterarTipoUsuarioDto.class));
    }

    @Test
    @DisplayName("GET /tipos-usuario/{id} - Devera retornar o tipo de Usuario com o campo nomeTipo")
    void testGetTipoUsuarioComNome() {
        when(tipoUsuarioService.getTipoUsuario(tipoUsuarioId)).thenReturn(tipoUsuario);

        var resultado = tipoUsuarioController.getTipoUsuarioById(tipoUsuarioId);

        assertNotNull(resultado);
        assertNotNull(resultado.getBody().getNomeTipo());
        verify(tipoUsuarioService, times(1)).getTipoUsuario(tipoUsuarioId);
    }
}
