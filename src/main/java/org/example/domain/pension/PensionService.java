package org.example.domain.pension;

import org.example.domain.pension.dto.PensionRequestDTO;
import org.example.domain.pension.dto.PensionUpdateDTO;
import org.example.domain.user.pensionManager.PensionManager;
import org.example.domain.user.pensionManager.PensionManagerRepository;
import java.util.List;
import java.util.NoSuchElementException;

public class PensionService {
    private static PensionService instance;
    private final PensionRepository pensionRepository;
    private final PensionManagerRepository pensionManagerRepository;

    private PensionService(PensionRepository pensionRepository) {
        this.pensionRepository = pensionRepository;
        this.pensionManagerRepository = PensionManagerRepository.getInstance();
    }

    public static void initialize(PensionRepository pensionRepository) {
        if (instance == null) {
            instance = new PensionService(pensionRepository);
        }
    }

    public static PensionService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PensionService가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public Pension findById(int id) {
        return pensionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + id + "인 펜션을 찾을 수 없습니다."));
    }

    public List<Pension> findAll() {
        return pensionRepository.findAll();
    }

    public List<Pension> findByPensionManagerId(int pensionManagerId) {
        return pensionRepository.findByPensionManagerId(pensionManagerId);
    }

    public Pension save(PensionRequestDTO requestDTO) {
        PensionManager pensionManager = pensionManagerRepository.findById(requestDTO.pensionManagerId())
            .orElseThrow(() -> new IllegalArgumentException("PensionManager not found: " + requestDTO.pensionManagerId()));
        
        Pension newPension = new Pension(
            requestDTO.name(),
            requestDTO.address(),
            requestDTO.phoneNumber(),
            requestDTO.description(),
            pensionManager,
            requestDTO.image()
        );
        return pensionRepository.save(newPension);
    }

    public Pension update(PensionUpdateDTO updateDTO) {
        findById(updateDTO.id());
        
        Pension pension = new Pension();
        pension.setId(updateDTO.id());
        pension.setName(updateDTO.name());
        pension.setAddress(updateDTO.address());
        pension.setPhoneNumber(updateDTO.phoneNumber());
        pension.setDescription(updateDTO.description());
        
        Pension updated = pensionRepository.update(pension);
        if (updated == null) {
            throw new NoSuchElementException("ID가 " + updateDTO.id() + "인 펜션을 찾을 수 없습니다.");
        }
        return updated;
    }

    public void deleteById(int id) {
        findById(id);
        pensionRepository.deleteById(id);
    }
}
