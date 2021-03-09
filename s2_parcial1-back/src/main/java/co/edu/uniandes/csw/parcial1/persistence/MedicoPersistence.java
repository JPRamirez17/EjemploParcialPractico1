package co.edu.uniandes.csw.parcial1.persistence;

import co.edu.uniandes.csw.parcial1.entities.MedicoEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Clase que maneja la persistencia para MedicoEntity.
 * @author j.ramirez28
 */
@Stateless
public class MedicoPersistence {
    
    //-------------------------------------------------------------------------
    // CONSTANTES
    //-------------------------------------------------------------------------
    private static final Logger LOGGER = Logger.getLogger(MedicoPersistence.class.getName());
    
    //-------------------------------------------------------------------------
    // ATRIBUTOS
    //-------------------------------------------------------------------------
    
    /**
     * Entity Manager que maneja el contexto de persistencia.
     */
    @PersistenceContext(unitName = "parcial1PU")
    protected EntityManager em;
    
    //-------------------------------------------------------------------------
    // MÉTODOS
    //-------------------------------------------------------------------------
    
    /**
     * Persiste un objeto médico en la base de datos.
     * @param medico Objeto que se va a persistir. 
     * @return objeto persistido.
     */
    public MedicoEntity create( MedicoEntity medico ) {
        LOGGER.log( Level.INFO, "Creando un médico nuevo" );
        em.persist( medico );
        LOGGER.log( Level.INFO, "Médico creado" );
        return medico;
    }
    
    /**
     * Busca si hay algún médico con el registro que se envía de argumento
     *
     * @param registro Registro del médico que se está buscando
     * @return null si no existe ningún médico con el registro del argumento.
     * Si existe alguna devuelve la primera.
     */
    public MedicoEntity findByRegistro(Integer registro) {
        LOGGER.log(Level.INFO, "Consultando médico por registro ", registro);
        TypedQuery query = em.createQuery("Select e From MedicoEntity e where e.registro = :registro", MedicoEntity.class);
        query = query.setParameter( "registro", registro );
        List< MedicoEntity > encontradas = query.getResultList();
        MedicoEntity resultado;
        if (encontradas == null) {
            resultado = null;
        } else if (encontradas.isEmpty()) {
            resultado = null;
        } else {
            resultado = encontradas.get(0);
        }
        LOGGER.log(Level.INFO, "Saliendo de consultar médico por registro ", registro);
        return resultado;
    }
}
