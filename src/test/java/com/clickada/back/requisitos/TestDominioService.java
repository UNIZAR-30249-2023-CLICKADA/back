package com.clickada.back.requisitos;

import com.clickada.back.BackApplication;
import com.clickada.back.application.DominioService;
import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.application.ReservaService;
import com.clickada.back.domain.EdificioRepository;
import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackApplication.class)
public class TestDominioService {
    @Autowired
    DominioService dominioService;
    @Autowired
    PersonaService personaService;
    @Autowired
    EspacioService espacioService;
    @Autowired
    ReservaService reservaService;
    @Autowired
    ReservaRepository reservaRepository;
    @Autowired
    PersonaRepository personaRepository;
    @Autowired
    EspacioRepository espacioRepository;
    @Autowired
    EdificioRepository edificioRepository;

    @Test
    public void requisito15_sinReservasImplicadas() throws Exception{
        //cambio que no afecta ninguna reserva
        double porcentajeNuevo = 80;
        List<Espacio> todosEspacios = espacioService.todosEspacios();
        Espacio sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        List<Persona> todasPersonas = personaService.todasPersonas();
        Persona gerente = todasPersonas.stream().filter(persona -> persona.rolPrincipal().equals(Rol.GERENTE)).findFirst().get();

        //when(personaRepository.getById(any())).thenReturn(gerente);
        //when(espacioRepository.getById(any())).thenReturn(sala_comun);
        //when(edificioRepository.getById(any())).thenReturn(edificio);
        assertEquals(100,sala_comun.getPorcentajeUsoPermitido());
        dominioService.cambiarPorcentajeEspacio(gerente.getIdPersona(),sala_comun.getIdEspacio(),porcentajeNuevo);

        todosEspacios = espacioService.todosEspacios();
        sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        assertEquals(porcentajeNuevo,sala_comun.getPorcentajeUsoPermitido());

        dominioService.cambiarPorcentajeEspacio(gerente.getIdPersona(),sala_comun.getIdEspacio(),100);
        todosEspacios = espacioService.todosEspacios();
        sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        assertEquals(100,sala_comun.getPorcentajeUsoPermitido());
    }

    @Test
    public void requisito15_conReservasImplicadas() throws Exception{
        //cambio que no afecta ninguna reserva
        double porcentajeNuevo = 80;
        List<Espacio> todosEspacios = espacioService.todosEspacios();
        Espacio sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        List<Persona> todasPersonas = personaService.todasPersonas();
        Persona gerente = todasPersonas.stream().filter(persona -> persona.rolPrincipal().equals(Rol.GERENTE)).findFirst().get();

        dominioService.reservarEspacio(gerente.getIdPersona(),new ArrayList<>(List.of(sala_comun.getIdEspacio())),
                LocalDate.now().plusDays(1),LocalTime.of(10,0),LocalTime.of(11,0),
                TipoUso.DOCENCIA,50,"Reserva de 50 personas para una sala comun");
        List<Reserva> reservasVivas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(1,reservasVivas.size());

        dominioService.cambiarPorcentajeEspacio(gerente.getIdPersona(),sala_comun.getIdEspacio(),porcentajeNuevo);

        reservasVivas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(0,reservasVivas.size());

        todosEspacios = espacioService.todosEspacios();
        sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        assertEquals(porcentajeNuevo,sala_comun.getPorcentajeUsoPermitido());

    }
}
