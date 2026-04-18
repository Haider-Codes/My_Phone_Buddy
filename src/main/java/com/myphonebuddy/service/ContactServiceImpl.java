package com.myphonebuddy.service;

import com.myphonebuddy.entity.Activity;
import com.myphonebuddy.entity.Contact;
import com.myphonebuddy.exception.ContactNotFoundException;
import com.myphonebuddy.repository.ActivityRepository;
import com.myphonebuddy.repository.ContactRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    private final ActivityRepository activityRepository;

    public ContactServiceImpl(ContactRepository contactRepository, ActivityRepository activityRepository) {
        this.contactRepository = contactRepository;
        this.activityRepository = activityRepository;
    }

    @Transactional
    @Override
    public Contact saveContact(Contact contact) {
        contact.setCreatedAt(LocalDateTime.now());

        // Creating an activity of the save operation/action
        var activity = Activity.builder()
                .content("Added " + contact.getName())
                .createdAt(LocalDateTime.now())
                .user(contact.getUser())
                .build();

        activityRepository.save(activity);

        return contactRepository.save(contact);
    }

    @Override
    public Optional<Contact> findContactById(String userEmail, UUID id) {
        return contactRepository.findByUserEmailAndId(userEmail, id);
    }

    @Override
    public Optional<Contact> findContactByEmail(String userEmail, String email) {
        return contactRepository.findByUserEmailAndEmail(userEmail, email);
    }

    @Transactional
    @Override
    public Contact updateContact(Contact contact) {
        var exist_contact = contactRepository.findByUserEmailAndId(contact.getUser().getEmail(), contact.getId())
                .orElseThrow(()->new ContactNotFoundException("Contact not found with name " + contact.getName()));

        // Updating contact information, if it exists
        exist_contact.setName(contact.getName());
        exist_contact.setEmail(contact.getEmail());
        exist_contact.setAddress(contact.getAddress());
        exist_contact.setDescription(contact.getDescription());
        exist_contact.setPhoneNumber(contact.getPhoneNumber());
        exist_contact.setWebsiteLink(contact.getWebsiteLink());
        exist_contact.setLinkedInLink(contact.getLinkedInLink());
        exist_contact.setFavourite(contact.isFavourite());
        exist_contact.setPicture(contact.getPicture());
        exist_contact.setPicturePublicId(contact.getPicturePublicId());
        exist_contact.setUpdatedAt(LocalDateTime.now());

        // Creating an activity of the update operation/action
        var activity = Activity.builder()
                .content("Updated " + exist_contact.getName())
                .createdAt(LocalDateTime.now())
                .user(exist_contact.getUser())
                .build();

        activityRepository.save(activity);

        return contactRepository.save(exist_contact);
    }

    @Transactional
    @Override
    public void deleteContact(Contact contact) {
        var ex_contact = contactRepository.findByUserEmailAndId(contact.getUser().getEmail(), contact.getId())
                .orElseThrow(()->new ContactNotFoundException("Contact not found with email " + contact.getUser().getEmail()));

        // Creating an activity of the delete operation/action
        var activity = Activity.builder()
                .content("Deleted " + ex_contact.getName())
                .createdAt(LocalDateTime.now())
                .user(ex_contact.getUser())
                .build();

        activityRepository.save(activity);

        contactRepository.delete(ex_contact);
    }

    @Override
    public boolean isContactExistByName(String name) {
        return contactRepository.findByEmail(name).isPresent();
    }

    @Override
    public List<Contact> findAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public List<Contact> findAllContactsByUserEmail(String email) {
        return contactRepository.findByUserEmail(email);
    }

    // Implementing Pagination with Sorting in retrieved contacts
    @Override
    public Page<Contact> findAllContactsByUserEmail(String email, int page, int size, String field, String direction) {

        var pageable = getPageable(page, size, field, direction);
        return contactRepository.findByUserEmail(email, pageable);
    }

    @Override
    public Page<Contact> searchByName(String userEmail, int page, int size, String name, String keyword, String direction) {
        var pageable = getPageable(page, size, name, direction);
        return contactRepository.findByUserEmailAndNameContaining(userEmail, keyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String userEmail, int page, int size, String email, String keyword, String direction) {
        var pageable = getPageable(page, size, email, direction);
        return contactRepository.findByUserEmailAndEmail(userEmail, keyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String userEmail, int page, int size, String phoneNumber, String keyword, String direction) {
        var pageable = getPageable(page, size, phoneNumber, direction);
        return contactRepository.findByUserEmailAndPhoneNumber(userEmail, keyword, pageable);
    }

    @Override
    public Page<Contact> searchByDescription(String userEmail, int page, int size, String description, String keyword, String direction) {
        var pageable = getPageable(page, size, description, direction);
        return contactRepository.findByUserEmailAndDescriptionContaining(userEmail, keyword, pageable);
    }

    private Pageable getPageable(int page, int size,  String field, String direction) {
        var sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(Sort.Direction.DESC, field) : Sort.by(Sort.Direction.ASC, field);
        return PageRequest.of(page, size, sort);
    }
}
