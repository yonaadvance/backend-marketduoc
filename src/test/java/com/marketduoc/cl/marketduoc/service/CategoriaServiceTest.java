package com.marketduoc.cl.marketduoc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketduoc.cl.marketduoc.model.Categoria;
import com.marketduoc.cl.marketduoc.repository.CategoriaRepository;

@SpringBootTest
public class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @MockBean
    private CategoriaRepository categoriaRepository;

    private Categoria createCategoria() {
        return new Categoria(1L, "Tecnologia");
    }

    @Test
    public void testFindAll(){
        when(categoriaRepository.findAll()).thenReturn(List.of(createCategoria()));
        List<Categoria> categorias = categoriaService.findAll();
        assertNotNull(categorias);
        assertEquals(1, categorias.size());
    }

    @Test
    public void testSave() {
        Categoria categoria = createCategoria();
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        Categoria savedCategoria = categoriaService.save(categoria);
        assertNotNull(savedCategoria);
        assertEquals("Tecnologia", savedCategoria.getNombre());
    }

    @Test
    public void testFindByNombre() {
        when(categoriaRepository.findByNombre("Tecnologia")).thenReturn(List.of(createCategoria()));
        List<Categoria> categorias = categoriaService.findByNombre("Tecnologia");
        assertEquals(1, categorias.size());
        assertEquals("Tecnologia", categorias.get(0).getNombre());
    }

    @Test
    public void testListaOrdenada() {
        when(categoriaRepository.listaOrdenada()).thenReturn(List.of(createCategoria()));
        List<Categoria> categorias = categoriaService.listaOrdenada();
        assertFalse(categorias.isEmpty());
        assertEquals("Tecnologia", categorias.get(0).getNombre());
    }
}
