package io.desafio.pitang.domain.repository;

import io.desafio.pitang.domain.entity.Carro;
import io.desafio.pitang.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CarrosRepository extends JpaRepository<Carro, Integer> {
    boolean existsByLicensePlate(String licensePlate);

    List<Carro> findAllByUsuario(Usuario usuario);

    @Modifying
    @Transactional
    @Query("DELETE FROM UsuarioCarro uc WHERE uc.carro.id = :id")
    void deleteUsuarioCarro(@Param("id") Integer id);


    @Query(value = "SELECT c FROM Carro c WHERE c.id!= :id AND c.licensePlate = :licensePlate", nativeQuery = false)
    Optional<Carro> verificaLicenseExist(@Param("id") Integer id, @Param("licensePlate") String licensePlate);

}
