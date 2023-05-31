package com.clickada.back.amqp;

import com.clickada.back.application.DominioService;
import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.application.ReservaService;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.TipoUso;
import com.clickada.back.dtos.EspacioDto;
import com.clickada.back.dtos.MapperDtos;
import com.clickada.back.dtos.ReservaDto;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AmqpReservas {
    @Autowired
    PersonaService personaService;
    @Autowired
    ReservaService reservaService;
    @Autowired
    EspacioService espacioService;
    @Autowired
    DominioService dominioService;

    @RabbitListener(queues = "reservas")
    public String receive(ArrayList<String> datos) {
        if (!datos.isEmpty()) {
            String op = datos.get(0);
            switch (op) {
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
                        dominioService.reservarEspacio(UUID.fromString(datos.get(1)), listaEspacios,
                                fecha, horaInicio, horaFinal,
                                tipoUso, Integer.parseInt(datos.get(8)), datos.get(7));
                    } catch (Exception e) {
                        return "ERR:" + e.getMessage();
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
                    String detalles = datos.get(7);

                    try {
                        List<UUID> listEspacios = dominioService.reservaAutomaticaEspacio(UUID.fromString(datos.get(1))
                                ,Integer.parseInt(datos.get(2))
                                ,fecha, horaInicio, horaFinal, Integer.parseInt(datos.get(3)),tipoUso,detalles);
                        JSONArray jsonEspacios = new JSONArray();
                        for (UUID listEspacio : listEspacios) {
                            jsonEspacios.put(listEspacio);
                        }
                        return jsonEspacios.toString();
                    } catch (Exception e) {
                        return "ERR:" + e.getMessage();
                    }
                }
                case "reservasVivas" -> {
                    try {
                        List<Reserva> listReservas = reservaService.obtenerReservasVivas(UUID.fromString(datos.get(1)));
                        MapperDtos mapperDtos = new MapperDtos();
                        List<ReservaDto> reservaDtos = mapperDtos.listaReservaDto(listReservas);
                        Gson gson = new Gson();
                        return gson.toJson(reservaDtos);

                    } catch (Exception e) {
                        return "ERR:" + e.getMessage();
                    }
                }
                case "todasReservas" -> {
                    try {
                        List<Reserva> listReservas = reservaService.listarTodasReservas();
                        MapperDtos mapperDtos = new MapperDtos();
                        List<ReservaDto> reservaDtos = mapperDtos.listaReservaDto(listReservas);
                        Gson gson = new Gson();
                        return gson.toJson(reservaDtos);

                    } catch (Exception e) {
                        return "ERR:" + e.getMessage();
                    }
                }
                case "eliminarReserva" -> {
                    try {
                        dominioService.eliminarReserva(UUID.fromString(datos.get(1)),UUID.fromString(datos.get(2)));
                        Gson gson = new Gson();
                        return gson.toJson("Reserva eliminada correctamente");

                    } catch (Exception e) {
                        return "ERR:" + e.getMessage();
                    }
                }
            }
        }
        return "Operación no válida";
    }
}