package com.ktu.xsports.repository;

import com.ktu.xsports.api.domain.Sport;

import java.util.ArrayList;
import java.util.List;

public class InMemorySportsRepository {
    private List<Sport> sports;

    public InMemorySportsRepository() {
        sports = new ArrayList<>();
    }

}
