package org.x_rust.aanestys.samples.backend;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.x_rust.aanestys.backend.DataService;
import org.x_rust.aanestys.backend.data.Category;
import org.x_rust.aanestys.backend.data.Nominee;

public class PersistenceUnitTest extends TestCase {

	private static Logger logger = Logger.getLogger(PersistenceUnitTest.class
			.getName());

	DataService ds;

	@Before
	public void setUp() {
		logger.info("PersistenceUnitTest.setUp();");
        final Properties p = new Properties();
        p.put("votingDatabase", "new://Resource?type=DataSource");
        p.put("votingDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("votingDatabase.JdbcUrl", "jdbc:hsqldb:mem:testdb");
        
        final Context context = EJBContainer.createEJBContainer(p).getContext();
		logger.info("Context found");

        try {
			ds = (DataService) context.lookup("java:global/aanestys-backend/DataService");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Failed to lookup DataService");
		}
	}
	
	@Test
	public void testNomineePersistence() {

		Nominee n = new Nominee();
		n.setNomineeName("Hello world");

		ds.update(n);

		assertTrue(ds.contains(n));

		Category c = new Category();
		c.setName("testikategoria");
		ds.update(c);
		assertTrue(ds.contains(c));

		c.addNominee(n);
		assertTrue(n.getCategories().contains(c));

		ds.deleteNominee(n);
		assertFalse(ds.contains(n));

		ds.deleteCategory(c);
		assertFalse(ds.contains(c));
	}

	@Test
	public void testCategoryRemoval() {

		Nominee n = new Nominee();
		n.setNomineeName("Hello worldadas");

		ds.update(n);

		assertTrue(ds.contains(n));

		Category c = new Category();
		c.setName("testikategoriaasda");
		ds.update(c);
		assertTrue(ds.contains(c));

		c.addNominee(n);
		assertTrue(n.getCategories().contains(c));

		ds.deleteCategory(c);
		assertFalse(ds.contains(c));

		ds.deleteNominee(n);
		assertFalse(ds.contains(n));

	}

	@Test
	public void testNomineeListing() {

		int startingNomineeCount = ds.getAllNominees().size();

		Nominee n = new Nominee();
		n.setNomineeName("Hello worldadas");

		ds.update(n);

		Nominee n2 = new Nominee();
		n2.setNomineeName("Hello worldadasdasdas");

		ds.update(n2);

		assertTrue(ds.getAllNominees().size() == startingNomineeCount + 2);

		ds.deleteNominee(n);
		ds.deleteNominee(n2);

		assertTrue(ds.getAllNominees().size() == startingNomineeCount);

	}

	@Test
	public void testCategoryListing() {

		int startingCount = ds.getAllCategories().size();

		Category c = new Category();
		c.setName("namee");

		ds.update(c);

		Category c2 = new Category();

		c2.setName("dalkkdjaslk");
		ds.update(c2);

		List<Category> allCategories = ds.getAllCategories();
		System.out.println(allCategories);
		assertTrue(allCategories.size() == startingCount + 2);

		ds.deleteCategory(c);
		ds.deleteCategory(c2);

		assertTrue(ds.getAllCategories().size() == startingCount);

	}

}
