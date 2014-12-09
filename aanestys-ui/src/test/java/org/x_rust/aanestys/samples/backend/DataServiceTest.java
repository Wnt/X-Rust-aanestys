package org.x_rust.aanestys.samples.backend;

import org.junit.Before;
import org.junit.Test;
import org.x_rust.aanestys.samples.backend.DataService;
import org.x_rust.aanestys.samples.backend.data.Nominee;
import org.x_rust.aanestys.samples.backend.mock.MockDataService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Simple unit test for the back-end data service.
 */
public class DataServiceTest {

    private DataService service;

    @Before
    public void setUp() throws Exception {
        service = MockDataService.getInstance();
    }

    @Test
    public void testDataServiceCanFetchProducts() throws Exception {
        assertFalse(service.getAllProducts().isEmpty());
    }

    @Test
    public void testDataServiceCanFetchCategories() throws Exception {
        assertFalse(service.getAllCategories().isEmpty());
    }

    @Test
    public void testUpdateProduct_updatesTheProduct() throws Exception {
        Nominee p = service.getAllProducts().iterator().next();
        p.setNomineeName("My Test Name");
        service.updateProduct(p);
        Nominee p2 = service.getAllProducts().iterator().next();
        assertEquals("My Test Name", p2.getNomineeName());
    }
}
