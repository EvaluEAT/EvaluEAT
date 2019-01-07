package com.evalueat.evalueat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evalueat.evalueat.domain.Favorit;
import com.evalueat.evalueat.domain.Food;
import com.evalueat.evalueat.repository.FoodRepository;
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
 * REST controller for managing Food.
 */
@RestController
@RequestMapping("/api")
public class FoodResource {

    private final Logger log = LoggerFactory.getLogger(FoodResource.class);

    private static final String ENTITY_NAME = "evaluEatFood";

    private final FoodRepository foodRepository;

    public FoodResource(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    /**
     * POST  /foods : Create a new food.
     *
     * @param food the food to create
     * @return the ResponseEntity with status 201 (Created) and with body the new food, or with status 400 (Bad Request) if the food has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/foods")
    @Timed
    public ResponseEntity<Food> createFood(@RequestBody Food food) throws URISyntaxException {
        log.debug("REST request to save Food : {}", food);
        if (food.getId() != null) {
            throw new BadRequestAlertException("A new food cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Food result = foodRepository.save(food);
        return ResponseEntity.created(new URI("/api/foods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * POST  /foods/search : Searching for foods
     *
     * @param example the example for searching by.
     * @return the ResponseEntity with status 201 (Created) and the list of matched entities, or with status 404 (Not found) if there is no matched entity.
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/foods/search")
    @Timed
    public List<Food> searchFoods(@RequestBody Food example) throws URISyntaxException {
        log.debug("REST request to search Foods : {}", example);
        
        return foodRepository.searchByExample(example);
    }

    /**
     * PUT  /foods : Updates an existing food.
     *
     * @param food the food to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated food,
     * or with status 400 (Bad Request) if the food is not valid,
     * or with status 500 (Internal Server Error) if the food couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/foods")
    @Timed
    public ResponseEntity<Food> updateFood(@RequestBody Food food) throws URISyntaxException {
        log.debug("REST request to update Food : {}", food);
        if (food.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Food result = foodRepository.save(food);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, food.getId().toString()))
            .body(result);
    }

    /**
     * GET  /foods : get all the foods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of foods in body
     */
    @GetMapping("/foods")
    @Timed
    public List<Food> getAllFoods() {
        log.debug("REST request to get all Foods");
        return foodRepository.findAll();
    }

    /**
     * GET  /foods/:id : get the "id" food.
     *
     * @param id the id of the food to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the food, or with status 404 (Not Found)
     */
    @GetMapping("/foods/{id}")
    @Timed
    public ResponseEntity<Food> getFood(@PathVariable Long id) {
        log.debug("REST request to get Food : {}", id);
        Optional<Food> food = foodRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(food);
    }

    /**
     * DELETE  /foods/:id : delete the "id" food.
     *
     * @param id the id of the food to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/foods/{id}")
    @Timed
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        log.debug("REST request to delete Food : {}", id);

        foodRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
