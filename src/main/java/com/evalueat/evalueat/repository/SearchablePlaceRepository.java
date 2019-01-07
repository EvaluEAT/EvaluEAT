package com.evalueat.evalueat.repository;

import com.evalueat.evalueat.domain.Place;
import java.util.List;

public interface SearchablePlaceRepository {
	public List<Place> searchByExample(Place place);
}
