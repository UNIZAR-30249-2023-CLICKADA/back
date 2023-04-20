package com.clickada.back.amqp;
import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.Reservabilidad;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.json.simple.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
                        if (personaService.cambiarRol(UUID.fromString(datos.get(0)), datos.get(1))) return "ok";
                        return "Persona no encontrada, no se ha podido cambiar su Rol";
                    }
                    case "todasPersonas" -> {
                        JSONArray lista = new JSONArray();
                        List<Persona> pl = personaService.todasPersonas();
                        for(Persona p : pl){
                            JSONObject persona = new JSONObject();
                            persona.put("idPersona",p.getIdPersona());
                            persona.put("nombre",p.getNombre());
                            persona.put("adscripcion",p.getAdscripcion());
                            persona.put("departamentoDisponible",p.isDepartamentoDisponible());
                            persona.put("roles",p.isDepartamentoDisponible());
                            persona.put("email",p.getEMail());
                            lista.add(persona);
                        }

                        return lista.toJSONString();
                    }
                    case "loginPersona" -> {
                        if (personaService.loginPersona(datos.get(1),datos.get(2))){
                            return "True";
                        }
                        else { return  "Login fallido"; }
                    }
                }
            }
        return "ok";
    }

}
