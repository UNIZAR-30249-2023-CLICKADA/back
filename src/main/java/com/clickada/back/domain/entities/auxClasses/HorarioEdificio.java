package com.clickada.back.domain.entities.auxClasses;

import javax.persistence.Entity;
import java.time.LocalTime;

@Entity
public class HorarioEdificio {
    private LocalTime horaInicio;
    private LocalTime horaFin;
}