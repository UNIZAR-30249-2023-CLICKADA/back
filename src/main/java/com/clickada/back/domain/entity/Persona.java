package com.clickada.back.domain.entity;

import com.clickada.back.domain.entity.auxClasses.Adscripcion;
import com.clickada.back.domain.entity.auxClasses.PersonaRol;
import com.clickada.back.domain.entity.auxClasses.Departamento;
import com.clickada.back.domain.entity.auxClasses.Rol;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "persona")
@NoArgsConstructor
public class Persona {
    @Id
    private UUID idPersona;
    String nombre;
    String eMail;
    Adscripcion adscripcion;
    boolean departamentoDisponible;
    ArrayList<Rol> roles;

    public Persona(String nombre, String eMail, Rol rol){
        this.idPersona = UUID.randomUUID();
        this.nombre = nombre;
        this.eMail = eMail;
        this.departamentoDisponible = optaADepartamento(rol);
        this.roles = new ArrayList<>();
        this.roles.add(rol);
    }
    public void cambiarRol(Rol nuevoRol) {
        this.roles.clear();
        this.roles.add(nuevoRol);
        // gestionar el nuevo rol con los departamentos
        boolean optaADepartamento = optaADepartamento(nuevoRol);
        if (departamentoDisponible && !optaADepartamento) {
            departamentoDisponible = false;
        } else if (!departamentoDisponible && optaADepartamento) {
            departamentoDisponible = true;
        }
    }

    public void anyadirRol() throws Exception {
        if(this.roles.get(0).equals(Rol.GERENTE)){
            this.roles.add(Rol.DOCENTE_INVESTIGADOR);
        }
        else {
            throw new Exception("No es Gerente, no puede tener segundo Rol");
        }
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
