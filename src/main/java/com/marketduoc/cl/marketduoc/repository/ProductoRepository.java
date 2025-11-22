package com.marketduoc.cl.marketduoc.repository;

import org.springframework.stereotype.Repository;
import com.marketduoc.cl.marketduoc.model.Producto;
import com.marketduoc.cl.marketduoc.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
    
    List<Producto> findByNombre(String nombre);

    Producto findById(Integer id);

    List<Producto> findByUsuario_id(Integer id);

    Producto findByNombreAndFechaCreacion(String nombre, Date fechaCreacion);

    List<Producto> findByUsuarioAndNombre(Usuario usuario, String nombre);

    @Query("SELECT c.nombre, e.nombre, COUNT(p) FROM Producto p " +
       "JOIN p.estado e " +
       "JOIN p.categoria c " +
       "GROUP BY c.nombre, e.nombre")
    List<Object[]> contarProductosPorEstadoYCategoria();

    @Query("SELECT p FROM Producto p " +
       "JOIN p.usuario u " +
       "JOIN u.tipoUsuario t " +
       "JOIN p.estado e " +
       "WHERE t.nombre = :tipoNombre AND e.nombre = 'Disponible'")
    List<Producto> buscarPorTipoDeUsuarioYEstadoDisponible(
        @Param("tipoNombre") String tipoNombre);

    @Query("SELECT p FROM Producto p " +
       "JOIN p.categoria c " +
       "JOIN p.estado e " +
       "JOIN p.usuario u " +
       "WHERE c.nombre = :categoriaNombre AND e.nombre = :estadoNombre AND u.nombre = :usuarioNombre")
    List<Producto> buscarPorCategoriaEstadoYUsuario(
        @Param("categoriaNombre") String categoriaNombre,
        @Param("estadoNombre") String estadoNombre,
        @Param("usuarioNombre") String usuarioNombre);


}
