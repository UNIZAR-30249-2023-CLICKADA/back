package com.clickada.back.domain.entity.auxClasses;

public enum CategoriaEspacio {
    AULA,
    SEMINARIO,
    LABORATORIO,
    DESPACHO,
    SALA_COMUN;

    public static CategoriaEspacio getCategoriaByString(String rol){
        if(rol.equals("Aula")) {return CategoriaEspacio.AULA;}
        if(rol.equals("Seminario")) {return CategoriaEspacio.SEMINARIO;}
        if(rol.equals("Laboratorio")) {return CategoriaEspacio.LABORATORIO;}
        if(rol.equals("Despacho")) {return CategoriaEspacio.DESPACHO;}
        if(rol.equals("Sala Comun")) {return CategoriaEspacio.SALA_COMUN;}
        // Cualquier otro caso
        else {return null;}
    }
}
