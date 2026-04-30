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

import com.smart_solutions_auth.API.dto.address.RegionDTO;
import com.smart_solutions_auth.API.model.Region;
import com.smart_solutions_auth.API.service.RegionService;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<RegionDTO.Response> getAll() {
        return regionService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public RegionDTO.Response getById(@PathVariable Long id) {
        return regionService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RegionDTO.Response> create(@RequestBody RegionDTO.CreateRequest dto) {
        RegionDTO.Response created = regionService.create(dto);
        return ResponseEntity.created(URI.create("/api/regions/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public RegionDTO.Response update(@PathVariable Long id, @RequestBody RegionDTO.CreateRequest dto) {
        RegionDTO.UpdateRequest ur = new RegionDTO.UpdateRequest(id, dto.regionName());
        return regionService.update(ur);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        regionService.delete(id);
        return ResponseEntity.noContent().build();
    }

   
}
