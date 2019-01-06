package com.evalueat.evalueat.web.rest;

import com.evalueat.evalueat.EvaluEatApp;

import com.evalueat.evalueat.domain.FoodOfPlace;
import com.evalueat.evalueat.repository.FoodOfPlaceRepository;
import com.evalueat.evalueat.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.evalueat.evalueat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FoodOfPlaceResource REST controller.
 *
 * @see FoodOfPlaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EvaluEatApp.class)
public class FoodOfPlaceResourceIntTest {

    @Autowired
    private FoodOfPlaceRepository foodOfPlaceRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFoodOfPlaceMockMvc;

    private FoodOfPlace foodOfPlace;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FoodOfPlaceResource foodOfPlaceResource = new FoodOfPlaceResource(foodOfPlaceRepository);
        this.restFoodOfPlaceMockMvc = MockMvcBuilders.standaloneSetup(foodOfPlaceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodOfPlace createEntity(EntityManager em) {
        FoodOfPlace foodOfPlace = new FoodOfPlace();
        return foodOfPlace;
    }

    @Before
    public void initTest() {
        foodOfPlace = createEntity(em);
    }

    @Test
    @Transactional
    public void createFoodOfPlace() throws Exception {
        int databaseSizeBeforeCreate = foodOfPlaceRepository.findAll().size();

        // Create the FoodOfPlace
        restFoodOfPlaceMockMvc.perform(post("/api/food-of-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodOfPlace)))
            .andExpect(status().isCreated());

        // Validate the FoodOfPlace in the database
        List<FoodOfPlace> foodOfPlaceList = foodOfPlaceRepository.findAll();
        assertThat(foodOfPlaceList).hasSize(databaseSizeBeforeCreate + 1);
        FoodOfPlace testFoodOfPlace = foodOfPlaceList.get(foodOfPlaceList.size() - 1);
    }

    @Test
    @Transactional
    public void createFoodOfPlaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = foodOfPlaceRepository.findAll().size();

        // Create the FoodOfPlace with an existing ID
        foodOfPlace.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodOfPlaceMockMvc.perform(post("/api/food-of-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodOfPlace)))
            .andExpect(status().isBadRequest());

        // Validate the FoodOfPlace in the database
        List<FoodOfPlace> foodOfPlaceList = foodOfPlaceRepository.findAll();
        assertThat(foodOfPlaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFoodOfPlaces() throws Exception {
        // Initialize the database
        foodOfPlaceRepository.saveAndFlush(foodOfPlace);

        // Get all the foodOfPlaceList
        restFoodOfPlaceMockMvc.perform(get("/api/food-of-places?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodOfPlace.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getFoodOfPlace() throws Exception {
        // Initialize the database
        foodOfPlaceRepository.saveAndFlush(foodOfPlace);

        // Get the foodOfPlace
        restFoodOfPlaceMockMvc.perform(get("/api/food-of-places/{id}", foodOfPlace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(foodOfPlace.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFoodOfPlace() throws Exception {
        // Get the foodOfPlace
        restFoodOfPlaceMockMvc.perform(get("/api/food-of-places/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFoodOfPlace() throws Exception {
        // Initialize the database
        foodOfPlaceRepository.saveAndFlush(foodOfPlace);

        int databaseSizeBeforeUpdate = foodOfPlaceRepository.findAll().size();

        // Update the foodOfPlace
        FoodOfPlace updatedFoodOfPlace = foodOfPlaceRepository.findById(foodOfPlace.getId()).get();
        // Disconnect from session so that the updates on updatedFoodOfPlace are not directly saved in db
        em.detach(updatedFoodOfPlace);

        restFoodOfPlaceMockMvc.perform(put("/api/food-of-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFoodOfPlace)))
            .andExpect(status().isOk());

        // Validate the FoodOfPlace in the database
        List<FoodOfPlace> foodOfPlaceList = foodOfPlaceRepository.findAll();
        assertThat(foodOfPlaceList).hasSize(databaseSizeBeforeUpdate);
        FoodOfPlace testFoodOfPlace = foodOfPlaceList.get(foodOfPlaceList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingFoodOfPlace() throws Exception {
        int databaseSizeBeforeUpdate = foodOfPlaceRepository.findAll().size();

        // Create the FoodOfPlace

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodOfPlaceMockMvc.perform(put("/api/food-of-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodOfPlace)))
            .andExpect(status().isBadRequest());

        // Validate the FoodOfPlace in the database
        List<FoodOfPlace> foodOfPlaceList = foodOfPlaceRepository.findAll();
        assertThat(foodOfPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFoodOfPlace() throws Exception {
        // Initialize the database
        foodOfPlaceRepository.saveAndFlush(foodOfPlace);

        int databaseSizeBeforeDelete = foodOfPlaceRepository.findAll().size();

        // Get the foodOfPlace
        restFoodOfPlaceMockMvc.perform(delete("/api/food-of-places/{id}", foodOfPlace.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FoodOfPlace> foodOfPlaceList = foodOfPlaceRepository.findAll();
        assertThat(foodOfPlaceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodOfPlace.class);
        FoodOfPlace foodOfPlace1 = new FoodOfPlace();
        foodOfPlace1.setId(1L);
        FoodOfPlace foodOfPlace2 = new FoodOfPlace();
        foodOfPlace2.setId(foodOfPlace1.getId());
        assertThat(foodOfPlace1).isEqualTo(foodOfPlace2);
        foodOfPlace2.setId(2L);
        assertThat(foodOfPlace1).isNotEqualTo(foodOfPlace2);
        foodOfPlace1.setId(null);
        assertThat(foodOfPlace1).isNotEqualTo(foodOfPlace2);
    }
}
