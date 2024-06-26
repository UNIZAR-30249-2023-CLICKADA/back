package com.clickada.back.requisitos;

import com.clickada.back.application.DominioService;
import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.application.ReservaService;
import com.clickada.back.domain.EdificioRepository;
import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.*;
import com.clickada.back.domain.entity.Persona;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    @Autowired
    DominioService dominioService;
    @InjectMocks
    ReservaService reservaService;
    @Mock
    EdificioRepository edificioRepository;

    @Test
    void requisito4() throws Exception {
        Persona persona = new Persona("Pepe","pepe@gmail.com","123", Rol.CONSERJE,null);

        assertEquals("Pepe",persona.getNombre());
        assertEquals("pepe@gmail.com",persona.getEMail());
        assertEquals(Rol.CONSERJE,persona.rolPrincipal());
        assertNull(persona.getDepartamento());

    }

    @Test
    public void requisito5() throws Exception {
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
        Persona resultado = personaService.cambiarRol(per.getIdPersona(),"Docente","Ingenieria");
        assertTrue(per.equals(resultado));
        resultado = personaService.cambiarRol(per.getIdPersona(),"Tecnico","Informatica");
        assertTrue(per.equals(resultado));
        resultado = personaService.cambiarRol(per.getIdPersona(),"Gerente",null);
        assertTrue(per.equals(resultado));

    }

    @Test
    void requisito6_7_8() throws Exception {
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
    void requisito9() throws Exception {
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
    void requisito10_11() throws Exception {
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
    public void requisito12() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        Espacio sala_comun = new Espacio(new Reservabilidad(false,CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio aula = new Espacio(new Reservabilidad(false,CategoriaReserva.AULA),150, 60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio seminario = new Espacio(new Reservabilidad(false,CategoriaReserva.SEMINARIO),150, 60,
                CategoriaEspacio.SEMINARIO,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Persona docente = new Persona("Ger","ger@clickada.es","1234",Rol.DOCENTE_INVESTIGADOR,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Reservabilidad reservabilidad1 = new Reservabilidad(false, CategoriaReserva.LABORATORIO);
        Reservabilidad reservabilidad2 = new Reservabilidad(true,CategoriaReserva.AULA);
        Reservabilidad reserSALA = new Reservabilidad(true,CategoriaReserva.SALA_COMUN);
        Reservabilidad reservabilidad3 = new Reservabilidad(false,CategoriaReserva.DESPACHO);
        Reservabilidad reserSeminario =  new Reservabilidad(false,CategoriaReserva.SEMINARIO);
        //sala comun error
        Exception thrown = assertThrows(Exception.class,()-> {
            sala_comun.modificarReservabilidad(gerente,reservabilidad1);
        });
        assertEquals("Las salas comunes solo pueden ser SEMINARIO o DESPACHO",thrown.getMessage());
        //sala_comun bien
        sala_comun.modificarReservabilidad(gerente,reservabilidad3);
        assertEquals(false,sala_comun.getReservabilidad().reservable);
        assertEquals(reservabilidad3.categoriaReserva,sala_comun.getReservabilidad().categoriaReserva);
        // aula error (distinto de seminario)
        thrown = assertThrows(Exception.class,()-> {
            aula.modificarReservabilidad(gerente,reservabilidad1);
        });
        assertEquals("Las aulas solo puede ser SEMINARIO",thrown.getMessage());
        //aula bien
        aula.modificarReservabilidad(gerente,reserSeminario);
        assertEquals(false,aula.getReservabilidad().reservable);
        assertEquals(reserSeminario.categoriaReserva,aula.getReservabilidad().categoriaReserva);

        //SEMINARIO ERROR (distinto a aula o sala comun)
        thrown = assertThrows(Exception.class,()-> {
            seminario.modificarReservabilidad(gerente,reservabilidad1);
        });
        assertEquals("Los seminarios solo pueden ser aulas o salas comunes",thrown.getMessage());
        //aula bien
        seminario.modificarReservabilidad(gerente,reservabilidad2);
        assertEquals(true,seminario.getReservabilidad().reservable);
        assertEquals(reservabilidad2.categoriaReserva,seminario.getReservabilidad().categoriaReserva);

        seminario.modificarReservabilidad(gerente,reserSALA);
        assertEquals(true,seminario.getReservabilidad().reservable);
        assertEquals(reserSALA.categoriaReserva,seminario.getReservabilidad().categoriaReserva);

        assertEquals(edificio.getHoraInicio(),sala_comun.getHoraInicio());
        sala_comun.modificarHorarioDisponible(gerente,
                LocalTime.of(9,0),
                LocalTime.of(19,0));
        assertNotEquals(edificio.getHoraFin(),sala_comun.getHoraFin());

        thrown = assertThrows(Exception.class,()-> {
            sala_comun.modificarHorarioDisponible(gerente,
                    LocalTime.of(7,0),
                    LocalTime.of(21,0));
        });
        assertEquals("Las horas nuevas de reserva tienen que estar dentro del periodo de reseerva del Edificio",thrown.getMessage());

        thrown = assertThrows(Exception.class,()-> {
            sala_comun.modificarHorarioDisponible(docente,
                    LocalTime.of(7,0),
                    LocalTime.of(21,0));
        });
        assertEquals("Si no es GERENTE no puede Modificar el horario de reserva del Espacio",thrown.getMessage());
        when(personaRepository.existsById(any())).thenReturn(true);
        when(espacioRepository.existsById(any())).thenReturn(true);
        when(personaRepository.getById(any())).thenReturn(gerente);
        when(espacioRepository.getById(any())).thenReturn(sala_comun);
        assertDoesNotThrow(()->espacioService.cambiarReservabilidadEspacio(sala_comun.getIdEspacio(),reserSeminario,gerente));


        assertDoesNotThrow(()->espacioService.cambiarReservabilidadEspacio(sala_comun.getIdEspacio(),reservabilidad3,gerente));

        when(personaRepository.getById(any())).thenReturn(docente);
        thrown = assertThrows(Exception.class,()->{
            espacioService.cambiarReservabilidadEspacio(sala_comun.getIdEspacio(),reservabilidad2,docente);
        });
        assertEquals("Si no es GERENTE no puede Modificar la Reservabilidad del Espacio",thrown.getMessage());

    }

    @Test
    void requisito13() throws Exception{
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);

        Espacio sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));

        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE,null);
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(sala_comun.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(9,0),LocalTime.of(10,0)),estudiante.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now());
        idEspacios = new ArrayList<>(List.of(laboratorio.getIdEspacio()));
        Reserva reserva_laboratorio = new Reserva(new PeriodoReserva(LocalTime.of(9,0),LocalTime.of(10,0)),estudiante.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now());

        when(personaRepository.getById(any())).thenReturn(estudiante);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(sala_comun);
        when(edificioRepository.findAll()).thenReturn(List.of(edificio));
        assertDoesNotThrow(()->espacioService.reservarEspacio(estudiante,new ArrayList<>(),reserva));

        //si intentamos reservar donde ya hay una reserva en el mismo horario dara error
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>(List.of(reserva)));
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(estudiante,new ArrayList<>(List.of(reserva)),reserva);
        });
        assertEquals("Ya existe una reserva en el horario introducido",thrown.getMessage());

        //Si el estudiante intenta reservar un laboratorio dara error

        when(espacioRepository.getById(any())).thenReturn(laboratorio);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(estudiante,new ArrayList<>(List.of()),reserva_laboratorio);
        });
        assertEquals("Un estudiante solo puede reservar SALAS COMUNES",thrown.getMessage());

        laboratorio.modificarReservabilidad(gerente,new Reservabilidad(true,CategoriaReserva.SALA_COMUN));
        //cambiamos la reservabilidad del laboratiorio poniendolo como sala comun -> nos tendria que dejar
        assertDoesNotThrow(()->
                espacioService.reservarEspacio(estudiante,new ArrayList<>(List.of()),reserva_laboratorio));
    }

    @Test
    void requisito14() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        Espacio sala_comun = new Espacio(new Reservabilidad(),150,60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        assertEquals(CategoriaEspacio.SALA_COMUN,sala_comun.getCategoriaEspacio());
    }
    @Test
    void requisito15() throws Exception {

        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Espacio sala_comun = new Espacio(new Reservabilidad(true,CategoriaReserva.SALA_COMUN),150,60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        assertEquals(CategoriaReserva.SALA_COMUN,sala_comun.getReservabilidad().categoriaReserva);
        sala_comun.modificarReservabilidad(gerente,new Reservabilidad(true,CategoriaReserva.SEMINARIO));
        assertEquals(CategoriaReserva.SEMINARIO,sala_comun.getReservabilidad().categoriaReserva);
        assertEquals(CategoriaEspacio.SALA_COMUN,sala_comun.getCategoriaEspacio());
    }
    //Test 14 y 15 son de porcentaje de uso maximo
    @Test
    void requisito16() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);

        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE,null);
        Persona tecnico_laboratorio = new Persona("Ger","ger@clickada.es","1234",Rol.TECNICO_LABORATORIO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Persona investigador = new Persona("Ger","ger@clickada.es","1234",Rol.INVESTIGADOR_CONTRATADO,Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        Persona docente = new Persona("Ger","ger@clickada.es","1234",Rol.DOCENTE_INVESTIGADOR,Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        Persona conserje = new Persona("Ger","ger@clickada.es","1234",Rol.CONSERJE,null);


        Exception thrown = assertThrows(Exception.class,()->{
            Espacio aula = new Espacio(new Reservabilidad(true, CategoriaReserva.AULA),150, 60,
                    CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS));
        });
        assertEquals("AULA no puede tener a un departamento como propietario de espacio",thrown.getMessage());

        Espacio aula = new Espacio(new Reservabilidad(true, CategoriaReserva.AULA),150, 60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        assertEquals(Eina.EINA,aula.getPropietarioEspacio().eina);

        thrown = assertThrows(Exception.class,()->{
            Espacio sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                    CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS));
        });
        assertEquals("SALA_COMUN no puede tener a un departamento como propietario de espacio",thrown.getMessage());

        Espacio sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        assertEquals(Eina.EINA,sala_comun.getPropietarioEspacio().eina);

        thrown = assertThrows(Exception.class,()->{
            Espacio despacho = new Espacio(new Reservabilidad(false, CategoriaReserva.DESPACHO),150, 60,
                    CategoriaEspacio.DESPACHO,edificio,new PropietarioEspacio(Eina.EINA));
        });
        assertEquals("DESPACHO no puede tener a la Eina como propietario de espacio",thrown.getMessage());

        Espacio despacho = new Espacio(new Reservabilidad(false, CategoriaReserva.DESPACHO),150, 60,
                CategoriaEspacio.DESPACHO,edificio,new PropietarioEspacio(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES));
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,despacho.getPropietarioEspacio().departamento);

        thrown = assertThrows(Exception.class,()->{
            new Espacio(new Reservabilidad(false, CategoriaReserva.DESPACHO),150, 60,
                    CategoriaEspacio.DESPACHO,edificio,new PropietarioEspacio(List.of(gerente)));
        });
        assertEquals("Para ser propietario de espacio se tiene que ser DOCENTE_INVESTIGADOR o INVESTIGADOR_CONTRATADO",thrown.getMessage());

        gerente.anyadirRol(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        despacho = new Espacio(new Reservabilidad(false, CategoriaReserva.DESPACHO),150, 60,
                CategoriaEspacio.DESPACHO,edificio,new PropietarioEspacio(List.of(gerente)));
        assertTrue(despacho.getPropietarioEspacio().esPersonas());

        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio seminario = new Espacio(new Reservabilidad(true, CategoriaReserva.SEMINARIO),150, 60,
                CategoriaEspacio.SEMINARIO,edificio,new PropietarioEspacio(Eina.EINA));

        assertTrue(laboratorio.getPropietarioEspacio().esEina());
        assertTrue(seminario.getPropietarioEspacio().esEina());

        laboratorio.asignarAEspacio(new PropietarioEspacio(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS));
        seminario.asignarAEspacio(new PropietarioEspacio(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS));

        assertTrue(laboratorio.getPropietarioEspacio().esDepartamento());
        assertTrue(seminario.getPropietarioEspacio().esDepartamento());

        thrown = assertThrows(Exception.class,()->{
            laboratorio.asignarAEspacio(new PropietarioEspacio(List.of(gerente)));
        });
        assertEquals("Solo los despachos pueden tener como propietario a persona/as",thrown.getMessage());
        thrown = assertThrows(Exception.class,()->{
            seminario.asignarAEspacio(new PropietarioEspacio(List.of(gerente)));
        });
        assertEquals("Solo los despachos pueden tener como propietario a persona/as",thrown.getMessage());

    }
    @Test
    void requisito20_22() throws Exception{
        //yyyy-mm-dd
        //hh:mm
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio laboratorio2 = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),100, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio sala_comun = new Espacio(new Reservabilidad(true,CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(laboratorio.getIdEspacio(),laboratorio2.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(9,0),LocalTime.of(10,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,100,"DD",LocalDate.now());
        Reserva reserva_300 = new Reserva(new PeriodoReserva(LocalTime.of(19,0),LocalTime.of(20,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,300,"DD",LocalDate.now());

        when(edificioRepository.findAll()).thenReturn(List.of(edificio));
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(laboratorio).thenReturn(laboratorio2);

        assertDoesNotThrow(()->espacioService.reservarEspacio(gerente,new ArrayList<>(),reserva));

        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(gerente,new ArrayList<>(),reserva_300);
        });
        assertEquals("Se supera el numero máximo de asistentes de los espacios seleccionados siendo "+
                120 + " el total de asistentes permitidos y "+300
                +" el numero de asistentes de la reserva.",thrown.getMessage());

        idEspacios.add(sala_comun.getIdEspacio());
        when(espacioRepository.getById(any())).thenReturn(laboratorio).thenReturn(laboratorio2).thenReturn(sala_comun);
        when(espacioRepository.findAll()).thenReturn(List.of(laboratorio,laboratorio2,sala_comun));
        when(espacioRepository.findAllById(any())).thenReturn(List.of(laboratorio,laboratorio2,sala_comun));
        //Ahoras la reserva con menos parametros semi-automatica
        List<UUID> listaBusqueda = espacioService.buscarEspacios(gerente,new ArrayList<>(),3,
                LocalTime.of(9,0),LocalTime.of(10,0),100);
        listaBusqueda.forEach(idEspacio -> {assertEquals(idEspacio,idEspacios.get(listaBusqueda.indexOf(idEspacio)));});

        List<UUID> listaBusqueda2 = espacioService.buscarEspacios(gerente,new ArrayList<>(),2,
                LocalTime.of(9,0),LocalTime.of(10,0),100);
        assertEquals(2,listaBusqueda2.size());

        thrown = assertThrows(Exception.class,()-> {
            espacioService.buscarEspacios(gerente,new ArrayList<>(),2,
                    LocalTime.of(9,0),LocalTime.of(10,0),130);
        });
        assertEquals("La cantidad de personas para la reserva es demasiado grande para la cantidad de espacios que se proporcionan",thrown.getMessage());

    }
    @Test
    void requisito21() throws Exception{
        //Reservas puntuales y empiezan un dia y acaban el mismo dia
    }
    @Test
    void requisito23() throws Exception{
        //true reservar AULA perona no estuiante ni tecnico de laboratiorio
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        Espacio aula = new Espacio(new Reservabilidad(true, CategoriaReserva.AULA),150, 60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE,null);
        Persona tecnico_laboratorio = new Persona("Ger","ger@clickada.es","1234",Rol.TECNICO_LABORATORIO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);

        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(aula.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(9,0),LocalTime.of(10,0)),
                estudiante.getIdPersona(),TipoUso.DOCENCIA,idEspacios,
                20,"DD",LocalDate.now());
        when(personaRepository.getById(any())).thenReturn(gerente).thenReturn(estudiante).thenReturn(tecnico_laboratorio);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(aula);
        when(edificioRepository.findAll()).thenReturn(List.of(edificio));
        assertDoesNotThrow(()->espacioService.reservarEspacio(gerente,new ArrayList<>(),reserva));
        //false reservar AULA siendo un estudiante
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(estudiante,new ArrayList<>(),reserva);
        });
        assertEquals("Un estudiante solo puede reservar SALAS COMUNES",thrown.getMessage());

        // false reservar AULA siendo un tecnico de laboratorio
        thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(tecnico_laboratorio,new ArrayList<>(),reserva);
        });
        assertEquals("Un Tecnico de laboratorio no puede reservar aulas",thrown.getMessage());
    }

    @Test
    void requisito24() throws Exception{
        //true reservar LABORATORIOS cualqueira menos estudiantes
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE,null);
        Persona tecnico_laboratorio = new Persona("Ger","ger@clickada.es","1234",Rol.TECNICO_LABORATORIO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Persona investigador = new Persona("Ger","ger@clickada.es","1234",Rol.INVESTIGADOR_CONTRATADO,Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        Persona docente = new Persona("Ger","ger@clickada.es","1234",Rol.DOCENTE_INVESTIGADOR,Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        Persona conserje = new Persona("Ger","ger@clickada.es","1234",Rol.CONSERJE,null);

        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(laboratorio.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(9,0),LocalTime.of(10,0)),
                gerente.getIdPersona(),TipoUso.DOCENCIA,idEspacios,
                20,"DD",LocalDate.now());
        when(personaRepository.getById(any())).thenReturn(gerente)
                .thenReturn(conserje)
                .thenReturn(estudiante)
                .thenReturn(docente)
                .thenReturn(investigador)
                .thenReturn(tecnico_laboratorio);
        when(personaService.getPersonaById(any())).thenReturn(gerente)
                .thenReturn(conserje)
                .thenReturn(estudiante)
                .thenReturn(docente)
                .thenReturn(investigador)
                .thenReturn(tecnico_laboratorio);
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(laboratorio);
        when(edificioRepository.findAll()).thenReturn(List.of(edificio));
        assertDoesNotThrow(()->espacioService.reservarEspacio(gerente,new ArrayList<>(),reserva));
        assertDoesNotThrow(()->espacioService.reservarEspacio(conserje,new ArrayList<>(),reserva));
        //false reservar LABORATORIO siendo un estudiante
        Exception thrown = assertThrows(Exception.class,()-> {
                    espacioService.reservarEspacio(estudiante,new ArrayList<>(),reserva);
        });
        assertEquals("Un estudiante solo puede reservar SALAS COMUNES",thrown.getMessage());

        // true reservar LABORATORIO siendo tecnico, investigador,docente solo pueden reservar
        //laboratiorios de su mismo departamento

        thrown =  assertThrows(Exception.class,()->{
            espacioService.reservarEspacio(docente,new ArrayList<>(),reserva);
        });

        assertEquals("Los tecnicos de laboratorio, investigador contratado y docente investigador solo pueden reservar " +
                "laboratorios que esten adscritos a un departamento",thrown.getMessage());

        laboratorio.asignarAEspacio(new PropietarioEspacio(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS));
        thrown = assertThrows(Exception.class,()->{
            espacioService.reservarEspacio(investigador,new ArrayList<>(),reserva);
        });
        assertEquals("Los tecnicos de laboratorio, investigador contratado y docente investigador solo pueden reservar " +
                "laboratorios de su mismo departamento",thrown.getMessage());

        tecnico_laboratorio.cambiarDepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertDoesNotThrow(()->espacioService.reservarEspacio(tecnico_laboratorio,new ArrayList<>(),reserva));
    }
    @Test
    void requisito25() throws Exception{
        // despachos no pueden ser reservables
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        Exception thrown = assertThrows(Exception.class,()->{new Espacio(new Reservabilidad(true, CategoriaReserva.DESPACHO), 150, 60,
                CategoriaEspacio.DESPACHO, edificio, new PropietarioEspacio(Eina.EINA));
        });
        assertEquals("Los despachos no pueden ser reservables",thrown.getMessage());
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Persona docente = new Persona("Ger","ger@clickada.es","1234",Rol.DOCENTE_INVESTIGADOR,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Espacio despacho = new Espacio(new Reservabilidad(false, CategoriaReserva.DESPACHO),150, 60,
                CategoriaEspacio.DESPACHO,edificio,new PropietarioEspacio(List.of(docente)));

        thrown = assertThrows(Exception.class,()->{
            despacho.modificarReservabilidad(gerente,new Reservabilidad(true,CategoriaReserva.DESPACHO));
        });
        assertEquals("Los despachos no pueden ser reservables",thrown.getMessage());

        assertFalse(despacho.getReservabilidad().reservable);
    }
    @Test
    void requisito26() throws Exception{
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        Espacio laboratorio = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),150, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio laboratorio2 = new Espacio(new Reservabilidad(true, CategoriaReserva.LABORATORIO),100, 60,
                CategoriaEspacio.LABORATORIO,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio sala_comun = new Espacio(new Reservabilidad(true,CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(laboratorio.getIdEspacio(),laboratorio2.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(9,0),LocalTime.of(10,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,100,"DD",LocalDate.now());
        Reserva reserva_excedida = new Reserva(new PeriodoReserva(LocalTime.of(9,0),LocalTime.of(10,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,300,"DD",LocalDate.now());

        when(edificioRepository.findAll()).thenReturn(List.of(edificio));
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>());
        when(espacioRepository.getById(any())).thenReturn(laboratorio).thenReturn(laboratorio2);

        assertDoesNotThrow(()->espacioService.reservarEspacio(gerente,new ArrayList<>(),reserva));
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(gerente,new ArrayList<>(),reserva_excedida);
        });
        assertEquals("Se supera el numero máximo de asistentes de los espacios seleccionados siendo "+
                120 + " el total de asistentes permitidos y "+300
                +" el numero de asistentes de la reserva.",thrown.getMessage());

        idEspacios.add(sala_comun.getIdEspacio());
        when(espacioRepository.getById(any())).thenReturn(laboratorio).thenReturn(laboratorio2).thenReturn(sala_comun);
        when(espacioRepository.findAll()).thenReturn(List.of(laboratorio,laboratorio2,sala_comun));
        when(espacioRepository.findAllById(any())).thenReturn(List.of(laboratorio,laboratorio2,sala_comun));
        //Ahoras la reserva con menos parametros semi-automatica
        List<UUID> listaBusqueda = espacioService.buscarEspacios(gerente,new ArrayList<>(),3,
                LocalTime.of(9,0),LocalTime.of(10,0),100);
        listaBusqueda.forEach(idEspacio -> {assertEquals(idEspacio,idEspacios.get(listaBusqueda.indexOf(idEspacio)));});

        List<UUID> listaBusqueda2 = espacioService.buscarEspacios(gerente,new ArrayList<>(),2,
                LocalTime.of(9,0),LocalTime.of(10,0),100);
        assertEquals(2,listaBusqueda2.size());

        thrown = assertThrows(Exception.class,()-> {
            espacioService.buscarEspacios(gerente,new ArrayList<>(),2,
                    LocalTime.of(9,0),LocalTime.of(10,0),130);
        });
        assertEquals("La cantidad de personas para la reserva es demasiado grande para la cantidad de espacios que se proporcionan",thrown.getMessage());

    }
    @Test
    void requisito27_29() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);

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
        when(edificioRepository.findAll()).thenReturn(List.of(edificio));

        assertDoesNotThrow(()->espacioService.reservarEspacio(estudiante,new ArrayList<>(),reserva));

        //si intentamos reservar donde ya hay una reserva en el mismo horario dara error
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>(List.of(reserva)));
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(estudiante,new ArrayList<>(List.of(reserva)),reserva);
        });
        assertEquals("Ya existe una reserva en el horario introducido",thrown.getMessage());
    }
    @Test
    void requisito28() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);

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
        when(edificioRepository.findAll()).thenReturn(List.of(edificio));

        assertDoesNotThrow(()->espacioService.reservarEspacio(gerente,new ArrayList<>(),reserva));

        //si intentamos reservar donde ya hay una reserva en el mismo horario dara error
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>(List.of(reserva)));
        ArrayList<UUID> finalIdEspacios = idEspacios;
        Exception thrown = assertThrows(Exception.class,()-> {
            espacioService.reservarEspacio(gerente,new ArrayList<>(List.of(reserva)),reserva);
        });
        assertEquals("Ya existe una reserva en el horario introducido",thrown.getMessage());
    }
    //RF-24 se cumple en los requisitos de reservas
    @Test
    void requisito30() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);

        Espacio sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));

        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE,null);
        Persona estudiante = new Persona("Ger","ger@clickada.es","1234",Rol.ESTUDIANTE,null);

        ArrayList<UUID> idEspacios = new ArrayList<>(List.of(sala_comun.getIdEspacio()));
        Reserva reserva = new Reserva(new PeriodoReserva(LocalTime.of(8,0),LocalTime.of(10,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now().plusDays(1));
        Reserva reserva2 = new Reserva(new PeriodoReserva(LocalTime.of(18,0),LocalTime.of(19,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now().plusDays(1));
        Reserva reserva3 = new Reserva(new PeriodoReserva(LocalTime.of(17,0),LocalTime.of(19,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.now().minusDays(1));
        Reserva reserva4 = new Reserva(new PeriodoReserva(LocalTime.of(8,0),LocalTime.of(10,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.of(2023,4,2));
        Reserva reserva5 = new Reserva(new PeriodoReserva(LocalTime.of(18,0),LocalTime.of(19,0)),gerente.getIdPersona(),
                TipoUso.DOCENCIA, idEspacios,20,"DD",LocalDate.of(2023,4,2));
        when(personaRepository.existsById(any())).thenReturn(true);
        when(personaRepository.getById(any())).thenReturn(estudiante).thenReturn(gerente);
        when(reservaRepository.findAll()).thenReturn(new ArrayList<>(List.of(reserva,reserva4,reserva2,reserva5,reserva3)));
        when(reservaRepository.findByFecha(any())).thenReturn(new ArrayList<>(List.of()));
        Exception thrown = assertThrows(Exception.class,()->{
            reservaService.obtenerReservasVivas(estudiante.getIdPersona());
        });
        assertEquals("Necesitas rol de gerente para obtener reservas vivas",thrown.getMessage());

        List<Reserva> listaReservas = reservaService.obtenerReservasVivas(gerente.getIdPersona());
        assertNotNull(listaReservas);
        assertEquals(2,listaReservas.size());

        when(personaRepository.existsById(any())).thenReturn(false);

        thrown = assertThrows(Exception.class,()->{
            reservaService.obtenerReservasVivas(estudiante.getIdPersona());
        });
        assertEquals("Esa persona no existe",thrown.getMessage());

    }

}
