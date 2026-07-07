package com.smart_solutions_auth.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.api.dto.address.AddressDTO;
import com.smart_solutions_auth.api.model.cache.AddressCache;
import com.smart_solutions_auth.api.model.cache.CommuneCache;
import com.smart_solutions_auth.api.model.cache.RegionCache;
import com.smart_solutions_auth.api.model.entity.Address;
import com.smart_solutions_auth.api.model.entity.Commune;
import com.smart_solutions_auth.api.model.entity.UserContact;
import com.smart_solutions_auth.api.repository.AddressRepository;
import com.smart_solutions_auth.api.repository.CommuneRepository;
import com.smart_solutions_auth.api.repository.UserContactRepository;
import com.smart_solutions_auth.api.util.Validations;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressService {

	@Autowired
	private  AddressRepository addressRepository;

	@Autowired
	private  CommuneRepository communeRepository;

	@Autowired
	private UserContactRepository userContactRepository;

	@Autowired
	private Validations validations;



    
	@Cacheable(value = "addresses")
	public List<AddressCache> findAll() {
        return addressRepository.findAll().stream()
            .map(this::mapToCache)
            .collect(Collectors.toList());
    }

	@Cacheable(value = "addresses", key = "#id")
	public AddressCache findById(Long id) {
        Address a = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dirección no encontrada."));
        return mapToCache(a); 
    }

	@CacheEvict(value = {"addresses"}, allEntries = true)
	public AddressDTO.Response create(AddressDTO.CreateRequest dto) {
		Commune commune = communeRepository.findById(dto.communeId())
			.orElseThrow(() -> new RuntimeException("Comuna no encontrada."));
		Address a = new Address();
		a.setSucursalName(dto.sucursalName());
		a.setStreet(dto.street());
		a.setNumber(dto.number());
		a.setCommune(commune);
		Address saved = addressRepository.save(a);
		
		return new AddressDTO.Response(
			a.getId(),
			a.getSucursalName(),
			saved.getStreet(), 
			saved.getNumber(), 
			saved.getCommune() != null ? 
			saved.getCommune().getId() : null);
	}

	@CacheEvict(value = {"addresses"}, allEntries = true)
	public AddressDTO.Response update(AddressDTO.UpdateRequest dto) {
		Address a = addressRepository.findById(dto.id())
			.orElseThrow(() -> new RuntimeException("Dirección no encontrada."));

		Commune commune = communeRepository.findById(dto.communeId())
			.orElseThrow(() -> new RuntimeException("Comuna no encontrada."));
		
			a.setSucursalName(dto.sucursalName());
		a.setStreet(dto.street());
		a.setNumber(dto.number());
		a.setCommune(commune);
		Address saved = addressRepository.save(a);
		
		return new AddressDTO.Response(
			a.getId(),
			a.getSucursalName(),
			saved.getStreet(), 
			saved.getNumber(), 
			saved.getCommune() != null ? 
			saved.getCommune().getId() : null);
	}

	@CacheEvict(value = {"addresses"}, allEntries = true)
	public void delete(Long id) {
		List<UserContact> associated = userContactRepository.findByUserAddressId(id);
		validations.assertNoUsersAssociated(associated, "sucursal");

		addressRepository.deleteById(id);
	}

	private AddressCache mapToCache(Address a) {
        CommuneCache communeCache = null;
        if (a.getCommune() != null) {
            RegionCache regionCache = null;
            if (a.getCommune().getRegion() != null) {
                regionCache = new RegionCache(
					a.getCommune().getRegion().getId(),
					a.getCommune().getRegion().getRegionName(),
					a.getCommune().getRegion().isActive());
            }
            communeCache = new CommuneCache(
				a.getCommune().getId(),
				a.getCommune().getCommuneName(),
				regionCache,
				a.getCommune().isActive());
        }
        
        return new AddressCache(
			a.getId(), 
			a.getSucursalName(), 
			a.getStreet(), 
			a.getNumber(), 
			communeCache);
    }
}
