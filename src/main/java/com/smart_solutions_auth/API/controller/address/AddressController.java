package com.smart_solutions_auth.API.controller.address;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.smart_solutions_auth.API.dto.address.AddressDTO;
import com.smart_solutions_auth.API.model.Address;
import com.smart_solutions_auth.API.service.AddressService;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<AddressDTO.Response> getAll() {
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public AddressDTO.Response getById(@PathVariable Long id) {
        return addressService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<AddressDTO.Response> create(@RequestBody AddressDTO.CreateRequest dto) {
        AddressDTO.Response created = addressService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public AddressDTO.Response update(@PathVariable Long id, @RequestBody AddressDTO.CreateRequest dto) {
        AddressDTO.UpdateRequest ur = new AddressDTO.UpdateRequest(id, dto.street(), dto.number(), dto.communeId());
        return addressService.update(ur);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
