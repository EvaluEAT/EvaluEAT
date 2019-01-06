package com.evalueat.evalueat.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service("specificationBuilder")
public class JpaSpecificationBuilder {
	public <T> Specification<T> toSpecification(Map<String, Object> filter, Class<T> type) {
		return new Specification<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				for(Map.Entry<String, Object> filterElement : filter.entrySet()) {
					if(filterElement.getValue() instanceof String) {
						addStringCriteria(filterElement, root, query, cb);
					} else if(type.isAssignableFrom(filterElement.getValue().getClass())) {
						addEntityCriteria(filterElement, root, query, cb, type);
					}
				}
				return null;
			}			
		};
	}

	protected <T> void addEntityCriteria(Entry<String, Object> filterElement, Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder cb, Class<T> type) {
		// TODO Auto-generated method stub
		
	}

	public void addStringCriteria(Entry<String, Object> filterElement, Root<?> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		
	}
}
