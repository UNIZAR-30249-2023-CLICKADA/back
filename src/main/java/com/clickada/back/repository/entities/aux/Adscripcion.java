package com.clickada.back.repository.entities.aux;

import com.clickada.back.valueObject.Departamento;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
@Slf4j
public class Adscripcion {
    public Departamento departamento;

    public Adscripcion(Departamento departamento){
        this.departamento = departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}
