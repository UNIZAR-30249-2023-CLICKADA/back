package com.clickada.back.amqp;
import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.domain.entity.Persona;

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

    @Autowired
    EspacioService espacioService;

        @RabbitListener(queues="personas")
        public String receive(ArrayList<String> datos) {
            if(!datos.isEmpty()){
                String op = datos.get(0);
                switch (op) {
                    case "cambiarRol" -> {
                        try {
                        personaService.cambiarRol(UUID.fromString(datos.get(1)), datos.get(2),datos.get(3));
                        return "OK";
                        } catch (Exception e) {
                            return "Persona no encontrada o rol inválido. No se han hecho cambios";
                        }
                    }
                    case "todasPersonas" -> {
                        JSONArray lista = new JSONArray();
                        List<Persona> pl = personaService.todasPersonas();
                        for(Persona p : pl){
                            JSONObject persona = new JSONObject();
                            persona.put("idPersona",p.getIdPersona());
                            persona.put("nombre",p.getNombre());
                            persona.put("departamento",p.getDepartamento());
                            persona.put("departamentoDisponible",p.isDepartamentoDisponible());
                            persona.put("roles",p.getRoles());
                            persona.put("email",p.getEMail());
                            lista.put(persona);
                        }
                        return lista.toString();
                    }
                    case "loginPersona" -> {
                        Persona p = personaService.loginPersona(datos.get(1),datos.get(2));
                        if (p!=null){
                            JSONObject persona = new JSONObject();
                            persona.put("idPersona",p.getIdPersona());
                            persona.put("nombre",p.getNombre());
                            persona.put("departamento",p.getDepartamento());
                            persona.put("departamentoDisponible",p.isDepartamentoDisponible());
                            persona.put("roles",p.getRoles());
                            persona.put("email",p.getEMail());
                            return persona.toString();
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
