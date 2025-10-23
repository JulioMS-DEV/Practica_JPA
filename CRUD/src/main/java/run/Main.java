package run;

import entities.Autor;
import entities.Categoria;
import entities.Libro;
import services.dao.MyDao;
import services.interfaces.ICRUD;
import java.util.List;

public class Main {

    public static final ICRUD dao = new MyDao();

    // --- CRUD AUTORES ---
    public static void insertarAutor() {
        System.out.println("Insertando autores...");
        Autor a = new Autor();
        a.setNombre("Gabriel Garcia Marquez");
        a.setNacionalidad("Colombiana"); // Corregido para ser preciso
        dao.insert(a);

        Autor r = new Autor();
        r.setNombre("Julio Verne");
        r.setNacionalidad("Francesa");
        dao.insert(r);
    }

    public static void listarAutores() {
        System.out.println("\n--- Autores Registrados ---");
        List<Autor> autores = dao.getAll("autores.All", Autor.class);
        autores.forEach(autor -> System.out.println("ID " + autor.getId() + ": " + autor.getNombre() + " (" + autor.getNacionalidad() + ")"));
    }

    public static void editarAutor() {
        System.out.println("\nEditando autor con ID 1...");
        Autor a = dao.findById(1, Autor.class);
        if (a != null) {
            a.setNacionalidad("Colombiana Inmortal");
            dao.update(a);
        } else {
            System.out.println("Autor no encontrado.");
        }
    }

    public static void eliminarAutor() {
        // Para no causar problemas con los libros, insertaremos uno y lo borraremos
        System.out.println("\nEliminando un autor de ejemplo...");
        Autor autorParaBorrar = new Autor();
        autorParaBorrar.setNombre("Autor Descartable");
        autorParaBorrar.setNacionalidad("Temporal");
        dao.insert(autorParaBorrar); // Le asignará un ID, ej: 3

        System.out.println("Autor a eliminar: " + autorParaBorrar.getNombre() + " con ID: " + autorParaBorrar.getId());
        dao.delete(autorParaBorrar);
    }

    // --- CRUD CATEGORÍAS ---
    public static void insertarCategoria() {
        System.out.println("\nInsertando categorías...");
        Categoria c1 = new Categoria();
        c1.setNombre("Realismo Mágico");
        dao.insert(c1);

        Categoria c2 = new Categoria();
        c2.setNombre("Ciencia Ficción");
        dao.insert(c2);
    }

    public static void listarCategorias() {
        System.out.println("\n--- Categorías Registradas ---");
        List<Categoria> categorias = dao.getAll("categorias.All", Categoria.class);
        categorias.forEach(cat -> System.out.println("ID " + cat.getId() + ": " + cat.getNombre()));
    }

    public static void editarCategoria() {
        System.out.println("\nEditando categoría con ID 2...");
        Categoria c = dao.findById(2, Categoria.class); // Asumimos que el ID 2 es "Ciencia Ficción"
        if (c != null) {
            c.setNombre("Aventura y Ciencia Ficción");
            dao.update(c);
        } else {
            System.out.println("Categoría no encontrada.");
        }
    }

    // La eliminación de categoría se omite en el flujo principal para no romper la integridad de los libros.

    // --- CRUD LIBROS ---
    public static void insertarLibro() {
        System.out.println("\nInsertando un libro...");
        // Para crear un libro, primero necesitamos un autor y una categoría existentes
        Autor autor = dao.findById(1, Autor.class); // Gabriel Garcia Marquez
        Categoria categoria = dao.findById(1, Categoria.class); // Realismo Mágico

        if (autor != null && categoria != null) {
            Libro libro = new Libro();
            libro.setTitulo("Cien años de soledad");
            libro.setAñoPub(1967);
            libro.setAutor(autor);
            libro.setCategoria(categoria);
            dao.insert(libro);
        } else {
            System.out.println("No se pudo insertar el libro porque el autor o la categoría no existen.");
        }
    }

    public static void listarLibros() {
        System.out.println("\n--- Libros Registrados ---");
        List<Libro> libros = dao.getAll("libros.All", Libro.class);
        libros.forEach(libro ->
                System.out.println(
                        "'" + libro.getTitulo() + "'" +
                                " (" + libro.getAñoPub() + ")" +
                                " - Autor: " + libro.getAutor().getNombre() +
                                " - Categoría: " + libro.getCategoria().getNombre()
                )
        );
    }

    public static void editarLibro() {
        System.out.println("\nEditando libro con ID 1...");
        Libro libro = dao.findById(1, Libro.class); // Cien años de soledad
        if (libro != null) {
            libro.setAñoPub(1968); // Cambiamos el año de publicación
            dao.update(libro);
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    public static void eliminarLibro() {
        System.out.println("\nEliminando libro con ID 1...");
        Libro libro = dao.findById(1, Libro.class);
        if (libro != null) {
            dao.delete(libro);
        } else {
            System.out.println("Libro a eliminar no encontrado.");
        }
    }

    public static void main(String[] args) {

        // 1. Insertamos las entidades que no dependen de otras
        insertarAutor();
        insertarCategoria();

        // 2. Mostramos lo que se insertó
        listarAutores();
        listarCategorias();

        // 3. Insertamos la entidad que depende de las anteriores
        insertarLibro();

        // 4. Mostramos la lista completa de libros
        listarLibros();

        // 5. Editamos una entidad de cada tipo
        editarAutor();
        editarCategoria();
        editarLibro();

        // 6. Mostramos todas las listas para ver los cambios
        System.out.println("\n\n--- ESTADO FINAL DESPUÉS DE EDITAR ---");
        listarAutores();
        listarCategorias();
        listarLibros();

        // 7. Eliminamos entidades
        eliminarAutor(); // El de ejemplo
        eliminarLibro();

        // 8. Mostramos el estado final después de eliminar
        System.out.println("\n\n--- ESTADO FINAL DESPUÉS DE ELIMINAR ---");
        listarAutores();
        listarLibros();
    }
}