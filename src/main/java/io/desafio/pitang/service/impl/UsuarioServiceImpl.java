package io.desafio.pitang.service.impl;

import io.desafio.pitang.domain.entity.Carro;
import io.desafio.pitang.domain.entity.Usuario;
import io.desafio.pitang.domain.repository.CarrosRepository;
import io.desafio.pitang.domain.repository.UsuarioRepository;
import io.desafio.pitang.exception.SenhaInvalidaException;
import io.desafio.pitang.rest.dto.CarroDTO;
import io.desafio.pitang.rest.dto.UsuarioDTO;
import io.desafio.pitang.service.UsuarioService;
import io.desafio.pitang.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    private final UsuarioRepository usuariosRepository;
    private final CarrosRepository carrosRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuariosRepository.findByLogin(username)
                .orElseThrow( ()-> new UsernameNotFoundException("Usuário não encontrado") );

        String[] roles = new String[]{"USER"};

        return User
                .builder()
                .username(usuario.getLogin())
                .password(usuario.getPassword())
                .roles(roles)
                .build();
    }


    public ResponseEntity<List<Usuario>> retornarTodos(){
        List<Usuario> usuario = usuariosRepository.findAll();

        if(!usuario.isEmpty()){
            return ResponseEntity.ok(usuario);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Usuario retornarUsuarioId(Integer id) {
        return usuariosRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado"));
    }

    @Override
    @Transactional
    public void deletaUsuarioId(Integer id) {

        Usuario usuario = usuariosRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado"));

        List<Carro> carros = carrosRepository.findAllByUsuario(usuario);
        carrosRepository.deleteAll(carros);

        usuariosRepository.delete(usuario);

    }

    @Override
    @Transactional
    public void atualizarUsuario(UsuarioDTO dto, Integer id) {
        Usuario usuario = usuariosRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado"));

        if (ValidationUtils.isNullOrEmpty(String.valueOf(dto.getBirthday())) || ValidationUtils.isNullOrEmpty(dto.getEmail()) || ValidationUtils.isNullOrEmpty(dto.getLogin()) || ValidationUtils.isNullOrEmpty(dto.getPassword()) || ValidationUtils.isNullOrEmpty(dto.getPhone()) || ValidationUtils.isNullOrEmpty(dto.getFirstName()) || ValidationUtils.isNullOrEmpty(dto.getLastName())) {
            throw new RuntimeException("{campo.users.fields.missing}");
        }

        Usuario usuVerify = usuariosRepository.verificarEmailOutrosUsuario(id, dto.getEmail());
        if (usuVerify != null) {
            throw new RuntimeException("Email already exists " + dto.getEmail());
        }

        Usuario UsuVerifyLogin = usuariosRepository.verificarLoginOutrosUsuario(id, dto.getLogin());

        if (UsuVerifyLogin != null) {
            throw new RuntimeException("Login already exists " + dto.getLogin());
        }

        usuario.setBirthday(dto.getBirthday());
        usuario.setEmail(dto.getEmail());
        usuario.setLogin(dto.getLogin());
        usuario.setPassword( passwordEncoder.encode(dto.getPassword()));
        usuario.setPhone(dto.getPhone());
        usuario.setFirstName(dto.getFirstName());
        usuario.setLastName(dto.getLastName());
        usuariosRepository.save(usuario);

    }

    @Override
    @Transactional
    public Usuario salvar(UsuarioDTO dto) {

        if (ValidationUtils.isNullOrEmpty(String.valueOf(dto.getBirthday())) || ValidationUtils.isNullOrEmpty(dto.getEmail()) || ValidationUtils.isNullOrEmpty(dto.getLogin()) || ValidationUtils.isNullOrEmpty(dto.getPassword()) || ValidationUtils.isNullOrEmpty(dto.getPhone()) || ValidationUtils.isNullOrEmpty(dto.getFirstName()) || ValidationUtils.isNullOrEmpty(dto.getLastName())) {
            throw new RuntimeException("{campo.users.fields.missing}");
        }

        if (usuariosRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists " + dto.getEmail());
        }

        if (usuariosRepository.existsByLogin(dto.getLogin())) {
            throw new RuntimeException("Login already exists " + dto.getLogin());
        }

        Usuario usuarioInsert = new Usuario();
        usuarioInsert.setBirthday(dto.getBirthday());
        usuarioInsert.setEmail(dto.getEmail());
        usuarioInsert.setLogin(dto.getLogin());
        usuarioInsert.setPassword( dto.getPassword());
        usuarioInsert.setPhone(dto.getPhone());
        usuarioInsert.setFirstName(dto.getFirstName());
        usuarioInsert.setLastName(dto.getLastName());

        Usuario usuario = usuariosRepository.save(usuarioInsert);

        List<Carro> carros = processarCarros(usuario, dto.getCars());
        carrosRepository.saveAll(carros);

        usuario.setCars(carros);


        return usuario;
    }


    public List<Carro> processarCarros(Usuario usuario, List<CarroDTO> listCars) {
        if(listCars.isEmpty()){
            throw new RuntimeException("Não é possível inserir um cliente sem carro associado");
        }

        Set<String> licensePlates = new HashSet<>();

        for (CarroDTO dto : listCars) {
            if (licensePlates.contains(dto.getLicensePlate())) {
                throw new RuntimeException("Já existe um carro com a placa de licença " + dto.getLicensePlate());
            }
            licensePlates.add(dto.getLicensePlate());

            if (carrosRepository.existsByLicensePlate(dto.getLicensePlate())) {
                throw new RuntimeException("Já existe um carro com a placa de licença " + dto.getLicensePlate());
            }
        }
        return listCars
                .stream()
                .map( dto -> {
                    Carro carro = new Carro();
                    carro.setUsuario(usuario);
                    carro.setModel(dto.getModel());
                    carro.setLicensePlate(dto.getLicensePlate());
                    carro.setColor(dto.getColor());
                    carro.setAno(dto.getAno());
                    return carro;
                }).collect(Collectors.toList());
    }


    public UserDetails autenticar( Usuario usuario ){
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasBatem = passwordEncoder.matches( usuario.getPassword(), user.getPassword() );

        if(senhasBatem){
            return user;
        }

        throw new  SenhaInvalidaException();
    }


}

