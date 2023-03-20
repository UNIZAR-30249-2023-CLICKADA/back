package com.clickada.back.application;

import com.clickada.back.domain.entity.EspacioReservable;
import java.util.List;
import java.util.UUID;

public interface EspacioReservableService {
    boolean reservarEspacio();

    List<EspacioReservable> todosEspacios();

    boolean cambiarReservabilidadEspacio(UUID idEspacio, boolean reservable);
}
