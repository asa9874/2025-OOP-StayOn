package org.example.domain.user.pensionManager.strategy;

import org.example.domain.user.pensionManager.PensionManager;
import java.util.ArrayList;
import java.util.List;

public class DefaultPensionManagerDataStrategy implements PensionManagerInitStrategy {
    @Override
    public List<PensionManager> initializeList() {
        List<PensionManager> list = new ArrayList<>();
        
        String[] names = {"이사장", "김관리", "박운영", "최대표", "정원장",
                         "강사장", "조관리자", "윤매니저", "장대표", "임사장",
                         "한운영자", "오대표", "서관리", "신사장", "권원장",
                         "황대표", "안관리자", "송운영", "류사장", "전매니저",
                         "홍대표", "고사장", "문관리", "양운영자", "손원장"};
        
        for (int i = 1; i <= 25; i++) {
            PensionManager manager = new PensionManager(
                names[i - 1],
                "manager" + i,
                "admin" + (100 + i),
                "010-5" + (100 + i * 10) + "-" + (1000 + i * 100),
                "manager" + i + "@pension.com"
            );
            manager.setId(i);
            list.add(manager);
        }
        
        return list;
    }
}
