package com.clickada.back.domain;

import com.clickada.back.domain.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
    //@Query("select r from Reserva r where r.fechaInicio = current_date and r.horaFin >= current_time ")
    //List<Reserva> findAllAfterTime();
    List<Reserva> findByFecha(LocalDate fecha);
}