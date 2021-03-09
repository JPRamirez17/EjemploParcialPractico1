package co.edu.uniandes.csw.parcial1.resources;

import co.edu.uniandes.csw.parcial1.dtos.MedicoDTO;
import co.edu.uniandes.csw.parcial1.ejb.MedicoLogic;
import co.edu.uniandes.csw.parcial1.exceptions.BusinessLogicException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Clase que implementa el recurso médico.
 * @author j.ramirez28
 */
@Path("/medicos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class MedicoResource {
    
    //------------------------------------------------------------------------
    // CONSTANTES
    //------------------------------------------------------------------------
    
    /**
     * Objeto para hacer log de mensajes en la aplicación.
     */
    private static final Logger LOGGER = Logger.getLogger( MedicoResource.class.getName() );
    
    //------------------------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------------------------
    
    /**
     * Inyección de dependencias para acceder a la lógica de la aplicación (Medico).
     */
    @Inject
    private MedicoLogic medicoLogica;
    
    /**
     * Crea un nuevo médico con la información que se recibe en el cuerpo
     * de la petición y se retorna un objeto idéntico con un id auto-generado
     * por la base de datos.
     * 
     * @param medico {@link MedicoDTO} El médico que se desea guardar.
     * @return JSON {@link MedicoDTO} El médico guardado con el atributo id
     * auto-generado.
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} por error
     * de lógica que se genera por incumplir reglas de negocio.
     */
    @POST
    public MedicoDTO createMedico( MedicoDTO medico ) throws BusinessLogicException {
        LOGGER.log( Level.INFO, "MedicoResource createMedico: input:{0}", medico );
        MedicoDTO nuevoMedicoDTO = new MedicoDTO( medicoLogica.createMedico( medico.toEntity() ) );
        LOGGER.log( Level.INFO, "MedicoResource createMedico: output:{0}", nuevoMedicoDTO );
        return nuevoMedicoDTO;
    }
}
