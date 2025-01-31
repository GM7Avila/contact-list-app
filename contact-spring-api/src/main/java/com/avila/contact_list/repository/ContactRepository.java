package com.avila.contact_list.repository;

import com.avila.contact_list.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    Optional<Contact> findById(String id);
    Optional<Contact> findByName(String name);
    Optional<Contact> findByEmail(String email);
    Optional<Contact> findByPhone(String phone);
}
