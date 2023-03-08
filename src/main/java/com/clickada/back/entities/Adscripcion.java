package com.clickada.back.entities;

import com.clickada.back.valueObject.Departamento;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
@Slf4j
public class Adscripcion {
    public ArrayList<Departamento> departamentos;

    public Adscripcion(Departamento departamento){
        departamentos = new ArrayList<>();
        departamentos.add(departamento);
    }

    public void anyadirDepartamento(Departamento departamento) throws Exception {
        if (departamentos.size() == 1) {
            if (departamentos.get(0).equals(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS))
                departamentos.add(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
            else departamentos.add(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        } else {
            throw new Exception("Ya tiene maximo numero de departamentos");
        }
    }

    public void limpiarAdscripcion(){
        this.departamentos.clear();
    }
}
