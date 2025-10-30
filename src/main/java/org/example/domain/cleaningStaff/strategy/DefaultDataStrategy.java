package org.example.domain.cleaningStaff.strategy;

import org.example.domain.cleaningStaff.CleaningStaff;
import java.util.ArrayList;
import java.util.List;

public class DefaultDataStrategy implements CleaningStaffInitStrategy {
    @Override
    public List<CleaningStaff> initializeList() {
        List<CleaningStaff> list = new ArrayList<>();
        
        CleaningStaff staff1 = new CleaningStaff();
        staff1.setId(1);
        staff1.setName("홍길동");
        staff1.setPhoneNumber("010-1234-5678");
        
        CleaningStaff staff2 = new CleaningStaff();
        staff2.setId(2);
        staff2.setName("김철수");
        staff2.setPhoneNumber("010-9876-5432");
        
        list.add(staff1);
        list.add(staff2);
        
        return list;
    }
}