package com.clickada.back.domain;

import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.auxClasses.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class LoadEspacios {
    private static final Logger log = LoggerFactory.getLogger(LoadEspacios.class);

    @Bean
    CommandLineRunner initDatabase2(EspacioRepository espacioRepository) throws Exception {
        espacioRepository.deleteAllInBatch();//Limpiar primero para evitar duplicados
        Edificio edificio = new Edificio(LocalTime.of(8,0), LocalTime.of(22,0), List.of(),100);
        Espacio espacio = new Espacio(new Reservabilidad(true, CategoriaReserva.AULA), 20L,60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio espacio2 = new Espacio(new Reservabilidad(false, CategoriaReserva.DESPACHO), 25L, 60,
                CategoriaEspacio.DESPACHO,edificio,new PropietarioEspacio(Eina.EINA));

        espacioRepository.save(espacio);
        espacioRepository.save(espacio2);
        return null;
    }
}
