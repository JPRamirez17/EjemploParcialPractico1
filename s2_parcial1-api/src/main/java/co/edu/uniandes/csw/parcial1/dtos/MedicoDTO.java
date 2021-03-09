package co.edu.uniandes.csw.parcial1.dtos;

import co.edu.uniandes.csw.parcial1.entities.MedicoEntity;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * MedicoDTO Objeto de transferencia de datos de medicos.
 * Los DTO contienen la representación JSON que se transfieren entre el cliente
 * y el servidor.
 * 
 * Al serializarse como JSON esta clase implementa el siguiente modelo:<br>
 * <pre>
 * {
 *    "id": long,
 *    "nombre": string,
 *    "apellido": string,
 *    "registro": integer,
 *    "especialidad": string
 * }
 * </pre>
 * Por ejemplo un médico se representa así:<br>
 * <pre>
 * {
 *    "id": 231523,
 *    "nombre": "Juan",
 *    "apellido": "Pelaez",
 *    "registro": 362536,
 *    "especialidad": "Corazón"
 * }
 * </pre>
 * 
 * @author j.ramirez28
 */
public class MedicoDTO implements Serializable {
    
    //-------------------------------------------------------------------------
    // ATRIBUTOS
    //-------------------------------------------------------------------------

    private Long id;
    private String nombre;
    private String apellido;
    private Integer registro;
    private String especialidad;
    
    //------------------------------------------------------------------------
    // CONSTRUCTORES
    //------------------------------------------------------------------------
    
    /**
     * Constructor por defecto.
     */
    public MedicoDTO() {
        // Constructor vacío por defecto para evitar conflictos.
    }
    
    /**
     * Constructor a partir de la entidad MedicoEntity.
     * @param medicoEntity La entidad del médico.
     */
    public MedicoDTO( MedicoEntity medicoEntity ) {
        if( medicoEntity != null ) {
            this.id = medicoEntity.getId();
            this.nombre = medicoEntity.getNombre();
            this.apellido = medicoEntity.getApellido();
            this.registro = medicoEntity.getRegistro();
            this.especialidad = medicoEntity.getEspecialidad();
        }
    }
    
    //------------------------------------------------------------------------
    // GETTERS-SETTERS
    //------------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    
    //------------------------------------------------------------------------
    // MÉTODOS
    //------------------------------------------------------------------------
    
    /**
     * Convierte un objeto MedicoDTO a MedicoEntity.
     * @return nuevo objeto MedicoEntity.
     */
    public MedicoEntity toEntity() {
        MedicoEntity medicoEntity = new MedicoEntity();
        medicoEntity.setId( this.getId() );
        medicoEntity.setNombre( this.getNombre() );
        medicoEntity.setApellido( this.getApellido() );
        medicoEntity.setRegistro( this.getRegistro() );
        medicoEntity.setEspecialidad( this.getEspecialidad() );
        return medicoEntity;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
    }
}
