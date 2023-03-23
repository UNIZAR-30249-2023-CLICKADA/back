package com.clickada.back.application;

import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.CategoriaReserva;
import com.clickada.back.domain.entity.auxClasses.Rol;

import java.util.List;
import java.util.UUID;

public interface PersonaService {
    boolean cambiarRol(UUID idPersona, String rol);

    List<Persona> todasPersonas();

    public  Persona obtenerPersona(String email);

    boolean loginPersona(String email, String pass);

    /* A partir del ID de un persona, devuelve la lista con todos los tipos de reserva que puede realizar */
    List<CategoriaReserva> permisosDeReserva(UUID idPersona);

    boolean aptoParaCambiar(UUID idPersona);
}
