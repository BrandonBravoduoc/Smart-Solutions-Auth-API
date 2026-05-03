package com.smart_solutions_auth.API.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.dto.address.AddressDTO;
import com.smart_solutions_auth.API.model.Address;
import com.smart_solutions_auth.API.model.Commune;
import com.smart_solutions_auth.API.repository.AddressRepository;
import com.smart_solutions_auth.API.repository.CommuneRepository;
import com.smart_solutions_auth.API.util.Validations;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressService {

	private final AddressRepository addressRepository;
	private final CommuneRepository communeRepository;
	private final Validations validations;

	public AddressService(AddressRepository addressRepository, CommuneRepository communeRepository, Validations validations) {

		this.addressRepository = addressRepository;
		this.communeRepository = communeRepository;
		this.validations = validations;
	}

    

	public List<AddressDTO.Response> findAll() {
		return addressRepository.findAll().stream()
			.map(a -> new AddressDTO.Response(
				a.getStreet(), 
				a.getNumber(), 
				a.getCommune() != null ? 
				a.getCommune().getId() : null))
			.collect(Collectors.toList());
	}

	public AddressDTO.Response findById(Long id) {
		Address a = validations.requireAddress(id);
		
		return new AddressDTO.Response(
			a.getStreet(), 
			a.getNumber(), 
			a.getCommune() != null ? 
			a.getCommune().getId() : null);
	}

	public AddressDTO.Response create(AddressDTO.CreateRequest dto) {
		Commune commune = validations.requireCommune(dto.communeId());
		Address a = new Address();
		
		a.setStreet(dto.street());
		a.setNumber(dto.number());
		a.setCommune(commune);
		Address saved = addressRepository.save(a);
		
		return new AddressDTO.Response(
			saved.getStreet(), 
			saved.getNumber(), 
			saved.getCommune() != null ? 
			saved.getCommune().getId() : null);
	}

	public AddressDTO.Response update(AddressDTO.UpdateRequest dto) {
		Address a = validations.requireAddress(dto.id());

		Commune commune = validations.requireCommune(dto.communeId());
		
		a.setStreet(dto.street());
		a.setNumber(dto.number());
		a.setCommune(commune);
		Address saved = addressRepository.save(a);
		
		return new AddressDTO.Response(
			saved.getStreet(), 
			saved.getNumber(), 
			saved.getCommune() != null ? 
			saved.getCommune().getId() : null);
	}

	public void delete(Long id) {

		addressRepository.deleteById(id);
	}
}
