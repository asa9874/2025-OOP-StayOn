package org.example.domain.user.customer.strategy;

import org.example.domain.user.customer.Customer;
import java.util.ArrayList;
import java.util.List;

public class DefaultCustomerDataStrategy implements CustomerInitStrategy {
    @Override
    public List<Customer> initializeList() {
        List<Customer> list = new ArrayList<>();
        
        String[] lastNames = {"김", "이", "박", "최", "정", "강", "조", "윤", "장", "임",
                             "한", "오", "서", "신", "권", "황", "안", "송", "류", "전",
                             "홍", "고", "문", "양", "손"};
        String[] firstNames = {"민수", "영희", "철수", "수지", "준호", "지은", "성민", "하늘",
                              "서연", "도윤", "예준", "시우", "하준", "서준", "지호",
                              "수빈", "은우", "유진", "지우", "현우", "민지", "태양",
                              "소율", "채원", "다은"};
        
        for (int i = 1; i <= 25; i++) {
            String name = lastNames[i - 1] + firstNames[i - 1];
            Customer customer = new Customer(
                name,
                "customer" + i,
                "password" + i,
                "010-" + (1000 + i * 100) + "-" + (2000 + i * 100),
                "customer" + i + "@example.com",
                50000 + (i * 10000)
            );
            customer.setId(i);
            list.add(customer);
        }
        
        return list;
    }
}
