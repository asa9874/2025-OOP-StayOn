package org.example.domain.pension.strategy;

import org.example.domain.pension.Pension;
import java.util.ArrayList;
import java.util.List;

public class DefaultDataStrategy implements PensionInitStrategy {
    @Override
    public List<Pension> initializeList() {
        List<Pension> list = new ArrayList<>();
        
        Pension pension1 = new Pension();
        pension1.setId(1);
        pension1.setName("바다뷰 펜션");
        pension1.setAddress("강원도 강릉시 해안로 123");
        pension1.setPhoneNumber("033-1234-5678");
        pension1.setDescription("아름다운 바다 전망을 자랑하는 펜션입니다.");
        pension1.setPensionManagerId(1);
        
        Pension pension2 = new Pension();
        pension2.setId(2);
        pension2.setName("산속의 휴식");
        pension2.setAddress("경기도 가평군 산속로 456");
        pension2.setPhoneNumber("031-9876-5432");
        pension2.setDescription("조용한 산속에서 힐링할 수 있는 펜션입니다.");
        pension2.setPensionManagerId(1);
        
        Pension pension3 = new Pension();
        pension3.setId(3);
        pension3.setName("제주 힐링 펜션");
        pension3.setAddress("제주특별자치도 서귀포시 중문로 789");
        pension3.setPhoneNumber("064-7777-8888");
        pension3.setDescription("제주의 자연을 만끽할 수 있는 프리미엄 펜션입니다.");
        pension3.setPensionManagerId(2);
        
        list.add(pension1);
        list.add(pension2);
        list.add(pension3);
        
        return list;
    }
}
