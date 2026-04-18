package com.myphonebuddy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
