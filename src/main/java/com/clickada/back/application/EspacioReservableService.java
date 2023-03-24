package com.clickada.back.application;

import com.clickada.back.domain.entity.EspacioReservable;
import com.clickada.back.domain.entity.auxClasses.CategoriaEspacio;
import com.clickada.back.domain.entity.auxClasses.TipoUso;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface EspacioReservableService {
    boolean reservarEspacio(UUID idPersona, UUID idEspacio, LocalDate fecha, LocalTime desde,
                            LocalTime hasta, TipoUso uso, int numAsistentes, String detalles);

    List<EspacioReservable> todosEspacios();

    boolean cambiarReservabilidadEspacio(UUID idEspacio, boolean reservable);

}
