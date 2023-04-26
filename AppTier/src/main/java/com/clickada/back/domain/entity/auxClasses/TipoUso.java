package com.clickada.back.domain.entity.auxClasses;

public enum TipoUso {
    DOCENCIA,
    INVESTIGACION,
    GESTION,
    OTROS;

    public static TipoUso getTipoUsoByString(String stringTipoUso) {
        if(stringTipoUso.equals("Docencia")) {return TipoUso.DOCENCIA;}
        if(stringTipoUso.equals("Investigacion")) {return TipoUso.INVESTIGACION;}
        if(stringTipoUso.equals("Gestion")) {return TipoUso.GESTION;}
        if(stringTipoUso.equals("Otros")) {return TipoUso.OTROS;}
        // Cualquier otro caso
        else {return null;}
    }
}
