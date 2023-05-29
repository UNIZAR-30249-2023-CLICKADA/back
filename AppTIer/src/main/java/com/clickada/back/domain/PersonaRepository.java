package com.clickada.back.domain;

import com.clickada.back.domain.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, UUID> {

    Persona findByeMail(String email);
    boolean existsByeMail(String email);
}
