package com.marketduoc.cl.marketduoc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketduoc.cl.marketduoc.model.Usuario;
import com.marketduoc.cl.marketduoc.model.TipoUsuario;
import com.marketduoc.cl.marketduoc.repository.UsuarioRepository;

@SpringBootTest
public class UsuarioServiceTest {
@Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;
    
    private Usuario createUsuario() {
        return new Usuario(
            1L,  
            "Pedro", 
            "Rodriguez", 
            "pe.rodriguez@duocuc.cl", 
            "hola1234", 
            new TipoUsuario()
        );
    }

    @Test
    public void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(createUsuario()));
        List<Usuario> usuarios = usuarioService.findAll();
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
    }

    @Test
    public void testFindById() {
        when(usuarioRepository.findById(1L)).thenReturn(java.util.Optional.of(createUsuario()));
        Usuario usuario = usuarioService.findById(1L);
        assertNotNull(usuario);
        assertEquals("Pedro", usuario.getNombre());
    }

    @Test
    public void testSave() {
        Usuario usuario = createUsuario();
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario savedUsuario = usuarioService.save(usuario);
        assertNotNull(savedUsuario);
        assertEquals("Pedro", savedUsuario.getNombre());
    }

    @Test
    public void testPatchUsuario() {
        Usuario existingUsuario = createUsuario();
        Usuario patchData = new Usuario();
        patchData.setNombre("Pedron");

        when(usuarioRepository.findById(1L)).thenReturn(java.util.Optional.of(existingUsuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(existingUsuario);

        Usuario patchedUsuario = usuarioService.patchUsuario(1L, patchData);
        assertNotNull(patchedUsuario);
        assertEquals("Pedron", patchedUsuario.getNombre());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(usuarioRepository).deleteById(1L);
        usuarioService.delete(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByApellidos() {
        Usuario usuario = createUsuario();
        when(usuarioRepository.findByApellidos("Rodriguez")).thenReturn(List.of(usuario));
        List<Usuario> usuarios = usuarioService.findByApellidos("Rodriguez");
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("Rodriguez", usuarios.get(0).getApellidos());
    }

    @Test
    public void testFindByCorreo() {
        Usuario usuario = createUsuario();
        when(usuarioRepository.findByCorreo("pe.rodriguez@duocuc.cl")).thenReturn(usuario);
        Usuario found = usuarioService.findByCorreo("pe.rodriguez@duocuc.cl");
        assertNotNull(found);
        assertEquals("Pedro", found.getNombre());
    }

    @Test
    public void testFindByNombreAndApellidos() {
        Usuario usuario = createUsuario();
        when(usuarioRepository.findByNombreAndApellidos("Pedro", "Rodriguez")).thenReturn(List.of(usuario));
        List<Usuario> usuarios = usuarioService.findByNombreAndApellidos("Pedro", "Rodriguez");
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
    }

    @Test
    public void testFindByNombreAndCorreo() {
        Usuario usuario = createUsuario();
        when(usuarioRepository.findByNombreAndCorreo("Pedro", "pe.rodriguez@duocuc.cl")).thenReturn(usuario);
        Usuario found = usuarioService.findByNombreAndCorreo("Pedro", "pe.rodriguez@duocuc.cl");
        assertNotNull(found);
        assertEquals("Rodriguez", found.getApellidos());
    }

    @Test
    public void testBuscarPorTipoYCategoriaDeProducto() {
        Usuario usuario = createUsuario();
        when(usuarioRepository.buscarPorTipoYCategoriaDeProducto("Cliente", "Tecnologia"))
            .thenReturn(List.of(usuario));
        List<Usuario> usuarios = usuarioService.buscarPorTipoYCategoriaDeProducto("Cliente", "Tecnologia");
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("Pedro", usuarios.get(0).getNombre());
    }
}
