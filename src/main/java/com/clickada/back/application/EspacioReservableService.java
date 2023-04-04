package com.clickada.back.application;

import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.TipoUso;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface EspacioReservableService {
    boolean reservarEspacio(UUID idPersona, List<UUID> idEspacios, LocalDate fecha, LocalTime horaInicio,
                            LocalTime horaFinal, TipoUso uso,int numAsistentes,String detalles);

    List<Espacio> todosEspacios();

    boolean cambiarReservabilidadEspacio(UUID idEspacio, boolean reservable);

    public List<Reserva> obtenerReservasVivas(UUID idPersona);

}
