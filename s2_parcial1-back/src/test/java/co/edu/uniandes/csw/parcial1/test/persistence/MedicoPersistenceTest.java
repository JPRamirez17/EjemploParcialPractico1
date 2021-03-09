package co.edu.uniandes.csw.parcial1.test.persistence;

import co.edu.uniandes.csw.parcial1.entities.MedicoEntity;
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
 *
 * Clase de pruebas de persistencia para MedicoPersistence.
 * @author j.ramirez28
 */
@RunWith( Arquillian.class )
public class MedicoPersistenceTest {
    
    //------------------------------------------------------------------------
    // CONSTANTES
    //------------------------------------------------------------------------
    
    /**
     * Constante que representa la cantidad de datos aleatorios que se deben
     * generar en la base de datos de pruebas.
     */
    private final static int CANTIDAD_DATOS = 25;
    
    //------------------------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------------------------
    
    /**
     * Crea una instancia de la persistencia de jardín infantil.
     */
    @Inject
    private MedicoPersistence persistence;
    
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
     * Lista de entidades persistidas para las pruebas.
     */
    private List< MedicoEntity > data;
    
    //------------------------------------------------------------------------
    // MÉTODOS
    //------------------------------------------------------------------------
    
    /**
     * Método encargado de retornar un archivo con lo que se va a probar en
     * el Glassfish embebido. El jar tiene las clases de Medico,
     * descriptor de la base de datos y el archivo beans.xml para resolver
     * la inyección de dependencias.
     * @return archivo de java con las puebas de persistencia de jardín infantil.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create( JavaArchive.class )
                .addPackage( MedicoEntity.class.getPackage() )
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
     * Elimina toda la información de la tabla de base de datos.
     */
    private void clearData() {
        // Query que elimina todas las tuplas de la tabla
        em.createQuery( "DELETE FROM MedicoEntity" ).executeUpdate();
    }
    
    /**
     * Crea y agrega objetos aleatorios en la lista de objetos de prueba.
     */
    private void insertData() {
        // Inicializa la lista de objetos
        data = new ArrayList<>();
        
        // Crear una fábrica de objetos
        PodamFactory factory = new PodamFactoryImpl();
        for ( int i = 0; i<CANTIDAD_DATOS; i++ ) {
            // La fábrica genera una entidad aleatoria
            MedicoEntity entity = factory
                    .manufacturePojo( MedicoEntity.class );
            
            // Se persiste la entidad creada y se guarda en la lista de pruebas
            em.persist( entity );
            data.add( entity );
        }
    }
    
    //------------------------------------------------------------------------
    // PRUEBAS
    //------------------------------------------------------------------------
    
    /**
     * Verifica el método create de la clase MedicoPersistence.
     * <b> Métodos a probar: </b><br>
     * create()<br>
     * <b>Objetivo:</b>
     * Probar que el método create sea capaz de persistir un nuevo objeto.
     * <b>Resultados esperados:</b>
     * 1. Persistir una entidad correctamente.<br>
     * 2. El objeto persistido tiene toda la información correcta.
     */
    @Test
    public void createTest() {
        // Crear una fábrica de objetos y un Médico aleatorio
        PodamFactory factory = new PodamFactoryImpl();
        MedicoEntity jardin = factory.manufacturePojo(
                MedicoEntity.class );
        
        // Crea una entidad médico - método a probar
        MedicoEntity resultado = persistence.create( jardin );
        // Verifica que la entidad creada no sea nula
        Assert.assertNotNull( resultado );
        
        // El em busca el objeto con id del objeto creado
        MedicoEntity entity = 
                em.find( MedicoEntity.class, resultado.getId() );
        Assert.assertNotNull( entity );
        
        // Los atributos del objeto buscado debe coincidir con el creado
        Assert.assertEquals( jardin.getId(), entity.getId() );
        Assert.assertEquals( jardin.getNombre(), entity.getNombre() );
        Assert.assertEquals( jardin.getApellido(), entity.getApellido() );
        Assert.assertEquals( jardin.getRegistro(), entity.getRegistro() );
         
    }
    
    /**
     * Verifica el método findByRegistro de la clase MedicoPersistence.
     * <b> Métodos a probar: </b><br>
     * findByRegistro()<br>
     * <b>Objetivo:</b>
     * Probar que el método findByRegistro sea capaz de buscar todos los objetos de
     * la base de datos con un registro dado.
     * <b>Resultados esperados:</b>
     * 1. Se recupera un médico con el registro dado correctamente.<br>
     * 2. Retorna una lista vacía si no encuentra ninguno.
     */
    @Test
    public void findByRegistroTest() {
        if ( !data.isEmpty() ) {
            // Se escoge un elemento aleatorio de la lista y se elimina de la
            // base de datos de persistencia
            int indice = ( int ) ( Math.random()*CANTIDAD_DATOS );
            MedicoEntity entity = data.get( indice );
            MedicoEntity buscado = persistence
                    .findByRegistro( entity.getRegistro() );

            // Debio encontrar al menos una con el registro buscado
            Assert.assertNotNull( buscado );
            Assert.assertEquals( entity.getId(), buscado.getId() );
            Assert.assertEquals( entity.getNombre(), buscado.getNombre() );
            Assert.assertEquals( entity.getApellido(), buscado.getApellido() );
            Assert.assertEquals( entity.getRegistro(), buscado.getRegistro() );
         
        }
        else {
            MedicoEntity buscado = persistence
                    .findByRegistro( -545432 );
            // No debe existir ningún jardín
            Assert.assertNull( buscado );
        }
    }
}
