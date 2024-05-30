package io.desafio.pitang.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@Entity
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    private Usuario usuario;

    private Integer ano;
    private String licensePlate;
    private String model;
    private String color;

    public Carro() {

    }
}
