package com.clickada.back.application;

import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {
    ReservaRepository reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository){
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> listarTodasReservas(){
        return reservaRepository.findAll();
    }
}
