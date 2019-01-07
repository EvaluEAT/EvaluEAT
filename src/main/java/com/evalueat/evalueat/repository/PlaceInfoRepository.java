package com.evalueat.evalueat.repository;

import com.evalueat.evalueat.domain.PlaceInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlaceInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaceInfoRepository extends JpaRepository<PlaceInfo, Long>, SearchablePlaceInfoRepository {

}
