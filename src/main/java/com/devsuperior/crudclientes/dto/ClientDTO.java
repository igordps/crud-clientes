package com.devsuperior.crudclientes.dto;

import com.devsuperior.crudclientes.entities.Client;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class ClientDTO {

    private Long id;
    @NotBlank(message = "Nome não pode ser vazio")
    private String name;
    private String cpf;
    private Double income;
    @PastOrPresent(message = "Data de Nascimento não pode ser uma data futura")
    private LocalDate birthDate;
    private Integer children;

    public ClientDTO() {
    }

    public ClientDTO(Client client){
        id = client.getId();
        name = client.getName();
        cpf = client.getCpf();
        income = client.getIncome();
        birthDate = client.getBirthDate();
        children = client.getChildren();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public Double getIncome() {
        return income;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Integer getChildren() {
        return children;
    }
}
