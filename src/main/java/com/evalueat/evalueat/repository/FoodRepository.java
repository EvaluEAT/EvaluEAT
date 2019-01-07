package com.evalueat.evalueat.repository;

import com.evalueat.evalueat.domain.Food;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Food entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodRepository extends JpaRepository<Food, Long>, SearchableFoodRepository {

}
