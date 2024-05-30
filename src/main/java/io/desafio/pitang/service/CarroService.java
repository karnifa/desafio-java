package io.desafio.pitang.service;

import io.desafio.pitang.domain.entity.Carro;
import io.desafio.pitang.domain.entity.Usuario;
import io.desafio.pitang.rest.dto.CarroDTO;

import java.util.List;
import java.util.Optional;

public interface CarroService {
    Optional<Usuario> retornarUsuario();


    List<Carro> retornarCarros();

    Optional<Carro> retornarCarro(Integer id);

    void removeCarro(Integer id) throws IllegalAccessException;

    void atualizarCarro(CarroDTO dto, Integer id);

}
