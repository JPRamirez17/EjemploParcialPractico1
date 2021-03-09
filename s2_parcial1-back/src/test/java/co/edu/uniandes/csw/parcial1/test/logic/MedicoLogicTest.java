package co.edu.uniandes.csw.parcial1.test.logic;

import co.edu.uniandes.csw.parcial1.ejb.MedicoLogic;
import co.edu.uniandes.csw.parcial1.entities.MedicoEntity;
import co.edu.uniandes.csw.parcial1.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.parcial1.persistence.MedicoPersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de lógica para Medico.
 * @author j.ramirez28
 */
@RunWith( Arquillian.class )
public class MedicoLogicTest {
    
    //------------------------------------------------------------------------
    // CONSTANTES
    //------------------------------------------------------------------------
    
    /**
     * Constante que representa la cantidad de datos aleatorios que se deben
     * generar en la base de datos de pruebas.
     */
    private final static int CANTIDAD_DATOS = 5;
    
    //------------------------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------------------------
    
    /**
     * Crea un creador de objetos aleatotios de Podam.
     */
    private PodamFactory factory = new PodamFactoryImpl();
    
    /**
     * Inyecta una instancia de la lógica de médico.
     */
    @Inject
    private MedicoLogic logica;
    
    /**
     * Contexto de Persistencia que se utiliza para acceder a la base de datos.
     * EntityManager para el manejo de objetos persistidos.
     */
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Variable para marcar las transacciones del EntityManager cuando
     * se crean/borran datos para las pruebas.
     */
    @Inject
    UserTransaction utx;
    
    /**
     * Lista de entidades medico persistidas para las pruebas.
     */
    private List< MedicoEntity > data;
    
    //------------------------------------------------------------------------
    // MÉTODOS
    //------------------------------------------------------------------------
    
    /**
     * Método encargado de retornar un archivo con lo que se va a probar en
     * el Glassfish embebido. El jar tiene las clases de JardinInfantil,
     * descriptor de la base de datos y el archivo beans.xml para resolver
     * la inyección de dependencias.
     * @return archivo de java con las puebas de lógica de convenio.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create( JavaArchive.class )
                .addPackage( MedicoEntity.class.getPackage() )
                .addPackage( MedicoLogic.class.getPackage() )
                .addPackage( MedicoPersistence.class.getPackage() )
                .addAsManifestResource( "META-INF/persistence.xml",
                        "persistence.xml")
                .addAsManifestResource( "META-INF/beans.xml","beans.xml" );
    }
    
    /**
     * Escenario de pruebas donde de manera transaccional se va a limpiar
     * la base de datos de prueba y llenar con datos aleatorios.
     * En caso de error se hace un rollback de la transacción y se notifica.
     */
    @Before
    public void setUp() {
        try {
            utx.begin();
            em.joinTransaction();
            clearData();
            insertData();
            utx.commit();
        }
        catch ( Exception e ) {
            e.printStackTrace();
            try {
                // Rollback de la transacción
                utx.rollback();
            }
            catch ( Exception e1 ) {
                e1.printStackTrace();
            }
        }
    }
    
    /**
     * Elimina toda la información de las tablas de base de datos.
     */
    private void clearData() {
        // Query que elimina todas las tuplas de las tablas
        em.createQuery( "DELETE FROM MedicoEntity" ).executeUpdate();
    }
    
    /**
     * Crea y agrega objetos aleatorios en la lista de objetos de prueba.
     */
    private void insertData() {
        // Inicializa las listas de objetos
        data = new ArrayList<>();

        for ( int i = 0; i<CANTIDAD_DATOS; i++ ) {
            MedicoEntity med = factory.manufacturePojo( MedicoEntity.class );
            em.persist( med );
            data.add( med );
        }
        
    }
    
    //------------------------------------------------------------------------
    // PRUEBAS
    //------------------------------------------------------------------------
    
    /**
     * Verifica el método createMedico de la clase MedicoLogic.
     * <b> Métodos a probar: </b><br>
     * createMedico()<br>
     * <b>Objetivo:</b>
     * Probar que el método createMedico sea capaz de persistir un nuevo objeto.
     * <b>Resultados esperados:</b>
     * 1. Se crea y persiste una entidad correctamente.<br>
     * 2. El objeto persistido tiene toda la información correcta.<br>
     * 3. Las reglas de negocio se prueban exitosamente.
     */
    @Test
    public void createJardinInfantilTest() {
        MedicoEntity nueva = factory.manufacturePojo( MedicoEntity.class );
        if ( nueva.getEspecialidad().length() < 4 ) {
                nueva.setEspecialidad( "abcde" );
        }
        try {
            MedicoEntity resultado = logica.createMedico( nueva );
            Assert.assertNotNull( resultado );
            MedicoEntity entity = em.find( MedicoEntity.class, resultado.getId() );
            Assert.assertEquals( nueva.getId(), entity.getId() );
            Assert.assertEquals( nueva.getNombre(), entity.getNombre() );
            Assert.assertEquals( nueva.getApellido(), entity.getApellido() );
            Assert.assertEquals( nueva.getRegistro(), entity.getRegistro() );
            Assert.assertEquals( nueva.getEspecialidad(), entity.getEspecialidad() );
        } catch ( BusinessLogicException e ) {
            Assert.fail( "No debería generar excepción" );
        }
        // Comprobar regla de negocio no existen médicos con igual registro.
        try {
            logica.createMedico( nueva );
            Assert.fail( "Debería generar excepción" );
        } catch ( BusinessLogicException e ) {
            // Debería generase
        }
        // Comprobar regla de negocio de nulos y vacíos.
        nueva.setRegistro( nueva.getRegistro()+1 );
        nueva.setNombre( null );
        try {
            logica.createMedico( nueva );
            Assert.fail( "Debería generar excepción" );
        } catch ( BusinessLogicException e ) {
            // Debería generase
        }
        nueva.setNombre( "" );
        try {
            logica.createMedico( nueva );
            Assert.fail( "Debería generar excepción" );
        } catch ( BusinessLogicException e ) {
            // Debería generase
        }
        nueva.setNombre( "Prueba" );
        nueva.setApellido( null );
        try {
            logica.createMedico( nueva );
            Assert.fail( "Debería generar excepción" );
        } catch ( BusinessLogicException e ) {
            // Debería generase
        }
        nueva.setApellido( "" );
        try {
            logica.createMedico( nueva );
            Assert.fail( "Debería generar excepción" );
        } catch ( BusinessLogicException e ) {
            // Debería generase
        }
        // Prueba regla de negocio de longitud de la especialidad
        nueva.setApellido( "Prueba" );
        nueva.setEspecialidad( "1" );
        try {
            logica.createMedico( nueva );
            Assert.fail( "Debería generar excepción" );
        } catch ( BusinessLogicException e ) {
            // Debería generase
        }
    }
}
