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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    private Persona investigador;
    private Persona docente;
    private Persona tecnico;
    private Persona estudiante;
    private Persona conserje;
    private Persona gerente;
    private Edificio edificio;
    private Espacio sala_comun;
    private Espacio aula;
    private Espacio seminario;
    private final double porcentaje_MAX = 100;
    @Before
    public void repoblarAntes() throws Exception {
        espacioRepository.deleteAllInBatch();//Limpiar primero para evitar duplicados
        edificioRepository.deleteAllInBatch();
        personaRepository.deleteAllInBatch();
        reservaRepository.deleteAllInBatch();

        investigador = new Persona("Juan", "juan@clickada.com","123", Rol.INVESTIGADOR_CONTRATADO, Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        docente = new Persona("docente","docente@clickada.com","123",Rol.DOCENTE_INVESTIGADOR,Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        tecnico = new Persona("tecnico","tecnico@clickada.com","123",Rol.TECNICO_LABORATORIO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        estudiante = new Persona("estudiante", "estudiante@clickada.com","123",Rol.ESTUDIANTE,null);
        conserje = new Persona("conserje","conserje@clickada.com","123",Rol.CONSERJE,null);
        gerente = new Persona("gerente","gerente@clickada.com","123",Rol.GERENTE,null);
        edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        edificio.setIdEdificio(UUID.randomUUID());
        sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        aula = new Espacio(new Reservabilidad(false,CategoriaReserva.AULA),150, 60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        seminario = new Espacio(new Reservabilidad(false,CategoriaReserva.SEMINARIO),150, 60,
                CategoriaEspacio.SEMINARIO,edificio,new PropietarioEspacio(Eina.EINA));
        edificioRepository.save(edificio);

        espacioRepository.save(sala_comun);
        espacioRepository.save(aula);
        espacioRepository.save(seminario);

        personaRepository.save(investigador);
        personaRepository.save(docente);
        personaRepository.save(tecnico);
        personaRepository.save(estudiante);
        personaRepository.save(conserje);
        personaRepository.save(gerente);
    }
    @After
    public void repoblar() throws Exception {
        espacioRepository.deleteAllInBatch();//Limpiar primero para evitar duplicados
        edificioRepository.deleteAllInBatch();
        personaRepository.deleteAllInBatch();
        reservaRepository.deleteAllInBatch();

        investigador = new Persona("Juan", "juan@clickada.com","123", Rol.INVESTIGADOR_CONTRATADO, Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        docente = new Persona("docente","docente@clickada.com","123",Rol.DOCENTE_INVESTIGADOR,Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        tecnico = new Persona("tecnico","tecnico@clickada.com","123",Rol.TECNICO_LABORATORIO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        estudiante = new Persona("estudiante", "estudiante@clickada.com","123",Rol.ESTUDIANTE,null);
        conserje = new Persona("conserje","conserje@clickada.com","123",Rol.CONSERJE,null);
        gerente = new Persona("gerente","gerente@clickada.com","123",Rol.GERENTE,null);
        edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        edificio.setIdEdificio(UUID.randomUUID());
        sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        aula = new Espacio(new Reservabilidad(false,CategoriaReserva.AULA),150, 60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        seminario = new Espacio(new Reservabilidad(false,CategoriaReserva.SEMINARIO),150, 60,
                CategoriaEspacio.SEMINARIO,edificio,new PropietarioEspacio(Eina.EINA));
        edificioRepository.save(edificio);

        espacioRepository.save(sala_comun);
        espacioRepository.save(aula);
        espacioRepository.save(seminario);

        personaRepository.save(investigador);
        personaRepository.save(docente);
        personaRepository.save(tecnico);
        personaRepository.save(estudiante);
        personaRepository.save(conserje);
        personaRepository.save(gerente);
    }
    @Test
    public void requisito14_cambiarPorcentajeEdificio_modificarTodos() throws Exception{
        //cambio que no afecta ninguna reserva
        double porcentajeNuevo = 80;

        assertEquals(porcentaje_MAX,sala_comun.getPorcentajeUsoPermitido());

        dominioService.cambiarPorcentajeEdificio(gerente.getIdPersona(),porcentajeNuevo);

        List<Espacio> todosEspacios = espacioService.todosEspacios();
        todosEspacios.forEach(espacio -> assertEquals(porcentajeNuevo,espacio.getPorcentajeUsoPermitido()));

    }
    @Test
    public void requisito14_cambiarPorcentajeEdificio_modificarNoPersonalizados() throws Exception{
        //cambio que no afecta ninguna reserva
        double porcentajeNuevo_Espacio = 80;
        double porcentajeNuevo_Edificio = 50;
        assertEquals(porcentaje_MAX,sala_comun.getPorcentajeUsoPermitido());
        dominioService.cambiarPorcentajeEspacio(gerente.getIdPersona(),sala_comun.getIdEspacio(),porcentajeNuevo_Espacio);

        List<Espacio> todosEspacios = espacioService.todosEspacios();
        sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        assertEquals(porcentajeNuevo_Espacio,sala_comun.getPorcentajeUsoPermitido());

        dominioService.cambiarPorcentajeEdificio(gerente.getIdPersona(),porcentajeNuevo_Edificio);

        todosEspacios = espacioService.todosEspacios();
        sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        assertEquals(porcentajeNuevo_Espacio,sala_comun.getPorcentajeUsoPermitido());
        todosEspacios.remove(todosEspacios.indexOf(sala_comun));

        todosEspacios.forEach(espacio -> assertEquals(porcentajeNuevo_Edificio,espacio.getPorcentajeUsoPermitido()));
    }
    @Test
    public void requisito14_buscarEspacio() throws Exception{
        //cambio que no afecta ninguna reserva
        /*List<Espacio> todosEspacios = espacioService.todosEspacios();
        Espacio sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        List<Persona> todasPersonas = personaService.todasPersonas();
        Persona gerente = todasPersonas.stream().filter(persona -> persona.rolPrincipal().equals(Rol.GERENTE)).findFirst().get();

        assertEquals(100,sala_comun.getPorcentajeUsoPermitido());*/

    }
    @Test
    public void requisito15_sinReservasImplicadas() throws Exception{
        //cambio que no afecta ninguna reserva
        double porcentajeNuevo = 80;

        //when(personaRepository.getById(any())).thenReturn(gerente);
        //when(espacioRepository.getById(any())).thenReturn(sala_comun);
        //when(edificioRepository.getById(any())).thenReturn(edificio);
        assertEquals(porcentaje_MAX,sala_comun.getPorcentajeUsoPermitido());
        dominioService.cambiarPorcentajeEspacio(gerente.getIdPersona(),sala_comun.getIdEspacio(),porcentajeNuevo);

        List<Espacio> todosEspacios = espacioService.todosEspacios();
        sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        assertEquals(porcentajeNuevo,sala_comun.getPorcentajeUsoPermitido());

    }

    @Test
    public void requisito15_conReservasImplicadas() throws Exception{
        //cambio que no afecta ninguna reserva
        double porcentajeNuevo = 80;


        dominioService.reservarEspacio(gerente.getIdPersona(),new ArrayList<>(List.of(sala_comun.getIdEspacio())),
                LocalDate.now().plusDays(1),LocalTime.of(10,0),LocalTime.of(11,0),
                TipoUso.DOCENCIA,50,"Reserva de 50 personas para una sala comun");
        List<Reserva> reservasVivas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(1,reservasVivas.size());

        dominioService.cambiarPorcentajeEspacio(gerente.getIdPersona(),sala_comun.getIdEspacio(),porcentajeNuevo);

        reservasVivas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(0,reservasVivas.size());

        List<Espacio> todosEspacios = espacioService.todosEspacios();
        sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        assertEquals(porcentajeNuevo,sala_comun.getPorcentajeUsoPermitido());

    }
}
