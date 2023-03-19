package com.clickada.back.domain;

import com.clickada.back.domain.entity.EspacioReservable;
import com.clickada.back.domain.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EspacioReservableRepository extends JpaRepository<EspacioReservable, UUID> {
}
