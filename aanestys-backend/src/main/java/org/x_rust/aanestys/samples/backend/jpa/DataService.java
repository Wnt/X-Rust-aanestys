package org.x_rust.aanestys.samples.backend.jpa;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.x_rust.aanestys.backend.data.Category;
import org.x_rust.aanestys.backend.data.Nominee;

public class DataService {

	private static DataService INSTANCE;
	private static Logger logger = Logger
			.getLogger(DataService.class.getName());

	private EntityManagerFactory emFactory;

	private EntityManager em;

	private Connection connection;

	public synchronized static DataService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataService();
		}
		return INSTANCE;
	}

	private DataService() {

		logger.info("Building JPA EntityManager");
		emFactory = Persistence.createEntityManagerFactory("testPU");
		em = emFactory.createEntityManager();
	}

	public void deleteNominee(Nominee n) {
		em.getTransaction().begin();

		for (Category c : n.getCategories()) {
			c.getNominees().remove(n);
		}
		em.remove(n);

		em.getTransaction().commit();
	}

	public void deleteCategory(Category c) {
		em.getTransaction().begin();

		// remove all Nominee links to clear the linking table
		c.getNominees().removeAll(c.getNominees());

		em.remove(c);
		em.getTransaction().commit();
	}

	public void close() {
		logger.info("Shuting down JPA layer.");
		if (em != null) {
			em.close();
		}
		if (emFactory != null) {
			emFactory.close();
		}
	}

	public void update(Object n) {
		em.getTransaction().begin();
		if (em.contains(n)) {
			em.merge(n);
		} else {
			em.persist(n);
		}
		em.getTransaction().commit();

	}

	public boolean contains(Object n) {
		return em.contains(n);
	}

	public List<Nominee> getAllNominees() {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Object> cq = cb.createQuery();
		Root<Nominee> e = cq.from(Nominee.class);
		Query query = em.createQuery(cq);
		return query.getResultList();
	}

	public List<Category> getAllCategories() {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Object> cq = cb.createQuery();
		Root<Category> e = cq.from(Category.class);
		Query query = em.createQuery(cq);
		return query.getResultList();
	}

	public Nominee getNomineeById(int id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Object> cq = cb.createQuery();
		Root<Nominee> e = cq.from(Nominee.class);
		cq.where(cb.equal(e.get("id"), cb.parameter(Long.class, "id")));
		Query query = em.createQuery(cq);
		query.setParameter("id", id);
		return (Nominee) query.getSingleResult();
	}
}
