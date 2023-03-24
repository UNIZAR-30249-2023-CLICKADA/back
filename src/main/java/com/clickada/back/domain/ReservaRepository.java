package com.clickada.back.domain;

import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
}