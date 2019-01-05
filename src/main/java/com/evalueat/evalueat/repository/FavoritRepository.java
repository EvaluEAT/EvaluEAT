package com.evalueat.evalueat.repository;

import com.evalueat.evalueat.domain.Favorit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Favorit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoritRepository extends JpaRepository<Favorit, Long> {

}
