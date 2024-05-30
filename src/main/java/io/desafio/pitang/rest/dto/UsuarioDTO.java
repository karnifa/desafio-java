package io.desafio.pitang.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class UsuarioDTO {

    @JsonIgnore
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthday;
    private String login;
    private String password;
    private String phone;
    private String token;
    private boolean admin;
    private List<CarroDTO> cars;

    public UsuarioDTO() {

    }

}
