package com.marketduoc.cl.marketduoc.repository;

import org.springframework.stereotype.Repository;
import com.marketduoc.cl.marketduoc.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    List<Usuario> findByApellidos(String apellidos);

    Usuario findById(Integer id);

    Usuario findByCorreo(String correo);

    List<Usuario> findByNombreAndApellidos(String nombres, String apellidos);

    Usuario findByNombreAndCorreo(String nombres, String correo);

    @Query("SELECT DISTINCT u FROM Usuario u " +
       "JOIN u.tipoUsuario t " +
       "JOIN Producto p ON p.usuario.id = u.id " +
       "JOIN p.categoria c " +
       "WHERE t.nombre = :tipoNombre AND c.nombre = :categoriaNombre")
    List<Usuario> buscarPorTipoYCategoriaDeProducto(
        @Param("tipoNombre") String tipoNombre,
        @Param("categoriaNombre") String categoriaNombre);

}
