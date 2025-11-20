package org.example.domain.facilities.strategy;

import org.example.domain.facilities.Facilities;
import java.util.ArrayList;
import java.util.List;

public class EmptyFacilitiesListStrategy implements FacilitiesInitStrategy {
    @Override
    public List<Facilities> initializeList() {
        return new ArrayList<>();
    }
}
