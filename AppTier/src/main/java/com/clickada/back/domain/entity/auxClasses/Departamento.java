package com.clickada.back.domain.entity.auxClasses;

public enum Departamento {
    INFORMATICA_E_INGENIERIA_DE_SISTEMAS,
    INGENIERIA_ELECTRONICA_Y_COMUNICACIONES;
    public static Departamento getDepartamentoByString(String departamento){
        if(departamento==null) return null;
        if(departamento.equals("Informatica")) {return Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES;}
        if(departamento.equals("Ingenieria")) {return Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS;}
        // Cualquier otro caso
        else {return null;}
    }
}
