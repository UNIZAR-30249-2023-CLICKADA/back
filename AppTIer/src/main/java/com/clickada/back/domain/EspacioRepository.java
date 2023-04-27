package com.clickada.back.domain;

import com.clickada.back.domain.entity.Espacio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EspacioRepository extends JpaRepository<Espacio, UUID> {
    
}
