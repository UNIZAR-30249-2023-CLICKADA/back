package com.clickada.back.requisitos;

import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
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

    PersonaService personaService;
    @Mock
    ReservaRepository reservaRepository;
    @Mock
    PersonaRepository personaRepository;
    @Mock
    EspacioRepository espacioRepository;
    @InjectMocks
    EspacioService espacioService;
    @Test
    void requisito2(){
        Persona persona = new Persona("Pepe","pepe@gmail.com","123", Rol.CONSERJE);

        Assert.state(persona.getEMail().equals("pepe@gmail.com"),"Conserje");
    }

    @Test
    public void requisito3(){
        Persona per = new Persona("Señor Tst","unico@mail.com", "123",
                Rol.getRolByString("Estudiante")) ;
        assertEquals(Rol.ESTUDIANTE,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Conserje"));
        assertEquals(Rol.CONSERJE,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Investigador"));
        assertEquals(Rol.INVESTIGADOR_CONTRATADO,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Docente"));
        assertEquals(Rol.DOCENTE_INVESTIGADOR,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Tecnico"));
        assertEquals(Rol.TECNICO_LABORATORIO,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Gerente"));
        assertEquals(Rol.GERENTE,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("RolErroneo"));
        //Debe mantener el anterior
        assertEquals(Rol.GERENTE,per.rolPrincipal());

    }

    @Test
    void requisito4_5_6() throws Exception {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";
        Rol rol_docente = Rol.DOCENTE_INVESTIGADOR;
        Rol rol_gerente = Rol.GERENTE;

        Persona persona = new Persona(nombre, eMail,"123",rol_docente);
        Exception thrown = assertThrows(Exception.class,()-> {
            persona.anyadirRol();
        });
        assertEquals("No es Gerente, no puede tener segundo Rol",thrown.getMessage());

        persona.setAdscripcion(new Adscripcion());
        persona.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);

        persona.cambiarRol(rol_gerente);
        Assertions.assertEquals(rol_gerente, persona.rolPrincipal());

        persona.anyadirRol();
        Assertions.assertEquals(rol_docente, persona.rolSecundario());

        persona.cambiarRol(Rol.ESTUDIANTE);
        Assertions.assertEquals(1,persona.getRoles().size());
        persona.cambiarRol(Rol.DOCENTE_INVESTIGADOR);
        persona.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        Assertions.assertEquals(1,persona.getRoles().size());

    }
    @Test
    void requisito7(){
        String nombre = "Juan";
        String eMail = "juan@clickada.com";

        Persona investigador = new Persona(nombre, eMail,"123",Rol.INVESTIGADOR_CONTRATADO);
        Persona docente = new Persona(nombre,eMail,"123",Rol.DOCENTE_INVESTIGADOR);
        Persona tecnico = new Persona(nombre,eMail,"123",Rol.TECNICO_LABORATORIO);

        investigador.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertEquals(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS,investigador.getAdscripcion().departamento);

        docente.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,docente.getAdscripcion().departamento);

        tecnico.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,tecnico.getAdscripcion().departamento);

    }
    @Test
    void requisito8_9() throws Exception {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";

        Persona estudiante = new Persona(nombre, eMail,"123",Rol.ESTUDIANTE);
        Persona conderje = new Persona(nombre,eMail,"123",Rol.CONSERJE);
        Persona gerente = new Persona(nombre,eMail,"123",Rol.GERENTE);

        estudiante.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertNull(estudiante.getAdscripcion());

        conderje.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertNull(conderje.getAdscripcion());

        gerente.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertNull(gerente.getAdscripcion());

        gerente.anyadirRol();
        gerente.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,gerente.getAdscripcion().departamento);

        gerente.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertEquals(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS,gerente.getAdscripcion().departamento);

    }
    @Test
    public void requisito10() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)),100);
        Espacio espacio = new Espacio(new Reservabilidad(),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);
        Persona docente = new Persona("Ger","ger@clickada.es","1234",Rol.DOCENTE_INVESTIGADOR);
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

        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE);
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);
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
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);
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
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);
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
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);
        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE);
        Persona tecnico_laboratorio = new Persona("Ger","ger@clickada.es","1234",Rol.TECNICO_LABORATORIO);

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
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);
        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE);
        Persona tecnico_laboratorio = new Persona("Ger","ger@clickada.es","1234",Rol.TECNICO_LABORATORIO);
        Persona investigador = new Persona("Ger","ger@clickada.es","1234",Rol.INVESTIGADOR_CONTRATADO);
        Persona docente = new Persona("Ger","ger@clickada.es","1234",Rol.DOCENTE_INVESTIGADOR);
        Persona conserje = new Persona("Ger","ger@clickada.es","1234",Rol.CONSERJE);

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
        investigador.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        thrown = assertThrows(Exception.class,()->{
            espacioService.reservarEspacio(investigador.getIdPersona(),idEspacios,
                    LocalDate.now(),LocalTime.of(9,0),LocalTime.of(10,0),TipoUso.DOCENCIA,
                    20,"DD");
        });
        assertEquals("Los tecnicos de laboratorio, investigador contratado y docente investigador solo pueden reservar " +
                "laboratorios de su mismo departamento",thrown.getMessage());

        tecnico_laboratorio.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
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
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);
        thrown = assertThrows(Exception.class,()->{
            despacho.modificarReservabilidad(gerente,new Reservabilidad(true,CategoriaReserva.DESPACHO));
        });
        assertEquals("Los despachos no pueden ser reservables",thrown.getMessage());

        assertFalse(despacho.getReservabilidad().reservable);
    }

}
