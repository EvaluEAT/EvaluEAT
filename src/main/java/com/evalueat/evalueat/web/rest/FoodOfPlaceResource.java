package com.evalueat.evalueat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evalueat.evalueat.domain.FoodOfPlace;
import com.evalueat.evalueat.repository.FoodOfPlaceRepository;
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
 * REST controller for managing FoodOfPlace.
 */
@RestController
@RequestMapping("/api")
public class FoodOfPlaceResource {

    private final Logger log = LoggerFactory.getLogger(FoodOfPlaceResource.class);

    private static final String ENTITY_NAME = "evaluEatFoodOfPlace";

    private final FoodOfPlaceRepository foodOfPlaceRepository;

    public FoodOfPlaceResource(FoodOfPlaceRepository foodOfPlaceRepository) {
        this.foodOfPlaceRepository = foodOfPlaceRepository;
    }

    /**
     * POST  /food-of-places : Create a new foodOfPlace.
     *
     * @param foodOfPlace the foodOfPlace to create
     * @return the ResponseEntity with status 201 (Created) and with body the new foodOfPlace, or with status 400 (Bad Request) if the foodOfPlace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/food-of-places")
    @Timed
    public ResponseEntity<FoodOfPlace> createFoodOfPlace(@RequestBody FoodOfPlace foodOfPlace) throws URISyntaxException {
        log.debug("REST request to save FoodOfPlace : {}", foodOfPlace);
        if (foodOfPlace.getId() != null) {
            throw new BadRequestAlertException("A new foodOfPlace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FoodOfPlace result = foodOfPlaceRepository.save(foodOfPlace);
        return ResponseEntity.created(new URI("/api/food-of-places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /food-of-places : Updates an existing foodOfPlace.
     *
     * @param foodOfPlace the foodOfPlace to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated foodOfPlace,
     * or with status 400 (Bad Request) if the foodOfPlace is not valid,
     * or with status 500 (Internal Server Error) if the foodOfPlace couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/food-of-places")
    @Timed
    public ResponseEntity<FoodOfPlace> updateFoodOfPlace(@RequestBody FoodOfPlace foodOfPlace) throws URISyntaxException {
        log.debug("REST request to update FoodOfPlace : {}", foodOfPlace);
        if (foodOfPlace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FoodOfPlace result = foodOfPlaceRepository.save(foodOfPlace);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, foodOfPlace.getId().toString()))
            .body(result);
    }

    /**
     * GET  /food-of-places : get all the foodOfPlaces.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of foodOfPlaces in body
     */
    @GetMapping("/food-of-places")
    @Timed
    public List<FoodOfPlace> getAllFoodOfPlaces() {
        log.debug("REST request to get all FoodOfPlaces");
        return foodOfPlaceRepository.findAll();
    }

    /**
     * GET  /food-of-places/:id : get the "id" foodOfPlace.
     *
     * @param id the id of the foodOfPlace to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the foodOfPlace, or with status 404 (Not Found)
     */
    @GetMapping("/food-of-places/{id}")
    @Timed
    public ResponseEntity<FoodOfPlace> getFoodOfPlace(@PathVariable Long id) {
        log.debug("REST request to get FoodOfPlace : {}", id);
        Optional<FoodOfPlace> foodOfPlace = foodOfPlaceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(foodOfPlace);
    }

    /**
     * DELETE  /food-of-places/:id : delete the "id" foodOfPlace.
     *
     * @param id the id of the foodOfPlace to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/food-of-places/{id}")
    @Timed
    public ResponseEntity<Void> deleteFoodOfPlace(@PathVariable Long id) {
        log.debug("REST request to delete FoodOfPlace : {}", id);

        foodOfPlaceRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
