package com.clickada.back.domain;

import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.auxClasses.CategoriaEspacio;
import com.clickada.back.domain.entity.auxClasses.CategoriaReserva;
import com.clickada.back.domain.entity.auxClasses.Reservabilidad;
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

        Espacio espacio = new Espacio(new Reservabilidad(true, CategoriaReserva.AULA), 20L,40, CategoriaEspacio.AULA);
        Espacio espacio2 = new Espacio(new Reservabilidad(false, CategoriaReserva.DESPACHO), 25L,40,CategoriaEspacio.DESPACHO);

        espacioRepository.save(espacio);
        espacioRepository.save(espacio2);
        return null;
    }
}
