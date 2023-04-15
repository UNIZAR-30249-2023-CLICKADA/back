package com.clickada.back.domain.entity.auxClasses;

public enum CategoriaReserva {
    AULA,
    SEMINARIO,
    LABORATORIO,
    DESPACHO,
    SALA_COMUN;

    public static CategoriaReserva getCategoriaByString(String rol){
        if(rol.equals("Aula")) {return CategoriaReserva.AULA;}
        if(rol.equals("Seminario")) {return CategoriaReserva.SEMINARIO;}
        if(rol.equals("Laboratorio")) {return CategoriaReserva.LABORATORIO;}
        if(rol.equals("Despacho")) {return CategoriaReserva.DESPACHO;}
        if(rol.equals("Sala Comun")) {return CategoriaReserva.SALA_COMUN;}
        // Cualquier otro caso
        else {return null;}
    }
}
