package com.marketduoc.cl.marketduoc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketduoc.cl.marketduoc.model.Estado;
import com.marketduoc.cl.marketduoc.repository.EstadoRepository;

@SpringBootTest
public class EstadoServiceTest {
    
    @Autowired
    private EstadoService estadoService;

    @MockBean
    private EstadoRepository estadoRepository;

    private Estado createEstado() {
        return new Estado(1L, "Disponible");
    }

    @Test
    public void testFindAll(){
        when(estadoRepository.findAll()).thenReturn(List.of(createEstado()));
        List<Estado> estados = estadoService.findAll();
        assertNotNull(estados);
        assertEquals(1, estados.size());
    }

    @Test
    public void testSave() {
        Estado estado = createEstado();
        when(estadoRepository.save(estado)).thenReturn(estado);
        Estado savedEstado = estadoService.save(estado);
        assertNotNull(savedEstado);
        assertEquals("Disponible", savedEstado.getNombre());
    }

    @Test
    public void testFindById() {
        Estado estado = createEstado();
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        Estado found = estadoService.findById(1L);
        assertNotNull(found);
        assertEquals("Disponible", found.getNombre());
    }

    @Test
    public void testFindByIdNotFound() {
        when(estadoRepository.findById(2L)).thenReturn(Optional.empty());
        Estado found = estadoService.findById(2L);
        assertNull(found);
    }

    @Test
    public void testDelete() {
        doNothing().when(estadoRepository).deleteById(1L);
        estadoService.delete(1L);
        verify(estadoRepository, times(1)).deleteById(1L);
    }
}
