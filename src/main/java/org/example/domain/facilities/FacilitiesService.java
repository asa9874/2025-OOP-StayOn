package org.example.domain.facilities;

import org.example.domain.facilities.dto.FacilitiesRequestDTO;
import org.example.domain.pension.Pension;
import org.example.domain.pension.PensionRepository;
import java.util.List;
import java.util.NoSuchElementException;

public class FacilitiesService {
    private static FacilitiesService instance;
    private final FacilitiesRepository facilitiesRepository;

    private FacilitiesService(FacilitiesRepository facilitiesRepository) {
        this.facilitiesRepository = facilitiesRepository;
    }

    public static void initialize(FacilitiesRepository facilitiesRepository) {
        if (instance == null) {
            instance = new FacilitiesService(facilitiesRepository);
        }
    }

    public static FacilitiesService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FacilitiesService가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public Facilities findById(int id) {
        return facilitiesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + id + "인 시설을 찾을 수 없습니다."));
    }

    public List<Facilities> findAll() {
        return facilitiesRepository.findAll();
    }

    public List<Facilities> findByPensionId(int pensionId) {
        return facilitiesRepository.findByPensionId(pensionId);
    }    public Facilities save(FacilitiesRequestDTO requestDTO) {
        Pension pension = PensionRepository.getInstance().findById(requestDTO.pensionId())
                .orElseThrow(() -> new NoSuchElementException("ID가 " + requestDTO.pensionId() + "인 펜션을 찾을 수 없습니다."));
        Facilities newFacility = new Facilities(
            0, // id will be set in repo
            requestDTO.name(),
            requestDTO.openingTime(),
            requestDTO.closingTime(),
            requestDTO.requireReservation(),
            pension,
            requestDTO.image()
        );
        return facilitiesRepository.save(newFacility);
    }

    public void deleteById(int id) {
        // 존재하는지 확인 후 삭제
        findById(id); // NotFoundException을 던질 수 있음
        facilitiesRepository.deleteById(id);
    }
}
