package io.desafio.pitang.service.impl;

import io.desafio.pitang.domain.entity.Carro;
import io.desafio.pitang.domain.entity.Usuario;
import io.desafio.pitang.domain.repository.CarrosRepository;
import io.desafio.pitang.domain.repository.UsuarioRepository;
import io.desafio.pitang.rest.dto.CarroDTO;
import io.desafio.pitang.security.jwt.JwtAuthFilter;
import io.desafio.pitang.service.CarroService;
import io.desafio.pitang.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarroServiceImpl implements CarroService, UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final UsuarioRepository usuariosRepository;
    private final CarrosRepository carrosRepository;
    private final UsuarioServiceImpl usuarioServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public Optional<Usuario> retornarUsuario() {
        return usuarioAuthRead();
    }

    @Override
    public List<Carro> retornarCarros() {
        Optional<Usuario> usuario = usuarioAuthRead();
        return usuario.get().getCars();
    }

    @Override
    public Optional<Carro> retornarCarro(Integer id) {
        Optional<Carro> carro = carrosRepository.findById(id);

        return carro;
    }

    @Override
    @Transactional
    public void removeCarro(Integer id) {
        carrosRepository.deleteUsuarioCarro(id);
        carrosRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void atualizarCarro(CarroDTO dto, Integer id) {

        Optional<Usuario> usuario = usuarioAuthRead();
        Optional<Carro> carro = carrosRepository.findById(id);

        if (ValidationUtils.isNullOrEmpty(String.valueOf(dto.getAno())) || ValidationUtils.isNullOrEmpty(dto.getLicensePlate()) ) {
            throw new RuntimeException("{campo.users.fields.missing}");
        }


        Optional<Carro> verificaCarro = carrosRepository.verificaLicenseExist(id,dto.getLicensePlate());
        if(!verificaCarro.isPresent()) {
            Carro carroInsert = new Carro();
            carroInsert.setId(id);
            carroInsert.setAno(dto.getAno());
            carroInsert.setUsuario(usuario.get());
            carroInsert.setLicensePlate(dto.getLicensePlate());
            carroInsert.setModel(dto.getModel());
            carroInsert.setColor(dto.getColor());
            carrosRepository.save(carroInsert);
        }else{
            throw new IllegalArgumentException("License plate already exists");
        }
    }


    public Optional<Usuario> usuarioAuthRead(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Optional<Usuario> usuario = Optional.of(new Usuario());
            usuario = usuariosRepository.findByLogin(userDetails.getUsername());
            return usuario;
        }
        return null;
    }
}