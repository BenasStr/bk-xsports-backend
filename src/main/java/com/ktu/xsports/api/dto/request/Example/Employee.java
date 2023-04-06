package com.ktu.xsports.api.dto.request.Example;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class Employee {
   private String changedFields;

   @JsonDeserialize()
   private LocalDate dob;
   private LocalDate hireDate;
   private int id;
}
