package com.clickada.back.entities;

import com.clickada.back.valueObject.CategoriaReserva;
import com.clickada.back.valueObject.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;
@Slf4j
@Entity
@NoArgsConstructor
@Data
public class EspacioReservable extends Edificio {
    @Id
    private UUID idEspacio; //o algo así intuyo
    private Reservabilidad reservabilidad;
    //private HorarioDisponible horarioDisponible;

    public EspacioReservable(Reservabilidad reservabilidad){
        super();
        idEspacio = UUID.randomUUID();
        this.reservabilidad = reservabilidad;

    }

    public void modificarReservabilidad(Persona persona, boolean reservable, CategoriaReserva categoriaReserva) throws Exception {
        if(!persona.getPersonaRol().roles.get(0).equals(Rol.GERENTE)) {
            throw new Exception("Si no es GERENTE no puede Modificar la Reservabilidad del Espacio");
        }
        this.reservabilidad.reservable = reservable;
        if(reservable){
            this.reservabilidad.categoriaReserva = categoriaReserva;
        }
    }


}
