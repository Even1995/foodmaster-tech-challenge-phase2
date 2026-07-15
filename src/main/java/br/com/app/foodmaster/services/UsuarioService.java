package br.com.app.foodmaster.services;

import br.com.app.foodmaster.dtos.AlterarUsuarioDto;
import br.com.app.foodmaster.dtos.EnderecoDto;
import br.com.app.foodmaster.dtos.SalvarUsuarioDto;
import br.com.app.foodmaster.entities.Endereco;
import br.com.app.foodmaster.entities.Usuario;
import br.com.app.foodmaster.exceptions.UsuarioNaoEncontradoException;
import br.com.app.foodmaster.repositories.TipoUsuarioRepository;
import br.com.app.foodmaster.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;;

    public UsuarioService(UsuarioRepository usuarioRepository, TipoUsuarioRepository tipoUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    private Usuario criarUsuario(SalvarUsuarioDto input) {
        Usuario usuario = new Usuario();
        usuario.setNome(input.nome());
        usuario.setEmail(input.email());
        usuario.setLogin(input.login());
        usuario.setSenha(input.senha());
        usuario.setTipoUsuario(tipoUsuarioRepository.findById(input.tipoUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Tipo de usuário não encontrado")));
        usuario.setEndereco(criarEndereco(input.enderecoDto()));
        return usuario;
    }

    private void atualizarUsuario(Usuario usuario, AlterarUsuarioDto input) {
        usuario.setNome(input.nome());
        usuario.setEmail(input.email());
        usuario.setLogin(input.login());
        usuario.setSenha(input.senha());
        usuario.setTipoUsuario(tipoUsuarioRepository.findById(input.tipoUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Tipo de usuário não encontrado")));
        usuario.setEndereco(criarEndereco(input.endereco()));
    }

    private Endereco criarEndereco(EnderecoDto enderecoDto) {
        Endereco endereco = new Endereco();
        endereco.setRua(enderecoDto.rua());
        endereco.setNumero(enderecoDto.numero());
        endereco.setCidade(enderecoDto.cidade());
        endereco.setCep(enderecoDto.cep());
        return endereco;
    }

    public Usuario salvarUsuario(SalvarUsuarioDto input) {
        Usuario usuario = criarUsuario(input);
        return usuarioRepository.save(usuario);
    }

    public void alterarUsuario(AlterarUsuarioDto input) {
        Usuario usuario = usuarioRepository.findById(input.id())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(input.id()));
        atualizarUsuario(usuario, input);
        usuarioRepository.save(usuario);
    }

    public void deletarUsuario(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        usuarioRepository.delete(usuario);
    }

    public Usuario getUsuario(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }
}

