package com.clickada.back.domain.entity.auxClasses;

public enum CategoriaEspacio {
    AULA,
    SEMINARIO,
    LABORATORIO,
    DESPACHO,
    SALA_COMUN;

    public static CategoriaEspacio getCategoriaByString(String categoria){
        if(categoria.equals("Aula")) {return CategoriaEspacio.AULA;}
        if(categoria.equals("Seminario")) {return CategoriaEspacio.SEMINARIO;}
        if(categoria.equals("Laboratorio")) {return CategoriaEspacio.LABORATORIO;}
        if(categoria.equals("Despacho")) {return CategoriaEspacio.DESPACHO;}
        if(categoria.equals("Sala Comun")) {return CategoriaEspacio.SALA_COMUN;}
        // Cualquier otro caso
        else {return null;}
    }
}
