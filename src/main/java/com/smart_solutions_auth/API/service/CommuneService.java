package com.smart_solutions_auth.API.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.dto.address.CommuneDTO;
import com.smart_solutions_auth.API.model.Commune;
import com.smart_solutions_auth.API.model.Region;
import com.smart_solutions_auth.API.repository.CommuneRepository;
import com.smart_solutions_auth.API.repository.RegionRepository;
import com.smart_solutions_auth.API.util.Validations;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CommuneService {

	private final CommuneRepository communeRepository;
	private final RegionRepository regionRepository;
	private final Validations validations;

	public CommuneService(CommuneRepository communeRepository, RegionRepository regionRepository, Validations validations) {
		this.communeRepository = communeRepository;
		this.regionRepository = regionRepository;
		this.validations = validations;
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
		Commune c = validations.requireCommune(id);
		
		return new CommuneDTO.Response(
			c.getId(),
			c.getCommuneName(),
			c.getRegion() != null ? c.getRegion().getId() : null,
			c.getRegion() != null ? c.getRegion().getRegionName() : null
		);
	}

	public CommuneDTO.Response create(CommuneDTO.CreateRequest dto) {
		Region region = validations.requireRegion(dto.regionId());
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
		Commune commune = validations.requireCommune(dto.id());

		Region region = validations.requireRegion(dto.regionId());
		
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
