package com.clickada.back.domain.entity;

import com.clickada.back.domain.entity.auxClasses.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Espacio{
    @Id
    UUID idEspacio;

    CategoriaEspacio categoriaEspacio;

    double tamanyo; //Tamaño del espacio en m2


    Reservabilidad reservabilidad;

    int numMaxOcupantes;

    double porcentajeUsoPermitido;
    LocalTime horaInicio;
    LocalTime horaFin;

    PropietarioEspacio propietarioEspacio;
    UUID idEdificio;

    public Espacio(Reservabilidad reservabilidad, double tamanyo,int numMaxOcupantes, CategoriaEspacio categoriaEspacio,
                   Edificio edificio,PropietarioEspacio propietarioEspacio)
            throws Exception {
        idEspacio = UUID.randomUUID();
        this.reservabilidad = reservabilidad;
        this.tamanyo = tamanyo;
        this.categoriaEspacio = categoriaEspacio;
        this.porcentajeUsoPermitido = edificio.getPorcentajeUsoPermitido();
        this.horaInicio = edificio.getHoraInicio();
        this.horaFin = edificio.getHoraFin();
        this.numMaxOcupantes = numMaxOcupantes;
        this.idEdificio = edificio.getIdEdificio();
        this.asignarAEspacio(propietarioEspacio);
    }

    public void modificarReservabilidad(Persona persona,Reservabilidad nuevaReservabilidad) throws Exception {
        if(!persona.rolPrincipal().equals(Rol.GERENTE)) {
            throw new Exception("Si no es GERENTE no puede Modificar la Reservabilidad del Espacio");
        }
        if(this.categoriaEspacio.equals(CategoriaEspacio.DESPACHO) && nuevaReservabilidad.reservable){
            throw new Exception("Un despacho no puede ser reservable");
        }
        this.reservabilidad = nuevaReservabilidad;
    }
    public void modificarHorarioDisponible(Persona persona,LocalTime horaInicioNueva, LocalTime horaFinNueva) throws Exception {
        if(!persona.rolPrincipal().equals(Rol.GERENTE)) {
            throw new Exception("Si no es GERENTE no puede Modificar el horario de reserva del Espacio");
        }
        if(this.horaInicio.isAfter(horaInicioNueva) || this.horaFin.isBefore(horaFinNueva)){
            throw new Exception("Las horas nuevas de reserva tienen que estar dentro del periodo de reseerva del Edificio");
        }
        this.horaFin=horaFinNueva;
        this.horaInicio = horaInicioNueva;
    }
    public void asignarAEspacio(PropietarioEspacio propietarioEspacio) throws Exception {
        if(propietarioEspacio.esEina()){
            if(     !this.categoriaEspacio.equals(CategoriaEspacio.AULA) &&
                    !this.categoriaEspacio.equals(CategoriaEspacio.SALA_COMUN) &&
                    !this.categoriaEspacio.equals(CategoriaEspacio.SEMINARIO) &&
                    !this.categoriaEspacio.equals(CategoriaEspacio.LABORATORIO)){
                throw new Exception(this.categoriaEspacio+" no puede tener a la Eina como propietario de espacio");
            }
        }
        if(propietarioEspacio.esDepartamento()){
            if(     !this.categoriaEspacio.equals(CategoriaEspacio.DESPACHO) &&
                    !this.categoriaEspacio.equals(CategoriaEspacio.SEMINARIO) &&
                    !this.categoriaEspacio.equals(CategoriaEspacio.LABORATORIO)){
                throw new Exception(this.categoriaEspacio+" no puede tener a un departamento como propietario de espacio");
            }
        }
        if(propietarioEspacio.esPersonas()){
            if(!this.categoriaEspacio.equals(CategoriaEspacio.DESPACHO)){
                throw new Exception("Solo los despachos pueden tener como propietario a persona/as");
            }
            for(Persona persona : propietarioEspacio.personas){
                if(!persona.asignable()){
                    throw new Exception("La persona/as tienen que ser investiador contratado o bien");
                }
            }
        }
        this.propietarioEspacio = propietarioEspacio;
    }

    public void modificarPorcentajeOcupacion(Persona persona, double porcentajeNuevo) throws Exception {
        if(!persona.rolPrincipal().equals(Rol.GERENTE)) {
            throw new Exception("Si no es GERENTE no puede Modificar el porcentaje de uso de un espacio");
        }
        if(porcentajeNuevo>100 || porcentajeNuevo<0) throw new Exception("Porcentaje No valido");
        this.porcentajeUsoPermitido = porcentajeNuevo;
    }

    public int getTotalAsistentesPermitidos (){
        return (int) (this.numMaxOcupantes * this.porcentajeUsoPermitido/100);
    }
}
