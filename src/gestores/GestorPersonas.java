package gestores;

import excepciones.PersonaDuplicadaException;
import excepciones.PersonaNoEncontradaException;
import modelo.personas.Administrador;
import modelo.personas.Bibliotecario;
import modelo.personas.Persona;

import java.util.*;

/**
 * Clase gestora que usa el administrador para manejar al personal del sistema
 * (bibliotecarios y administradores).
 * Permite el ABMCL: Alta, Baja (logica), Modificacion, Consulta y Listado.
 */
public class GestorPersonas {
    //Map: guardamos a las personas con su dni como clave, asi la busqueda por dni es directa
    //(no hace falta recorrer toda la coleccion).
    //Es de tipo Persona, asi en el mismo Map entran bibliotecarios y administradores (polimorfismo).
    private Map<String, Persona> personas = new HashMap<>();
    //Set: guardamos los emails ya usados. Un Set no admite elementos repetidos,
    //y eso es justo lo que necesitamos: dos personas no pueden tener el mismo email (se usa para el login).
    private Set<String> emailsRegistrados = new HashSet<>();

    //constructor:
    public GestorPersonas() {}

    //metodos:
    //ALTA: agrega una persona nueva al sistema.
    public void agregarPersona(Persona nueva) throws PersonaDuplicadaException {
        //si ya existe alguien con ese dni, tira la excepcion.
        if (personas.containsKey(nueva.getDni())) {
            throw new PersonaDuplicadaException("Ya existe una persona con el DNI " + nueva.getDni() + ".");
        }
        //si el email ya esta usado, tira la excepcion.
        if (emailsRegistrados.contains(nueva.getEmail().toLowerCase())) {
            throw new PersonaDuplicadaException("El email " + nueva.getEmail() + " ya esta registrado.");
        }
        //si esta todo bien, la guarda en el Map y su email en el Set.
        personas.put(nueva.getDni(), nueva);
        emailsRegistrados.add(nueva.getEmail().toLowerCase());
    }

    //BAJA LOGICA: no se borra a la persona, se la marca como inactiva.
    //Asi no se pierden sus datos ni su historial.
    public void darDeBaja(String dni) throws PersonaNoEncontradaException {
        Persona buscada = buscarPorDni(dni);
        buscada.setActivo(false);
    }

    //vuelve a activar a una persona que habia sido dada de baja.
    public void reactivar(String dni) throws PersonaNoEncontradaException {
        Persona buscada = buscarPorDni(dni);
        buscada.setActivo(true);
    }

    //MODIFICACION: reemplaza los datos de una persona por los nuevos.
    public void modificarPersona(String dni, Persona nueva) throws PersonaNoEncontradaException, PersonaDuplicadaException {
        //busca a la persona, si no existe tira la excepcion.
        Persona vieja = buscarPorDni(dni);
        //el dni no se puede cambiar, porque es la clave del Map.
        if (nueva == null || !nueva.getDni().equals(dni)) {
            throw new PersonaNoEncontradaException("Los datos nuevos no corresponden al DNI " + dni + ".");
        }
        String emailViejo = vieja.getEmail().toLowerCase();
        String emailNuevo = nueva.getEmail().toLowerCase();
        //si cambio el email, revisa que el nuevo no lo tenga otra persona.
        if (!emailNuevo.equals(emailViejo) && emailsRegistrados.contains(emailNuevo)) {
            throw new PersonaDuplicadaException("El email " + nueva.getEmail() + " ya esta registrado.");
        }
        //actualiza el Set de emails y reemplaza a la persona en el Map.
        emailsRegistrados.remove(emailViejo);
        emailsRegistrados.add(emailNuevo);
        //se mantiene el estado (activo/inactivo) que tenia.
        nueva.setActivo(vieja.isActivo());
        personas.put(dni, nueva);
    }

    //CONSULTA: busca una persona por su dni.
    public Persona buscarPorDni(String dni) throws PersonaNoEncontradaException {
        Persona buscada = personas.get(dni);
        if (buscada == null) {
            throw new PersonaNoEncontradaException("No existe una persona con el DNI " + dni + ".");
        }
        return buscada;
    }

    //CONSULTA: busca una persona por su email (lo vamos a usar en el login).
    public Persona buscarPorEmail(String email) throws PersonaNoEncontradaException {
        for (Persona persona : personas.values()) {
            if (persona.getEmail().equalsIgnoreCase(email)) {
                return persona;
            }
        }
        throw new PersonaNoEncontradaException("No existe una persona con el email " + email + ".");
    }

    //LISTADO: devuelve todas las personas (activas e inactivas).
    //List: porque es una lista ordenada que se recorre facil para mostrarla en pantalla.
    public List<Persona> listarTodas() {
        return new ArrayList<>(personas.values());
    }

    //LISTADO: devuelve solo las personas activas.
    public List<Persona> listarActivas() {
        List<Persona> activas = new ArrayList<>();
        for (Persona persona : personas.values()) {
            if (persona.isActivo()) {
                activas.add(persona);
            }
        }
        return activas;
    }

    //LISTADO: devuelve solo los bibliotecarios.
    //instanceof pregunta si la persona es de la clase Bibliotecario.
    public List<Bibliotecario> listarBibliotecarios() {
        List<Bibliotecario> bibliotecarios = new ArrayList<>();
        for (Persona persona : personas.values()) {
            if (persona instanceof Bibliotecario) {
                bibliotecarios.add((Bibliotecario) persona);
            }
        }
        return bibliotecarios;
    }

    //LISTADO: devuelve solo los administradores.
    public List<Administrador> listarAdministradores() {
        List<Administrador> administradores = new ArrayList<>();
        for (Persona persona : personas.values()) {
            if (persona instanceof Administrador) {
                administradores.add((Administrador) persona);
            }
        }
        return administradores;
    }
}