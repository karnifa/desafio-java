package io.desafio.pitang.domain.repository;

import io.desafio.pitang.domain.entity.Carro;
import io.desafio.pitang.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    @Query(value =  "SELECT * FROM usuario c where c.id != :id and c.email = :email",nativeQuery = true)
    Usuario verificarEmailOutrosUsuario(@Param("id") Integer id, @Param("email") String email);

    @Query(value =  "SELECT * FROM usuario c where c.id != :id and c.login = :login",nativeQuery = true)
    Usuario verificarLoginOutrosUsuario(@Param("id") Integer id, @Param("login") String email);

    @Query(value =  "SELECT * FROM usuario c where c.id != :id",nativeQuery = true)
    List<Usuario> buscarOutrosUsuarios(@Param("id") Integer id);

    Optional<Usuario> findByLogin(String login);

}
