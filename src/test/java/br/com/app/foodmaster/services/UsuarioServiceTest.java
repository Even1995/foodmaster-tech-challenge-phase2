package br.com.app.foodmaster.services;

import br.com.app.foodmaster.dtos.AlterarUsuarioDto;
import br.com.app.foodmaster.dtos.EnderecoDto;
import br.com.app.foodmaster.dtos.SalvarUsuarioDto;
import br.com.app.foodmaster.entities.Endereco;
import br.com.app.foodmaster.entities.TipoUsuario;
import br.com.app.foodmaster.entities.Usuario;
import br.com.app.foodmaster.exceptions.UsuarioNaoEncontradoException;
import br.com.app.foodmaster.repositories.UsuarioRepository;
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

@DisplayName("UsuarioService Tests")
class UsuarioServiceTest {

    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private UUID usuarioId;
    private Usuario usuarioMock;
    private TipoUsuario tipoUsuario;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioService = new UsuarioService(usuarioRepository);

        usuarioId = UUID.randomUUID();
        tipoUsuario = new TipoUsuario();
        tipoUsuario.setNomeTipo("Cliente");

        endereco = new Endereco();
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCidade("São Paulo");
        endereco.setCep("01234-567");

        usuarioMock = new Usuario();
        usuarioMock.setNome("João Silva");
        usuarioMock.setEmail("joao@email.com");
        usuarioMock.setLogin("joao123");
        usuarioMock.setSenha("senha123");
        usuarioMock.setTipoUsuario(tipoUsuario);
        usuarioMock.setEndereco(endereco);
    }

    @Test
    @DisplayName("Devera salvar um usuário com sucesso")
    void testSalvarUsuario() {
        EnderecoDto enderecoDto = new EnderecoDto(
            "Rua das Flores",
            "123",
            "São Paulo",
            "SP",
            "01234-567"
        );

        SalvarUsuarioDto salvarUsuarioDto = new SalvarUsuarioDto(
            "João Silva",
            "joao@email.com",
            "joao123",
            "senha123",
            tipoUsuario,
            enderecoDto
        );

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        Usuario resultado = usuarioService.salvarUsuario(salvarUsuarioDto);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        assertEquals("joao123", resultado.getLogin());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Devera alterar um usuário existente")
    void testAlterarUsuario() {
        EnderecoDto enderecoDto = new EnderecoDto(
            "Rua Atualizada",
            "456",
            "Rio de Janeiro",
            "RJ",
            "99999-999"
        );

        AlterarUsuarioDto alterarUsuarioDto = new AlterarUsuarioDto(
            usuarioId,
            "João Silva Atualizado",
            "joao.novo@email.com",
            "joao456",
            "novaSenha456",
            tipoUsuario,
            enderecoDto
        );

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        usuarioService.alterarUsuario(alterarUsuarioDto);

        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        assertEquals("João Silva Atualizado", usuarioMock.getNome());
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar alterar um usuário não encontrado")
    void testAlterarUsuarioNaoEncontrado() {
        EnderecoDto enderecoDto = new EnderecoDto(
            "Rua",
            "123",
            "Cidade",
            "Estado",
            "12345-678"
        );

        AlterarUsuarioDto alterarUsuarioDto = new AlterarUsuarioDto(
            usuarioId,
            "João",
            "email@email.com",
            "login",
            "senha",
            tipoUsuario,
            enderecoDto
        );

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class,
            () -> usuarioService.alterarUsuario(alterarUsuarioDto));

        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Devera deletar o Usuário com sucesso")
    void testDeletarUsuario() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioMock));

        usuarioService.deletarUsuario(usuarioId);

        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, times(1)).delete(usuarioMock);
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar deletar um usuário não encontrado")
    void testDeletarUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class,
            () -> usuarioService.deletarUsuario(usuarioId));

        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }

    @Test
    @DisplayName("Devera retornar o Usuário por ID")
    void testGetUsuario() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioMock));

        Usuario resultado = usuarioService.getUsuario(usuarioId);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(usuarioRepository, times(1)).findById(usuarioId);
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar obter um usuário não encontrado")
    void testGetUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class,
            () -> usuarioService.getUsuario(usuarioId));

        verify(usuarioRepository, times(1)).findById(usuarioId);
    }

    @Test
    @DisplayName("Devera salvar um usuário com sucesso")
    void testSalvarUsuarioComTodosCampos() {
        EnderecoDto enderecoDto = new EnderecoDto(
            "Avenida Paulista",
            "1000",
            "São Paulo",
            "SP",
            "01311-100"
        );

        SalvarUsuarioDto salvarUsuarioDto = new SalvarUsuarioDto(
            "Maria Santos",
            "maria@email.com",
            "maria123",
            "senhaMariaForte",
            tipoUsuario,
            enderecoDto
        );

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        Usuario resultado = usuarioService.salvarUsuario(salvarUsuarioDto);

        assertNotNull(resultado);
        assertNotNull(resultado.getEndereco());
        assertNotNull(resultado.getTipoUsuario());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Devera alterar o endereço do Usuário")
    void testAlterarUsuarioComEnderecoAtualizado() {
        EnderecoDto novoEnderecoDto = new EnderecoDto(
            "Rua Nova",
            "999",
            "Belo Horizonte",
            "MG",
            "30123-456"
        );

        AlterarUsuarioDto alterarUsuarioDto = new AlterarUsuarioDto(
            usuarioId,
            "João",
            "joao@novo.com",
            "joao_novo",
            "senha_nova",
            tipoUsuario,
            novoEnderecoDto
        );

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        usuarioService.alterarUsuario(alterarUsuarioDto);

        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}
