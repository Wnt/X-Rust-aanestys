package org.x_rust.aanestys.samples.backend;

import java.util.Collection;

import org.x_rust.aanestys.samples.backend.data.Category;
import org.x_rust.aanestys.samples.backend.data.Nominee;
import org.x_rust.aanestys.samples.backend.mock.MockDataService;

/**
 * Back-end service interface for retrieving and updating product data.
 */
public abstract class DataService {

    public abstract Collection<Nominee> getAllProducts();

    public abstract Collection<Category> getAllCategories();

    public abstract void updateProduct(Nominee p);

    public abstract void deleteProduct(int productId);

    public abstract Nominee getProductById(int productId);

    public static DataService get() {
        return MockDataService.getInstance();
    }

}
