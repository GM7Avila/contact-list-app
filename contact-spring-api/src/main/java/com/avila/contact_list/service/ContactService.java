package com.avila.contact_list.service;

import com.avila.contact_list.model.Contact;
import com.avila.contact_list.repository.ContactRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.avila.contact_list.constant.Constant.PHOTO_DIRECTORY;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public Page<Contact> getAllContacts(int page, int size) {
        return contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Contact getContactById(String id) {
        return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteContact(String id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
        contactRepository.delete(contact);
        log.info("Contact with id {} deleted successfully", id);
    }

    public String uploadPhoto(String id, MultipartFile file) {
        log.info("Uploading photo for contact with id {}", id);
        Contact contact = getContactById(id);
        String photoUrl = photoFunction.apply(id, file);
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return contact.getPhotoUrl();
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse("png");

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {

        String filename = id + fileExtension.apply(image.getOriginalFilename());

        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) { Files.createDirectories(fileStorageLocation); }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/image/" + filename).toUriString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to save image", e);
        }
    };

}
