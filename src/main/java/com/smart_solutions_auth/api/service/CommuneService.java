package com.smart_solutions_auth.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.api.dto.address.CommuneDTO;
import com.smart_solutions_auth.api.model.cache.CommuneCache;
import com.smart_solutions_auth.api.model.cache.RegionCache;
import com.smart_solutions_auth.api.model.entity.Commune;
import com.smart_solutions_auth.api.model.entity.Region;
import com.smart_solutions_auth.api.model.entity.UserContact;
import com.smart_solutions_auth.api.repository.CommuneRepository;
import com.smart_solutions_auth.api.repository.UserContactRepository;
import com.smart_solutions_auth.api.util.Validations;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CommuneService {

	@Autowired
	private  CommuneRepository communeRepository;

	@Autowired
	private UserContactRepository userContactRepository;

	@Autowired
	private  Validations validations;


	@Cacheable(value = "communes")
	public List<CommuneCache> findAll() {
        return communeRepository.findAll().stream()
            .map(this::mapToCache)
            .collect(Collectors.toList());
    }

	@Cacheable(value = "communes", key = "#id")
	public CommuneCache findById(Long id) {
        Commune c = validations.requireCommune(id);
        return mapToCache(c);
    }

	@CacheEvict(value = {"communes"}, allEntries = true)
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
			saved.getRegion() != null ? saved.getRegion().getRegionName() : null,
			saved.isActive()
		);
	}

	@CacheEvict(value = {"communes", "addresses"}, allEntries = true)
	public CommuneCache setActive(Long id, boolean active) {
		Commune commune = validations.requireCommune(id);

		if (!active) {
			List<UserContact> associated = userContactRepository.findByUserAddress_Commune_Id(id);
			validations.assertNoUsersAssociated(associated, "comuna");
		}

		commune.setActive(active);
		Commune saved = communeRepository.save(commune);

		return mapToCache(saved);
	}

	private CommuneCache mapToCache(Commune c) {
        RegionCache regionCache = null;
        if (c.getRegion() != null) {
            regionCache = new RegionCache(
				c.getRegion().getId(),
				c.getRegion().getRegionName(),
				c.getRegion().isActive());
        }
        return new CommuneCache(c.getId(), c.getCommuneName(), regionCache, c.isActive());
    }

}
