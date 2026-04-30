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
import jakarta.validation.Valid;

import com.smart_solutions_auth.API.dto.address.CommuneDTO;
import com.smart_solutions_auth.API.model.Commune;
import com.smart_solutions_auth.API.service.CommuneService;

@RestController
@RequestMapping("/api/communes")
public class CommuneController {

    private final CommuneService communeService;

    public CommuneController(CommuneService communeService) {
        this.communeService = communeService;
    }

    @GetMapping
    public List<CommuneDTO.Response> getAll() {
        return communeService.findAll();
    }

    @GetMapping("/{id}")
    public CommuneDTO.Response getById(@PathVariable Long id) {
        return communeService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CommuneDTO.Response> create(@RequestBody @Valid CommuneDTO.CreateRequest dto) {
        CommuneDTO.Response created = communeService.create(dto);
        return ResponseEntity.created(URI.create("/api/communes/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public CommuneDTO.Response update(@PathVariable Long id, @RequestBody @Valid CommuneDTO.CreateRequest dto) {
        CommuneDTO.UpdateRequest ur = new CommuneDTO.UpdateRequest(id, dto.communeName(), dto.regionId());
        return communeService.update(ur);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        communeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
