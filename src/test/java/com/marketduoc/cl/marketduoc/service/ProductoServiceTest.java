package com.marketduoc.cl.marketduoc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketduoc.cl.marketduoc.model.Producto;
import com.marketduoc.cl.marketduoc.model.Usuario;
import com.marketduoc.cl.marketduoc.model.Estado;
import com.marketduoc.cl.marketduoc.model.Categoria;
import com.marketduoc.cl.marketduoc.repository.ProductoRepository;

@SpringBootTest
public class ProductoServiceTest {
@Autowired
    private ProductoService productoService;

    @MockBean
    private ProductoRepository productoRepository;
    
    private Producto createProducto() {
        return new Producto(
            1L,  
            "Mouse", 
            "Mouse gamer logitech",
            null, // <--- AQUÍ ESTÁ EL ARREGLO: Agregamos null para la imagen
            new Date(System.currentTimeMillis()), 
            new Usuario(), 
            new Estado(),
            new Categoria()
        );
    }

    @Test
    public void testFindAll() {
        when(productoRepository.findAll()).thenReturn(List.of(createProducto()));
        List<Producto> productos = productoService.findAll();
        assertNotNull(productos);
        assertEquals(1, productos.size());
    }

    @Test
    public void testFindById() {
        when(productoRepository.findById(1L)).thenReturn(java.util.Optional.of(createProducto()));
        Producto producto = productoService.findById(1L);
        assertNotNull(producto);
        assertEquals("Mouse", producto.getNombre());
    }

    @Test
    public void testSave() {
        Producto producto = createProducto();
        when(productoRepository.save(producto)).thenReturn(producto);
        Producto savedProducto = productoService.save(producto);
        assertNotNull(savedProducto);
        assertEquals("Mouse", savedProducto.getNombre());
    }

    @Test
    public void testPatchProducto() {
        Producto existingProducto = createProducto();
        Producto patchData = new Producto();
        patchData.setNombre("Audifonos");

        when(productoRepository.findById(1L)).thenReturn(java.util.Optional.of(existingProducto));
        when(productoRepository.save(any(Producto.class))).thenReturn(existingProducto);

        Producto patchedProducto = productoService.patchProducto(1L, patchData);
        assertNotNull(patchedProducto);
        assertEquals("Audifonos", patchedProducto.getNombre());
    }

    @Test
    public void testDelete() {
        doNothing().when(productoRepository).deleteById(1L);
        productoService.delete(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByNombre() {
        when(productoRepository.findByNombre("Mouse")).thenReturn(List.of(createProducto()));
        List<Producto> productos = productoService.findByNombre("Mouse");
        assertNotNull(productos);
        assertEquals(1, productos.size());
    }

    @Test
    public void testFindByUsuarioId() {
        when(productoRepository.findByUsuario_id(1)).thenReturn(List.of(createProducto()));
        List<Producto> productos = productoService.findByUsuarioId(1);
        assertNotNull(productos);
        assertEquals(1, productos.size());
    }

    @Test
    public void testFindByUsuarioAndNombre() {
        Usuario usuario = new Usuario();
        when(productoRepository.findByUsuarioAndNombre(usuario, "Mouse")).thenReturn(List.of(createProducto()));
        List<Producto> productos = productoService.findByUsuarioAndNombre(usuario, "Mouse");
        assertNotNull(productos);
        assertEquals(1, productos.size());
    }

    @Test
    public void testFindByNombreAndFechaCreacion() {
        Date fecha = new Date(System.currentTimeMillis());
        Producto producto = createProducto();
        when(productoRepository.findByNombreAndFechaCreacion("Mouse", fecha)).thenReturn(producto);
        Producto resultado = productoService.findByNombreAndFechaCreacion("Mouse", fecha);
        assertNotNull(resultado);
        assertEquals("Mouse", resultado.getNombre());
    }

    @Test
    public void testContarProductosPorEstadoYCategoria() {
        Object[] fila = new Object[] { "Categoria1", "Disponible", 5L };
        List<Object[]> resultadoEsperado = new ArrayList<>();
        resultadoEsperado.add(fila);

        when(productoRepository.contarProductosPorEstadoYCategoria()).thenReturn(resultadoEsperado);

        List<Object[]> resultadoReal = productoService.contarProductosPorEstadoYCategoria();

        assertNotNull(resultadoReal);
        assertEquals(1, resultadoReal.size());
        assertArrayEquals(fila, resultadoReal.get(0));
    }

    @Test
    public void testBuscarPorTipoDeUsuarioYEstadoDisponible() {
        when(productoRepository.buscarPorTipoDeUsuarioYEstadoDisponible("Cliente")).thenReturn(List.of(createProducto()));
        List<Producto> productos = productoService.buscarPorTipoDeUsuarioYEstadoDisponible("Cliente");
        assertNotNull(productos);
        assertEquals(1, productos.size());
    }

    @Test
    public void testBuscarPorCategoriaEstadoYUsuario() {
        when(productoRepository.buscarPorCategoriaEstadoYUsuario("Categoria1", "Disponible", "Usuario1"))
            .thenReturn(List.of(createProducto()));
        List<Producto> productos = productoService.buscarPorCategoriaEstadoYUsuario("Categoria1", "Disponible", "Usuario1");
        assertNotNull(productos);
        assertEquals(1, productos.size());
    }
}