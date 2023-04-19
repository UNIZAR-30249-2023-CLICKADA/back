package com.clickada.back.domain.entity;

import com.clickada.back.domain.entity.auxClasses.Departamento;
import com.clickada.back.domain.entity.auxClasses.Rol;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Base64;
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
    @Column (unique = true)
    String eMail;
    String contrasenya;
    Departamento departamento;
    boolean departamentoDisponible;
    ArrayList<Rol> roles;
    public Persona(String nombre, String eMail, String pass, Rol rol, Departamento departamento) throws Exception {
        this.idPersona = UUID.randomUUID();
        this.nombre = nombre;
        this.eMail = eMail;
        this.contrasenya = Base64.getEncoder().encodeToString(pass.getBytes());
        this.departamentoDisponible = rol.adscribible(rol);
        if(this.departamentoDisponible){
            if(departamento==null) {
                throw new Exception("El departamento proporcionado no existe, y este rol necesita estar asignado a un departamento");
            }
            this.departamento = departamento;
        }
        this.roles = new ArrayList<>();
        this.roles.add(rol);
    }
    public void cambiarRol(Rol nuevoRol,Departamento departamento) throws Exception {
        if (nuevoRol!= null){
            // gestionar el nuevo rol con los departamentos
            boolean adscribible = nuevoRol.adscribible(nuevoRol);
            if (departamentoDisponible && !adscribible) {
                departamentoDisponible = false;
                this.departamento = null;
            } else if (!departamentoDisponible && adscribible) {
                if(departamento == null) throw new Exception("Este rol necesita estar asignado a un Departamento");
                departamentoDisponible = true;
                this.departamento = departamento;
            } else if(departamentoDisponible && adscribible){
                if(departamento!=null){
                    this.departamento = departamento;
                }
            }
            this.roles.clear();
            this.roles.add(nuevoRol);
        }
    }

    public void cambiarDepartamento(Departamento departamento){
        if(this.departamentoDisponible && departamento!=null){
            this.departamento = departamento;
        }
    }
    public void anyadirRol(Departamento departamento) throws Exception {
        if(this.rolPrincipal().equals(Rol.GERENTE) && departamento!=null){
            this.roles.add(Rol.DOCENTE_INVESTIGADOR);
            this.departamento = departamento;
            departamentoDisponible = true;
        }
        else {
            throw new Exception("No es Gerente o no existe el departamento y necesita uno");
        }
    }
    public boolean asignable(){
        return this.rolPrincipal().equals(Rol.DOCENTE_INVESTIGADOR) ||
                this.rolPrincipal().equals(Rol.INVESTIGADOR_CONTRATADO) || gerente_docente();
    }
    public boolean gerente_docente(){
        return this.roles.size()>1;
    }
    public Rol rolPrincipal(){
        return this.roles.get(0);
    }
    public Rol rolSecundario(){
        return this.roles.get(1);
    }
}
