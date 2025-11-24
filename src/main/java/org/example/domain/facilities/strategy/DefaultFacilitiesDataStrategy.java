package org.example.domain.facilities.strategy;

import org.example.domain.facilities.Facilities;
import org.example.domain.pension.Pension;
import org.example.domain.pension.PensionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DefaultFacilitiesDataStrategy implements FacilitiesInitStrategy {
    @Override
    public List<Facilities> initializeList() {
        List<Facilities> list = new ArrayList<>();
        List<Pension> pensions = PensionRepository.getInstance().findAll();
        if (pensions.isEmpty()) {
            return list; // No pensions, no facilities
        }
        Pension pension = pensions.get(0); // Use first pension for simplicity

        String[] facilityNames = {"수영장", "사우나", "헬스장", "테니스장", "골프장"};
        LocalDateTime openingTime = LocalDateTime.of(2023, 1, 1, 9, 0);
        LocalDateTime closingTime = LocalDateTime.of(2023, 1, 1, 18, 0);

        for (int i = 1; i <= 5; i++) {
            Facilities facility = new Facilities(
                i,
                facilityNames[i - 1],
                openingTime,
                closingTime,
                i % 2 == 0, // Alternate requireReservation
                pension
            );
            list.add(facility);
        }

        return list;
    }
}
