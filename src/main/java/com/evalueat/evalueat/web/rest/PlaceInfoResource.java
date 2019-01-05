package com.evalueat.evalueat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evalueat.evalueat.domain.PlaceInfo;
import com.evalueat.evalueat.repository.PlaceInfoRepository;
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
 * REST controller for managing PlaceInfo.
 */
@RestController
@RequestMapping("/api")
public class PlaceInfoResource {

    private final Logger log = LoggerFactory.getLogger(PlaceInfoResource.class);

    private static final String ENTITY_NAME = "evaluEatPlaceInfo";

    private final PlaceInfoRepository placeInfoRepository;

    public PlaceInfoResource(PlaceInfoRepository placeInfoRepository) {
        this.placeInfoRepository = placeInfoRepository;
    }

    /**
     * POST  /place-infos : Create a new placeInfo.
     *
     * @param placeInfo the placeInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new placeInfo, or with status 400 (Bad Request) if the placeInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/place-infos")
    @Timed
    public ResponseEntity<PlaceInfo> createPlaceInfo(@RequestBody PlaceInfo placeInfo) throws URISyntaxException {
        log.debug("REST request to save PlaceInfo : {}", placeInfo);
        if (placeInfo.getId() != null) {
            throw new BadRequestAlertException("A new placeInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlaceInfo result = placeInfoRepository.save(placeInfo);
        return ResponseEntity.created(new URI("/api/place-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /place-infos : Updates an existing placeInfo.
     *
     * @param placeInfo the placeInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated placeInfo,
     * or with status 400 (Bad Request) if the placeInfo is not valid,
     * or with status 500 (Internal Server Error) if the placeInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/place-infos")
    @Timed
    public ResponseEntity<PlaceInfo> updatePlaceInfo(@RequestBody PlaceInfo placeInfo) throws URISyntaxException {
        log.debug("REST request to update PlaceInfo : {}", placeInfo);
        if (placeInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlaceInfo result = placeInfoRepository.save(placeInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, placeInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /place-infos : get all the placeInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of placeInfos in body
     */
    @GetMapping("/place-infos")
    @Timed
    public List<PlaceInfo> getAllPlaceInfos() {
        log.debug("REST request to get all PlaceInfos");
        return placeInfoRepository.findAll();
    }

    /**
     * GET  /place-infos/:id : get the "id" placeInfo.
     *
     * @param id the id of the placeInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the placeInfo, or with status 404 (Not Found)
     */
    @GetMapping("/place-infos/{id}")
    @Timed
    public ResponseEntity<PlaceInfo> getPlaceInfo(@PathVariable Long id) {
        log.debug("REST request to get PlaceInfo : {}", id);
        Optional<PlaceInfo> placeInfo = placeInfoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(placeInfo);
    }

    /**
     * DELETE  /place-infos/:id : delete the "id" placeInfo.
     *
     * @param id the id of the placeInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/place-infos/{id}")
    @Timed
    public ResponseEntity<Void> deletePlaceInfo(@PathVariable Long id) {
        log.debug("REST request to delete PlaceInfo : {}", id);

        placeInfoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
