package io.desafio.pitang.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "usuario_carro")
public class UsuarioCarro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "carro_id", nullable = false)
    private Carro carro;

    // Construtores
    public UsuarioCarro() {}

    public UsuarioCarro(Usuario usuario, Carro carro) {
        this.usuario = usuario;
        this.carro = carro;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }
}
