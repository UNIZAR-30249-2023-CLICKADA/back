package com.clickada.back.application;

import com.clickada.back.domain.EspacioReservableRepository;
import com.clickada.back.domain.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspacioReservableServiceImpl implements EspacioReservableService {

    EspacioReservableRepository espacioReservableRepository;
    @Autowired
    public EspacioReservableServiceImpl(EspacioReservableRepository espacioReservableRepository){
        this.espacioReservableRepository = espacioReservableRepository;
    }

    @Override
    public boolean reservarEspacio() {
        return false;
    }
}
