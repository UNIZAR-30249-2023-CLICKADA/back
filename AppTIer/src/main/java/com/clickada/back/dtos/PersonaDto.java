package com.clickada.back.dtos;

import com.clickada.back.domain.entity.auxClasses.*;
import lombok.Builder;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.UUID;

@Builder
@Setter
public class PersonaDto {

    @Id
    private UUID idPersona;

    String nombre;

    String eMail;

    Departamento departamento;

    boolean departamentoDisponible;

    ArrayList<Rol> roles;

    PropietarioEspacio propietarioEspacio;
}
