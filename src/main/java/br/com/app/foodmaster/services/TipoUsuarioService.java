package br.com.app.foodmaster.services;

import br.com.app.foodmaster.dtos.SalvarTipoUsuarioDto;
import br.com.app.foodmaster.dtos.AlterarTipoUsuarioDto;
import br.com.app.foodmaster.entities.TipoUsuario;
import br.com.app.foodmaster.exceptions.TipoUsuarioNaoEncontradoException;
import br.com.app.foodmaster.repositories.TipoUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    private TipoUsuario criarTipoUsuario(SalvarTipoUsuarioDto input) {
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setNomeTipo(input.nomeTipo());
        return tipoUsuario;
    }

    private void atualizarTipoUsuario(TipoUsuario tipoUsuario, AlterarTipoUsuarioDto input) {
        tipoUsuario.setNomeTipo(input.nomeTipo());
    }

    public TipoUsuario salvarTipoUsuario(SalvarTipoUsuarioDto input) {
        TipoUsuario tipoUsuario = criarTipoUsuario(input);
        return tipoUsuarioRepository.save(tipoUsuario);
    }

    public void alterarTipoUsuario(AlterarTipoUsuarioDto input) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(input.id())
                .orElseThrow(() -> new TipoUsuarioNaoEncontradoException(input.id()));
        atualizarTipoUsuario(tipoUsuario, input);
        tipoUsuarioRepository.save(tipoUsuario);
    }

    public void deleteTipoUsuario(UUID id) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new TipoUsuarioNaoEncontradoException(id));
        tipoUsuarioRepository.delete(tipoUsuario);
    }

    public TipoUsuario getTipoUsuario(UUID id) {
        return tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new TipoUsuarioNaoEncontradoException(id));
    }
}