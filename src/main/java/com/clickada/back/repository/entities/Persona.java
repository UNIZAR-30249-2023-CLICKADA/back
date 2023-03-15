package com.clickada.back.repository.entities;

import com.clickada.back.repository.entities.aux.Adscripcion;
import com.clickada.back.repository.entities.aux.PersonaRol;
import com.clickada.back.valueObject.Departamento;
import com.clickada.back.valueObject.Rol;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Slf4j
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persona")
public class Persona {
    @Id
    private UUID idPersona;
    String nombre;
    String eMail;
    PersonaRol personaRol;
    Adscripcion adscripcion;
    boolean departamentoDisponible;

    public Persona(String nombre, String eMail, Rol rol){
        this.idPersona = UUID.randomUUID();
        this.nombre = nombre;
        this.eMail = eMail;
        this.departamentoDisponible = optaADepartamento(rol);
        this.personaRol = new PersonaRol(rol);
    }
    public void cambiarRol(Rol nuevoRol) {
        personaRol.cambiarRol(nuevoRol);
        // gestionar el nuevo rol con los departamentos
        boolean optaADepartamento = optaADepartamento(nuevoRol);
        if (departamentoDisponible && !optaADepartamento) {
            departamentoDisponible = false;
        } else if (!departamentoDisponible && optaADepartamento) {
            departamentoDisponible = true;
        }
    }

    public void anyadirRol() throws Exception {
        personaRol.anyadirRol();
        departamentoDisponible = true;
    }

    public void adscripcionADepartamento(Departamento departamento){
        if(this.departamentoDisponible){
            if(this.adscripcion == null){
                this.adscripcion = new Adscripcion(departamento);
            }else {
                this.adscripcion.setDepartamento(departamento);
            }
        }
    }
    private boolean optaADepartamento(Rol rol){
        if(rol.equals(Rol.DOCENTE_INVESTIGADOR) ||
                rol.equals(Rol.INVESTIGADOR_CONTRATADO) ||
                rol.equals(Rol.TECNICO_LABORATORIO)){
            return true;
        }
        return false;
    }
}
