package com.evalueat.evalueat.web.rest;

import com.evalueat.evalueat.EvaluEatApp;

import com.evalueat.evalueat.domain.PlaceInfo;
import com.evalueat.evalueat.repository.PlaceInfoRepository;
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
 * Test class for the PlaceInfoResource REST controller.
 *
 * @see PlaceInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EvaluEatApp.class)
public class PlaceInfoResourceIntTest {

    private static final Long DEFAULT_PLACE_ID = 1L;
    private static final Long UPDATED_PLACE_ID = 2L;

    private static final Long DEFAULT_TYPE = 1L;
    private static final Long UPDATED_TYPE = 2L;

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PlaceInfoRepository placeInfoRepository;

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

    private MockMvc restPlaceInfoMockMvc;

    private PlaceInfo placeInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlaceInfoResource placeInfoResource = new PlaceInfoResource(placeInfoRepository);
        this.restPlaceInfoMockMvc = MockMvcBuilders.standaloneSetup(placeInfoResource)
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
    public static PlaceInfo createEntity(EntityManager em) {
        PlaceInfo placeInfo = new PlaceInfo()
            .placeId(DEFAULT_PLACE_ID)
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return placeInfo;
    }

    @Before
    public void initTest() {
        placeInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlaceInfo() throws Exception {
        int databaseSizeBeforeCreate = placeInfoRepository.findAll().size();

        // Create the PlaceInfo
        restPlaceInfoMockMvc.perform(post("/api/place-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeInfo)))
            .andExpect(status().isCreated());

        // Validate the PlaceInfo in the database
        List<PlaceInfo> placeInfoList = placeInfoRepository.findAll();
        assertThat(placeInfoList).hasSize(databaseSizeBeforeCreate + 1);
        PlaceInfo testPlaceInfo = placeInfoList.get(placeInfoList.size() - 1);
        assertThat(testPlaceInfo.getPlaceId()).isEqualTo(DEFAULT_PLACE_ID);
        assertThat(testPlaceInfo.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPlaceInfo.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testPlaceInfo.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPlaceInfo.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPlaceInfo.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testPlaceInfo.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    @Transactional
    public void createPlaceInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = placeInfoRepository.findAll().size();

        // Create the PlaceInfo with an existing ID
        placeInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceInfoMockMvc.perform(post("/api/place-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeInfo)))
            .andExpect(status().isBadRequest());

        // Validate the PlaceInfo in the database
        List<PlaceInfo> placeInfoList = placeInfoRepository.findAll();
        assertThat(placeInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPlaceInfos() throws Exception {
        // Initialize the database
        placeInfoRepository.saveAndFlush(placeInfo);

        // Get all the placeInfoList
        restPlaceInfoMockMvc.perform(get("/api/place-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(placeInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].placeId").value(hasItem(DEFAULT_PLACE_ID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getPlaceInfo() throws Exception {
        // Initialize the database
        placeInfoRepository.saveAndFlush(placeInfo);

        // Get the placeInfo
        restPlaceInfoMockMvc.perform(get("/api/place-infos/{id}", placeInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(placeInfo.getId().intValue()))
            .andExpect(jsonPath("$.placeId").value(DEFAULT_PLACE_ID.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlaceInfo() throws Exception {
        // Get the placeInfo
        restPlaceInfoMockMvc.perform(get("/api/place-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlaceInfo() throws Exception {
        // Initialize the database
        placeInfoRepository.saveAndFlush(placeInfo);

        int databaseSizeBeforeUpdate = placeInfoRepository.findAll().size();

        // Update the placeInfo
        PlaceInfo updatedPlaceInfo = placeInfoRepository.findById(placeInfo.getId()).get();
        // Disconnect from session so that the updates on updatedPlaceInfo are not directly saved in db
        em.detach(updatedPlaceInfo);
        updatedPlaceInfo
            .placeId(UPDATED_PLACE_ID)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        restPlaceInfoMockMvc.perform(put("/api/place-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlaceInfo)))
            .andExpect(status().isOk());

        // Validate the PlaceInfo in the database
        List<PlaceInfo> placeInfoList = placeInfoRepository.findAll();
        assertThat(placeInfoList).hasSize(databaseSizeBeforeUpdate);
        PlaceInfo testPlaceInfo = placeInfoList.get(placeInfoList.size() - 1);
        assertThat(testPlaceInfo.getPlaceId()).isEqualTo(UPDATED_PLACE_ID);
        assertThat(testPlaceInfo.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPlaceInfo.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testPlaceInfo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPlaceInfo.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPlaceInfo.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testPlaceInfo.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingPlaceInfo() throws Exception {
        int databaseSizeBeforeUpdate = placeInfoRepository.findAll().size();

        // Create the PlaceInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceInfoMockMvc.perform(put("/api/place-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeInfo)))
            .andExpect(status().isBadRequest());

        // Validate the PlaceInfo in the database
        List<PlaceInfo> placeInfoList = placeInfoRepository.findAll();
        assertThat(placeInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlaceInfo() throws Exception {
        // Initialize the database
        placeInfoRepository.saveAndFlush(placeInfo);

        int databaseSizeBeforeDelete = placeInfoRepository.findAll().size();

        // Get the placeInfo
        restPlaceInfoMockMvc.perform(delete("/api/place-infos/{id}", placeInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PlaceInfo> placeInfoList = placeInfoRepository.findAll();
        assertThat(placeInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaceInfo.class);
        PlaceInfo placeInfo1 = new PlaceInfo();
        placeInfo1.setId(1L);
        PlaceInfo placeInfo2 = new PlaceInfo();
        placeInfo2.setId(placeInfo1.getId());
        assertThat(placeInfo1).isEqualTo(placeInfo2);
        placeInfo2.setId(2L);
        assertThat(placeInfo1).isNotEqualTo(placeInfo2);
        placeInfo1.setId(null);
        assertThat(placeInfo1).isNotEqualTo(placeInfo2);
    }
}
