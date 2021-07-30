package com.seeme.domain.weather;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClothesRepository extends CrudRepository<Clothes, Long> {
	List<Clothes> findAllByTempAndAgeAndCategory(int temp, int age, String category);
}
