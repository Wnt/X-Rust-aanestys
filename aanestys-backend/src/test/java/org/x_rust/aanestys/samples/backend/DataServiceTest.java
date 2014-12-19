package org.x_rust.aanestys.samples.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.x_rust.aanestys.backend.DataService;
import org.x_rust.aanestys.backend.data.Category;
import org.x_rust.aanestys.backend.data.Nominee;

/**
 * Simple unit test for the back-end data ds.
 */
public class DataServiceTest {

    private DataService ds;

    @Before
    public void setUp() throws Exception {
        final Properties p = new Properties();
        p.put("testDatabase", "new://Resource?type=DataSource");
        p.put("testDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("testDatabase.JdbcUrl", "jdbc:hsqldb:mem:testdb");
        
        final Context context = EJBContainer.createEJBContainer(p).getContext();

        try {
			ds = (DataService) context.lookup("java:global/aanestys-backend/DataService");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Nominee n = new Nominee();
        n.setNomineeName("no");
        ds.update(n);
        
        Category c = new Category();
        c.setName("dasd");
        ds.update(c);
    }

    @Test
    public void testDataServiceCanFetchNominees() throws Exception {
        assertFalse(ds.getAllNominees().isEmpty());
    }

    @Test
    public void testDataServiceCanFetchCategories() throws Exception {
        assertFalse(ds.getAllCategories().isEmpty());
    }

    @Test
    public void testUpdateNominee() throws Exception {
        Nominee n = ds.getAllNominees().iterator().next();
        n.setNomineeName("My Test Name");
        ds.update(n);
        Nominee p2 = ds.getAllNominees().iterator().next();
        assertEquals("My Test Name", p2.getNomineeName());
    }
    
    @After
    public void teardown() {
    	for (Nominee nominee : ds.getAllNominees()) {
			ds.deleteNominee(nominee);
		}
    	for (Category category : ds.getAllCategories()) {
			ds.deleteCategory(category);
		}
    }
}
