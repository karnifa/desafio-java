package io.desafio.pitang.rest.controller;

import io.desafio.pitang.domain.entity.Carro;
import io.desafio.pitang.rest.dto.CarroDTO;
import io.desafio.pitang.rest.dto.UsuarioDTO;
import io.desafio.pitang.service.CarroService;
import io.desafio.pitang.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import io.desafio.pitang.domain.entity.Usuario;
import io.desafio.pitang.domain.repository.CarrosRepository;
import io.desafio.pitang.domain.repository.UsuarioRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/cars")
public class CarrosController {

    private final UsuarioRepository usuariosRepository;
    private final CarrosRepository carrosRepository;
    private final UsuarioService usuariosService;
    private final CarroService carService;

    public CarrosController(UsuarioRepository usuariosRepository, CarrosRepository carrosRepository, UsuarioService usuariosService, CarroService carService) {
        this.usuariosRepository = usuariosRepository;
        this.carrosRepository = carrosRepository;
        this.usuariosService = usuariosService;
        this.carService = carService;
    }

    @GetMapping(value = "me")
    public Optional<Usuario> retornarUsuario(){
        return carService.retornarUsuario();
    }

    @GetMapping(value = "")
    public List<Carro> retornarCarros(){
        return carService.retornarCarros();
    }

    @GetMapping(value = "{id}")
    public Optional<Carro> retornarCarro(@PathVariable Integer id){
        return carService.retornarCarro(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCarro(@PathVariable Integer id) throws IllegalAccessException {
        carService.removeCarro(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarCarro(@PathVariable Integer id, @RequestBody CarroDTO dto){
        carService.atualizarCarro(dto, id);
    }


    private String getAuthenticatedUsername() {
        // Obtenha o contexto de autenticação
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // Obtenha os detalhes do usuário autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }

        return null;
    }

}
