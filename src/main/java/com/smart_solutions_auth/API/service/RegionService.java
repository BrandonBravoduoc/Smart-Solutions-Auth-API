package com.smart_solutions_auth.API.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.dto.address.RegionDTO;
import com.smart_solutions_auth.API.model.Region;
import com.smart_solutions_auth.API.repository.RegionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {

	private final RegionRepository regionRepository;

	public RegionService(RegionRepository regionRepository) {
		this.regionRepository = regionRepository;
	}

	public List<RegionDTO.Response> findAll() {
		return regionRepository.findAll().stream()
			.map(r -> new RegionDTO.Response(r.getId(), r.getRegionName()))
			.collect(Collectors.toList());
	}

	public RegionDTO.Response findById(Long id) {
		Region r = regionRepository.findById(id).orElseThrow(() -> new RuntimeException("Region not found"));
		
		return new RegionDTO.Response(r.getId(), r.getRegionName());
	}

	public RegionDTO.Response create(RegionDTO.CreateRequest dto) {
		Region region = new Region();
		region.setRegionName(dto.regionName());
		Region saved = regionRepository.save(region);
		
		return new RegionDTO.Response(saved.getId(), saved.getRegionName());
	}

	public RegionDTO.Response update(RegionDTO.UpdateRequest dto) {
		Region region = regionRepository.findById(dto.id()).orElseThrow(() -> new RuntimeException("Region not found"));
		region.setRegionName(dto.regionName());
		Region saved = regionRepository.save(region);
		
		return new RegionDTO.Response(saved.getId(), saved.getRegionName());
	}

	public void delete(Long id) {
		regionRepository.deleteById(id);
	}

}
