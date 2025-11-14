package org.example.domain.cleaningStaff.strategy;

import org.example.domain.cleaningStaff.CleaningStaff;
import java.util.ArrayList;
import java.util.List;

public class DefaultDataStrategy implements CleaningStaffInitStrategy {
    @Override
    public List<CleaningStaff> initializeList() {
        List<CleaningStaff> list = new ArrayList<>();
        
        String[] names = {"홍길동", "김철수", "이영희", "박민수", "최수지",
                         "정준호", "강지은", "조성민", "윤하늘", "장서연",
                         "임도윤", "한예준", "오시우", "서하준", "신서준",
                         "권지호", "황수빈", "안은우", "송유진", "류지우",
                         "전현우", "홍민지", "고태양", "문소율", "양채원"};
        
        for (int i = 1; i <= 25; i++) {
            CleaningStaff staff = new CleaningStaff();
            staff.setId(i);
            staff.setName(names[i - 1]);
            staff.setPhoneNumber("010-" + (1000 + i * 100) + "-" + (5000 + i * 100));
            list.add(staff);
        }
        
        return list;
    }
}