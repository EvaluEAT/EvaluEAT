package com.evalueat.evalueat.repository;

import com.evalueat.evalueat.domain.Food;
import java.util.List;

public interface SearchableFoodRepository {
	public List<Food> searchByExample(Food food);
}
