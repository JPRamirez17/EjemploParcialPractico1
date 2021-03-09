package co.edu.uniandes.csw.parcial1.ejb;

import co.edu.uniandes.csw.parcial1.entities.MedicoEntity;
import co.edu.uniandes.csw.parcial1.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.parcial1.persistence.MedicoPersistence;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Clase que implementa la conexión con la persistencia para la entidad de
 * Medico.
 * @author j.ramirez28
 */
@Stateless
public class MedicoLogic {
    
    //------------------------------------------------------------------------
    // CONSTANTES
    //------------------------------------------------------------------------
    
    /**
     * Objeto para hacer log de mensajes en la aplicación.
     */
    private static final Logger LOGGER = Logger.getLogger( MedicoLogic.class.getName() );
    
    //------------------------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------------------------
    
    /**
     * Inyección de dependencias para acceder a la persistencia de la aplicación.
     */
    @Inject
    private MedicoPersistence persistence;
    
    
    //------------------------------------------------------------------------
    // MÉTODOS
    //------------------------------------------------------------------------
    
    /**
     * Crea un médico en la persistencia.
     * 
     * @param medicoEntity La entidad que representa el médico
     * a persistir.
     * @return La entidad del médico luego de persistirla.
     * @throws BusinessLogicException si el médico a persistir ya existe con su número de registro,
     * los nombres y apellidos son nulos o vacíos, o la especialidad tiene menos de o igual a 4 caracteres.
     */
    public MedicoEntity createMedico( MedicoEntity medicoEntity ) throws BusinessLogicException {
        LOGGER.log( Level.INFO, "Inicia proceso de creación del médico" );
        // Verifica que no haya un médico con mismo registro
        if ( persistence.findByRegistro( medicoEntity.getRegistro( )) != null ) {
            throw new BusinessLogicException( "Ya existe un Medico con el registro \"" + medicoEntity.getRegistro() + "\"" );
        }
        // Verifica la regla de negocio que el nombre y apellido no sean nulos ni vacíos
        if ( medicoEntity.getNombre() == null || medicoEntity.getNombre().trim().isEmpty() ) {
            throw new BusinessLogicException( "El médico creado debería tener un nombre" );
        }
        if ( medicoEntity.getApellido() == null || medicoEntity.getApellido().trim().isEmpty() ) {
            throw new BusinessLogicException( "El médico creado debería tener un apellido" );
        }
        // Verifica la regla de negocio de que la espacialidad debe tener más de 4 caracteres
        if ( medicoEntity.getEspecialidad().length() <= 4 ) {
            throw new BusinessLogicException( "El médico debe tener una especialidad con más de 4 caracteres" );
        }
        // Invoca la persistencia para crear el médico
        persistence.create( medicoEntity );
        LOGGER.log( Level.INFO, "Termina el proceso de creación del médico" );
        return medicoEntity;
    }
}
