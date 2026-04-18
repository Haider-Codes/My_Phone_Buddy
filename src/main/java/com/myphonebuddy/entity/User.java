package com.myphonebuddy.entity;

import com.myphonebuddy.enums.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    //User Information
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 10000)
    private String password;

    @Column(length = 10000)
    private String about;

    @Column(length = 10000)
    private String profilePic;

    @Column(unique = true, length = 10)
    private String phoneNumber;

    // Other Information
    private boolean isEnabled = false;  // Used for enabling and disabling user
                                        // if disabled, user won't be able to log in.

    private boolean isEmailVerified = false;
    private String verificationToken = null;

    private boolean isPhoneVerified = false;

    // SIGN-UP Details
    // @Builder.Default is essential to let builder take default enum-based values, defined here.
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Provider provider = Provider.SELF;

    private String providerUserId = UUID.randomUUID().toString();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.DETACH,
                           CascadeType.MERGE,
                           CascadeType.PERSIST,
                           CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
