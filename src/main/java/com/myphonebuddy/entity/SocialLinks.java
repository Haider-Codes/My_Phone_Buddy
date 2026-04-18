package com.myphonebuddy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "contact_social_links")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLinks {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 10000)
    private String socialLink;

    @Column
    private String title;

    @ManyToOne
    @JoinColumn(name = "contact_contact_id")
    private Contact contact;
}
