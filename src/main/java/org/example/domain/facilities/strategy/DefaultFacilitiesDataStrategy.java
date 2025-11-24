package org.example.domain.facilities.strategy;

import org.example.domain.facilities.Facilities;
import org.example.domain.pension.Pension;
import org.example.domain.pension.PensionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DefaultFacilitiesDataStrategy implements FacilitiesInitStrategy {    @Override
    public List<Facilities> initializeList() {
        List<Facilities> list = new ArrayList<>();
        List<Pension> pensions = PensionRepository.getInstance().findAll();
        if (pensions.isEmpty()) {
            return list; // No pensions, no facilities
        }

        String[] facilityNames = {"수영장", "사우나", "헬스장", "테니스장", "골프장"};
        String[] facilityImages = {
            "src/main/java/org/example/image/facilities/pool.jpeg",
            "src/main/java/org/example/image/facilities/sauna.jpeg",
            "src/main/java/org/example/image/facilities/gym.jpeg",
            "src/main/java/org/example/image/facilities/tennis.jpeg",
            "src/main/java/org/example/image/facilities/golf.jpeg"
        };
        
        int facilityId = 1;
        
        // 모든 펜션에 대해 부대시설 생성
        for (Pension pension : pensions) {
            // 각 펜션마다 2~5개의 랜덤한 부대시설 생성
            int facilitiesCount = 2 + (pension.getId() % 4); // 2~5개
            
            for (int i = 0; i < facilitiesCount && i < facilityNames.length; i++) {
                int facilityIndex = (pension.getId() + i) % facilityNames.length;
                
                // 운영 시간 다양화
                int openingHour = 8 + (pension.getId() % 3); // 8~10시
                int closingHour = 18 + (pension.getId() % 4); // 18~21시
                LocalDateTime openingTime = LocalDateTime.of(2023, 1, 1, openingHour, 0);
                LocalDateTime closingTime = LocalDateTime.of(2023, 1, 1, closingHour, 0);
                
                Facilities facility = new Facilities(
                    facilityId++,
                    facilityNames[facilityIndex],
                    openingTime,
                    closingTime,
                    facilityIndex % 2 == 0, // 예약 필요 여부
                    pension,
                    facilityImages[facilityIndex]
                );
                list.add(facility);
            }
        }

        return list;
    }
}
