package com.clickada.back.amqp;

import com.clickada.back.application.DominioService;
import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.TipoUso;
import com.clickada.back.dtos.ReservaDto;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
                    JSONArray lista = new JSONArray();
                    List<Espacio> espacios = espacioService.todosEspacios();
                    for (Espacio e : espacios) {
                        JSONObject esp = new JSONObject();
                        esp.put("idEspacio", e.getIdEspacio());
                        esp.put("categoriaEspacio", e.getCategoriaEspacio());
                        esp.put("tamanyo", e.getTamanyo());
                        esp.put("numMaxOcupantes", e.getNumMaxOcupantes());
                        esp.put("porcentajeUsoPermitido", e.getPorcentajeUsoPermitido());
                        esp.put("totalAsistentesPermitidos", e.getTotalAsistentesPermitidos());
                        esp.put("categoriaEspacio", e.getCategoriaEspacio());
                        JSONArray propiet = new JSONArray();
                        propiet.put(new JSONObject().put("eina", String.valueOf(e.getPropietarioEspacio().getEina())));
                        propiet.put(new JSONObject().put("departamento", String.valueOf(e.getPropietarioEspacio().getDepartamento())));
                        propiet.put(new JSONObject().put("personas", String.valueOf(e.getPropietarioEspacio().getPersonas())));
                        esp.put("propietarioEspacio", propiet);

                        JSONArray reservab = new JSONArray();
                        reservab.put(new JSONObject().put("categoríaReserva", String.valueOf(e.getReservabilidad().categoriaReserva)));
                        reservab.put(new JSONObject().put("reservable", String.valueOf(e.getReservabilidad().reservable)));
                        esp.put("reservabilidad", reservab);

                        esp.put("horaInicio", e.getHoraInicio());
                        esp.put("horaFin", e.getHoraFin());
                        esp.put("horaInicio", e.getHoraInicio());

                        lista.put(esp);
                    }
                    return lista.toString();
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
