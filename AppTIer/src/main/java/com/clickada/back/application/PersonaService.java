package com.clickada.back.application;

import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.entity.auxClasses.CategoriaReserva;
import com.clickada.back.domain.entity.auxClasses.Departamento;
import com.clickada.back.domain.entity.auxClasses.PropietarioEspacio;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PersonaService {

    PersonaRepository personaRepository;

    @Autowired
    public PersonaService(PersonaRepository personaRepository){ this.personaRepository = personaRepository;}
    @Transactional
    public Persona cambiarRol(UUID idPersona, String rol, String departamentoString) throws Exception {
        Rol rolEnum = Rol.getRolByString(rol);
        Departamento departamento = Departamento.getDepartamentoByString(departamentoString);
        if(!personaRepository.existsById(idPersona) || rolEnum == null) {
            throw new Exception("No existe el rol o la persona");
        }
        Persona persona = personaRepository.getById(idPersona);
        persona.cambiarRol(rolEnum,departamento);
        personaRepository.save(persona);
        return persona;
    }
    @Transactional (readOnly = true)
    public List<Persona> todasPersonas(){
        return personaRepository.findAll();
    }
    @Transactional (readOnly = true)
    public boolean aptoParaCambiar(UUID idPersona) {
        if(personaRepository.existsById(idPersona)){
            Persona persona = personaRepository.getById(idPersona);
            return persona.rolPrincipal().equals(Rol.GERENTE);
        }
        return false;
    }
    @Transactional (readOnly = true)
    public Persona loginPersona(String email, String pass){
        Persona p = personaRepository.findByeMail(email);
        if (p!=null && p.getContrasenya().equals(pass)){
            return p;
        }
        else return null;
    }
    @Transactional (readOnly = true)
    public List<CategoriaReserva> permisosDeReserva(UUID idPersona) {
        List<CategoriaReserva> l = new ArrayList<>();
        if (personaRepository.existsById(idPersona)) {
            Persona p = personaRepository.getById(idPersona);
            Rol rol = p.rolPrincipal();
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

    public Persona getPersonaById(UUID idPersona) throws Exception{
        Persona persona = personaRepository.getById(idPersona);
        if(persona==null) throw new Exception("El idPersona no existe");
        return persona;
    }
    public List<Persona> getPersonasById(List<UUID> idPersonas) throws Exception{
        return personaRepository.findAllById(idPersonas);
    }

    public void comprobarPropietarios(PropietarioEspacio propietarioEspacio) throws Exception {
        List<Persona> personas = personaRepository.findAllById(propietarioEspacio.personas);
        for(Persona persona : personas){
            if(!persona.asignable()){
                throw new Exception("La persona/as tienen que ser investiador contratado o bien");
            }
        }
    }
}
