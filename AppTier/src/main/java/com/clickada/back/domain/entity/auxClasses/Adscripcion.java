package com.clickada.back.domain.entity.auxClasses;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Slf4j
@NoArgsConstructor
public class Adscripcion implements Serializable {
    public Departamento departamento;

    public Adscripcion(Departamento departamento){
        this.departamento = departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}
