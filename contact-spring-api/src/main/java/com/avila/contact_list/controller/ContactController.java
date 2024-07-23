package com.avila.contact_list.controller;

import lombok.RequiredArgsConstructor;
import com.avila.contact_list.model.Contact;
import com.avila.contact_list.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.avila.contact_list.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        return ResponseEntity.created(URI.create("/contacts/userID")).body(contactService.createContact(contact));
    }

    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(contactService.getAllContacts(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(contactService.getContactById(id));
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteContact(@RequestParam("id") String id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }


     /**
     * Retrieves a photo from the specified filename.
     *
     * @param filename The name of the photo file.
     * @return The photo content as a byte array.
     * @throws IOException If there is an error reading the photo file.
     */
    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }

}
