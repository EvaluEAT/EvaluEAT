package com.evalueat.evalueat.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.criterion.Example;

import com.evalueat.evalueat.domain.Comment;

public class CommentRepositoryImpl implements SearchableCommentRepository {
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * Opens native Hibernate session which supports searchByExample - method.
	 */
	@Override
	public List<Comment> searchByExample(Comment comment) {
		Session session = entityManager
				.getEntityManagerFactory()
				.unwrap(SessionFactory.class)
				.openSession();
		
        Example commentExample = Example.create(comment).excludeZeroes();
        Criteria criteria = session.createCriteria(Comment.class).add(commentExample);        
        return criteria.list();
	}
}
