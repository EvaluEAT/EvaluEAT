package com.evalueat.evalueat.repository;

import com.evalueat.evalueat.domain.Comment;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, SearchableCommentRepository {

}