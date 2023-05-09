package com.clickada.back.amqp;

import com.clickada.back.application.DominioService;
import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.dtos.EspacioDto;
import com.clickada.back.dtos.MapperDtos;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AmqpEspacios {
    @Autowired
    PersonaService personaService;

    @Autowired
    DominioService dominioService;
    @Autowired
    EspacioService espacioService;

    @RabbitListener(queues="espacios")
    public String receive(ArrayList<String> datos) {
        if (!datos.isEmpty()) {
            String op = datos.get(0);
            switch (op) {
                case "todosEspacios" -> {
                    List<Espacio> espacios = espacioService.todosEspacios();
                    MapperDtos mapperDtos = new MapperDtos();
                    List<EspacioDto> espacioDtos = mapperDtos.listaEspacioDto(espacios);
                    Gson gson = new Gson();
                    return gson.toJson(espacioDtos);
                }
                case "cambiarPorcentajeEspacio" -> {
                    try {
                        dominioService.cambiarPorcentajeEspacio(UUID.fromString(datos.get(1)),
                                UUID.fromString(datos.get(2)),Double.parseDouble(datos.get(3)));
                        return "Porcentaje cambiado correctamente";
                    } catch (Exception e) {
                        return "ERR:" + e.getMessage();
                    }
                }
                case "cambiarPorcentajeEdificio" -> {
                    try {
                        dominioService.cambiarPorcentajeEdificio(UUID.fromString(datos.get(1)),
                                Double.parseDouble(datos.get(2)));
                        return "Porcentaje cambiado correctamente";
                    } catch (Exception e) {
                        return "ERR:" + e.getMessage();
                    }
                }
            }
        }
        return "Operación no válida";
    }
}
