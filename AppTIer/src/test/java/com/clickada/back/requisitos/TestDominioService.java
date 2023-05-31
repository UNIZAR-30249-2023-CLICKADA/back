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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;

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
    private Espacio despacho;
    private final double porcentaje_MAX = 100;
    private double epsilon = 0.000001d;
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
                new ArrayList<>(List.of(LocalDate.now().plusDays(2))),100);
        sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        aula = new Espacio(new Reservabilidad(false,CategoriaReserva.AULA),150, 60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        seminario = new Espacio(new Reservabilidad(false,CategoriaReserva.SEMINARIO),150, 60,
                CategoriaEspacio.SEMINARIO,edificio,new PropietarioEspacio(Eina.EINA));
        despacho = new Espacio(new Reservabilidad(false,CategoriaReserva.DESPACHO),150, 60,
                CategoriaEspacio.DESPACHO,edificio,new PropietarioEspacio(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS));
        edificioRepository.save(edificio);

        espacioRepository.save(sala_comun);
        espacioRepository.save(aula);
        espacioRepository.save(seminario);
        espacioRepository.save(despacho);
        personaRepository.deleteAllInBatch();
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
                new ArrayList<>(List.of(LocalDate.now().plusDays(2))),100);
        sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        aula = new Espacio(new Reservabilidad(false,CategoriaReserva.AULA),150, 60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        seminario = new Espacio(new Reservabilidad(false,CategoriaReserva.SEMINARIO),150, 60,
                CategoriaEspacio.SEMINARIO,edificio,new PropietarioEspacio(Eina.EINA));
        despacho = new Espacio(new Reservabilidad(false,CategoriaReserva.DESPACHO),150, 60,
                CategoriaEspacio.DESPACHO,edificio,new PropietarioEspacio(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS));
        edificioRepository.save(edificio);

        espacioRepository.save(sala_comun);
        espacioRepository.save(aula);
        espacioRepository.save(seminario);
        espacioRepository.save(despacho);

        personaRepository.save(investigador);
        personaRepository.save(docente);
        personaRepository.save(tecnico);
        personaRepository.save(estudiante);
        personaRepository.save(conserje);
        personaRepository.save(gerente);
    }
    @Test
    public void requisito17_cambiarPorcentajeEdificio_modificarTodos() throws Exception{
        //cambio que no afecta ninguna reserva
        double porcentajeNuevo = 80;

        assertEquals(porcentaje_MAX,sala_comun.getPorcentajeUsoPermitido(),epsilon);

        dominioService.cambiarPorcentajeEdificio(gerente.getIdPersona(),porcentajeNuevo);

        List<Espacio> todosEspacios = espacioService.todosEspacios();
        todosEspacios.forEach(espacio -> assertEquals(porcentajeNuevo,espacio.getPorcentajeUsoPermitido(),epsilon));

    }
    @Test
    public void requisito17_cambiarPorcentajeEdificio_modificarNoPersonalizados() throws Exception{
        //cambio que no afecta ninguna reserva
        double porcentajeNuevo_Espacio = 80;
        double porcentajeNuevo_Edificio = 50;
        assertEquals(porcentaje_MAX,sala_comun.getPorcentajeUsoPermitido(),epsilon);
        dominioService.cambiarPorcentajeEspacio(gerente.getIdPersona(),sala_comun.getIdEspacio(),porcentajeNuevo_Espacio);

        List<Espacio> todosEspacios = espacioService.todosEspacios();
        sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        assertEquals(porcentajeNuevo_Espacio,sala_comun.getPorcentajeUsoPermitido(),epsilon);

        dominioService.cambiarPorcentajeEdificio(gerente.getIdPersona(),porcentajeNuevo_Edificio);

        todosEspacios = espacioService.todosEspacios();
        sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        assertEquals(porcentajeNuevo_Espacio,sala_comun.getPorcentajeUsoPermitido(),epsilon);
        todosEspacios.remove(todosEspacios.indexOf(sala_comun));

        todosEspacios.forEach(espacio -> assertEquals(porcentajeNuevo_Edificio,espacio.getPorcentajeUsoPermitido(),epsilon));
    }
    @Test
    public void requisito18_buscarEspacio() throws Exception{
        //DEMASIADOS ESPACIOS PARA LOS QUE HAY DISPONIBLES

        Exception thrown = assertThrows(Exception.class,()->{
            dominioService.reservaAutomaticaEspacio(gerente.getIdPersona(),3,LocalDate.now().plusDays(1),
                    LocalTime.of(18,0),LocalTime.of(19,0),30,TipoUso.DOCENCIA,
                    "Reservar mas espacios de los que hay disponibles");
        });
        assertEquals("No existen esapcios suficientes disponibles con esas caracteristicas",thrown.getMessage());

        // ESPACIOS DISPONIBLES, PERO NO HAY PARA TANTAS PERSONAS
        thrown = assertThrows(Exception.class,()->{
            dominioService.reservaAutomaticaEspacio(gerente.getIdPersona(),1,LocalDate.now().plusDays(1),
                    LocalTime.of(18,0),LocalTime.of(19,0),100,TipoUso.DOCENCIA,
                    "Reservar mas espacios de los que hay disponibles");
        });
        assertEquals("La cantidad de personas para la reserva es demasiado grande para la cantidad de espacios que se proporcionan",thrown.getMessage());
        Espacio sala_comun2 = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio sala_comun3 = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));

        espacioRepository.save(sala_comun2);
        espacioRepository.save(sala_comun3);
        //Espacios y personas correctas
        ArrayList<UUID> resultado = dominioService.reservaAutomaticaEspacio(gerente.getIdPersona(),3,LocalDate.now(),
                LocalTime.of(18,0),LocalTime.of(19,0),180,TipoUso.DOCENCIA,
                "Reservar correcta");
        ArrayList<UUID> listaLocal  = new ArrayList<>(List.of(sala_comun.getIdEspacio(),sala_comun2.getIdEspacio(),
                sala_comun3.getIdEspacio()));
        assertEquals(listaLocal.size(),resultado.size(),epsilon);
        listaLocal.forEach(idEspacio->assertTrue(resultado.contains(idEspacio)));
        //hacemos una reserva en la sala_comun2 y no nos tendria que dejar
        dominioService.reservarEspacio(gerente.getIdPersona(),new ArrayList<>(List.of(sala_comun.getIdEspacio())),
                LocalDate.now().plusDays(1),LocalTime.of(18,30),LocalTime.of(19,30),
                TipoUso.DOCENCIA,50,"Reserva de 50 personas para una sala comun");
       thrown = assertThrows(Exception.class,()->{
            dominioService.reservaAutomaticaEspacio(gerente.getIdPersona(),3,LocalDate.now().plusDays(1),
                    LocalTime.of(18,0),LocalTime.of(19,0),180,TipoUso.DOCENCIA,
                    "Reservar mas espacios de los que hay disponibles");
        });
        assertEquals("No existen esapcios suficientes disponibles con esas caracteristicas",thrown.getMessage());
    }
    @Test
    public void requisito16_CambioRolYPropietarioEspacio() throws Exception{
        Departamento departamentoDocente = docente.getDepartamento();
        //asignamos al despacho como propietario un investigador
        assertTrue(despacho.getPropietarioEspacio().esDepartamento());
        PropietarioEspacio nuevoPropietario = new PropietarioEspacio(List.of(investigador,docente));
        dominioService.cambiarPropietarioEspacio(despacho.getIdEspacio(),nuevoPropietario,gerente.getIdPersona());
        despacho = espacioService.todosEspacios().stream().filter(espacio ->
                espacio.getIdEspacio().equals(despacho.getIdEspacio())).toList().get(0);
        assertTrue(despacho.getPropietarioEspacio().esPersonas());
        assertEquals(2,despacho.getPropietarioEspacio().personas.size());
        //CAMBIAMOS ROL AL INVESTIGADOR a CONSERJE

        dominioService.cambiarRol(gerente.getIdPersona(),investigador.getIdPersona(),"Conserje",null);

        investigador = personaService.todasPersonas().stream().filter(persona ->
                persona.getIdPersona().equals(investigador.getIdPersona())).toList().get(0);
        assertEquals(Rol.CONSERJE,investigador.rolPrincipal());
        assertFalse(investigador.asignable());

        despacho = espacioService.todosEspacios().stream().filter(espacio ->
                espacio.getIdEspacio().equals(despacho.getIdEspacio())).toList().get(0);
        assertTrue(despacho.getPropietarioEspacio().esPersonas());
        assertEquals(1,despacho.getPropietarioEspacio().personas.size());//ahora solo esta el docente como propietario

        //Cambiamos rol al docente y se tendria que asignar como propietario de espacio el departamento del docente
        dominioService.cambiarRol(gerente.getIdPersona(),docente.getIdPersona(),"Conserje",null);

        docente = personaService.todasPersonas().stream().filter(persona ->
                persona.getIdPersona().equals(docente.getIdPersona())).toList().get(0);
        assertEquals(Rol.CONSERJE,docente.rolPrincipal());

        despacho = espacioService.todosEspacios().stream().filter(espacio ->
                espacio.getIdEspacio().equals(despacho.getIdEspacio())).toList().get(0);
        assertFalse(despacho.getPropietarioEspacio().esPersonas());
        assertTrue(despacho.getPropietarioEspacio().esDepartamento());
        assertEquals(departamentoDocente,despacho.getPropietarioEspacio().getDepartamento());
    }
    @Test
    public void requisito19_sinReservasImplicadas() throws Exception{
        //cambio que no afecta ninguna reserva
        double porcentajeNuevo = 80;

        //when(personaRepository.getById(any())).thenReturn(gerente);
        //when(espacioRepository.getById(any())).thenReturn(sala_comun);
        //when(edificioRepository.getById(any())).thenReturn(edificio);
        assertEquals(porcentaje_MAX,sala_comun.getPorcentajeUsoPermitido(),epsilon);
        dominioService.cambiarPorcentajeEspacio(gerente.getIdPersona(),sala_comun.getIdEspacio(),porcentajeNuevo);

        List<Espacio> todosEspacios = espacioService.todosEspacios();
        sala_comun = todosEspacios.stream().filter(espacio -> espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)).findFirst().get();
        assertEquals(porcentajeNuevo,sala_comun.getPorcentajeUsoPermitido(),epsilon);

    }

    @Test
    public void requisito19_Y_32_conReservasImplicadas() throws Exception{
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
        assertEquals(porcentajeNuevo,sala_comun.getPorcentajeUsoPermitido(),epsilon);

    }
    @Test
    public void reservarDiaNoReservable() throws Exception{

        Exception thrown = assertThrows(Exception.class,()-> {
                    dominioService.reservarEspacio(gerente.getIdPersona(), new ArrayList<>(List.of(sala_comun.getIdEspacio())),
                            LocalDate.now().plusDays(2), LocalTime.of(10, 0), LocalTime.of(11, 0),
                            TipoUso.DOCENCIA, 50, "Reserva en un dia no reservable");
                });
        assertEquals("El edificio no tiene habilitado ese día como reservable",thrown.getMessage());

        List<Reserva> reservasVivas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(0,reservasVivas.size());

    }
    @Test
    public void cambioRolDesdeDominioService() throws Exception{
        Exception thrown = assertThrows(Exception.class,()-> {
            dominioService.cambiarRol(estudiante.getIdPersona(),estudiante.getIdPersona(),
                    "Conserje",null);
        });
        assertEquals("Se ncesita un rol gerente para cambiar el rol de cualquier persona",thrown.getMessage());
        //estudiante a consejer bien
        dominioService.cambiarRol(gerente.getIdPersona(),estudiante.getIdPersona(),
                "Conserje",null);
        estudiante = personaService.todasPersonas().stream().filter(persona ->
                persona.getIdPersona().equals(estudiante.getIdPersona())).toList().get(0);
        assertEquals(Rol.CONSERJE,estudiante.rolPrincipal());
        dominioService.cambiarReservabilidadEspacio(aula.getIdEspacio(),new Reservabilidad(true,CategoriaReserva.AULA),
                gerente.getIdPersona());
        aula = espacioService.todosEspacios().stream().filter(espacio ->
                espacio.getIdEspacio().equals(aula.getIdEspacio())).toList().get(0);
        assertTrue(aula.getReservabilidad().reservable);
        // se reserva como conserje un aula
        dominioService.reservarEspacio(estudiante.getIdPersona(),
                new ArrayList<>(List.of(aula.getIdEspacio())),LocalDate.now().plusDays(1),
                LocalTime.of(18,0),LocalTime.of(19,0),TipoUso.DOCENCIA,40,
                "Reservar mas espacios de los que hay disponibles");
        List<Reserva> reservasVivas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(1,reservasVivas.size());
        //se cambia de conserje a estudiante- > como un estudiante no puede reservar un aula, la reserva se eliminara
        dominioService.cambiarRol(gerente.getIdPersona(),estudiante.getIdPersona(),
                "Estudiante",null);
        estudiante = personaService.todasPersonas().stream().filter(persona ->
                persona.getIdPersona().equals(estudiante.getIdPersona())).toList().get(0);
        assertEquals(Rol.ESTUDIANTE,estudiante.rolPrincipal());

        reservasVivas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(0,reservasVivas.size());

    }
    @Test
    public void cambioReservabilidadDesdeDominioService() throws Exception{
        /*Exception thrown = assertThrows(Exception.class,()-> {

        });
        assertEquals("Se ncesita un rol gerente para cambiar el rol de cualquier persona",thrown.getMessage());*/
        //estudiante a consejer bien
        dominioService.reservarEspacio(estudiante.getIdPersona(),
                new ArrayList<>(List.of(sala_comun.getIdEspacio())),LocalDate.now().plusDays(1),
                LocalTime.of(18,0),LocalTime.of(19,0),TipoUso.DOCENCIA,40,
                "Reservar mas espacios de los que hay disponibles");
        List<Reserva> reservasVivas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(1,reservasVivas.size());

        dominioService.cambiarReservabilidadEspacio(sala_comun.getIdEspacio(),new Reservabilidad(true,CategoriaReserva.SEMINARIO),
                gerente.getIdPersona());
        sala_comun = espacioService.todosEspacios().stream().filter(espacio ->
                espacio.getIdEspacio().equals(sala_comun.getIdEspacio())).toList().get(0);
        assertTrue(sala_comun.getReservabilidad().reservable);
        assertEquals(CategoriaReserva.SEMINARIO,sala_comun.getReservabilidad().categoriaReserva);

        reservasVivas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(0,reservasVivas.size());

    }
    //creamos reserva y la eliminamos siendo gerente
    @Test
    public void requisito_31_eliminarReserva() throws Exception {
        dominioService.reservarEspacio(estudiante.getIdPersona(),
                new ArrayList<>(List.of(sala_comun.getIdEspacio())),LocalDate.now().plusDays(1),
                LocalTime.of(18,0),LocalTime.of(19,0),TipoUso.DOCENCIA,40,
                "Reservar mas espacios de los que hay disponibles");
        List<Reserva> reservasVivas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(1,reservasVivas.size());
        Exception thrown = assertThrows(Exception.class,()-> {
            dominioService.eliminarReserva(estudiante.getIdPersona(), reservasVivas.get(0).getIdReserva());
        });
        assertEquals("Se necesita un rol gerente para eliminar una reserva",thrown.getMessage());

        dominioService.eliminarReserva(gerente.getIdPersona(),reservasVivas.get(0).getIdReserva());

        List<Reserva> finalReservasVivas  = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertEquals(0,finalReservasVivas.size());
    }
}
