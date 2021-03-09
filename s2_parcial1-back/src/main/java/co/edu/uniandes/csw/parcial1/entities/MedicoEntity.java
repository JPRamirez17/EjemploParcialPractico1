package co.edu.uniandes.csw.parcial1.entities;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 * Entidad que representa un médico.
 * @author j.ramirez28
 */
@Entity
public class MedicoEntity extends BaseEntity implements Serializable {
    
    //-------------------------------------------------------------------------
    // ATRIBUTOS
    //-------------------------------------------------------------------------

    private String nombre;
    private String apellido;
    private Integer registro;
    private String especialidad;
    
    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------
    
    public MedicoEntity() {
        // COnstructor vacío para evitar problemas con JPA
    }
    
    //-------------------------------------------------------------------------
    // GETTERS-SETTERS
    //-------------------------------------------------------------------------

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    
}
