package com.monsalon.monSalonBackend.Services;

import com.monsalon.monSalonBackend.Dto.servicesCreation.ServiceCreateDto;
import com.monsalon.monSalonBackend.Dto.servicesCreation.ServiceReadDto;
import com.monsalon.monSalonBackend.exceptions.ResourceNotFoundException; // Assuming this exception
import com.monsalon.monSalonBackend.mappers.Mapper;
import com.monsalon.monSalonBackend.models.Salon;
import com.monsalon.monSalonBackend.models.Services;
import com.monsalon.monSalonBackend.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicesService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AuthService authService; // To get the current user's salon

    /**
     * Creates a new service for the authenticated salon.
     *
     * @param serviceCreateDto The DTO containing the details for the new service.
     * @return A ServiceReadDto representing the newly created service.
     */
    @Transactional
    public ServiceReadDto createService(ServiceCreateDto serviceCreateDto) {
        try {
            Salon salon = authService.getCurrentUser().getSalon();
            Services service = Mapper.fromServiceCreateDtoToService(serviceCreateDto);
            service.setSalon(salon);
            Services savedService = serviceRepository.save(service);
            return Mapper.fromServiceToServiceReadDto(savedService);
        } catch (Exception e) {
            System.err.println("Error creating service: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create service.", e);
        }
    }

    /**
     * Retrieves all services for the currently authenticated salon.
     *
     * @return A list of ServiceReadDto.
     */
    @Transactional(readOnly = true)
    public List<ServiceReadDto> getAllServices() {
        Long salonId = authService.getCurrentUser().getSalon().getId();
        Optional<List<Services>> services = serviceRepository.findBySalonId(salonId);
        return services.get().stream()
                .map(Mapper::fromServiceToServiceReadDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single service by its ID, ensuring it belongs to the authenticated salon.
     *
     * @param id The ID of the service to retrieve.
     * @return A ServiceReadDto.
     * @throws ResourceNotFoundException if the service does not exist or does not belong to the salon.
     */
    @Transactional(readOnly = true)
    public ServiceReadDto getServiceById(Long id) {
        Long salonId = authService.getCurrentUser().getSalon().getId();
        Services service = serviceRepository.findByIdAndSalonId(id, salonId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id.toString()));
        return Mapper.fromServiceToServiceReadDto(service);
    }

    /**
     * Updates an existing service for the authenticated salon.
     *
     * @param id The ID of the service to update.
     * @param serviceUpdateDto The DTO containing the updated details.
     * @return A ServiceReadDto representing the updated service.
     * @throws ResourceNotFoundException if the service does not exist or does not belong to the salon.
     */
    @Transactional
    public ServiceReadDto updateService(Long id, ServiceCreateDto serviceUpdateDto) {
        Long salonId = authService.getCurrentUser().getSalon().getId();
        Services existingService = serviceRepository.findByIdAndSalonId(id, salonId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id.toString()));

        // Update fields from the DTO
        existingService.setName(serviceUpdateDto.getName());
        existingService.setDurationInMinutes(serviceUpdateDto.getDurationInMinutes());
        existingService.setPrice(serviceUpdateDto.getPrice());

        Services updatedService = serviceRepository.save(existingService);
        return Mapper.fromServiceToServiceReadDto(updatedService);
    }

    /**
     * Deletes a service by its ID, ensuring it belongs to the authenticated salon.
     *
     * @param id The ID of the service to delete.
     * @return true if deleted, false if not found or deletion failed (e.g., due to constraints).
     */
    @Transactional
    public boolean deleteService(Long id) {
        Long salonId = authService.getCurrentUser().getSalon().getId();
        try {
            Optional<Services> serviceOptional = serviceRepository.findByIdAndSalonId(id, salonId);
            if (serviceOptional.isPresent()) {
                serviceRepository.delete(serviceOptional.get());
                return true;
            } else {
                System.out.println("Service with ID " + id + " not found or does not belong to salon " + salonId);
                return false; // Not found or not authorized for this salon
            }
        } catch (Exception e) {
            System.err.println("Error deleting service with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            // Depending on your error handling, you might throw a specific exception here
            return false; // Indicate failure
        }
    }
}