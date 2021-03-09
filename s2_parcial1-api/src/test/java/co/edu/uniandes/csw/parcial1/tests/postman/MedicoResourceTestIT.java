package co.edu.uniandes.csw.parcial1.tests.postman;

import co.edu.uniandes.csw.parcial1.dtos.MedicoDTO;
import co.edu.uniandes.csw.parcial1.mappers.BusinessLogicExceptionMapper;
import co.edu.uniandes.csw.parcial1.resources.MedicoResource;
import co.edu.uniandes.csw.parcial1.resources.RestConfig;
import co.edu.uniandes.csw.postman.tests.PostmanTestBuilder;
import java.io.File;
import java.io.IOException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Pruebas de integracion del recurso médico.
 * @author j.ramirez28
 */
@RunWith(Arquillian.class)
public class MedicoResourceTestIT {
    
    private static final String COLLECTION = "MedicoResourceTest.postman_collection";

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "s2_parcial1-api.war")//War del modulo api
                // Se agrega las dependencias
                .addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                        .importRuntimeDependencies().resolve()
                        .withTransitivity().asFile())
                // Se agregan los compilados de los paquetes de servicios
                .addPackage(MedicoResource.class.getPackage()) //No importa cual recurso usar, lo importante es agregar el paquet
                .addPackage(MedicoDTO.class.getPackage()) //No importa cual dto usar, lo importante es agregar el paquete.
                .addPackage(BusinessLogicExceptionMapper.class.getPackage())
                // El archivo que contiene la configuracion a la base de datos.
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                // El archivo beans.xml es necesario para injeccion de dependencias.
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
                // El archivo web.xml es necesario para el despliegue de los servlets
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"));
    }

    @Test
    @RunAsClient
    public void postman() throws IOException {
        PostmanTestBuilder tp = new PostmanTestBuilder();
        tp.setTestWithoutLogin(COLLECTION, "Entorno-IT.postman_environment");
        String desiredResult = "0";
        Assert.assertEquals("Error en Requests de: " + COLLECTION, desiredResult, tp.getRequests_failed());
        Assert.assertEquals("Error en Assertions de: " + COLLECTION, desiredResult, tp.getAssertions_failed());
    }
}
