package com.evalueat.evalueat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evalueat.evalueat.domain.Comment;
import com.evalueat.evalueat.domain.Favorit;
import com.evalueat.evalueat.repository.FavoritRepository;
import com.evalueat.evalueat.web.rest.errors.BadRequestAlertException;
import com.evalueat.evalueat.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Favorit.
 */
@RestController
@RequestMapping("/api")
public class FavoritResource {

    private final Logger log = LoggerFactory.getLogger(FavoritResource.class);

    private static final String ENTITY_NAME = "evaluEatFavorit";

    private final FavoritRepository favoritRepository;

    public FavoritResource(FavoritRepository favoritRepository) {
        this.favoritRepository = favoritRepository;
    }

    /**
     * POST  /favorits : Create a new favorit.
     *
     * @param favorit the favorit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new favorit, or with status 400 (Bad Request) if the favorit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/favorits")
    @Timed
    public ResponseEntity<Favorit> createFavorit(@RequestBody Favorit favorit) throws URISyntaxException {
        log.debug("REST request to save Favorit : {}", favorit);
        if (favorit.getId() != null) {
            throw new BadRequestAlertException("A new favorit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Favorit result = favoritRepository.save(favorit);
        return ResponseEntity.created(new URI("/api/favorits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * POST  /favorits/search : Create a new favorit.
     *
     * @param example the example for searching by.
     * @return the ResponseEntity with status 201 (Created) and the list of matched entities, or with status 404 (Not found) if there is no matched entity.
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/favorits/search")
    @Timed
    public List<Favorit> searchFavorits(@RequestBody Favorit example) throws URISyntaxException {
        log.debug("REST request to search Favorits : {}", example);
        
        return favoritRepository.searchByExample(example);
    }

    /**
     * PUT  /favorits : Updates an existing favorit.
     *
     * @param favorit the favorit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated favorit,
     * or with status 400 (Bad Request) if the favorit is not valid,
     * or with status 500 (Internal Server Error) if the favorit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/favorits")
    @Timed
    public ResponseEntity<Favorit> updateFavorit(@RequestBody Favorit favorit) throws URISyntaxException {
        log.debug("REST request to update Favorit : {}", favorit);
        if (favorit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Favorit result = favoritRepository.save(favorit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, favorit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favorits : get all the favorits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of favorits in body
     */
    @GetMapping("/favorits")
    @Timed
    public List<Favorit> getAllFavorits() {
        log.debug("REST request to get all Favorits");
        return favoritRepository.findAll();
    }

    /**
     * GET  /favorits/:id : get the "id" favorit.
     *
     * @param id the id of the favorit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the favorit, or with status 404 (Not Found)
     */
    @GetMapping("/favorits/{id}")
    @Timed
    public ResponseEntity<Favorit> getFavorit(@PathVariable Long id) {
        log.debug("REST request to get Favorit : {}", id);
        Optional<Favorit> favorit = favoritRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(favorit);
    }

    /**
     * DELETE  /favorits/:id : delete the "id" favorit.
     *
     * @param id the id of the favorit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/favorits/{id}")
    @Timed
    public ResponseEntity<Void> deleteFavorit(@PathVariable Long id) {
        log.debug("REST request to delete Favorit : {}", id);

        favoritRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
