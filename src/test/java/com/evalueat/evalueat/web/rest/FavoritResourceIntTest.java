package com.evalueat.evalueat.web.rest;

import com.evalueat.evalueat.EvaluEatApp;

import com.evalueat.evalueat.domain.Favorit;
import com.evalueat.evalueat.repository.FavoritRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.evalueat.evalueat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FavoritResource REST controller.
 *
 * @see FavoritResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EvaluEatApp.class)
public class FavoritResourceIntTest {

    private static final String DEFAULT_EVALUATABLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVALUATABLE_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_EVALUATABLE_ID = 1L;
    private static final Long UPDATED_EVALUATABLE_ID = 2L;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FavoritRepository favoritRepository;

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

    private MockMvc restFavoritMockMvc;

    private Favorit favorit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FavoritResource favoritResource = new FavoritResource(favoritRepository);
        this.restFavoritMockMvc = MockMvcBuilders.standaloneSetup(favoritResource)
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
    public static Favorit createEntity(EntityManager em) {
        Favorit favorit = new Favorit()
            .evaluatableType(DEFAULT_EVALUATABLE_TYPE)
            .evaluatableId(DEFAULT_EVALUATABLE_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return favorit;
    }

    @Before
    public void initTest() {
        favorit = createEntity(em);
    }

    @Test
    @Transactional
    public void createFavorit() throws Exception {
        int databaseSizeBeforeCreate = favoritRepository.findAll().size();

        // Create the Favorit
        restFavoritMockMvc.perform(post("/api/favorits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favorit)))
            .andExpect(status().isCreated());

        // Validate the Favorit in the database
        List<Favorit> favoritList = favoritRepository.findAll();
        assertThat(favoritList).hasSize(databaseSizeBeforeCreate + 1);
        Favorit testFavorit = favoritList.get(favoritList.size() - 1);
        assertThat(testFavorit.getEvaluatableType()).isEqualTo(DEFAULT_EVALUATABLE_TYPE);
        assertThat(testFavorit.getEvaluatableId()).isEqualTo(DEFAULT_EVALUATABLE_ID);
        assertThat(testFavorit.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testFavorit.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testFavorit.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testFavorit.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    @Transactional
    public void createFavoritWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = favoritRepository.findAll().size();

        // Create the Favorit with an existing ID
        favorit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavoritMockMvc.perform(post("/api/favorits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favorit)))
            .andExpect(status().isBadRequest());

        // Validate the Favorit in the database
        List<Favorit> favoritList = favoritRepository.findAll();
        assertThat(favoritList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFavorits() throws Exception {
        // Initialize the database
        favoritRepository.saveAndFlush(favorit);

        // Get all the favoritList
        restFavoritMockMvc.perform(get("/api/favorits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favorit.getId().intValue())))
            .andExpect(jsonPath("$.[*].evaluatableType").value(hasItem(DEFAULT_EVALUATABLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].evaluatableId").value(hasItem(DEFAULT_EVALUATABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getFavorit() throws Exception {
        // Initialize the database
        favoritRepository.saveAndFlush(favorit);

        // Get the favorit
        restFavoritMockMvc.perform(get("/api/favorits/{id}", favorit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(favorit.getId().intValue()))
            .andExpect(jsonPath("$.evaluatableType").value(DEFAULT_EVALUATABLE_TYPE.toString()))
            .andExpect(jsonPath("$.evaluatableId").value(DEFAULT_EVALUATABLE_ID.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFavorit() throws Exception {
        // Get the favorit
        restFavoritMockMvc.perform(get("/api/favorits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavorit() throws Exception {
        // Initialize the database
        favoritRepository.saveAndFlush(favorit);

        int databaseSizeBeforeUpdate = favoritRepository.findAll().size();

        // Update the favorit
        Favorit updatedFavorit = favoritRepository.findById(favorit.getId()).get();
        // Disconnect from session so that the updates on updatedFavorit are not directly saved in db
        em.detach(updatedFavorit);
        updatedFavorit
            .evaluatableType(UPDATED_EVALUATABLE_TYPE)
            .evaluatableId(UPDATED_EVALUATABLE_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        restFavoritMockMvc.perform(put("/api/favorits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFavorit)))
            .andExpect(status().isOk());

        // Validate the Favorit in the database
        List<Favorit> favoritList = favoritRepository.findAll();
        assertThat(favoritList).hasSize(databaseSizeBeforeUpdate);
        Favorit testFavorit = favoritList.get(favoritList.size() - 1);
        assertThat(testFavorit.getEvaluatableType()).isEqualTo(UPDATED_EVALUATABLE_TYPE);
        assertThat(testFavorit.getEvaluatableId()).isEqualTo(UPDATED_EVALUATABLE_ID);
        assertThat(testFavorit.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFavorit.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFavorit.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testFavorit.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingFavorit() throws Exception {
        int databaseSizeBeforeUpdate = favoritRepository.findAll().size();

        // Create the Favorit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoritMockMvc.perform(put("/api/favorits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favorit)))
            .andExpect(status().isBadRequest());

        // Validate the Favorit in the database
        List<Favorit> favoritList = favoritRepository.findAll();
        assertThat(favoritList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFavorit() throws Exception {
        // Initialize the database
        favoritRepository.saveAndFlush(favorit);

        int databaseSizeBeforeDelete = favoritRepository.findAll().size();

        // Get the favorit
        restFavoritMockMvc.perform(delete("/api/favorits/{id}", favorit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Favorit> favoritList = favoritRepository.findAll();
        assertThat(favoritList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Favorit.class);
        Favorit favorit1 = new Favorit();
        favorit1.setId(1L);
        Favorit favorit2 = new Favorit();
        favorit2.setId(favorit1.getId());
        assertThat(favorit1).isEqualTo(favorit2);
        favorit2.setId(2L);
        assertThat(favorit1).isNotEqualTo(favorit2);
        favorit1.setId(null);
        assertThat(favorit1).isNotEqualTo(favorit2);
    }
}
