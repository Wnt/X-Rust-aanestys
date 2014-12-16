package org.x_rust.aanestys.samples.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.x_rust.aanestys.backend.data.Category;
import org.x_rust.aanestys.backend.data.Nominee;
import org.x_rust.aanestys.samples.backend.jpa.DataService;

/**
 * Simple unit test for the back-end data service.
 */
public class DataServiceTest {

    private DataService service;

    @Before
    public void setUp() throws Exception {
        service = DataService.getInstance();
        Nominee n = new Nominee();
        n.setNomineeName("no");
        service.update(n);
        
        Category c = new Category();
        c.setName("dasd");
        service.update(c);
    }

    @Test
    public void testDataServiceCanFetchNominees() throws Exception {
        assertFalse(service.getAllNominees().isEmpty());
    }

    @Test
    public void testDataServiceCanFetchCategories() throws Exception {
        assertFalse(service.getAllCategories().isEmpty());
    }

    @Test
    public void testUpdateNominee() throws Exception {
        Nominee n = service.getAllNominees().iterator().next();
        n.setNomineeName("My Test Name");
        service.update(n);
        Nominee p2 = service.getAllNominees().iterator().next();
        assertEquals("My Test Name", p2.getNomineeName());
    }
    
    @After
    public void teardown() {
    	for (Nominee nominee : service.getAllNominees()) {
			service.deleteNominee(nominee);
		}
    	for (Category category : service.getAllCategories()) {
			service.deleteCategory(category);
		}
    }
}
