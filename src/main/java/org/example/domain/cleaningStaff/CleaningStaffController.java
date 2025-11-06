package org.example.domain.cleaningStaff;

import org.example.domain.cleaningStaff.dto.CleaningStaffRequestDTO;
import java.util.List;

public class CleaningStaffController {
    private static CleaningStaffController instance;
    private final CleaningStaffService cleaningStaffService;
    
    private CleaningStaffController(CleaningStaffService cleaningStaffService) {
        this.cleaningStaffService = cleaningStaffService;
    }
    
    public static void initialize(CleaningStaffService cleaningStaffService) {
        if (instance == null) {
            instance = new CleaningStaffController(cleaningStaffService);
        }
    }

    public static CleaningStaffController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CleaningStaffController가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public CleaningStaff findById(int id) {
        return cleaningStaffService.findById(id);
    }

    public CleaningStaff save(CleaningStaffRequestDTO requestDTO) {
        return cleaningStaffService.save(requestDTO);
    }

    public List<CleaningStaff> findAll() {
        return cleaningStaffService.findAll();
    }

    public void deleteById(int id) {
        cleaningStaffService.deleteById(id);
    }
}