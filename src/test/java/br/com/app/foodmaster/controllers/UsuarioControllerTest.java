package br.com.app.foodmaster.controllers;

import br.com.app.foodmaster.dtos.AlterarUsuarioDto;
import br.com.app.foodmaster.dtos.EnderecoDto;
import br.com.app.foodmaster.dtos.SalvarUsuarioDto;
import br.com.app.foodmaster.entities.Endereco;
import br.com.app.foodmaster.entities.TipoUsuario;
import br.com.app.foodmaster.entities.Usuario;
import br.com.app.foodmaster.exceptions.UsuarioNaoEncontradoException;
import br.com.app.foodmaster.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("UsuarioController Tests")
class UsuarioControllerTest {

    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    private UUID usuarioId;
    private Usuario usuario;
    private TipoUsuario tipoUsuario;
    private Endereco endereco;
    private SalvarUsuarioDto salvarUsuarioDto;
    private AlterarUsuarioDto alterarUsuarioDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioController = new UsuarioController(usuarioService);

        usuarioId = UUID.randomUUID();

        tipoUsuario = new TipoUsuario();
        tipoUsuario.setNomeTipo("Cliente");

        endereco = new Endereco();
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCidade("São Paulo");
        endereco.setCep("01234-567");

        usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setEndereco(endereco);

        EnderecoDto enderecoDto = new EnderecoDto(
            "Rua das Flores",
            "123",
            "São Paulo",
            "SP",
            "01234-567"
        );

        salvarUsuarioDto = new SalvarUsuarioDto(
            "João Silva",
            "joao@email.com",
            "joao123",
            "senha123",
            tipoUsuario,
            enderecoDto
        );

        alterarUsuarioDto = new AlterarUsuarioDto(
            usuarioId,
            "João Silva",
            "joao@email.com",
            "joao123",
            "senha123",
            tipoUsuario,
            enderecoDto
        );
    }

    @Test
    @DisplayName("POST /usuarios - Devera salvar o usuario e retornar CREATED")
    void testSalvarUsuarioSucesso() {
        when(usuarioService.salvarUsuario(any(SalvarUsuarioDto.class))).thenReturn(usuario);

        var resultado = usuarioController.salvarUsuario(salvarUsuarioDto);

        assertNotNull(resultado);
        assertEquals(201, resultado.getStatusCode().value());
        assertEquals("João Silva", resultado.getBody().getNome());
        verify(usuarioService, times(1)).salvarUsuario(any(SalvarUsuarioDto.class));
    }

    @Test
    @DisplayName("PUT /usuarios - Devera alterar o usuário com sucesso")
    void testAlterarUsuarioSucesso() {
        doNothing().when(usuarioService).alterarUsuario(any(AlterarUsuarioDto.class));

        var resultado = usuarioController.alterarUsuario(alterarUsuarioDto);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(usuarioService, times(1)).alterarUsuario(any(AlterarUsuarioDto.class));
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} - Devera deletar o usuario e retornar NO_CONTENT")
    void testDeleteUsuarioSucesso() {
        doNothing().when(usuarioService).deletarUsuario(usuarioId);

        var resultado = usuarioController.deleteUsuario(usuarioId);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(usuarioService, times(1)).deletarUsuario(usuarioId);
    }

    @Test
    @DisplayName("GET /usuarios/{id} - Devera retornar o usuario com status OK")
    void testGetUsuarioSucesso() {
        when(usuarioService.getUsuario(usuarioId)).thenReturn(usuario);

        var resultado = usuarioController.getUsuarioById(usuarioId);

        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCode().value());
        assertEquals("João Silva", resultado.getBody().getNome());
        assertEquals("joao@email.com", resultado.getBody().getEmail());
        verify(usuarioService, times(1)).getUsuario(usuarioId);
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} - Devera lançar uma exceção quando não encontrar o usuario")
    void testDeleteUsuarioNaoEncontrado() {
        doThrow(new UsuarioNaoEncontradoException(usuarioId))
            .when(usuarioService).deletarUsuario(usuarioId);

        assertThrows(UsuarioNaoEncontradoException.class,
            () -> usuarioController.deleteUsuario(usuarioId));

        verify(usuarioService, times(1)).deletarUsuario(usuarioId);
    }

    @Test
    @DisplayName("GET /usuarios/{id} - Devera lançar uma exceção quando não encontrar o usuario")
    void testGetUsuarioNaoEncontrado() {
        when(usuarioService.getUsuario(usuarioId))
            .thenThrow(new UsuarioNaoEncontradoException(usuarioId));

        assertThrows(UsuarioNaoEncontradoException.class,
            () -> usuarioController.getUsuarioById(usuarioId));

        verify(usuarioService, times(1)).getUsuario(usuarioId);
    }

    @Test
    @DisplayName("POST /usuarios - Devera salvar o usuario e retornar CREATED")
    void testSalvarUsuarioComDadosValidos() {
        usuario.setNome("Maria Santos");
        usuario.setEmail("maria@email.com");

        when(usuarioService.salvarUsuario(any(SalvarUsuarioDto.class))).thenReturn(usuario);

        var resultado = usuarioController.salvarUsuario(salvarUsuarioDto);

        assertNotNull(resultado);
        assertEquals(201, resultado.getStatusCode().value());
        assertNotNull(resultado.getBody());
        verify(usuarioService, times(1)).salvarUsuario(any(SalvarUsuarioDto.class));
    }

    @Test
    @DisplayName("PUT /usuarios - Devera alterar o usuário com sucesso")
    void testAlterarUsuarioComNovosDados() {
        doNothing().when(usuarioService).alterarUsuario(any(AlterarUsuarioDto.class));

        var resultado = usuarioController.alterarUsuario(alterarUsuarioDto);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(usuarioService, times(1)).alterarUsuario(any(AlterarUsuarioDto.class));
    }

    @Test
    @DisplayName("GET /usuarios/{id} - Devera retornar o usuario com todos os campos preenchidos")
    void testGetUsuarioComTodosCampos() {
        when(usuarioService.getUsuario(usuarioId)).thenReturn(usuario);

        var resultado = usuarioController.getUsuarioById(usuarioId);

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertNotNull(resultado.getBody().getNome());
        assertNotNull(resultado.getBody().getEmail());
        assertNotNull(resultado.getBody().getLogin());
        verify(usuarioService, times(1)).getUsuario(usuarioId);
    }
}

