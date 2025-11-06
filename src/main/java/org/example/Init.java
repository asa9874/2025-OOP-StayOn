package org.example;

import org.example.domain.cleaningStaff.CleaningStaffController;
import org.example.domain.cleaningStaff.CleaningStaffRepository;
import org.example.domain.cleaningStaff.CleaningStaffService;
import org.example.domain.cleaningStaff.strategy.CleaningStaffInitStrategy;
import org.example.domain.cleaningStaff.strategy.DefaultDataStrategy;
import org.example.domain.user.customer.CustomerController;
import org.example.domain.user.customer.CustomerRepository;
import org.example.domain.user.customer.CustomerService;
import org.example.domain.user.customer.strategy.CustomerInitStrategy;
import org.example.domain.user.customer.strategy.DefaultCustomerDataStrategy;
import org.example.domain.user.pensionManager.PensionManagerController;
import org.example.domain.user.pensionManager.PensionManagerRepository;
import org.example.domain.user.pensionManager.PensionManagerService;
import org.example.domain.user.pensionManager.strategy.PensionManagerInitStrategy;
import org.example.domain.user.pensionManager.strategy.DefaultPensionManagerDataStrategy;

public class Init {
    public static void initializeDependencies() { // DI 하는 메서드
        initializeCleaningStaffModule(new DefaultDataStrategy());
        initializeCustomerModule(new DefaultCustomerDataStrategy());
        initializePensionManagerModule(new DefaultPensionManagerDataStrategy());
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

    public static void initializeCustomerModule(CustomerInitStrategy strategy) {
        CustomerInitStrategy customerInitStrategy;
        if (strategy == null) {
            customerInitStrategy = new DefaultCustomerDataStrategy();
        }
        customerInitStrategy = strategy;
        CustomerRepository.initialize(customerInitStrategy);
        CustomerService.initialize(CustomerRepository.getInstance());
        CustomerController.initialize(CustomerService.getInstance());
    }

    public static void initializePensionManagerModule(PensionManagerInitStrategy strategy) {
        PensionManagerInitStrategy pensionManagerInitStrategy;
        if (strategy == null) {
            pensionManagerInitStrategy = new DefaultPensionManagerDataStrategy();
        }
        pensionManagerInitStrategy = strategy;
        PensionManagerRepository.initialize(pensionManagerInitStrategy);
        PensionManagerService.initialize(PensionManagerRepository.getInstance());
        PensionManagerController.initialize(PensionManagerService.getInstance());
    }

}
