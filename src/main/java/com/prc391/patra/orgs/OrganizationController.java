package com.prc391.patra.orgs;

import com.prc391.patra.exceptions.EntityNotFoundException;
import com.prc391.patra.orgs.requests.CreateOrganizationRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v0/organization")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final ModelMapper mapper;

    @Autowired
    public OrganizationController(OrganizationService organizationService, ModelMapper mapper) {
        this.organizationService = organizationService;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganization(
            @PathVariable("id") String id
    ) throws EntityNotFoundException {
        return ResponseEntity.ok(organizationService.getOrganization(id));
    }

    @PostMapping
    public ResponseEntity<Organization> insertOrganization(
            @RequestBody CreateOrganizationRequest newOrg) {
        return ResponseEntity.ok(organizationService.insertOrganization(mapper.map(newOrg,Organization.class)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(
            @PathVariable("id") String id,
            @RequestBody CreateOrganizationRequest updateOrg) throws EntityNotFoundException {
        return ResponseEntity.ok(organizationService.updateOrganization(id, mapper.map(updateOrg, Organization.class)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrganization(
            @PathVariable("id") String id
    ) throws EntityNotFoundException {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok().build();
    }
}