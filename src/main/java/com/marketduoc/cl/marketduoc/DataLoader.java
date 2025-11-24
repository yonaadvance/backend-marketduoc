package com.marketduoc.cl.marketduoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.marketduoc.cl.marketduoc.model.Categoria;
import com.marketduoc.cl.marketduoc.model.Estado;
import com.marketduoc.cl.marketduoc.model.TipoUsuario;
import com.marketduoc.cl.marketduoc.model.Usuario;
import com.marketduoc.cl.marketduoc.repository.CategoriaRepository;
import com.marketduoc.cl.marketduoc.repository.EstadoRepository;
import com.marketduoc.cl.marketduoc.repository.TipoUsuarioRepository;
import com.marketduoc.cl.marketduoc.repository.UsuarioRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Crear Tipos de Usuario
        if (tipoUsuarioRepository.count() == 0) {
            crearTipo("Cliente");
            crearTipo("Vendedor");
            crearTipo("Administrador");
        }

        // 2. Crear Categorías Fijas
        if (categoriaRepository.count() == 0) {
            crearCategoria("Tecnología");
            crearCategoria("Manualidades");
            crearCategoria("Otros");
        }

        // 3. Crear Estados
        if (estadoRepository.count() == 0) {
            crearEstado("Disponible");
            crearEstado("Vendido");
            crearEstado("Sin Stock");
        }

        // 4. Crear Usuario Admin por defecto
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellidos("MarketDuoc");
            admin.setCorreo("admin@marketduoc.cl");
            admin.setContraseña("123456");
            admin.setTipoUsuario(tipoUsuarioRepository.findAll().get(0));
            usuarioRepository.save(admin);
        }
        
        System.out.println("✅ DATOS LIMPIOS CARGADOS.");
    }

    private void crearTipo(String nombre) {
        TipoUsuario t = new TipoUsuario();
        t.setNombre(nombre);
        tipoUsuarioRepository.save(t);
    }

    private void crearCategoria(String nombre) {
        Categoria c = new Categoria();
        c.setNombre(nombre);
        categoriaRepository.save(c);
    }

    private void crearEstado(String nombre) {
        Estado e = new Estado();
        e.setNombre(nombre);
        estadoRepository.save(e);
    }
}