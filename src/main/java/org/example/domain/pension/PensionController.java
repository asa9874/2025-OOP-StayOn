package org.example.domain.pension;

import org.example.domain.pension.dto.PensionRequestDTO;
import org.example.domain.pension.dto.PensionUpdateDTO;
import java.util.List;

public class PensionController {
    private static PensionController instance;
    private final PensionService pensionService;
    
    private PensionController(PensionService pensionService) {
        this.pensionService = pensionService;
    }
    
    public static void initialize(PensionService pensionService) {
        if (instance == null) {
            instance = new PensionController(pensionService);
        }
    }

    public static PensionController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PensionController가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public Pension findById(int id) {
        return pensionService.findById(id);
    }

    public List<Pension> findAll() {
        return pensionService.findAll();
    }

    public List<Pension> findByPensionManagerId(int pensionManagerId) {
        return pensionService.findByPensionManagerId(pensionManagerId);
    }

    public Pension save(PensionRequestDTO requestDTO) {
        return pensionService.save(requestDTO);
    }

    public Pension update(PensionUpdateDTO updateDTO) {
        return pensionService.update(updateDTO);
    }

    public void deleteById(int id) {
        pensionService.deleteById(id);
    }
}
