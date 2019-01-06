package com.evalueat.evalueat.domain;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;

public class CommentSpecification implements Specification<Comment> {

	private static final long serialVersionUID = -1457948212559405411L;
	private CommentSearchModel filter;
	
	public CommentSpecification(CommentSearchModel filter) {
		this.filter = filter;
	}	
	
	@Override
	public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		final List<Predicate> predicates = new LinkedList<Predicate>();
		
		Path<Integer> parentId = root.get("parentId");
		
		if(filter.getParentId() != null) {
			predicates.add(criteriaBuilder.equal(parentId, filter.getParentId()));
		}
		
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));			
	}
}
