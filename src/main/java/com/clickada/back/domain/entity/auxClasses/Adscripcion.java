package com.clickada.back.domain.entity.auxClasses;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;

@Slf4j
@Embeddable
@NoArgsConstructor
public class Adscripcion {
    public Departamento departamento;

    public Adscripcion(Departamento departamento){
        this.departamento = departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}
