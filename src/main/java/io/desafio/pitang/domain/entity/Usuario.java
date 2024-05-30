package io.desafio.pitang.domain.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
@ToString
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstName;
    private String lastName;
    private String email;
    private Date birthday;
    private String login;
    private String password;
    private String phone;
    private boolean admin;

    @ManyToMany
    @JoinTable(name = "usuario_carro",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "carro_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Carro> cars;


    public Usuario() {

    }

}
