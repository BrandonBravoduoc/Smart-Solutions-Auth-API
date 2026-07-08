package com.smart_solutions_auth.api.controller.address;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_solutions_auth.api.dto.address.CommuneDTO;
import com.smart_solutions_auth.api.service.CommuneService;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import com.smart_solutions_auth.api.model.cache.CommuneCache;

@RestController
@RequestMapping("/api/v1/communes")
public class CommuneController {

    private final CommuneService communeService;

    public CommuneController(CommuneService communeService) {

        this.communeService = communeService;
    }

    @GetMapping
    public List<CommuneCache> getAll() {

        return communeService.findAll();
    }

    @GetMapping("/{id}")
    public CommuneCache getById(@PathVariable Long id) {

        return communeService.findById(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public CommuneDTO.Response update(@PathVariable Long id, @RequestBody @Valid CommuneDTO.CreateRequest dto) {

        CommuneDTO.UpdateRequest ur = new CommuneDTO.UpdateRequest(id, dto.communeName(), dto.regionId());
        return communeService.update(ur);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public CommuneCache setActive(@PathVariable Long id, @RequestBody CommuneDTO.StatusRequest dto) {
        return communeService.setActive(id, dto.active());
    }
}
