package com.clickada.back.domain.entity.auxClasses;

public enum Rol {
    ESTUDIANTE,
    CONSERJE,
    INVESTIGADOR_CONTRATADO,
    DOCENTE_INVESTIGADOR,
    TECNICO_LABORATORIO,
    GERENTE;

    public static Rol getRolByString(String rol){
        if(rol.equals("Estudiante")) {return Rol.ESTUDIANTE;}
        if(rol.equals("Conserje")) {return Rol.CONSERJE;}
        if(rol.equals("Investigador")) {return Rol.INVESTIGADOR_CONTRATADO;}
        if(rol.equals("Docente")) {return Rol.DOCENTE_INVESTIGADOR;}
        if(rol.equals("Tecnico")) {return Rol.TECNICO_LABORATORIO;}
        if(rol.equals("Gerente")) {return Rol.GERENTE;}
        // Cualquier otro caso
        else {return null;}
    }
}
