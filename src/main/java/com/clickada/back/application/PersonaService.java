package com.clickada.back.application;

import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.Rol;

import java.util.List;
import java.util.UUID;

public interface PersonaService {
    boolean cambiarRol(UUID idPersona, String rol);

    List<Persona> todasPersonas();
}
