package org.example;

import org.example.domain.cleaningStaff.CleaningStaffController;
import org.example.domain.cleaningStaff.CleaningStaffRepository;
import org.example.domain.cleaningStaff.CleaningStaffService;
import org.example.domain.cleaningStaff.strategy.CleaningStaffInitStrategy;
import org.example.domain.cleaningStaff.strategy.DefaultDataStrategy;

public class Init {
    public static void initializeDependencies() { // DI 하는 메서드
        initializeCleaningStaffModule(new DefaultDataStrategy());
    }

    public static void initializeCleaningStaffModule(CleaningStaffInitStrategy strategy) {
        CleaningStaffInitStrategy cleaningStaffInitStrategy;
        if (strategy == null) {
            cleaningStaffInitStrategy = new DefaultDataStrategy();
        }
        cleaningStaffInitStrategy = strategy;
        CleaningStaffRepository.initialize(cleaningStaffInitStrategy);
        CleaningStaffService.initialize(CleaningStaffRepository.getInstance());
        CleaningStaffController.initialize(CleaningStaffService.getInstance());
    }

}
