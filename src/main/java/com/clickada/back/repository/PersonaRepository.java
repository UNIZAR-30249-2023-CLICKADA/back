package com.clickada.back.repository;

import com.clickada.back.repository.entities.Persona;
import com.clickada.back.repository.entities.aux.HorarioEdificio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, UUID> {

}
