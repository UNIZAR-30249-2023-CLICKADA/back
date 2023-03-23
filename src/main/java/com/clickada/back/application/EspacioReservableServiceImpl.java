package com.clickada.back.application;

import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.entity.EspacioReservable;
import com.clickada.back.domain.entity.auxClasses.CategoriaEspacio;
import com.clickada.back.domain.entity.auxClasses.CategoriaReserva;
import com.clickada.back.domain.entity.auxClasses.Reservabilidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EspacioReservableServiceImpl implements EspacioReservableService{
    EspacioRepository espacioRepository;
    @Autowired
    public EspacioReservableServiceImpl(EspacioRepository espacioRepository){this.espacioRepository = espacioRepository;}

    @Override
    public List<EspacioReservable> todosEspacios() {
        return this.espacioRepository.findAll();
    }

    @Override
    public boolean cambiarReservabilidadEspacio(UUID idEspacio, boolean reservable) {
        if (espacioRepository.existsById(idEspacio)) {
            EspacioReservable espacioReservable = espacioRepository.getById(idEspacio);
            Reservabilidad reservabilidad = espacioReservable.getReservabilidad();
            Reservabilidad reservabilidadNueva = new Reservabilidad(reservable, reservabilidad.categoriaReserva);
            espacioReservable.setReservabilidad(reservabilidadNueva);
            espacioRepository.save(espacioReservable);
            return true;
        }
        return false;
    }
    @Override
    public boolean reservarEspacio() {
        return false;
    }

}
