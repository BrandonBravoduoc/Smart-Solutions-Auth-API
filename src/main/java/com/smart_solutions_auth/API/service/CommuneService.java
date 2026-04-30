package com.smart_solutions_auth.API.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.dto.address.CommuneDTO;
import com.smart_solutions_auth.API.model.Commune;
import com.smart_solutions_auth.API.model.Region;
import com.smart_solutions_auth.API.repository.CommuneRepository;
import com.smart_solutions_auth.API.repository.RegionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CommuneService {

	private final CommuneRepository communeRepository;
	private final RegionRepository regionRepository;

	public CommuneService(CommuneRepository communeRepository, RegionRepository regionRepository) {
		this.communeRepository = communeRepository;
		this.regionRepository = regionRepository;
	}

	public List<CommuneDTO.Response> findAll() {
		return communeRepository.findAll().stream()
			.map(c -> new CommuneDTO.Response(
				c.getId(),
				c.getCommuneName(),
				c.getRegion() != null ? c.getRegion().getId() : null,
				c.getRegion() != null ? c.getRegion().getRegionName() : null
			))
			.collect(Collectors.toList());
	}

	public CommuneDTO.Response findById(Long id) {
		Commune c = communeRepository.findById(id).orElseThrow(() -> new RuntimeException("Commune not found"));
		return new CommuneDTO.Response(
			c.getId(),
			c.getCommuneName(),
			c.getRegion() != null ? c.getRegion().getId() : null,
			c.getRegion() != null ? c.getRegion().getRegionName() : null
		);
	}

	public CommuneDTO.Response create(CommuneDTO.CreateRequest dto) {
		Region region = regionRepository.findById(dto.regionId()).orElseThrow(() -> new RuntimeException("Region not found"));
		Commune commune = new Commune();
		commune.setCommuneName(dto.communeName());
		commune.setRegion(region);
		Commune saved = communeRepository.save(commune);
		return new CommuneDTO.Response(
			saved.getId(),
			saved.getCommuneName(),
			saved.getRegion() != null ? saved.getRegion().getId() : null,
			saved.getRegion() != null ? saved.getRegion().getRegionName() : null
		);
	}

	public CommuneDTO.Response update(CommuneDTO.UpdateRequest dto) {
		Commune commune = communeRepository.findById(dto.id()).orElseThrow(() -> new RuntimeException("Commune not found"));
		Region region = regionRepository.findById(dto.regionId()).orElseThrow(() -> new RuntimeException("Region not found"));
		commune.setCommuneName(dto.communeName());
		commune.setRegion(region);
		Commune saved = communeRepository.save(commune);
		return new CommuneDTO.Response(
			saved.getId(),
			saved.getCommuneName(),
			saved.getRegion() != null ? saved.getRegion().getId() : null,
			saved.getRegion() != null ? saved.getRegion().getRegionName() : null
		);
	}

	public void delete(Long id) {
		communeRepository.deleteById(id);
	}
}
