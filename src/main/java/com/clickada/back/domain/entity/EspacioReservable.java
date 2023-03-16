package com.clickada.back.domain.entity;

import com.clickada.back.domain.entity.auxClasses.Edificio;
import com.clickada.back.domain.entity.auxClasses.Reservabilidad;
import com.clickada.back.domain.entity.auxClasses.CategoriaReserva;
import com.clickada.back.domain.entity.auxClasses.Rol;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.UUID;

@Slf4j
@Entity
@NoArgsConstructor
@Getter
@Setter
public class EspacioReservable extends Edificio {
    @Id
    UUID idEspacio; //o algo así intuyo
    @Transient
    Reservabilidad reservabilidad;
    //private HorarioDisponible horarioDisponible;

    public EspacioReservable(Reservabilidad reservabilidad){
        super();
        idEspacio = UUID.randomUUID();
        this.reservabilidad = reservabilidad;

    }

    public void modificarReservabilidad(Persona persona, boolean reservable, CategoriaReserva categoriaReserva) throws Exception {
        if(!persona.roles.get(0).equals(Rol.GERENTE)) {
            throw new Exception("Si no es GERENTE no puede Modificar la Reservabilidad del Espacio");
        }
        this.reservabilidad.reservable = reservable;
        if(reservable){
            this.reservabilidad.categoriaReserva = categoriaReserva;
        }
    }


}
