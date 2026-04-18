package com.myphonebuddy.service;

import com.myphonebuddy.entity.Contact;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactService {

    Contact saveContact(Contact contact);

    Optional<Contact> findContactById(String userEmail, UUID id);

    Optional<Contact> findContactByEmail(String userEmail, String email);

    Contact updateContact(Contact contact);

    void deleteContact(Contact contact);

    boolean isContactExistByName(String name);

    List<Contact> findAllContacts();

    List<Contact> findAllContactsByUserEmail(String email);

    // Defining a pagination with sorting method
    Page<Contact> findAllContactsByUserEmail(String email, int page, int size, String field, String direction);

    // Defining a pagination based search methods

    Page<Contact> searchByName(String userEmail, int page, int size, String name, String keyword, String direction);

    Page<Contact> searchByEmail(String userEmail, int page, int size, String email, String keyword, String direction);

    Page<Contact> searchByPhoneNumber(String userEmail, int page, int size, String phoneNumber, String keyword, String direction);

    Page<Contact> searchByDescription(String userEmail, int page, int size, String description, String keyword, String direction);

}
