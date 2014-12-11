package org.x_rust.aanestys.samples.backend.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.x_rust.aanestys.samples.backend.data.Category;
import org.x_rust.aanestys.samples.backend.data.Nominee;

public class MockDataGenerator {
	private static int nextCategoryId = 1;
	private static int nextProductId = 1;
	private static final Random random = new Random(1);
	private static final String categoryNames[] = new String[] {
			"Vuoden roudari", "Vuoden puristi", "Vuoden kulttuurunedistäjä",
			"Vuoden tulokas" };

	private static String[] word1 = new String[] { "Kalle", "Maria", "Marjut",
			"Katri", "Jonni", "Jani", "Sirpa", "Sami" };

	private static String[] word2 = new String[] { "Karvanen", "Mäkelä",
			"Mikola", "Lehto", "Nakari", "Savola", "Fälden", "Mäkelä" };

	static List<Category> createCategories() {
		List<Category> categories = new ArrayList<Category>();
		for (String name : categoryNames) {
			Category c = createCategory(name);
			categories.add(c);
		}
		return categories;

	}

	static List<Nominee> createProducts(List<Category> categories) {
		List<Nominee> products = new ArrayList<Nominee>();
		for (int i = 0; i < 100; i++) {
			Nominee p = createProduct(categories);
			products.add(p);
		}

		return products;
	}

	private static Category createCategory(String name) {
		Category c = new Category();
		c.setId(nextCategoryId++);
		c.setName(name);
		return c;
	}

	private static Nominee createProduct(List<Category> categories) {
		Nominee p = new Nominee();
		p.setId(nextProductId++);
		p.setNomineeName(generateNomineeName());

		p.setPrice(new BigDecimal((random.nextInt(250) + 50) / 10.0));

		p.setCategory(getCategory(categories, 1, 2));
		return p;
	}

	private static Set<Category> getCategory(List<Category> categories,
			int min, int max) {
		int nr = random.nextInt(max) + min;
		HashSet<Category> productCategories = new HashSet<Category>();
		for (int i = 0; i < nr; i++) {
			productCategories.add(categories.get(random.nextInt(categories
					.size())));
		}

		return productCategories;
	}

	private static String generateNomineeName() {
		return word1[random.nextInt(word1.length)] + " "
				+ word2[random.nextInt(word2.length)];
	}

}
