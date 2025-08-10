package com.monsalon.monSalonBackend.controllers;

import com.monsalon.monSalonBackend.Dto.servicesCreation.ServiceCreateDto;
import com.monsalon.monSalonBackend.Dto.servicesCreation.ServiceReadDto;
import com.monsalon.monSalonBackend.Services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    /**
     * Creates a new service for the authenticated salon.
     * POST /api/v1/services
     */
    @PostMapping
    public ResponseEntity<ServiceReadDto> createService(@Valid @RequestBody ServiceCreateDto serviceCreateDto) {
        ServiceReadDto createdService = servicesService.createService(serviceCreateDto);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    /**
     * Retrieves all services for the authenticated salon.
     * GET /api/v1/services
     */
    @GetMapping
    public ResponseEntity<List<ServiceReadDto>> getAllServices() {
        List<ServiceReadDto> services = servicesService.getAllServices();
        return ResponseEntity.ok(services);
    }

    /**
     * Retrieves a single service by ID for the authenticated salon.
     * GET /api/v1/services/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceReadDto> getServiceById(@PathVariable Long id) {
        ServiceReadDto service = servicesService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    /**
     * Updates an existing service for the authenticated salon.
     * PUT /api/v1/services/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceReadDto> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceCreateDto serviceUpdateDto) {
        ServiceReadDto updatedService = servicesService.updateService(id, serviceUpdateDto);
        return ResponseEntity.ok(updatedService);
    }

    /**
     * Deletes a service by ID for the authenticated salon.
     * DELETE /api/v1/services/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteService(@PathVariable Long id) {
        boolean isDeleted = servicesService.deleteService(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
        } else {
            // If the service wasn't found or couldn't be deleted due to other reasons
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND); // Or HttpStatus.CONFLICT if due to foreign keys
        }
    }
}