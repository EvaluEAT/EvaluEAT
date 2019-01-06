package com.evalueat.evalueat.repository;

import com.evalueat.evalueat.domain.FoodOfPlace;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FoodOfPlace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodOfPlaceRepository extends JpaRepository<FoodOfPlace, Long> {

}
