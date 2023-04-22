package com.clickada.back.amqp;

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
                        esp.put("diasNoReservables", e.getDiasNoReservables());
                        lista.put(esp);
                    }
                    return lista.toString();
                }
                case "reservarEspacio" -> {


                    DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
                    //convert String to LocalDate
                    TipoUso tipoUso = TipoUso.getTipoUsoByString(datos.get(3));
                    LocalDate fecha = LocalDate.parse(datos.get(4), formatterDay);
                    LocalTime horaInicio = LocalTime.parse(datos.get(5), formatterTime);
                    LocalTime horaFinal = LocalTime.parse(datos.get(6), formatterTime);

                    if (tipoUso == null) {
                        return "Ese tipo de uso no existe, pruebe con otro";
                    }
                    try {
                        JSONArray je = new JSONArray(datos.get(2));

                        ArrayList<UUID> listaEspacios = new ArrayList<>();
                        for (int i = 0; i < je.length(); i++) {
                            listaEspacios.add(UUID.fromString(je.getString(i)));
                        }
                        espacioService.reservarEspacio(UUID.fromString(datos.get(1)), listaEspacios,
                                fecha, horaInicio, horaFinal,
                                tipoUso, Integer.parseInt(datos.get(8)), datos.get(7));
                    } catch (Exception e) {
                        return e.toString();
                    }

                    return ("Reserva realizada correctamente");
                }
                case "reservarAutomatica" -> {

                    DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
                    //convert String to LocalDate
                    TipoUso tipoUso = TipoUso.getTipoUsoByString(datos.get(8));
                    LocalDate fecha = LocalDate.parse(datos.get(4), formatterDay);
                    LocalTime horaInicio = LocalTime.parse(datos.get(5), formatterTime);
                    LocalTime horaFinal = LocalTime.parse(datos.get(6), formatterTime);

                    try {
                        List<UUID> listEspacios = espacioService.buscarEspacios(UUID.fromString(datos.get(1)),
                                Integer.parseInt(datos.get(2)),fecha,horaInicio,horaFinal, Integer.parseInt(datos.get(3)),tipoUso, datos.get(7));

                        JSONArray jsonEspacios = new JSONArray();
                        for (UUID listEspacio : listEspacios) {
                            jsonEspacios.put(listEspacio);
                        }
                        return jsonEspacios.toString();
                    } catch (Exception e) {
                        return e.toString();
                    }
                }
                case "cambiarPorcentajeUso" -> {
                    try {
                        if (!espacioService.modificarPorcentajeOcupacion(UUID.fromString(datos.get(1)),
                                UUID.fromString(datos.get(2)),Integer.parseInt(datos.get(3)))){
                            return  "No tiene permisos para hacer esta operación o no exixte el espacio";
                        }
                        return "Porcentaje cambiado correctamente";

                    } catch (Exception e) {
                        return e.toString();
                    }
                }
            }
        }
        return "Operación no válida";
    }
}
