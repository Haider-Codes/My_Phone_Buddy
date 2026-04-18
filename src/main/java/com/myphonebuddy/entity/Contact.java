package com.myphonebuddy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Contact Information
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 10000)
    private String picture;

    @Column(length = 10000)
    private String picturePublicId;

    @Column(unique = true, length = 10, nullable = false)
    private String phoneNumber;

    @Column(length = 10000)
    private String address;

    @Column(length = 10000)
    private String description;

    // Other Information
    private boolean isFavourite = false;

    @Column(length = 10000)
    private String websiteLink;

    @Column(length = 10000)
    private String linkedInLink;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    // Mapping contacts
    @ManyToOne
    @JoinColumn(name = "user_user_id")
    @JsonIgnore
    // Required since we are making an ajax rest api call to fetch contact of a user, so if not provided this annotation, it will recursively loop.
    private User user;

    // Mapping Social Link
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "contact", orphanRemoval = true)
    private List<SocialLinks> socialLinks = new ArrayList<>();
}
