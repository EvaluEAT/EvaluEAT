package com.evalueat.evalueat.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.criterion.Example;

import com.evalueat.evalueat.domain.Place;

public class PlaceRepositoryImpl implements SearchablePlaceRepository {
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * Opens native Hibernate session which supports searchByExample - method.
	 */
	@Override
	public List<Place> searchByExample(Place place) {
		Session session = entityManager
				.getEntityManagerFactory()
				.unwrap(SessionFactory.class)
				.openSession();
		
        Example placeExample = Example.create(place).excludeZeroes();
        Criteria criteria = session.createCriteria(Place.class).add(placeExample);        
        return criteria.list();
	}
}
