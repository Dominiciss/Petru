
package com.grupop.petru.servicios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Imagen;
import com.grupop.petru.entidades.Proyecto;
import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.enumeraciones.Visibilidad;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.repositorios.ProyectoRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProyectoServicio {
    
    @Autowired
    private ProyectoRepositorio proyectoRepositorio;
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    @Transactional
    public void guardar(MultipartFile archivo, String nombre, Visibilidad visibilidad, 
            String notas, List<Usuario> usuarios) throws MiException {
        validar(nombre, visibilidad);
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        proyecto.setVisibilidad(visibilidad);
        proyecto.setUsuarios(usuarios);
        proyecto.setNotas(notas);
        Imagen imagen = imagenServicio.guardar(archivo);
        proyecto.setImagen(imagen);
        proyecto.setBaja(Boolean.FALSE);
        proyectoRepositorio.save(proyecto);
    }
    
    
    // ALTA Y BAJA DEL PROYECTO
    
    @Transactional
    public void alta(String id) throws MiException {
        try{
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Proyecto proyecto = respuesta.get();
            proyecto.setBaja(false);
            proyectoRepositorio.save(proyecto);            
        }
        } catch (Exception e) {
                System.err.println(e.getMessage());
        }

    }

    @Transactional
    public void baja(String id) throws MiException {
        try{
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Proyecto proyecto = respuesta.get();
            proyecto.setBaja(true);
            proyectoRepositorio.save(proyecto);            
        }
        } catch (Exception e) {
                System.err.println(e.getMessage());
        }
    }   
    

    // VALIDACIONES

    private void validar(String nombre, Visibilidad visibilidad) throws MiException {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (visibilidad == null) {
            throw new MiException("La visibilidad no puede ser nulo o estar vacio");
        }
    }
    
    @Transactional(readOnly = true)
    public Proyecto getOne(String id) {
        return proyectoRepositorio.getOne(id);
    }
    
    @Transactional(readOnly = true)
    public List<Proyecto> listarTodos() {
        return proyectoRepositorio.findAll();
    }     
}