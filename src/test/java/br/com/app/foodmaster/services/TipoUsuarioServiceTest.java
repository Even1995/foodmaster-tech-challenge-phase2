package br.com.app.foodmaster.services;

import br.com.app.foodmaster.dtos.AlterarTipoUsuarioDto;
import br.com.app.foodmaster.dtos.SalvarTipoUsuarioDto;
import br.com.app.foodmaster.entities.TipoUsuario;
import br.com.app.foodmaster.exceptions.TipoUsuarioNaoEncontradoException;
import br.com.app.foodmaster.repositories.TipoUsuarioRepository;
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

@DisplayName("TipoUsuarioService Tests")
class TipoUsuarioServiceTest {

    private TipoUsuarioService tipoUsuarioService;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    private UUID tipoUsuarioId;
    private TipoUsuario tipoUsuarioMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tipoUsuarioService = new TipoUsuarioService(tipoUsuarioRepository);

        tipoUsuarioId = UUID.randomUUID();
        tipoUsuarioMock = new TipoUsuario();
        tipoUsuarioMock.setNomeTipo("Cliente");
    }

    @Test
    @DisplayName("Devera salvar o Tipo de Usuário com sucesso")
    void testSalvarTipoUsuario() {
        SalvarTipoUsuarioDto salvarTipoUsuarioDto = new SalvarTipoUsuarioDto("Administrador");

        when(tipoUsuarioRepository.save(any(TipoUsuario.class))).thenReturn(tipoUsuarioMock);

        TipoUsuario resultado = tipoUsuarioService.salvarTipoUsuario(salvarTipoUsuarioDto);

        assertNotNull(resultado);
        assertEquals("Cliente", resultado.getNomeTipo());
        verify(tipoUsuarioRepository, times(1)).save(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Devera alterar o Tipo de Usuário com sucesso")
    void testAlterarTipoUsuario() {
        AlterarTipoUsuarioDto alterarTipoUsuarioDto = new AlterarTipoUsuarioDto(
            tipoUsuarioId,
            "Premium"
        );

        tipoUsuarioMock.setNomeTipo("Premium");
        when(tipoUsuarioRepository.findById(tipoUsuarioId)).thenReturn(Optional.of(tipoUsuarioMock));
        when(tipoUsuarioRepository.save(any(TipoUsuario.class))).thenReturn(tipoUsuarioMock);

        tipoUsuarioService.alterarTipoUsuario(alterarTipoUsuarioDto);

        verify(tipoUsuarioRepository, times(1)).findById(tipoUsuarioId);
        verify(tipoUsuarioRepository, times(1)).save(any(TipoUsuario.class));
        assertEquals("Premium", tipoUsuarioMock.getNomeTipo());
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar alterar um tipo de usuário não encontrado")
    void testAlterarTipoUsuarioNaoEncontrado() {
        AlterarTipoUsuarioDto alterarTipoUsuarioDto = new AlterarTipoUsuarioDto(
            tipoUsuarioId,
            "Novo Tipo"
        );

        when(tipoUsuarioRepository.findById(tipoUsuarioId)).thenReturn(Optional.empty());

        assertThrows(TipoUsuarioNaoEncontradoException.class,
            () -> tipoUsuarioService.alterarTipoUsuario(alterarTipoUsuarioDto));

        verify(tipoUsuarioRepository, times(1)).findById(tipoUsuarioId);
        verify(tipoUsuarioRepository, never()).save(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Devera deletar o Tipo de Usuário com sucesso")
    void testDeleteTipoUsuario() {
        when(tipoUsuarioRepository.findById(tipoUsuarioId)).thenReturn(Optional.of(tipoUsuarioMock));

        tipoUsuarioService.deleteTipoUsuario(tipoUsuarioId);

        verify(tipoUsuarioRepository, times(1)).findById(tipoUsuarioId);
        verify(tipoUsuarioRepository, times(1)).delete(tipoUsuarioMock);
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar deletar um tipo de usuário não encontrado")
    void testDeleteTipoUsuarioNaoEncontrado() {
        when(tipoUsuarioRepository.findById(tipoUsuarioId)).thenReturn(Optional.empty());

        assertThrows(TipoUsuarioNaoEncontradoException.class,
            () -> tipoUsuarioService.deleteTipoUsuario(tipoUsuarioId));

        verify(tipoUsuarioRepository, times(1)).findById(tipoUsuarioId);
        verify(tipoUsuarioRepository, never()).delete(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Devera retornar o Tipo de Usuário por ID")
    void testGetTipoUsuario() {
        when(tipoUsuarioRepository.findById(tipoUsuarioId)).thenReturn(Optional.of(tipoUsuarioMock));

        TipoUsuario resultado = tipoUsuarioService.getTipoUsuario(tipoUsuarioId);

        assertNotNull(resultado);
        assertEquals("Cliente", resultado.getNomeTipo());
        verify(tipoUsuarioRepository, times(1)).findById(tipoUsuarioId);
    }

    @Test
    @DisplayName("Devera lançar uma exceção quando tentar obter um tipo de usuário não encontrado")
    void testGetTipoUsuarioNaoEncontrado() {
        when(tipoUsuarioRepository.findById(tipoUsuarioId)).thenReturn(Optional.empty());

        assertThrows(TipoUsuarioNaoEncontradoException.class,
            () -> tipoUsuarioService.getTipoUsuario(tipoUsuarioId));

        verify(tipoUsuarioRepository, times(1)).findById(tipoUsuarioId);
    }

    @Test
    @DisplayName("Devera salvar o Tipo de Usuário com diferentes nomes")
    void testSalvarTipoUsuarioComDiferentesNomes() {
        SalvarTipoUsuarioDto dto1 = new SalvarTipoUsuarioDto("Garcom");
        SalvarTipoUsuarioDto dto2 = new SalvarTipoUsuarioDto("Gerente");

        TipoUsuario tipoUsuario1 = new TipoUsuario();
        tipoUsuario1.setNomeTipo("Garcom");

        when(tipoUsuarioRepository.save(any(TipoUsuario.class)))
            .thenReturn(tipoUsuario1);

        TipoUsuario resultado = tipoUsuarioService.salvarTipoUsuario(dto1);

        assertNotNull(resultado);
        verify(tipoUsuarioRepository, times(1)).save(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Devera alterar o nome do Tipo de Usuário")
    void testAlterarNomeTipoUsuario() {
        AlterarTipoUsuarioDto alterarDto = new AlterarTipoUsuarioDto(
            tipoUsuarioId,
            "Super Admin"
        );

        when(tipoUsuarioRepository.findById(tipoUsuarioId)).thenReturn(Optional.of(tipoUsuarioMock));
        when(tipoUsuarioRepository.save(any(TipoUsuario.class))).thenReturn(tipoUsuarioMock);

        tipoUsuarioService.alterarTipoUsuario(alterarDto);

        verify(tipoUsuarioRepository, times(1)).findById(tipoUsuarioId);
        verify(tipoUsuarioRepository, times(1)).save(any(TipoUsuario.class));
    }
}
