package com.evalueat.evalueat.repository;

import com.evalueat.evalueat.domain.PlaceInfo;
import java.util.List;

public interface SearchablePlaceInfoRepository {
	public List<PlaceInfo> searchByExample(PlaceInfo placeInfo);
}
