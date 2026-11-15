package gestores;
import Dao.LibroDao;
import Dao.PersonaDao;
import Dao.PrestamoDao;
import Dao.ReservaDao;
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
    private PersonaDao personaDAO = new PersonaDao();
    private LibroDao libroDAO = new LibroDao();
    private PrestamoDao prestamoDAO = new PrestamoDao();
    private ReservaDao reservaDAO = new ReservaDao();
    //constructores:
    public GestorBiblioteca(){
        for (Usuario usuariodao : personaDAO.listarUsuarios()) usuarios.put(usuariodao.getDni(), usuariodao);
        for (Libro librodao : libroDAO.listar()) libros.put(librodao.getNumeroIdentificador(), librodao);
        for (Prestamo prestamodao : prestamoDAO.listar(usuarios, libros)) {
            prestamos.put(prestamodao.getNumeroPrestamo(), prestamodao);
            //los contadores siguen desde el ultimo numero usado.
            contadorPrestamos = Math.max(contadorPrestamos, prestamodao.getNumeroPrestamo());
        }
        for (Reserva r : reservaDAO.listar(usuarios, libros)) {
            reservas.put(r.getNumeroReserva(), r);
            contadorReserva = Math.max(contadorReserva, r.getNumeroReserva());
        }
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
        personaDAO.insertar(agregado);
        return true;
    }
    public boolean agregarLibro(Libro agregado){
        if (libros.containsKey(agregado.getNumeroIdentificador()))
        {
            return false;
        }
        libros.put(agregado.getNumeroIdentificador(), agregado);
        libroDAO.insertar(agregado);

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
            reservaDAO.eliminar(encontrada.getNumeroReserva());
        }
        //y se agrega a la desponibilidad de prestado.
        libro.setEstaDispponible(Disponibilidad.PRESTADO);
        libroDAO.actualizar(libro);
        //se asigna su numero de prestamo
        contadorPrestamos++;

        Prestamo prestado = new Prestamo(contadorPrestamos, usuarioBuscado, libro);
        //se carga el nuevo prestamo
        prestamos.put(contadorPrestamos, prestado);
        prestamoDAO.insertar(prestado);

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
        //si el libro ya tiene una reserva (de cualquier usuario), no permite otra.
        if (buscarReservaDeLibro(libro) != null){
            throw new LibroNoDisponible("El libro ya esta reservado, no se puede reservar.");
        }
        //si el libro esta prestado al mismo usuario que quiere reservarlo, no tiene sentido reservarlo.
        Prestamo prestamoActual = buscarPrestamoDeLibro(libro);
        if (prestamoActual != null && prestamoActual.getUsuario().getDni().equals(dni)){
            throw new LibroNoDisponible("Ya tenes este libro prestado, no lo podes reservar.");
        }
        //el libro sigue PRESTADO mientras no se devuelva (la reserva queda guardada aparte).
        //cuando se devuelva, eliminarPrestamo lo pasa a RESERVADO.
        //se asigna el numero de reserva
        contadorReserva++;

        Reserva reservado = new Reserva(contadorReserva, usuarioBuscado, libro);
        //se carga la nueva reserva
        reservas.put(contadorReserva, reservado);
        reservaDAO.insertar(reservado);

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
    //devuelve la reserva que tiene un libro, o null si nadie lo reservo.
    public Reserva buscarReservaDeLibro(Libro libro){
        for (Reserva reserva : reservas.values()){
            if (reserva.getLibro().equals(libro)){
                return reserva;
            }
        }
        return null;
    }
    //devuelve el prestamo que tiene un libro, o null si no esta prestado.
    public Prestamo buscarPrestamoDeLibro(Libro libro){
        for (Prestamo prestamo : prestamos.values()){
            if (prestamo.getLibro().equals(libro)){
                return prestamo;
            }
        }
        return null;
    }
    //busca todos los libros activos cuyo titulo contenga el texto (sin importar mayusculas).
    public List<Libro> buscarLibrosPorTitulo(String texto){
        List<Libro> encontrados = new ArrayList<>();
        for (Libro libro : libros.values()){
            if (libro.isActivo() && libro.getNombre().toLowerCase().contains(texto.toLowerCase())){
                encontrados.add(libro);
            }
        }
        return encontrados;
    }
    //busca todos los libros activos cuyo autor contenga el texto (sin importar mayusculas).
    public List<Libro> buscarLibrosPorAutor(String texto){
        List<Libro> encontrados = new ArrayList<>();
        for (Libro libro : libros.values()){
            if (libro.isActivo() && libro.getAutor().toLowerCase().contains(texto.toLowerCase())){
                encontrados.add(libro);
            }
        }
        return encontrados;
    }
    //busca todos los libros activos cuyo genero contenga el texto (sin importar mayusculas).
    public List<Libro> buscarLibrosPorGenero(String texto){
        List<Libro> encontrados = new ArrayList<>();
        for (Libro libro : libros.values()){
            if (libro.isActivo() && libro.getGenero().toLowerCase().contains(texto.toLowerCase())){
                encontrados.add(libro);
            }
        }
        return encontrados;
    }
    //metodos para listar:
    //devuelve todos los prestamos activos (para que el bibliotecario los vea).
    public List<Prestamo> listarPrestamos(){
        return new ArrayList<>(prestamos.values());
    }
    //devuelve todas las reservas.
    public List<Reserva> listarReservas(){
        return new ArrayList<>(reservas.values());
    }
    //devuelve los prestamos de un usuario (con sus fechas).
    public List<Prestamo> listarPrestamosDeUsuario(String dni){
        List<Prestamo> encontrados = new ArrayList<>();
        for (Prestamo prestamo : prestamos.values()){
            if (prestamo.getUsuario().getDni().equals(dni)){
                encontrados.add(prestamo);
            }
        }
        return encontrados;
    }
    //devuelve las reservas de un usuario.
    public List<Reserva> listarReservasDeUsuario(String dni){
        List<Reserva> encontradas = new ArrayList<>();
        for (Reserva reserva : reservas.values()){
            if (reserva.getUsuario().getDni().equals(dni)){
                encontradas.add(reserva);
            }
        }
        return encontradas;
    }

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
        //se actualizan los datos del mismo objeto (no se reemplaza), porque los prestamos
        //y reservas apuntan a ese objeto. Asi tambien se mantiene su estado (activo/dado de baja).
        Usuario viejo = usuarios.get(dni);
        viejo.setNombre(nuevo.getNombre());
        viejo.setApellido(nuevo.getApellido());
        viejo.setEmail(nuevo.getEmail());
        viejo.setContrasenia(nuevo.getContrasenia());
        viejo.setDireccion(nuevo.getDireccion());
        viejo.setTelefono(nuevo.getTelefono());
        personaDAO.actualizar(viejo);
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
        //se actualizan los datos del mismo objeto (no se reemplaza), porque los prestamos
        //y reservas apuntan a ese objeto. Asi tambien se mantiene su estado y si esta activo.
        Libro viejo = libros.get(numeroIdentificador);
        viejo.setNombre(nuevo.getNombre());
        viejo.setAutor(nuevo.getAutor());
        viejo.setGenero(nuevo.getGenero());
        libroDAO.actualizar(viejo);

        return true;
    }
    public boolean modificarReserva(int numeroReserva, Reserva nueva) throws ErrorBuscandoReserva, SinUsuarioException, LibroNoDisponible {
        //busca la reserva, si no existe devuelve una excepcion.
        Reserva vieja = reservas.get(numeroReserva);
        if (vieja == null){
            throw new ErrorBuscandoReserva("No existe una reserva asociada a ese numero.");
        }
        //comprueba que los datos nuevos no esten vacios y que coincida la key(numero reserva).
        if (nueva == null || nueva.getNumeroReserva() != numeroReserva || nueva.getUsuario() == null){
            throw new ErrorBuscandoReserva("Los datos de la reserva son erroneos.");
        }
        //el usuario nuevo tiene que existir y estar activo.
        Usuario usuarioNuevo = usuarios.get(nueva.getUsuario().getDni());
        if (usuarioNuevo == null){
            throw new SinUsuarioException("Este usuario no existe.");
        }
        if (!usuarioNuevo.isActivo()){
            throw new SinUsuarioException("El usuario esta dado de baja.");
        }
        //si el libro esta prestado al usuario nuevo, no tiene sentido que lo reserve.
        Prestamo prestamoActual = buscarPrestamoDeLibro(vieja.getLibro());
        if (prestamoActual != null && prestamoActual.getUsuario().getDni().equals(usuarioNuevo.getDni())){
            throw new LibroNoDisponible("Ese usuario ya tiene este libro prestado, no lo puede reservar.");
        }
        //se actualiza el mismo objeto. El libro no se cambia: su estado depende de esta reserva.
        vieja.setUsuario(usuarioNuevo);
        reservaDAO.actualizar(vieja);

        return true;
    }
    public boolean modificarPrestamo(int numeroPrestamo, Prestamo nuevo) throws ErrorBuscandoPrestamo, SinUsuarioException {
        //busca el prestamo, si no existe devuelve una excepcion.
        Prestamo viejo = prestamos.get(numeroPrestamo);
        if (viejo == null){
            throw new ErrorBuscandoPrestamo("No existe un prestamo ligado a ese numero.");
        }
        //comprueba que los datos nuevos no esten vacios y que coincida la key(numero prestamo).
        if (nuevo == null || nuevo.getNumeroPrestamo() != numeroPrestamo || nuevo.getUsuario() == null || nuevo.getFechaPrestamo() == null || nuevo.getFechaDevolucion() == null){
            throw new ErrorBuscandoPrestamo("Los datos del prestamo son erroneos.");
        }
        //la fecha de devolucion no puede ser anterior a la del prestamo.
        if (nuevo.getFechaDevolucion().isBefore(nuevo.getFechaPrestamo())){
            throw new ErrorBuscandoPrestamo("La fecha de devolucion no puede ser anterior a la del prestamo.");
        }
        //el usuario nuevo tiene que existir y estar activo.
        Usuario usuarioNuevo = usuarios.get(nuevo.getUsuario().getDni());
        if (usuarioNuevo == null){
            throw new SinUsuarioException("Este usuario no existe.");
        }
        if (!usuarioNuevo.isActivo()){
            throw new SinUsuarioException("El usuario esta dado de baja.");
        }
        //se actualiza el mismo objeto. El libro no se cambia: su estado depende de este prestamo.
        viejo.setUsuario(usuarioNuevo);
        viejo.setFechaPrestamo(nuevo.getFechaPrestamo());
        viejo.setFechaDevolucion(nuevo.getFechaDevolucion());
        prestamoDAO.actualizar(viejo);

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
        personaDAO.actualizarActivo(dni, false);

        return true;
    }
    //vuelve a activar a un usuario que habia sido dado de baja.
    public boolean reactivarUsuario(String dni) throws ErrorBuscandoUsuario{
        //comprueba que el usuario exista, si no existe tira la excepcion.
        if (!usuarios.containsKey(dni)){
            throw new ErrorBuscandoUsuario("El usuario que quiere reactivar no existe.");
        }
        usuarios.get(dni).setActivo(true);
        personaDAO.actualizarActivo(dni, true);

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
        libroDAO.actualizar(buscado);

        return true;
    }
    //vuelve a activar un libro que habia sido dado de baja.
    public boolean reactivarLibro(int numeroIdentificador) throws ErrorBuscandoLibro{
        if (!libros.containsKey(numeroIdentificador)){
            throw new ErrorBuscandoLibro("El libro que quiere reactivar no existe.");
        }
        libros.get(numeroIdentificador).setActivo(true);
        libroDAO.actualizar(libros.get(numeroIdentificador));
        return true;
    }
    //DEVOLUCION por numero de libro: busca el prestamo de ese libro y lo elimina.
    //(es mas facil para el bibliotecario, que tiene el libro en la mano).
    public boolean devolverLibro(int numeroIdentificador) throws ErrorBuscandoPrestamo{
        Libro libro = libros.get(numeroIdentificador);
        Prestamo prestamo = (libro == null) ? null : buscarPrestamoDeLibro(libro);
        if (prestamo == null){
            throw new ErrorBuscandoPrestamo("Ese libro no esta prestado.");
        }
        return eliminarPrestamo(prestamo.getNumeroPrestamo());
    }
    //RENOVACION (modificacion del prestamo): extiende 15 dias la fecha de devolucion.
    //no se puede renovar si otro usuario esta esperando el libro.
    public boolean renovarPrestamo(int numeroIdentificador) throws ErrorBuscandoPrestamo, LibroNoDisponible{
        Libro libro = libros.get(numeroIdentificador);
        Prestamo prestamo = (libro == null) ? null : buscarPrestamoDeLibro(libro);
        if (prestamo == null){
            throw new ErrorBuscandoPrestamo("Ese libro no esta prestado.");
        }
        if (buscarReservaDeLibro(libro) != null){
            throw new LibroNoDisponible("No se puede renovar: otro usuario reservo el libro.");
        }
        prestamo.renovar(15);
        prestamoDAO.actualizarFechaDevolucion(prestamo);
        return true;
    }
    //cancela la reserva de un libro (buscandola por el numero del libro).
    public boolean cancelarReservaDeLibro(int numeroIdentificador) throws ErrorBuscandoReserva{
        Libro libro = libros.get(numeroIdentificador);
        Reserva reserva = (libro == null) ? null : buscarReservaDeLibro(libro);
        if (reserva == null){
            throw new ErrorBuscandoReserva("Ese libro no tiene ninguna reserva.");
        }
        return eliminarReserva(reserva.getNumeroReserva());
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
        reservaDAO.eliminar(numeroReserva);
        //si el libro estaba esperando al que lo reservo, vuelve a estar disponible.
        //si todavia esta prestado, sigue prestado.
        if (libro.getEstaDispponible() == Disponibilidad.RESERVADO){
            libro.setEstaDispponible(Disponibilidad.DISPONIBLE);
            libroDAO.actualizar(libro);
        }

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
        prestamoDAO.eliminar(numeroPrestamo);
        //si alguien lo tenia reservado, el libro queda RESERVADO para esa persona.
        //si no, vuelve a estar disponible.
        if (buscarReservaDeLibro(libro) != null){
            libro.setEstaDispponible(Disponibilidad.RESERVADO);
            libroDAO.actualizar(libro);
        } else {
            libro.setEstaDispponible(Disponibilidad.DISPONIBLE);
            libroDAO.actualizar(libro);
        }
        return true;
    }
}