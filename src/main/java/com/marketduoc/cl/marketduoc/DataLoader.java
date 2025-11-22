package com.marketduoc.cl.marketduoc;

import java.util.List;
import java.util.Random;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.marketduoc.cl.marketduoc.model.Categoria;
import com.marketduoc.cl.marketduoc.model.Estado;
import com.marketduoc.cl.marketduoc.model.Producto;
import com.marketduoc.cl.marketduoc.model.TipoUsuario;
import com.marketduoc.cl.marketduoc.model.Usuario;
import com.marketduoc.cl.marketduoc.repository.CategoriaRepository;
import com.marketduoc.cl.marketduoc.repository.EstadoRepository;
import com.marketduoc.cl.marketduoc.repository.ProductoRepository;
import com.marketduoc.cl.marketduoc.repository.TipoUsuarioRepository;
import com.marketduoc.cl.marketduoc.repository.UsuarioRepository;
import net.datafaker.Faker;

@Profile("dev")
@Component
public class DataLoader  implements CommandLineRunner{

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            TipoUsuario tipoUsuario = new TipoUsuario();
            tipoUsuario.setNombre(faker.job().position());
            tipoUsuarioRepository.save(tipoUsuario);
        }

        List<TipoUsuario> tipoUsuario = tipoUsuarioRepository.findAll();

        for (int i = 0; i < 6; i++) {
            Categoria categoria = new Categoria();
            categoria.setNombre(faker.commerce().department());
            categoriaRepository.save(categoria);
        }

        List<Categoria> categoria = categoriaRepository.findAll();

        for (int i = 0; i < 3; i++) {
            Estado estado = new Estado();
            estado.setNombre(i == 0 ? "Disponible" : i == 1 ? "Vendido" : "Sin Stock");
            estadoRepository.save(estado);
        }

        List<Estado> estado = estadoRepository.findAll();

        for (int i = 0; i < 50; i++) {
            Usuario usuario = new Usuario();
            usuario.setNombre(faker.name().firstName());
            usuario.setApellidos(faker.name().lastName());
            usuario.setCorreo(faker.internet().emailAddress());
            usuario.setContraseña(faker.internet().password());
            usuario.setTipoUsuario(tipoUsuario.get(random.nextInt(tipoUsuario.size())));
            usuarioRepository.save(usuario);
        }

        List<Usuario> usuario = usuarioRepository.findAll();

        for (int i = 0;  i < 50; i++) {
            Producto producto = new Producto();
            producto.setNombre(faker.commerce().productName());
            producto.setContenido(faker.lorem().characters(100, 255));
            producto.setFechaCreacion(new Date());
            producto.setUsuario(usuario.get(random.nextInt(usuario.size())));
            producto.setEstado(estado.get(random.nextInt(estado.size())));
            producto.setCategoria(categoria.get(random.nextInt(categoria.size())));
            productoRepository.save(producto);
        }
    }
}
