package com.marketduoc.cl.marketduoc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketduoc.cl.marketduoc.model.TipoUsuario;
import com.marketduoc.cl.marketduoc.repository.TipoUsuarioRepository;

@SpringBootTest
public class TipoUsuarioServiceTest {

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @MockBean
    private TipoUsuarioRepository tipoUsuarioRepository;

    private TipoUsuario createTipoUsuario() {
        return new TipoUsuario(1L, "Usuario");
    }

    @Test
    public void testFindAll(){
        when(tipoUsuarioRepository.findAll()).thenReturn(List.of(createTipoUsuario()));
        List<TipoUsuario> tipos = tipoUsuarioService.findAll();
        assertNotNull(tipos);
        assertEquals(1, tipos.size());
    }

    @Test
    public void testSave() {
        TipoUsuario tipoUsuario = createTipoUsuario();
        when(tipoUsuarioRepository.save(tipoUsuario)).thenReturn(tipoUsuario);
        TipoUsuario savedTipoUsuario = tipoUsuarioService.save(tipoUsuario);
        assertNotNull(savedTipoUsuario);
        assertEquals("Usuario", savedTipoUsuario.getNombre());
    }

    @Test
    public void testDelete() {
        doNothing().when(tipoUsuarioRepository).deleteById(1L);
        tipoUsuarioService.delete(1L);
        verify(tipoUsuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testPatchTipoUsuario() {
        TipoUsuario original = createTipoUsuario();
        TipoUsuario cambios = new TipoUsuario();
        cambios.setNombre("Cliente");

        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(original));
        when(tipoUsuarioRepository.save(any(TipoUsuario.class))).thenReturn(original);

        TipoUsuario actualizado = tipoUsuarioService.patchTipoUsuario(1L, cambios);

        assertNotNull(actualizado);
        assertEquals("Cliente", actualizado.getNombre());
    }

    @Test
    public void testFindByNombre() {
        TipoUsuario tipo = createTipoUsuario();
        when(tipoUsuarioRepository.findByNombre("Administrador")).thenReturn(List.of(tipo));

        List<TipoUsuario> tipos = tipoUsuarioService.findByNombre("Administrador");

        assertNotNull(tipos);
        assertEquals(1, tipos.size());
        assertEquals("Administrador", tipos.get(0).getNombre());
    }
}
