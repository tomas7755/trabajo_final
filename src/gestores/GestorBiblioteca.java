package gestores;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Reserva;
import modelo.personas.Usuario;
import util.LibroAuxiliar;
import excepciones.*;
import enums.Disponibilidad;
import java.util.*;

/**
 * Clase gestora del sistema: guarda en memoria los usuarios, libros, prestamos y reservas
 * y contiene la logica para darlos de alta, buscarlos, listarlos, modificarlos y eliminarlos.
 * No es una persona: los datos del bibliotecario estan en modelo.personas.Bibliotecario.
 */
public class GestorBiblioteca {
    private int contadorPrestamos = 0;
    private int contadorReserva = 0;
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<Integer, Libro> libros = new HashMap<>();
    private Map<Integer, Prestamo> prestamos = new HashMap<>();
    private Map<Integer, Reserva> reservas = new HashMap<>();
    //constructores:
    public GestorBiblioteca(){}
    public GestorBiblioteca(Map<String, Usuario> usuarios, Map<Integer, Libro> libros, Map<Integer, Prestamo> prestamos, Map<Integer, Reserva> reservas) {
        this.usuarios = usuarios;
        this.libros = libros;
        this.prestamos = prestamos;
        this.reservas = reservas;
    }
    //getters:
    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }
    public Map<Integer, Libro> getLibros() {
        return libros;
    }
    public Map<Integer, Prestamo> getPrestamos() {
        return prestamos;
    }
    public Map<Integer, Reserva> getReservas() {
        return reservas;
    }
    //setters:
    public void setUsuarios(Map<String, Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    public void setLibros(Map<Integer, Libro> libros) {
        this.libros = libros;
    }
    public void setPrestamos(Map<Integer, Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
    public void setReservas(Map<Integer, Reserva> reservas) {
        this.reservas = reservas;
    }

    //metodos:
    //metodos para agregar cada uno de los objetos:
    public boolean agregarUsuario(Usuario agregado){
        if(usuarios.containsKey(agregado.getDni())){
            return false;
        }
        usuarios.put(agregado.getDni(), agregado);
        return true;
    }
    public boolean agregarLibro(Libro agregado){
        if (libros.containsKey(agregado.getNumeroIdentificador()))
        {
            return false;
        }
        libros.put(agregado.getNumeroIdentificador(), agregado);

        return true;
    }
    public boolean agregarPrestamo(String dni, int libroPrestamo)throws SinLibrosException, SinUsuarioException, LibroNoDisponible {
        //primero buscamos al usuario y el libro a prestar.
        Usuario usuarioBuscado = usuarios.get(dni);
        Libro libro =  libros.get(libroPrestamo);
        //si las casillas se dejan vaciaso no los encuentra se tira la excepcion.
        if (libro == null){
            throw new SinLibrosException("Este libro no existe.");
        }
        //si el libro fue dado de baja (baja logica), no se puede prestar ni reservar.
        if (!libro.isActivo()){
            throw new SinLibrosException("El libro fue dado de baja.");
        }
        if (usuarioBuscado == null){
            throw new SinUsuarioException("Este usuario no existe.");
        }
        //si el usuario fue dado de baja (baja logica), no puede pedir prestamos ni reservas.
        if (!usuarioBuscado.isActivo()){
            throw new SinUsuarioException("El usuario esta dado de baja.");
        }
        //si el libro ya está prestado, avisa de su desponibilidad.
        if (libro.getEstaDispponible() == Disponibilidad.PRESTADO){
            throw new LibroNoDisponible("El libro ya esta prestado.");
        }
        //si el libro ya está reservado, primero busca si la persona que tiene la reserva
        //es la misma que el nuevo usuario que quiere el prestamo ahora
        if (libro.getEstaDispponible() == Disponibilidad.RESERVADO){
            Reserva encontrada = null;
            for(Reserva reserva : reservas.values()){
                //primero busca el libro
                if (reserva.getLibro().equals(libro)){
                    //despues busca al usuario, si no es el mismo, tira la excepcion
                    if(!reserva.getUsuario().getDni().equals(dni)){
                        throw new LibroNoDisponible("El libro esta reservado por otro usuario.");
                    }
                    //se guarda la reserva para luego eliminarla.
                    encontrada = reserva;
                    break;
                }
            }
            //si la reserva encontrada esta vacia, se tira la excepcion.
            if (encontrada == null){
                throw new LibroNoDisponible("Reserva del libro no encontrada.");
            }
            //si si es la misma persona, y la reserva esta cargada, elimina el libro de los reservados
            reservas.remove(encontrada.getNumeroReserva());
        }
        //y se agrega a la desponibilidad de prestado.
        libro.setEstaDispponible(Disponibilidad.PRESTADO);
        //se asigna su numero de prestamo
        contadorPrestamos++;

        Prestamo prestado = new Prestamo(contadorPrestamos, usuarioBuscado, libro);
        //se carga el nuevo prestamo
        prestamos.put(contadorPrestamos, prestado);

        return true;
    }
    public boolean agregarReservas(String dni, int libroReservado)throws SinLibrosException, SinUsuarioException, LibroNoDisponible{
        //primero buscamos al usuario y el libro a prestar.
        Usuario usuarioBuscado = usuarios.get(dni);
        Libro libro =  libros.get(libroReservado);
        //si las casillas se dejan vacias o no los encuentra se tira la excepcion.
        if (libro == null){
            throw new SinLibrosException("Este libro no existe.");
        }
        //si el libro fue dado de baja (baja logica), no se puede prestar ni reservar.
        if (!libro.isActivo()){
            throw new SinLibrosException("El libro fue dado de baja.");
        }
        if (usuarioBuscado == null){
            throw new SinUsuarioException("Este usuario no existe.");
        }
        //si el usuario fue dado de baja (baja logica), no puede pedir prestamos ni reservas.
        if (!usuarioBuscado.isActivo()){
            throw new SinUsuarioException("El usuario esta dado de baja.");
        }
        //si el libro está disponible, no hace la reserva, ya que se puede hacer el prestamo,
        //y tira la excepcion
        if (libro.getEstaDispponible() == Disponibilidad.DISPONIBLE){
            throw new LibroNoDisponible("El libro esta disponible, puede pedir el prestamo.");
        }
        //si el libro ya está reservado, no permite reservarlo a otra persona.
        if (libro.getEstaDispponible() == Disponibilidad.RESERVADO){
            throw new LibroNoDisponible("El libro ya esta reservado, no se puede reservar.");
        }
        //cambia el estado a reservado
        libro.setEstaDispponible(Disponibilidad.RESERVADO);
        //se asigna el numero de reserva
        contadorReserva++;

        Reserva reservado = new Reserva(contadorReserva, usuarioBuscado, libro);
        //se carga la nueva reserva
        reservas.put(contadorReserva, reservado);

        return true;
    }
    //metodos para buscar a cada uno:
    public Usuario buscarUsuario(String dni){
        Usuario usuarioBuscado = null;
        if (usuarios.containsKey(dni)){
            usuarioBuscado = usuarios.get(dni);
        }
        return usuarioBuscado;
    }
    public Usuario buscarUsuarioPorNombre(String nombre){
        for (Usuario buscado : usuarios.values()){
            if (buscado.getNombre().equalsIgnoreCase(nombre)){
                return buscado;
            }
        }
        return null;
    }
    public Libro buscarLibro(int numeroBuscado){
        Libro libroBuscado = null;
        if (libros.containsKey(numeroBuscado)){
            libroBuscado= libros.get(numeroBuscado);
        }
        return libroBuscado;
    }
    public Libro buscarLibroPorNombre(String nombre){
        for (Libro buscado : libros.values()){
            //solo se buscan los libros activos (los dados de baja no se muestran).
            if(buscado.isActivo() && buscado.getNombre().equals(nombre)){
                return buscado;
            }
        }
        return null;
    }
    public Libro buscarLibroPorAutor(String autor){
        for (Libro buscado : libros.values()){
            //solo se buscan los libros activos (los dados de baja no se muestran).
            if (buscado.isActivo() && buscado.getAutor().equalsIgnoreCase(autor)){
                return buscado;
            }
        }
        return null;
    }
    public Map<Integer, Libro> buscarLibrosPorUsuario(String dni){
        Map<Integer, Libro> buscados = new TreeMap<>();
        //primero busco los libros que tiene reservados.
        for (Reserva reservado : reservas.values()){
            if (reservado.getUsuario().getDni().equals(dni)){
                Libro libroBuscado = reservado.getLibro();

                buscados.put(libroBuscado.getNumeroIdentificador(), libroBuscado);
            }
        }
        //despúes busco en los que tiene prestados.
        for (Prestamo prestados : prestamos.values()){
            if (prestados.getUsuario().getDni().equals(dni)){
                Libro prestamosBuscados = prestados.getLibro();

                buscados.put(prestamosBuscados.getNumeroIdentificador(), prestamosBuscados);
            }
        }
        //retorna todos los libros prestados y reservados que tenga el usuario.
        return buscados;
    }
    public Map<Integer, Libro> buscarPrestamosPorUsuario(String dni){
        Map<Integer, Libro> buscados = new TreeMap<>();
        //busco los libros que tiene prestados.
        for (Prestamo prestados : prestamos.values()){
            if (prestados.getUsuario().getDni().equals(dni)){
                Libro prestamosBuscados = prestados.getLibro();

                buscados.put(prestamosBuscados.getNumeroIdentificador(), prestamosBuscados);
            }
        }
        //retorna todos los encontrados.
        return buscados;
    }
    public Map<Integer, Libro> buscarReservasPorUsuario(String dni){
        Map<Integer, Libro> buscados = new TreeMap<>();
        //busco los libros que tiene reservados.
        for (Reserva reservado : reservas.values()){
            if (reservado.getUsuario().getDni().equals(dni)){
                Libro libroBuscado = reservado.getLibro();

                buscados.put(libroBuscado.getNumeroIdentificador(), libroBuscado);
            }
        }
        //retorno todos los libros encontrados.
        return buscados;
    }
    public Usuario buscarUsuarioPorLibro (int numeroIdentificador){
        //recorro las reservas para buscarlo
        for (Reserva reservado : reservas.values()){
            if (reservado.getLibro().getNumeroIdentificador() == numeroIdentificador){
                //si lo encuentra, lo devuelve y corta
                return reservado.getUsuario();
            }
        }
        //recorro los prestamos para buscarlo
        for (Prestamo prestados : prestamos.values()){
            if (prestados.getLibro().getNumeroIdentificador() == numeroIdentificador){
                //si lo encuentra, lo devuelve y corta
                return prestados.getUsuario();
            }
        }
        //si no encuentra nada, devuelve un vacio
        return null;
    }
    public Prestamo buscarPrestamo(int numeroIdentificador){
        for (Prestamo buscado : prestamos.values()){
            if (buscado.getNumeroPrestamo() == numeroIdentificador){
                return buscado;
            }
        }
        return null;
    }
    public Reserva buscarReserva(int numeroIdentificador){
        for (Reserva buscado : reservas.values()){
            if (buscado.getNumeroReserva() == numeroIdentificador){
                return buscado;
            }
        }
        return null;
    }
    //metodos para listar:
    public List<LibroAuxiliar> listarUsuarioconLibros(){
        //hacemos un arraylist de una clase auxiliar para guardar todos los datos(usuarios, libros y disponibilidad)
        //en el mismo lugar (para ayudar al front)
        List<LibroAuxiliar> todo = new ArrayList<>();
        //guardamos a todos los usuarios que reservaron
        for (Reserva reservados : reservas.values()){
            todo.add(new LibroAuxiliar(reservados.getUsuario(), reservados.getLibro(), Disponibilidad.RESERVADO));
        }
        //guardamos a todos los usuarios que tienen prestamos.
        for (Prestamo prestados : prestamos.values()){
            todo.add(new LibroAuxiliar(prestados.getUsuario(), prestados.getLibro(), Disponibilidad.PRESTADO));
        }
        return todo;
    }
    public List<Libro> listarTodosLosLibros(){
        List <Libro> todos = new ArrayList<>();
        for (Libro libro : libros.values()){
            //solo se listan los libros activos (los dados de baja no se muestran).
            if (libro.isActivo()){
                todos.add(libro);
            }
        }
        return todos;
    }
    public List<Libro> listarLibrosDisponibles(){
        //se crea un arraylist para poder guardar los datos
        List<Libro> buscados = new ArrayList<>();
        //se busca en los libros cuales son los disponibles.
        for (Libro libro : libros.values())
        {
            //si el libro esta activo y disponible, se guarda en el arraylist
            if (libro.isActivo() && libro.getEstaDispponible().equals(Disponibilidad.DISPONIBLE)){
                buscados.add(libro);
            }
        }
        //retornamos los encontrados.
        return buscados;
    }
    //metodos para modificar:
    public boolean modificarDatosUsuario(String dni, Usuario nuevo)throws ErrorBuscandoUsuario {
        //busca al usuario, si no existe devuelve una excepcion.
        if (!usuarios.containsKey(dni)){
            throw new ErrorBuscandoUsuario("El usuario no existe.");
        }
        //comprueba si el nuevo usuario no este vacio y que coincida la key(dni), si no,
        //tira la excepcion.
        if(nuevo == null || !nuevo.getDni().equals(dni)){
            throw new ErrorBuscandoUsuario("Los datos del usuario son invalidos.");
        }
        //si todo esta correcto, lo sobreescribe..
        usuarios.put(dni, nuevo);
        return true;
    }
    public boolean modificarLibro(int numeroIdentificador, Libro nuevo) throws ErrorBuscandoLibro {
        //busca el libro, si no existe devuelve una excepcion.
        if (!libros.containsKey(numeroIdentificador)){
            throw new ErrorBuscandoLibro("El libro no existe.");
        }
        //comprueba si el nuevo libro no este vacio y que coincida la key(numero identificador),
        //si no, tira la excepcion.
        if (nuevo == null || nuevo.getNumeroIdentificador() != numeroIdentificador){
            throw new ErrorBuscandoLibro("Los datos del libro son erroneos.");
        }
        //si todo esta correcto, lo sobreescribe..
        libros.put(numeroIdentificador, nuevo);

        return true;
    }
    public boolean modificarReserva(int numeroReserva, Reserva nueva) throws ErrorBuscandoReserva {
        //busca la reserva, si no existe devuelve una excepcion.
        if (!reservas.containsKey(numeroReserva)){
            throw new ErrorBuscandoReserva("No existe una reserva asociada a ese numero.");
        }
        //comprueba que la nueva reserva no este vacia y que coincida la key(numero reserva),
        //si no, tira la excepcion.
        if (nueva == null || nueva.getNumeroReserva() != numeroReserva){
            throw new ErrorBuscandoReserva("Los datos de la reserva son erroneos.");
        }
        //si todo esta correcto, lo sobreescribe..
        reservas.put(numeroReserva, nueva);

        return true;
    }
    public boolean modificarPrestamo(int numeroPrestamo, Prestamo nuevo) throws ErrorBuscandoPrestamo {
        //busca el prestamo, si no existe devuelve una excepcion.
        if(!prestamos.containsKey(numeroPrestamo)){
            throw new ErrorBuscandoPrestamo("No existe un prestamo ligado a ese numero.");
        }
        //comprueba que el nuevo prestamo no este vacio y que coincida la key(numero prestamo),
        //si no, tira la excepcion.
        if (nuevo == null || nuevo.getNumeroPrestamo() != numeroPrestamo){
            throw new ErrorBuscandoPrestamo("Los datos del prestamo son erroneos.");
        }
        //si todo esta correcto, lo sobreescribe..
        prestamos.put(numeroPrestamo, nuevo);

        return true;
    }
    //metodos para eliminar:
    public boolean eliminarUsuario(String dni) throws ErrorBuscandoUsuario{
        //comprueba que el usuario exista, si no existe tira la exceppcion.
        if (!usuarios.containsKey(dni)){
            throw new ErrorBuscandoUsuario("El usuario que quiere eliminar no existe.");
        }
        //Busca que el usuario mo tenga ni reservas ni prestamos asociados.
        for (Reserva buscado : reservas.values())
        {
            if (buscado.getUsuario().getDni().equalsIgnoreCase(dni)){
                throw new ErrorBuscandoUsuario("El usuario tiene una o varias reservas asignadas.");
            }
        }
        for (Prestamo buscado : prestamos.values())
        {
            if (buscado.getUsuario().getDni().equalsIgnoreCase(dni)){
                throw new ErrorBuscandoUsuario("El usuario tiene uno o varios prestamos asignados.");
            }
        }
        //si no tiene nada asociado, se da de baja (baja logica): no se borra del Map,
        //se marca como inactivo para no perder sus datos ni su historial.
        usuarios.get(dni).setActivo(false);

        return true;
    }
    public boolean eliminarLibro (int numeroIdentificador)throws ErrorBuscandoLibro{
        //primero revisa si el libro buscado existe, si no, tira la excepcion
        if (!libros.containsKey(numeroIdentificador)){
            throw new ErrorBuscandoLibro("El libro buscado no exite");
        }
        //luego revisa que el libro se encuentre disponible, si no, tira la excepcion.
        Libro buscado = libros.get(numeroIdentificador);
        if (!buscado.getEstaDispponible().equals(Disponibilidad.DISPONIBLE)){
            throw new ErrorBuscandoLibro("El libro se encuentra Reservado o en un Prestamo.");
        }
        //si todo esta correcto, se da de baja (baja logica): no se borra del Map,
        //se marca como inactivo para no perder sus datos ni su historial.
        buscado.setActivo(false);

        return true;
    }
    public boolean eliminarReserva(int numeroReserva)throws ErrorBuscandoReserva{
        //primero busca que exista la reserva, si no, tira la excepcion.
        if (!reservas.containsKey(numeroReserva)){
            throw new ErrorBuscandoReserva("La reserva que busca no existe.");
        }
        //se guarda el libro para hacer el cambio de disponibilidad.
        Libro libro = reservas.get(numeroReserva).getLibro();
        //si se encuentra, se elimina.
        reservas.remove(numeroReserva);
        //se cambia la disponibilidad del libro a "disponible".
        libro.setEstaDispponible(Disponibilidad.DISPONIBLE);

        return true;
    }
    public boolean eliminarPrestamo(int numeroPrestamo) throws ErrorBuscandoPrestamo{
        //primero se busca que exista el prestamo, si no, se tira la excepcion.
        if (!prestamos.containsKey(numeroPrestamo)){
            throw new ErrorBuscandoPrestamo("El prestamo buscado no existe.");
        }
        //se guarda el libro para cambiar la disponibilidad.
        Libro libro = prestamos.get(numeroPrestamo).getLibro();
        //se elimina el prestamo.
        prestamos.remove(numeroPrestamo);
        //se modifica la disponibilidad del libro a "disponible".
        libro.setEstaDispponible(Disponibilidad.DISPONIBLE);
        return true;
    }
}