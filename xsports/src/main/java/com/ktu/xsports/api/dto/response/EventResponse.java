package com.ktu.xsports.api.dto.response;

import java.time.LocalDate;
import java.util.List;

public class EventResponse {

    long id;
    String name;
    String photo;
    String location;
    LocalDate date;
    List<Long> sports;

}
