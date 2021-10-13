package com.shoppr.shipping;

import org.springframework.data.repository.CrudRepository;

import com.shoppr.common.entity.Country;
import com.shoppr.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {
	
	public ShippingRate findByCountryAndState(Country country, String state);
}
