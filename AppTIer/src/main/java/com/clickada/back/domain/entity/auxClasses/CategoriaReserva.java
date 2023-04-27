package com.clickada.back.domain.entity.auxClasses;

public enum CategoriaReserva {
    AULA,
    SEMINARIO,
    LABORATORIO,
    DESPACHO,
    SALA_COMUN;

    public static CategoriaReserva getCategoriaByString(String categoria){
        if(categoria.equals("Aula")) {return CategoriaReserva.AULA;}
        if(categoria.equals("Seminario")) {return CategoriaReserva.SEMINARIO;}
        if(categoria.equals("Laboratorio")) {return CategoriaReserva.LABORATORIO;}
        if(categoria.equals("Despacho")) {return CategoriaReserva.DESPACHO;}
        if(categoria.equals("Sala Comun")) {return CategoriaReserva.SALA_COMUN;}
        // Cualquier otro caso
        else {return null;}
    }
}
