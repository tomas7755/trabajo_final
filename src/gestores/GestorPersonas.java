package gestores;

import excepciones.PersonaDuplicadaException;
import excepciones.PersonaNoEncontradaException;
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

    //CONSULTA: busca una persona por su dni.
    public Persona buscarPorDni(String dni) throws PersonaNoEncontradaException {
        Persona buscada = personas.get(dni);
        if (buscada == null) {
            throw new PersonaNoEncontradaException("No existe una persona con el DNI " + dni + ".");
        }
        return buscada;
    }
}