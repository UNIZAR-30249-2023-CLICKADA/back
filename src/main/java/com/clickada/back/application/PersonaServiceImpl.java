package com.clickada.back.application;

import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.entity.auxClasses.CategoriaReserva;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class PersonaServiceImpl implements PersonaService {

    PersonaRepository personaRepository;

    @Autowired
    public PersonaServiceImpl(PersonaRepository personaRepository){ this.personaRepository = personaRepository;}

    @Override
    public boolean cambiarRol(UUID idPersona, String rol) {
        Rol rolEnum = Rol.getRolByString(rol);
        if(personaRepository.existsById(idPersona) && rolEnum != null){

            Persona persona = personaRepository.getById(idPersona);
            persona.cambiarRol(rolEnum);
            personaRepository.save(persona);
            return true;
        }
        return false;
    }

    @Override
    public List<Persona> todasPersonas(){
        return personaRepository.findAll();
    }

    @Override
    public boolean aptoParaCambiar(UUID idPersona) {
        if(personaRepository.existsById(idPersona)){
            Persona persona = personaRepository.getById(idPersona);
            return persona.getRoles().get(0).equals(Rol.GERENTE);
        }
        return false;
    }

    @Override
    public  Persona obtenerPersona(String email){
        return personaRepository.findByeMail(email);
    }
    @Override
    public boolean loginPersona(String email, String pass){
        Persona p = personaRepository.findByeMail(email);
        if (p!=null) return p.getContrasenya().equals(pass);
        else return false;
    }

    @Override
    public List<CategoriaReserva> permisosDeReserva(UUID idPersona) {
        List<CategoriaReserva> l = new ArrayList<>();
        if (personaRepository.existsById(idPersona)) {
            Persona p = personaRepository.getById(idPersona);
            Rol rol = p.getRoles().get(0);
            switch (rol) {
                case ESTUDIANTE -> l.add(CategoriaReserva.SALA_COMUN);
                case CONSERJE -> {
                    l.add(CategoriaReserva.AULA); l.add(CategoriaReserva.SALA_COMUN);
                }
                case DOCENTE_INVESTIGADOR, INVESTIGADOR_CONTRATADO -> {
                    l.add(CategoriaReserva.AULA); l.add(CategoriaReserva.SALA_COMUN);
                    l.add(CategoriaReserva.LABORATORIO);
                }
                case TECNICO_LABORATORIO -> l.add(CategoriaReserva.SALA_COMUN);
                case GERENTE -> {
                    l.add(CategoriaReserva.SALA_COMUN); l.add(CategoriaReserva.LABORATORIO);
                    l.add(CategoriaReserva.AULA); l.add(CategoriaReserva.SEMINARIO);
                }
            }
        }
        return l;
    }

}