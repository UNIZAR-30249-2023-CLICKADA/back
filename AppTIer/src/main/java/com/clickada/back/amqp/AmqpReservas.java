package com.clickada.back.amqp;

import com.clickada.back.application.DominioService;
import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.application.ReservaService;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.TipoUso;
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
                        Persona p = personaService.getPersonaById(UUID.fromString(datos.get(1)));
                        List<UUID> listEspacios = espacioService.buscarEspacios(p, new ArrayList<>(),
                                Integer.parseInt(datos.get(2)), horaInicio, horaFinal, Integer.parseInt(datos.get(3)));

                        JSONArray jsonEspacios = new JSONArray();
                        for (UUID listEspacio : listEspacios) {
                            jsonEspacios.put(listEspacio);
                        }
                        return jsonEspacios.toString();
                    } catch (Exception e) {
                        return e.toString();
                    }
                }
                case "reservasVivas" -> {
                    try {
                        List<Reserva> listReservas = reservaService.obtenerReservasVivas(UUID.fromString(datos.get(1)));
                        JSONArray lista = new JSONArray();

                        for (Reserva r : listReservas) {
                            JSONObject res = new JSONObject();
                            res.put("idReserva", r.getIdReserva().toString());
                            JSONArray periodo = new JSONArray();
                            periodo.put(new JSONObject().put("horaInicio", String.valueOf(r.getPeriodoReserva().getHoraInicio())));
                            periodo.put(new JSONObject().put("horaFin", String.valueOf(r.getPeriodoReserva().getHoraFin())));
                            res.put("periodoReserva", periodo);
                            res.put("idPersona", r.getIdPersona().toString());
                            res.put("tipoDeuso", r.getTipoDeUso());
                            res.put("numOcupantes", r.getNumOcupantes());
                            res.put("detallesReserva", r.getDetallesReserva());
                            res.put("fecha", r.getFecha());

                            JSONArray idsEsp = new JSONArray();
                            List<UUID> lis = r.getIdEspacios();
                            for (UUID id : lis) {
                                idsEsp.put(id.toString());
                            }
                            res.put("idEspacios", idsEsp);
                            lista.put(res);
                        }
                        return lista.toString();
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                }
                case "todasReservas" -> {
                    try {
                        List<Reserva> listReservas = reservaService.listarTodasReservas();
                        JSONArray l = new JSONArray();

                        for (Reserva r : listReservas) {
                            JSONObject res = new JSONObject();
                            res.put("idReserva", r.getIdReserva().toString());
                            JSONArray periodo = new JSONArray();
                            periodo.put(new JSONObject().put("horaInicio", String.valueOf(r.getPeriodoReserva().getHoraInicio())));
                            periodo.put(new JSONObject().put("horaFin", String.valueOf(r.getPeriodoReserva().getHoraFin())));
                            res.put("periodoReserva", periodo);
                            res.put("idPersona", r.getIdPersona().toString());
                            res.put("tipoDeuso", r.getTipoDeUso());
                            res.put("numOcupantes", r.getNumOcupantes());
                            res.put("detallesReserva", r.getDetallesReserva());
                            res.put("fecha", r.getFecha());

                            JSONArray idsEsp = new JSONArray();
                            List<UUID> lis = r.getIdEspacios();
                            for (UUID id : lis) {
                                idsEsp.put(id.toString());
                            }
                            res.put("idEspacios", idsEsp);
                            l.put(res);
                        }
                        return l.toString();
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                }
            }
        }
        return "Operación no válida";
    }
}