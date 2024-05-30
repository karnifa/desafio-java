package io.desafio.pitang.service;

import io.desafio.pitang.domain.entity.Usuario;
import io.desafio.pitang.rest.dto.UsuarioDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Usuario salvar(UsuarioDTO dto);
    ResponseEntity<List<Usuario>> retornarTodos();

    Usuario retornarUsuarioId(Integer id);

    void deletaUsuarioId(Integer id);

    void atualizarUsuario(UsuarioDTO dto, Integer id);
}
