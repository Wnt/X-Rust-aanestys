package org.x_rust.aanestys.backend;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.x_rust.aanestys.backend.data.Category;
import org.x_rust.aanestys.backend.data.Nominee;

@Stateless
public class DataService {

	private static Logger logger = Logger
			.getLogger(DataService.class.getName());


	@PersistenceContext(unitName = "voting-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	public DataService() {
		logger.info("Building DataService");
	}

	public void deleteNominee(Nominee n) {

		for (Category c : n.getCategories()) {
			c.getNominees().remove(n);
		}
		em.remove(n);

	}

	public void deleteCategory(Category c) {

		// remove all Nominee links to clear the linking table
		c.getNominees().removeAll(c.getNominees());

		em.remove(c);
	}

	public void update(Object n) {
		if (em.contains(n)) {
			em.merge(n);
		} else {
			em.persist(n);
		}
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
