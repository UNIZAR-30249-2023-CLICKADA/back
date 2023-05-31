package com.clickada.back.amqp;
import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.domain.entity.Persona;

import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.dtos.MapperDtos;
import com.clickada.back.dtos.PersonaDto;
import com.clickada.back.dtos.ReservaDto;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AmqpPersonas {

    @Autowired
    PersonaService personaService;

        @RabbitListener(queues="personas")
        public String receive(ArrayList<String> datos) {
            if(!datos.isEmpty()){
                String op = datos.get(0);
                switch (op) {
                    case "agregarPersona" -> {
                        try {
                            Persona p = personaService.agregarPersona(UUID.fromString(datos.get(1)), datos.get(2),datos.get(3),datos.get(4));
                            if (p!=null){
                                MapperDtos mapperDtos = new MapperDtos();
                                PersonaDto pers = mapperDtos.dePersonaAPersonaDto(p);
                                Gson gson = new Gson();
                                return gson.toJson(pers);
                            }
                        } catch (Exception e) {
                            return "ERR:" + e.getMessage();
                        }
                    }
                    case "cambiarRol" -> {
                        try {
                            personaService.cambiarRol(UUID.fromString(datos.get(1)), datos.get(2),datos.get(3));
                            return "OK";
                        } catch (Exception e) {
                            return "Persona no encontrada o rol inválido. No se han hecho cambios";
                        }
                    }
                    case "cambiarDpto" -> {
                        try {
                            personaService.cambiarDpto(UUID.fromString(datos.get(1)),UUID.fromString(datos.get(2)),datos.get(3));
                            return "OK";
                        } catch (Exception e) {
                            return "ERR:" + e.getMessage();
                        }
                    }
                    case "todasPersonas" -> {
                        List<Persona> listPersonas = personaService.todasPersonas();
                        MapperDtos mapperDtos = new MapperDtos();
                        List<PersonaDto> personaDtos = mapperDtos.listaPersonaDto(listPersonas);
                        Gson gson = new Gson();
                        return gson.toJson(personaDtos);
                    }
                    case "loginPersona" -> {
                        Persona p = personaService.loginPersona(datos.get(1),datos.get(2));
                        if (p!=null){
                            MapperDtos mapperDtos = new MapperDtos();
                            PersonaDto pers = mapperDtos.dePersonaAPersonaDto(p);
                            Gson gson = new Gson();
                            return gson.toJson(pers);
                        }
                        else { return "Login fallido";  }
                    }
                    case "permisosReserva" -> {
                            return personaService.permisosDeReserva(UUID.fromString(datos.get(1))).toString();
                    }
                }
            }
        return "Operación inválida";
    }

}
