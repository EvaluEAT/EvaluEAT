package com.evalueat.evalueat.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.criterion.Example;

import com.evalueat.evalueat.domain.Food;

public class FoodRepositoryImpl implements SearchableFoodRepository {
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * Opens native Hibernate session which supports searchByExample - method.
	 */
	@Override
	public List<Food> searchByExample(Food food) {
		Session session = entityManager
				.getEntityManagerFactory()
				.unwrap(SessionFactory.class)
				.openSession();
		
        Example foodExample = Example.create(food).excludeZeroes();
        Criteria criteria = session.createCriteria(Food.class).add(foodExample);        
        return criteria.list();
	}
}
