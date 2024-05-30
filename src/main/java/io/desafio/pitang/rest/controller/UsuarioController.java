package io.desafio.pitang.rest.controller;

import io.desafio.pitang.domain.entity.Carro;
import io.desafio.pitang.domain.entity.Usuario;
import io.desafio.pitang.domain.repository.CarrosRepository;
import io.desafio.pitang.domain.repository.UsuarioRepository;
import io.desafio.pitang.rest.dto.CarroDTO;
import io.desafio.pitang.rest.dto.TokenDTO;
import io.desafio.pitang.rest.dto.UsuarioDTO;
import io.desafio.pitang.service.UsuarioService;
import io.desafio.pitang.service.impl.UsuarioServiceImpl;
import io.desafio.pitang.exception.SenhaInvalidaException;
import io.desafio.pitang.rest.dto.CredenciaisDTO;
import io.desafio.pitang.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuariosRepository;
    private final CarrosRepository carrosRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioServiceImpl usuarioServiceImpl;
    private final JwtService jwtService;


    @GetMapping
    public ResponseEntity<List<Usuario>> retornarTodos(){
        return usuarioService.retornarTodos();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario save(@RequestBody UsuarioDTO usuarioDTO){

        String senhaCriptografada = passwordEncoder.encode(usuarioDTO.getPassword());
        usuarioDTO.setPassword(senhaCriptografada);
        return usuarioService.salvar(usuarioDTO);
    }

    @GetMapping("{id}")
    public Usuario retornarUsuarioId(@PathVariable Integer id){
        return usuarioService.retornarUsuarioId(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletaUsuarioId(@PathVariable Integer id){
        usuarioService.deletaUsuarioId(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarUsuario(@PathVariable Integer id, @RequestBody UsuarioDTO dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuarioService.atualizarUsuario(dto, id);
    }

    @PostMapping("/signin")
    public UsuarioDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .password(credenciais.getSenha()).build();

            UserDetails usuarioAutenticado = usuarioServiceImpl.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);

            Optional<Usuario> usuarioNew = usuariosRepository.findByLogin(usuario.getLogin());

            List<CarroDTO> carroDTOs = new ArrayList<>();

            for (Carro carro : usuarioNew.get().getCars()) {
                CarroDTO carroDTO = new CarroDTO();
                carroDTO.setId(carro.getId());
                carroDTO.setAno(carro.getAno());
                carroDTO.setLicensePlate(carro.getLicensePlate());
                carroDTO.setModel(carro.getModel());
                carroDTO.setColor(carro.getColor());
                carroDTOs.add(carroDTO);
            }

            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setToken(token);
            usuarioDTO.setFirstName(usuarioNew.get().getFirstName());
            usuarioDTO.setLastName(usuarioNew.get().getLastName());
            usuarioDTO.setEmail(usuarioNew.get().getEmail());
            usuarioDTO.setBirthday(usuarioNew.get().getBirthday());
            usuarioDTO.setLogin(usuarioNew.get().getLogin());
            usuarioDTO.setPassword(usuarioNew.get().getPassword());
            usuarioDTO.setPhone(usuarioNew.get().getPhone());
            usuarioDTO.setCars(carroDTOs);

            return usuarioDTO;
        } catch (UsernameNotFoundException | SenhaInvalidaException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
