package com.evalueat.evalueat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evalueat.evalueat.domain.Comment;
import com.evalueat.evalueat.repository.CommentRepository;
import com.evalueat.evalueat.web.rest.errors.BadRequestAlertException;
import com.evalueat.evalueat.web.rest.util.HeaderUtil;
import com.hazelcast.map.impl.operation.GetEntryViewOperation;

import io.github.jhipster.web.util.ResponseUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;

/**
 * REST controller for managing Comment.
 */
@RestController
@RequestMapping("/api")
public class CommentResource {

    private final Logger log = LoggerFactory.getLogger(CommentResource.class);

    private static final String ENTITY_NAME = "evaluEatComment";

    private final CommentRepository commentRepository;

    public CommentResource(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * POST  /comments : Create a new comment.
     *
     * @param comment the comment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new comment, or with status 400 (Bad Request) if the comment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comments")
    @Timed
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) throws URISyntaxException {
        log.debug("REST request to save Comment : {}", comment);
        if (comment.getId() != null) {
            throw new BadRequestAlertException("A new comment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Comment result = commentRepository.save(comment);
        return ResponseEntity.created(new URI("/api/comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * POST  /comments/search : Create a new comment.
     *
     * @param example the example for searching by.
     * @return the ResponseEntity with status 201 (Created) and the list of matched entities, or with status 404 (Not found) if there is no matched entity.
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comments/search")
    @Timed
    public List<Comment> searchComments(@RequestBody Comment example) throws URISyntaxException {
        log.debug("REST request to search Comments : {}", example);
        
        return commentRepository.searchByExample(example);
    }

    /**
     * PUT  /comments : Updates an existing comment.
     *
     * @param comment the comment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated comment,
     * or with status 400 (Bad Request) if the comment is not valid,
     * or with status 500 (Internal Server Error) if the comment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comments")
    @Timed
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) throws URISyntaxException {
        log.debug("REST request to update Comment : {}", comment);
        if (comment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Comment result = commentRepository.save(comment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, comment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comments : get all the comments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of comments in body
     */
    @GetMapping("/comments")
    @Timed
    public List<Comment> getAllComments() {
        log.debug("REST request to get all Comments");
        return commentRepository.findAll();
    }

    /**
     * GET  /comments/:id : get the "id" comment.
     *
     * @param id the id of the comment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the comment, or with status 404 (Not Found)
     */
    @GetMapping("/comments/{id}")
    @Timed
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        log.debug("REST request to get Comment : {}", id);
        Optional<Comment> comment = commentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(comment);
    }

    /**
     * DELETE  /comments/:id : delete the "id" comment.
     *
     * @param id the id of the comment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comments/{id}")
    @Timed
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.debug("REST request to delete Comment : {}", id);

        commentRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
