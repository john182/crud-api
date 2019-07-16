package com.crud.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {


    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    private String email;
    private String passord;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_permission", joinColumns = @JoinColumn(name = "id_usuario")
            , inverseJoinColumns = @JoinColumn(name = "id_permission"))
    private List<Permission> permissions;
}
