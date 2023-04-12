package com.ktu.xsports.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "changes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Change {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

}
