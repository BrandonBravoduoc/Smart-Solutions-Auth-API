package com.smart_solutions_auth.api.controller.address;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_solutions_auth.api.dto.address.RegionDTO;
import com.smart_solutions_auth.api.service.RegionService;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public List<RegionDTO.Response> getAll() {

        return regionService.findAll();
    }

    @GetMapping("/{id}")
    public RegionDTO.Response getById(@PathVariable Long id) {

        return regionService.findById(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public RegionDTO.Response update(@PathVariable Long id, @RequestBody RegionDTO.CreateRequest dto) {
        RegionDTO.UpdateRequest ur = new RegionDTO.UpdateRequest(id, dto.regionName());
        return regionService.update(ur);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public RegionDTO.Response setActive(@PathVariable Long id, @RequestBody RegionDTO.StatusRequest dto) {
        return regionService.setActive(id, dto.active());
    }

}
