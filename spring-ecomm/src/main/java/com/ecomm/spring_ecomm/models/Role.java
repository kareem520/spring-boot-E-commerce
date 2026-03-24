package com.ecomm.spring_ecomm.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ROLES")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "NAME", nullable = false)
    private AppRole name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role(AppRole name) {
        this.name = name;
    }
}