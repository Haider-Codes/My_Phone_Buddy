package com.myphonebuddy.repository;

import com.myphonebuddy.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {

    Optional<Contact> findByEmail(String email);

    Optional<Contact> findByUserEmailAndId(String userEmail, UUID id);

    Optional<Contact> findByUserEmailAndEmail(String userEmail, String email);

    // Without pagination
    List<Contact> findByUserEmail(String email);

    // Implementing pagination
    Page<Contact> findByUserEmail(String email, Pageable pageable);

    // With pagination
    Page<Contact> findByUserEmailAndNameContaining(String userEmail, String name,  Pageable pageable);

    // With pagination
    Page<Contact> findByUserEmailAndEmail(String userEmail, String email,  Pageable pageable);

    // With pagination
    Page<Contact> findByUserEmailAndPhoneNumber(String userEmail, String phoneNumber,  Pageable pageable);

    // With pagination
    Page<Contact> findByUserEmailAndDescriptionContaining(String userEmail, String description,  Pageable pageable);
}
