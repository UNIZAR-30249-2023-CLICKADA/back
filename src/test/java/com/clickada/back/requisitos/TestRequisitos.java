package com.clickada.back.requisitos;

import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.application.ReservaService;
import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.*;
import com.clickada.back.domain.entity.Persona;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TestRequisitos {
    @InjectMocks
    PersonaService personaService;
    @Mock
    ReservaRepository reservaRepository;
    @Mock
    PersonaRepository personaRepository;
    @Mock
    EspacioRepository espacioRepository;
    @InjectMocks
    EspacioService espacioService;
    @InjectMocks
    ReservaService reservaService;
    @Test
    void requisito2() throws Exception {
        Persona persona = new Persona("Pepe","pepe@gmail.com","123", Rol.CONSERJE,null);

        Assert.state(persona.getEMail().equals("pepe@gmail.com"),"Conserje");
    }

    @Test
    public void requisito3() throws Exception {
        Persona per = new Persona("Señor Tst","unico@mail.com", "123",
                Rol.getRolByString("Estudiante"),null) ;
        assertEquals(Rol.ESTUDIANTE,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Conserje"),null);
        assertEquals(Rol.CONSERJE,per.rolPrincipal());

        Exception thrown = assertThrows(Exception.class,()->{
            per.cambiarRol(Rol.getRolByString("Investigador"),null);
        });
        assertEquals("Este rol necesita estar asignado a un Departamento",thrown.getMessage());
        assertEquals(Rol.CONSERJE,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Investigador"),Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(Rol.INVESTIGADOR_CONTRATADO,per.rolPrincipal());
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,per.getDepartamento());

        per.cambiarRol(Rol.getRolByString("Docente"),null);
        assertEquals(Rol.DOCENTE_INVESTIGADOR,per.rolPrincipal());
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,per.getDepartamento());

        per.cambiarRol(Rol.getRolByString("Tecnico"),null);
        assertEquals(Rol.TECNICO_LABORATORIO,per.rolPrincipal());
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,per.getDepartamento());

        per.cambiarRol(Rol.getRolByString("Gerente"),Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertEquals(Rol.GERENTE,per.rolPrincipal());
        assertNull(per.getDepartamento());

        thrown = assertThrows(Exception.class,()->{
            per.anyadirRol(null);
                });
        assertEquals("No es Gerente o no existe el departamento y necesita uno",thrown.getMessage());

        //Debe mantener el anterior
        assertEquals(Rol.GERENTE,per.rolPrincipal());
        assertEquals(1,per.getRoles().size());

        per.anyadirRol(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertEquals(Rol.DOCENTE_INVESTIGADOR,per.rolSecundario());

        when(personaRepository.existsById(any())).thenReturn(true);
        when(personaRepository.getById(any())).thenReturn(per);
        boolean resultado = personaService.cambiarRol(per.getIdPersona(),"Docente","Ingenieria");
        assertTrue(resultado);
        resultado = personaService.cambiarRol(per.getIdPersona(),"Tecnico","Informatica");
        assertTrue(resultado);
        resultado = personaService.cambiarRol(per.getIdPersona(),"Gerente",null);
        assertTrue(resultado);

    }

    @Test
    void requisito4_5_6() throws Exception {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";
        Rol rol_docente = Rol.DOCENTE_INVESTIGADOR;
        Rol rol_gerente = Rol.GERENTE;

        Persona persona = new Persona(nombre, eMail,"123",rol_docente,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Exception thrown = assertThrows(Exception.class,()-> {
            persona.anyadirRol(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        });
        assertEquals("No es Gerente o no existe el departamento y necesita uno",thrown.getMessage());

        persona.cambiarRol(rol_gerente,null);
        Assertions.assertEquals(rol_gerente, persona.rolPrincipal());

        persona.anyadirRol(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Assertions.assertEquals(rol_docente, persona.rolSecundario());

        persona.cambiarRol(Rol.ESTUDIANTE,null);
        Assertions.assertEquals(1,persona.getRoles().size());
        persona.cambiarRol(Rol.DOCENTE_INVESTIGADOR,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Assertions.assertEquals(1,persona.getRoles().size());

    }
    @Test
    void requisito7() throws Exception {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";

        Persona investigador = new Persona(nombre, eMail,"123",Rol.INVESTIGADOR_CONTRATADO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Persona docente = new Persona(nombre,eMail,"123",Rol.DOCENTE_INVESTIGADOR,Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        Persona tecnico = new Persona(nombre,eMail,"123",Rol.TECNICO_LABORATORIO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);

        assertEquals(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS,investigador.getDepartamento());

        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,docente.getDepartamento());

        assertEquals(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS,tecnico.getDepartamento());

    }
    @Test
    void requisito8_9() throws Exception {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";

        Persona estudiante = new Persona(nombre, eMail,"123",Rol.ESTUDIANTE,null);
        Persona conderje = new Persona(nombre,eMail,"123",Rol.CONSERJE,null);
        Persona gerente = new Persona(nombre,eMail,"123",Rol.GERENTE,null);

        assertNull(estudiante.getDepartamento());

        assertNull(conderje.getDepartamento());

        assertNull(gerente.getDepartamento());

        gerente.anyadirRol(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertEquals(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS,gerente.getDepartamento());

        gerente.cambiarDepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,gerente.getDepartamento());

    }
    @Test
    public void requisito10() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);
        Espacio espacio = new Espacio(new Reservabilidad(false,CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Persona docente = new Persona("Ger","ger@clickada.es","1234",Rol.DOCENTE_INVESTIGADOR,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Reservabilidad reservabilidad1 = new Reservabilidad(false, CategoriaReserva.LABORATORIO);
        Reservabilidad reservabilidad2 = new Reservabilidad(true,CategoriaReserva.AULA);

        espacio.modificarReservabilidad(gerente,reservabilidad1);
        assertEquals(false,espacio.getReservabilidad().reservable);
        assertEquals(reservabilidad1.categoriaReserva,espacio.getReservabilidad().categoriaReserva);

        espacio.modificarReservabilidad(gerente,reservabilidad2);
        assertEquals(true,espacio.getReservabilidad().reservable);
        assertEquals(CategoriaReserva.AULA,espacio.getReservabilidad().categoriaReserva);

        assertEquals(edificio.getHoraInicio(),espacio.getHoraInicio());
        espacio.modificarHorarioDisponible(gerente,
                LocalTime.of(9,0),
                LocalTime.of(19,0));
        assertNotEquals(edificio.getHoraFin(),espacio.getHoraFin());

        Exception thrown = assertThrows(Exception.class,()-> {
            espacio.modificarHorarioDisponible(gerente,
                    LocalTime.of(7,0),
                    LocalTime.of(21,0));
        });
        assertEquals("Las horas nuevas de reserva tienen que estar dentro del periodo de reseerva del Edificio",thrown.getMessage());

        thrown = assertThrows(Exception.class,()-> {
            espacio.modificarHorarioDisponible(docente,
                    LocalTime.of(7,0),
                    LocalTime.of(21,0));
        });
        assertEquals("Si no es GERENTE no puede Modificar el horario de reserva del Espacio",thrown.getMessage());
        when(personaRepository.existsById(any())).thenReturn(true);
        when(espacioRepository.existsById(any())).thenReturn(true);
        when(personaRepository.getById(any())).thenReturn(gerente);
        when(espacioRepository.getById(any())).thenReturn(espacio);
        boolean resultado = espacioService.cambiarReservabilidadEspacio(espacio.getIdEspacio(),reservabilidad1,gerente.getIdPersona());
        assertTrue(resultado);

        resultado = espacioService.cambiarReservabilidadEspacio(espacio.getIdEspacio(),reservabilidad2,gerente.getIdPersona());
        assertTrue(resultado);
        when(personaRepository.getById(any())).thenReturn(docente);
        thrown = assertThrows(Exception.class,()->{
            espacioService.cambiarReservabilidadEspacio(espacio.getIdEspacio(),reservabilidad2,docente.getIdPersona());
        });
        assertEquals("Si no es GERENTE no puede Modificar la Reservabilidad del Espacio",thrown.getMessage());

    }

    @Test
    void requisito11() throws Exception{
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);

        Espacio sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));

        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE,null);
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(sala_comun.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(8,0),LocalTime.of(10,0)),estudiante.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now());

        when(personaRepository.getById(any())).thenReturn(estudiante);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(sala_comun);

        boolean reservaCorrecta = espacioService.reservarEspacio(estudiante.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                20,"DD");
        assertTrue(reservaCorrecta);

        //si intentamos reservar donde ya hay una reserva en el mismo horario dara error
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>(List.of(reserva)));
        ArrayList<UUID> finalIdEspacios = idEspacios;
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(estudiante.getIdPersona(), finalIdEspacios,
                    LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0), TipoUso.DOCENCIA,
                    20, "DD");
        });
        assertEquals("Ya existe una reserva en el horario introducido",thrown.getMessage());

        //Si el estudiante intenta reservar un laboratorio dara error
        idEspacios = new ArrayList<>(List.of(laboratorio.getIdEspacio()));
        when(espacioRepository.getById(any())).thenReturn(laboratorio);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(estudiante.getIdPersona(), finalIdEspacios,
                    LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0), TipoUso.DOCENCIA,
                    20, "DD");
        });
        assertEquals("Un estudiante solo puede reservar SALAS COMUNES",thrown.getMessage());

        laboratorio.modificarReservabilidad(gerente,new Reservabilidad(true,CategoriaReserva.SALA_COMUN));
        //cambiamos la reservabilidad del laboratiorio poniendolo como sala comun -> nos tendria que dejar
        reservaCorrecta = espacioService.reservarEspacio(estudiante.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                20,"DD");
        assertTrue(reservaCorrecta);
    }

    @Test
    void requisito12() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);
        Espacio sala_comun = new Espacio(new Reservabilidad(),150,60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        assertEquals(CategoriaEspacio.SALA_COMUN,sala_comun.getCategoriaEspacio());
    }
    @Test
    void requisito13() throws Exception {

        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Espacio sala_comun = new Espacio(new Reservabilidad(true,CategoriaReserva.SALA_COMUN),150,60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        assertEquals(CategoriaReserva.SALA_COMUN,sala_comun.getReservabilidad().categoriaReserva);
        sala_comun.modificarReservabilidad(gerente,new Reservabilidad(true,CategoriaReserva.LABORATORIO));
        assertEquals(CategoriaReserva.LABORATORIO,sala_comun.getReservabilidad().categoriaReserva);
        assertEquals(CategoriaEspacio.SALA_COMUN,sala_comun.getCategoriaEspacio());
    }
    //Test 14 y 15 son de porcentaje de uso maximo

    @Test
    void requisito16() throws Exception{
        //yyyy-mm-dd
        //hh:mm
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);
        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio laboratorio2 = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),100, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio sala_comun = new Espacio(new Reservabilidad(true,CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(laboratorio.getIdEspacio(),laboratorio2.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(8,0),LocalTime.of(10,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now());

        when(personaRepository.getById(any())).thenReturn(gerente);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(laboratorio).thenReturn(laboratorio2);

        boolean reservaCorrecta = espacioService.reservarEspacio(gerente.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                100,"DD");
        assertTrue(reservaCorrecta);
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(gerente.getIdPersona(),idEspacios,
                    LocalDate.now(),LocalTime.of(19,0),LocalTime.of(20,0),TipoUso.DOCENCIA,
                    300,"DD");
        });
        assertEquals("Se supera el numero máximo de asistentes de los espacios seleccionados siendo "+
                120 + " el total de asistentes permitidos y "+300
                +" el numero de asistentes de la reserva.",thrown.getMessage());

        idEspacios.add(sala_comun.getIdEspacio());
        when(espacioRepository.getById(any())).thenReturn(laboratorio).thenReturn(laboratorio2).thenReturn(sala_comun);
        when(espacioRepository.findAll()).thenReturn(List.of(laboratorio,laboratorio2,sala_comun));
        //Ahoras la reserva con menos parametros semi-automatica
        List<UUID> listaBusqueda = espacioService.buscarEspacios(gerente.getIdPersona(),3,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),100,TipoUso.DOCENCIA,"DD");
        listaBusqueda.forEach(idEspacio -> {assertEquals(idEspacio,idEspacios.get(listaBusqueda.indexOf(idEspacio)));});

        List<UUID> listaBusqueda2 = espacioService.buscarEspacios(gerente.getIdPersona(),2,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),100,TipoUso.DOCENCIA,"DD");
        assertEquals(2,listaBusqueda2.size());

        thrown = assertThrows(Exception.class,()-> {
            espacioService.buscarEspacios(gerente.getIdPersona(),2,
                    LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),130,TipoUso.DOCENCIA,"DD");
        });
        assertEquals("La cantidad de personas para la reserva es demasiado grande para la cantidad de espacios que se proporcionan",thrown.getMessage());

    }
    @Test
    void requisito17() throws Exception{
        //Reservas puntuales y empiezan un dia y acaban el mismo dia
    }
    @Test
    void requisito18() throws Exception{
        //true reservar AULA perona no estuiante ni tecnico de laboratiorio
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);
        Espacio aula = new Espacio(new Reservabilidad(true, CategoriaReserva.AULA),150, 60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE,null);
        Persona tecnico_laboratorio = new Persona("Ger","ger@clickada.es","1234",Rol.TECNICO_LABORATORIO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);

        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(aula.getIdEspacio()));
        when(personaRepository.getById(any())).thenReturn(gerente).thenReturn(estudiante).thenReturn(tecnico_laboratorio);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(aula);
        boolean reservaCorrecta = espacioService.reservarEspacio(gerente.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                20,"DD");
        assertTrue(reservaCorrecta);
        //false reservar AULA siendo un estudiante
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(estudiante.getIdPersona(),idEspacios,
                    LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                    20,"DD");
        });
        assertEquals("Un estudiante solo puede reservar SALAS COMUNES",thrown.getMessage());

        // false reservar AULA siendo un tecnico de laboratorio
        thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(tecnico_laboratorio.getIdPersona(),idEspacios,
                    LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                    20,"DD");
        });
        assertEquals("Un Tecnico de laboratorio no puede reservar aulas",thrown.getMessage());
    }

    @Test
    void requisito19() throws Exception{
        //true reservar LABORATORIOS cualqueira menos estudiantes
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);
        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE,null);
        Persona tecnico_laboratorio = new Persona("Ger","ger@clickada.es","1234",Rol.TECNICO_LABORATORIO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Persona investigador = new Persona("Ger","ger@clickada.es","1234",Rol.INVESTIGADOR_CONTRATADO,Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        Persona docente = new Persona("Ger","ger@clickada.es","1234",Rol.DOCENTE_INVESTIGADOR,Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        Persona conserje = new Persona("Ger","ger@clickada.es","1234",Rol.CONSERJE,null);

        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(laboratorio.getIdEspacio()));
        when(personaRepository.getById(any())).thenReturn(gerente)
                .thenReturn(conserje)
                .thenReturn(estudiante)
                .thenReturn(docente)
                .thenReturn(investigador)
                .thenReturn(tecnico_laboratorio);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(laboratorio);
        boolean reservaCorrecta = espacioService.reservarEspacio(gerente.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                20,"DD");
        assertTrue(reservaCorrecta);
        reservaCorrecta = espacioService.reservarEspacio(conserje.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                20,"DD");
        assertTrue(reservaCorrecta);
        //false reservar LABORATORIO siendo un estudiante
        Exception thrown = assertThrows(Exception.class,()-> {
                    espacioService.reservarEspacio(estudiante.getIdPersona(), idEspacios,
                            LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0), TipoUso.DOCENCIA,
                            20, "DD");
        });
        assertEquals("Un estudiante solo puede reservar SALAS COMUNES",thrown.getMessage());

        // true reservar LABORATORIO siendo tecnico, investigador,docente solo pueden reservar
        //laboratiorios de su mismo departamento

        thrown =  assertThrows(Exception.class,()->{
            espacioService.reservarEspacio(docente.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                20,"DD");
        });

        assertEquals("Los tecnicos de laboratorio, investigador contratado y docente investigador solo pueden reservar " +
                "laboratiorios que esten adscritos a un departamento",thrown.getMessage());

        laboratorio.asignarAEspacio(new PropietarioEspacio(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS));
        thrown = assertThrows(Exception.class,()->{
            espacioService.reservarEspacio(investigador.getIdPersona(),idEspacios,
                    LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                    20,"DD");
        });
        assertEquals("Los tecnicos de laboratorio, investigador contratado y docente investigador solo pueden reservar " +
                "laboratorios de su mismo departamento",thrown.getMessage());

        tecnico_laboratorio.cambiarDepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        boolean resultado = espacioService.reservarEspacio(tecnico_laboratorio.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                20,"DD");
        assertTrue(resultado);
    }
    @Test
    void requisito20() throws Exception{
        // despachos no pueden ser reservables
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);

        Exception thrown = assertThrows(Exception.class,()->{new Espacio(new Reservabilidad(true, CategoriaReserva.DESPACHO), 150, 60,
                CategoriaEspacio.DESPACHO, edificio, new PropietarioEspacio(Eina.EINA));
        });
        assertEquals("Los despachos no pueden ser reservables",thrown.getMessage());
        Espacio despacho = new Espacio(new Reservabilidad(false, CategoriaReserva.DESPACHO),150, 60,
                CategoriaEspacio.DESPACHO,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        thrown = assertThrows(Exception.class,()->{
            despacho.modificarReservabilidad(gerente,new Reservabilidad(true,CategoriaReserva.DESPACHO));
        });
        assertEquals("Los despachos no pueden ser reservables",thrown.getMessage());

        assertFalse(despacho.getReservabilidad().reservable);
    }
    @Test
    void requisito21() throws Exception{
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);
        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio laboratorio2 = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),100, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio sala_comun = new Espacio(new Reservabilidad(true,CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(laboratorio.getIdEspacio(),laboratorio2.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(8,0),LocalTime.of(10,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now());

        when(personaRepository.getById(any())).thenReturn(gerente);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(laboratorio).thenReturn(laboratorio2);

        boolean reservaCorrecta = espacioService.reservarEspacio(gerente.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                100,"DD");
        assertTrue(reservaCorrecta);
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(gerente.getIdPersona(),idEspacios,
                    LocalDate.now(),LocalTime.of(19,0),LocalTime.of(20,0),TipoUso.DOCENCIA,
                    300,"DD");
        });
        assertEquals("Se supera el numero máximo de asistentes de los espacios seleccionados siendo "+
                120 + " el total de asistentes permitidos y "+300
                +" el numero de asistentes de la reserva.",thrown.getMessage());

        idEspacios.add(sala_comun.getIdEspacio());
        when(espacioRepository.getById(any())).thenReturn(laboratorio).thenReturn(laboratorio2).thenReturn(sala_comun);
        when(espacioRepository.findAll()).thenReturn(List.of(laboratorio,laboratorio2,sala_comun));
        //Ahoras la reserva con menos parametros semi-automatica
        List<UUID> listaBusqueda = espacioService.buscarEspacios(gerente.getIdPersona(),3,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),100,TipoUso.DOCENCIA,"DD");
        listaBusqueda.forEach(idEspacio -> {assertEquals(idEspacio,idEspacios.get(listaBusqueda.indexOf(idEspacio)));});

        List<UUID> listaBusqueda2 = espacioService.buscarEspacios(gerente.getIdPersona(),2,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),100,TipoUso.DOCENCIA,"DD");
        assertEquals(2,listaBusqueda2.size());

        thrown = assertThrows(Exception.class,()-> {
            espacioService.buscarEspacios(gerente.getIdPersona(),2,
                    LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),130,TipoUso.DOCENCIA,"DD");
        });
        assertEquals("La cantidad de personas para la reserva es demasiado grande para la cantidad de espacios que se proporcionan",thrown.getMessage());

    }
    @Test
    void requisito22() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);

        Espacio sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));

        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE,null);
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(sala_comun.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(8,0),LocalTime.of(10,0)),estudiante.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now());

        when(personaRepository.getById(any())).thenReturn(estudiante);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(sala_comun);

        boolean reservaCorrecta = espacioService.reservarEspacio(estudiante.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                20,"DD");
        assertTrue(reservaCorrecta);

        //si intentamos reservar donde ya hay una reserva en el mismo horario dara error
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>(List.of(reserva)));
        ArrayList<UUID> finalIdEspacios = idEspacios;
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(estudiante.getIdPersona(), finalIdEspacios,
                    LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0), TipoUso.DOCENCIA,
                    20, "DD");
        });
        assertEquals("Ya existe una reserva en el horario introducido",thrown.getMessage());
    }
    @Test
    void requisito23() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);

        Espacio sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio aula = new Espacio(new Reservabilidad(true, CategoriaReserva.AULA),150, 60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio seminario = new Espacio(new Reservabilidad(true, CategoriaReserva.SEMINARIO),150, 60,
                CategoriaEspacio.SEMINARIO,edificio,new PropietarioEspacio(Eina.EINA));

        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(sala_comun.getIdEspacio(),laboratorio.getIdEspacio(),aula.getIdEspacio()
                ,seminario.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(8,0),LocalTime.of(10,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now());

        when(personaRepository.getById(any())).thenReturn(gerente);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(sala_comun)
                .thenReturn(laboratorio)
                .thenReturn(aula)
                .thenReturn(seminario).thenReturn(sala_comun)
                .thenReturn(laboratorio)
                .thenReturn(aula)
                .thenReturn(seminario);

        boolean reservaCorrecta = espacioService.reservarEspacio(gerente.getIdPersona(),idEspacios,
                LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                20,"DD");
        assertTrue(reservaCorrecta);

        //si intentamos reservar donde ya hay una reserva en el mismo horario dara error
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>(List.of(reserva)));
        ArrayList<UUID> finalIdEspacios = idEspacios;
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(gerente.getIdPersona(), finalIdEspacios,
                    LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0), TipoUso.DOCENCIA,
                    20, "DD");
        });
        assertEquals("Ya existe una reserva en el horario introducido",thrown.getMessage());
    }
    //RF-24 se cumple en los requisitos de reservas
    @Test
    void requisito25() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);

        Espacio sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));

        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE,null);

        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(sala_comun.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(8,0),LocalTime.of(10,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now());
        Reserva reserva2 = new Reserva(new PeriodoReserva(LocalTime.of(18,0),LocalTime.of(19,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now());
        Reserva reserva3 = new Reserva(new PeriodoReserva(LocalTime.of(17,0),LocalTime.of(19,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now());
        Reserva reserva4 = new Reserva(new PeriodoReserva(LocalTime.of(8,0),LocalTime.of(10,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.of(2023,4,25));
        Reserva reserva5 = new Reserva(new PeriodoReserva(LocalTime.of(18,0),LocalTime.of(19,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.of(2023,4,12));
        when(personaRepository.existsById(any())).thenReturn(true);
        when(personaRepository.getById(any())).thenReturn(estudiante).thenReturn(gerente);
        when(reservaRepository.findAll()).thenReturn(new ArrayList<>(List.of(reserva,reserva4,reserva2,reserva5,reserva3)));
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>(List.of(reserva,reserva2,reserva3)));
        Exception thrown = assertThrows(Exception.class,()->{
            reservaService.obtenerReservasVivas(estudiante.getIdPersona());
        });
        assertEquals("Necesitas rol de gerente para obtener reservas vivas",thrown.getMessage());

        List<Reserva> listaReservas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertNotNull(listaReservas);
        assertEquals(3,listaReservas.size());

        when(personaRepository.existsById(any())).thenReturn(false);

        thrown = assertThrows(Exception.class,()->{
            reservaService.obtenerReservasVivas(estudiante.getIdPersona());
        });
        assertEquals("Esa persona no existe",thrown.getMessage());

    }
}
