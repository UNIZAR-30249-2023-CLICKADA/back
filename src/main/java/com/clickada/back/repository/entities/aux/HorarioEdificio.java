package com.clickada.back.repository.entities.aux;

import javax.persistence.Entity;
import java.time.LocalTime;

@Entity
public class HorarioEdificio {
    private LocalTime horaInicio;
    private LocalTime horaFin;
}