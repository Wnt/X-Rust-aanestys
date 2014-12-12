package org.x_rust.aanestys.samples.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.x_rust.aanestys.samples.backend.data.Category;
import org.x_rust.aanestys.samples.backend.data.Nominee;

public class PersistenceUnitTest extends TestCase {

	private static Logger logger = Logger.getLogger(PersistenceUnitTest.class
			.getName());

	private EntityManagerFactory emFactory;

	private EntityManager em;

	private Connection connection;

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		try {
			logger.info("Starting in-memory HSQL database for unit tests");
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection(
					"jdbc:hsqldb:mem:unit-testing-jpa", "sa", "");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Exception during HSQL database startup.");
		}
		try {
			logger.info("Building JPA EntityManager for unit tests");
			emFactory = Persistence.createEntityManagerFactory("testPU");
			em = emFactory.createEntityManager();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Exception during JPA EntityManager instanciation.");
		}
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
		logger.info("Shuting down JPA layer.");
		if (em != null) {
			em.close();
		}
		if (emFactory != null) {
			emFactory.close();
		}
		logger.info("Stopping in-memory HSQL database.");
		try {
			connection.createStatement().execute("SHUTDOWN");
		} catch (Exception ex) {
		}
	}

	@Test
	public void testNomineePersistence() {
		try {

            Nominee n = new Nominee();
            n.setNomineeName("Hello world");
			em.getTransaction().begin();
            em.persist(n);
            assertTrue(em.contains(n));
            
            Category c = new Category();
            c.setName("testikategoria");
            em.persist(c);
            assertTrue(em.contains(c));
            
            c.addNominee(n);
            assertTrue(n.getGategories().contains(c));
            
            
            em.remove(n);
            assertFalse(em.contains(n));

            em.getTransaction().commit();
		} catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            fail("Exception during testNomineePersistence");
		}
	}

}
