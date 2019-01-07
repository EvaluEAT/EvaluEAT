package com.evalueat.evalueat.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.criterion.Example;

import com.evalueat.evalueat.domain.PlaceInfo;

public class PlaceInfoRepositoryImpl implements SearchablePlaceInfoRepository {
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * Opens native Hibernate session which supports searchByExample - method.
	 */
	@Override
	public List<PlaceInfo> searchByExample(PlaceInfo placeInfo) {
		Session session = entityManager
				.getEntityManagerFactory()
				.unwrap(SessionFactory.class)
				.openSession();
		
        Example placeInfoExample = Example.create(placeInfo).excludeZeroes();
        Criteria criteria = session.createCriteria(PlaceInfo.class).add(placeInfoExample);        
        return criteria.list();
	}
}
