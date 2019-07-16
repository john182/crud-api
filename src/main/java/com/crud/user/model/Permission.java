package com.crud.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Permission {

    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    private String description;
}
