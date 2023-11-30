package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceValue;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;


}
