package org.example.domain.facilities;

import org.example.domain.facilities.dto.FacilitiesRequestDTO;
import java.util.List;

public class FacilitiesController {
    private static FacilitiesController instance;
    private final FacilitiesService facilitiesService;

    private FacilitiesController(FacilitiesService facilitiesService) {
        this.facilitiesService = facilitiesService;
    }

    public static void initialize(FacilitiesService facilitiesService) {
        if (instance == null) {
            instance = new FacilitiesController(facilitiesService);
        }
    }

    public static FacilitiesController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FacilitiesController가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public Facilities findById(int id) {
        return facilitiesService.findById(id);
    }

    public Facilities save(FacilitiesRequestDTO requestDTO) {
        return facilitiesService.save(requestDTO);
    }

    public List<Facilities> findAll() {
        return facilitiesService.findAll();
    }

    public List<Facilities> findByPensionId(int pensionId) {
        return facilitiesService.findByPensionId(pensionId);
    }

    public void deleteById(int id) {
        facilitiesService.deleteById(id);
    }
}
