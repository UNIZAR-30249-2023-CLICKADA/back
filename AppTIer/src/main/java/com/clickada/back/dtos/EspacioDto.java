package com.clickada.back.dtos;

import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.auxClasses.CategoriaEspacio;
import com.clickada.back.domain.entity.auxClasses.PropietarioEspacio;
import com.clickada.back.domain.entity.auxClasses.Reservabilidad;
import lombok.Builder;
import lombok.Setter;

import javax.persistence.Id;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Builder
@Setter
public class EspacioDto {
    @Id
    UUID idEspacio;

    CategoriaEspacio categoriaEspacio;

    double tamanyo; //Tamaño del espacio en m2

    String nombre;

    Reservabilidad reservabilidad;

    int numMaxOcupantes;

    double porcentajeUsoPermitido;
    String horaInicio;
    String horaFin;

    PropietarioEspacio propietarioEspacio;


}
