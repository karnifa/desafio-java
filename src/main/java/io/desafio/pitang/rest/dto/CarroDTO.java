package io.desafio.pitang.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarroDTO {

    private Integer id;
    private Integer ano;
    private String licensePlate;
    private String model;
    private String color;

    public CarroDTO() {

    }
}
