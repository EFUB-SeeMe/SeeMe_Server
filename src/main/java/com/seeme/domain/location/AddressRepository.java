package com.seeme.domain.location;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, Long> {
	List<Address> findAllByBjdong(String bjdong);
}
