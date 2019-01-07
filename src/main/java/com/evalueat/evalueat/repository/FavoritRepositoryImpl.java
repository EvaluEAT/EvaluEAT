package com.evalueat.evalueat.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.criterion.Example;

import com.evalueat.evalueat.domain.Favorit;

public class FavoritRepositoryImpl implements SearchableFavoritRepository {
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * Opens native Hibernate session which supports searchByExample - method.
	 */
	@Override
	public List<Favorit> searchByExample(Favorit favorit) {
		Session session = entityManager
				.getEntityManagerFactory()
				.unwrap(SessionFactory.class)
				.openSession();
		
        Example favoritExample = Example.create(favorit).excludeZeroes();
        Criteria criteria = session.createCriteria(Favorit.class).add(favoritExample);        
        return criteria.list();
	}
}
