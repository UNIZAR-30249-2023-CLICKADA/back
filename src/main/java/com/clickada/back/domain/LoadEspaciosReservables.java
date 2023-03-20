package com.clickada.back.domain;

import com.clickada.back.domain.entity.EspacioReservable;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.CategoriaReserva;
import com.clickada.back.domain.entity.auxClasses.Reservabilidad;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadEspaciosReservables {
    private static final Logger log = LoggerFactory.getLogger(LoadEspaciosReservables.class);

    @Bean
    CommandLineRunner initDatabase2(EspacioRepository espacioRepository) {

        EspacioReservable espacioReservable = new EspacioReservable(new Reservabilidad(true, CategoriaReserva.AULA));
        EspacioReservable espacioReservable2 = new EspacioReservable(new Reservabilidad(false, CategoriaReserva.DESPACHO));

        espacioRepository.save(espacioReservable);
        espacioRepository.save(espacioReservable2);
        return null;
    }
}
