package org.example.domain.pension.strategy;

import org.example.domain.pension.Pension;
import java.util.ArrayList;
import java.util.List;

public class DefaultDataStrategy implements PensionInitStrategy {
    @Override
    public List<Pension> initializeList() {
        List<Pension> list = new ArrayList<>();
        
        String[] names = {"바다뷰", "산속의 휴식", "제주 힐링", "강변 힐링", "도심 속 휴식", 
                         "설악 펜션", "남해 바다", "한라산", "경포대", "속초 해변",
                         "평창 산장", "양양 서핑", "부산 해운대", "여수 밤바다", "전주 한옥",
                         "경주 고택", "안동 전통", "대구 도심", "인천 송도", "수원 화성",
                         "춘천 호반", "태안 해변", "보령 머드", "서산 낙조", "당진 왜목"};
        String[] regions = {"강원도", "경기도", "제주도", "부산광역시", "서울특별시"};
        
        for (int i = 1; i <= 25; i++) {
            Pension pension = new Pension();
            pension.setId(i);
            pension.setName(names[i - 1] + " 펜션");
            pension.setAddress(regions[i % 5] + " " + names[i - 1] + "로 " + (100 + i * 10));
            pension.setPhoneNumber("0" + (30 + (i % 7)) + "-" + (1000 + i * 100) + "-" + (5000 + i * 100));
            pension.setDescription(names[i - 1] + " 펜션에서 편안한 휴식을 즐기세요. 최고의 서비스를 제공합니다.");
            pension.setPensionManagerId((i % 3) + 1);
            pension.setImage("image/pension.png");
            list.add(pension);
        }
        
        return list;
    }
}
