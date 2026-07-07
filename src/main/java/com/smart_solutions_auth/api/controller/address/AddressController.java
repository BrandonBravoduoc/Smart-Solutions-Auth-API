package com.smart_solutions_auth.api.controller.address;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_solutions_auth.api.dto.address.AddressDTO;
import com.smart_solutions_auth.api.model.cache.AddressCache;
import com.smart_solutions_auth.api.service.AddressService;


@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public List<AddressCache> getAll() {
        return addressService.findAll();
    }


    @GetMapping("/{id}")

    public AddressCache getById(@PathVariable Long id) {
        return addressService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE')")
    public ResponseEntity<AddressDTO.Response> create(@RequestBody AddressDTO.CreateRequest dto) {
        AddressDTO.Response created = addressService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public AddressDTO.Response update(@PathVariable Long id, @RequestBody AddressDTO.CreateRequest dto) {
        AddressDTO.UpdateRequest ur = new AddressDTO.UpdateRequest(
            id, 
            dto.sucursalName(),
            dto.street(), 
            dto.number(), 
            dto.communeId());
        return addressService.update(ur);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
