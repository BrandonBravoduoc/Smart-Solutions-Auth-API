package com.smart_solutions_auth.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.api.dto.address.RegionDTO;
import com.smart_solutions_auth.api.model.entity.Region;
import com.smart_solutions_auth.api.repository.RegionRepository;
import com.smart_solutions_auth.api.util.Validations;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {

	@Autowired
	private  RegionRepository regionRepository;

	@Autowired
	private  Validations validations;

	
	@Cacheable(value = "regions")
	public List<RegionDTO.Response> findAll() {
		return regionRepository.findAll().stream()
			.map(r -> new RegionDTO.Response(
				r.getId(), 
				r.getRegionName()))
			.collect(Collectors.toList());
	}

	@Cacheable(value = "regions", key = "#id")
	public RegionDTO.Response findById(Long id) {
		Region r = validations.requireRegion(id);
		
		return new RegionDTO.Response(
			r.getId(), 
			r.getRegionName());
	}

	@CacheEvict(value = {"regions"}, allEntries = true)
	public RegionDTO.Response create(RegionDTO.CreateRequest dto) {
		Region region = new Region();
		region.setRegionName(dto.regionName());
		Region saved = regionRepository.save(region);
		
		return new RegionDTO.Response(
			saved.getId(), 
			saved.getRegionName());
	}

	@CacheEvict(value = {"regions"}, allEntries = true)
	public RegionDTO.Response update(RegionDTO.UpdateRequest dto) {
		Region region = validations.requireRegion(dto.id());
		region.setRegionName(dto.regionName());
		Region saved = regionRepository.save(region);
		
		return new RegionDTO.Response(saved.getId(), saved.getRegionName());
	}

	@CacheEvict(value = {"regions"}, allEntries = true)
	public void delete(Long id) {
		regionRepository.deleteById(id);
	}

	
}
