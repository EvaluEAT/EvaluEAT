package com.evalueat.evalueat.repository;

import com.evalueat.evalueat.domain.Favorit;
import java.util.List;

public interface SearchableFavoritRepository {
	public List<Favorit> searchByExample(Favorit favorit);
}
