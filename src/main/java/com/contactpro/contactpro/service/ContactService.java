package com.contactpro.contactpro.service;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.contactpro.contactpro.repository.ContactRepository;
import com.contactpro.contactpro.repository.InteractionRepository;
import com.contactpro.contactpro.repository.UserRepository;
import com.contactpro.contactpro.model.Contact;
import com.contactpro.contactpro.model.Interaction;
import com.contactpro.contactpro.model.User;
import com.contactpro.contactpro.dto.ContactRequest;
import com.contactpro.contactpro.dto.ContactResponse;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
        private final InteractionRepository interactionRepository;
    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository,
                                                  InteractionRepository interactionRepository,
                          UserRepository userRepository) {
        this.contactRepository = contactRepository;
                this.interactionRepository = interactionRepository;
        this.userRepository = userRepository;
    }

    public Page<ContactResponse> getContactsByUserPaginated(
            Long userId,
            int page,
            int size) {

        Page<Contact> contactPage =
                contactRepository.findByUserId(
                        userId,
                        PageRequest.of(page, size)
                );

        return contactPage.map(contact ->
                new ContactResponse(
                        contact.getId(),
                        contact.getName(),
                        contact.getPhone(),
                        contact.getEmail(),
                        contact.getCategory(),
                        contact.isBlocked(),
                        contact.isFavorite(),
                        contact.getCreatedAt()
                )
        );
    }

    public Page<ContactResponse> searchContacts(
            Long userId,
            String keyword,
            int page,
            int size) {

        Page<Contact> contactPage =
                contactRepository.findByUserIdAndNameContainingIgnoreCase(
                        userId,
                        keyword,
                        PageRequest.of(page, size)
                );

        return contactPage.map(contact ->
                new ContactResponse(
                        contact.getId(),
                        contact.getName(),
                        contact.getPhone(),
                        contact.getEmail(),
                        contact.getCategory(),
                        contact.isBlocked(),
                        contact.isFavorite(),
                        contact.getCreatedAt()
                )
        );
    }

    /*
     * Create new contact for a specific user
     */
    public ContactResponse createContact(ContactRequest request) {

        // Find user first
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setCategory(request.getCategory());
        contact.setNotes(request.getNotes());
        contact.setUser(user);

        Contact saved = contactRepository.save(contact);

        return new ContactResponse(
                saved.getId(),
                saved.getName(),
                saved.getPhone(),
                saved.getEmail(),
                saved.getCategory(),
                saved.isBlocked(),
                saved.isFavorite(),
                saved.getCreatedAt()
        );
    }

    public ContactResponse updateContact(Long contactId,
                                         Long userId,
                                         ContactRequest request) {

        // Find contact
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        // Validate ownership
        if (!contact.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to update this contact");
        }

        // Update allowed fields
        contact.setName(request.getName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setCategory(request.getCategory());
        contact.setNotes(request.getNotes());

        Contact updated = contactRepository.save(contact);

        return new ContactResponse(
                updated.getId(),
                updated.getName(),
                updated.getPhone(),
                updated.getEmail(),
                updated.getCategory(),
                updated.isBlocked(),
                updated.isFavorite(),
                updated.getCreatedAt()
        );
    }

    public ContactResponse toggleFavorite(Long contactId, Long userId) {

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        // Ownership validation
        if (!contact.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to modify this contact");
        }

        // Toggle favorite
        contact.setFavorite(!contact.isFavorite());

        Contact updated = contactRepository.save(contact);

        return new ContactResponse(
                updated.getId(),
                updated.getName(),
                updated.getPhone(),
                updated.getEmail(),
                updated.getCategory(),
                updated.isBlocked(),
                updated.isFavorite(),
                updated.getCreatedAt()
        );
    }

    public void importFromVcf(Long userId, MultipartFile file) {

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<VCard> vcards = Ezvcard.parse(file.getInputStream()).all();

            for (VCard vcard : vcards) {

                Contact contact = new Contact();

                if (vcard.getFormattedName() != null) {
                    contact.setName(vcard.getFormattedName().getValue());
                }

                if (!vcard.getTelephoneNumbers().isEmpty()) {
                    contact.setPhone(
                            vcard.getTelephoneNumbers().get(0).getText()
                    );
                }

                if (!vcard.getEmails().isEmpty()) {
                    contact.setEmail(
                            vcard.getEmails().get(0).getValue()
                    );
                }

                contact.setUser(user);

                contactRepository.save(contact);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import VCF contacts");
        }
    }

    /*
     * Get all contacts of a specific user
     */
    public List<ContactResponse> getContactsByUser(Long userId) {

        return contactRepository.findByUserId(userId)
                .stream()
                .map(contact -> new ContactResponse(
                        contact.getId(),
                        contact.getName(),
                        contact.getPhone(),
                        contact.getEmail(),
                        contact.getCategory(),
                        contact.isBlocked(),
                        contact.isFavorite(),
                        contact.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public Page<ContactResponse> getContactsPaged(Long userId, int page, int size) {

        Page<Contact> contacts =
                contactRepository.findByUserId(
                        userId,
                        PageRequest.of(page, size)
                );

        return contacts.map(contact ->
                new ContactResponse(
                        contact.getId(),
                        contact.getName(),
                        contact.getPhone(),
                        contact.getEmail(),
                        contact.getCategory(),
                        contact.isBlocked(),
                        contact.isFavorite(),
                        contact.getCreatedAt()
                )
        );
    }
    // converts searched contacts into API response format
    public List<ContactResponse> searchContacts(String name) {

        return contactRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(contact -> new ContactResponse(
                        contact.getId(),
                        contact.getName(),
                        contact.getPhone(),
                        contact.getEmail(),
                        contact.getCategory(),
                        contact.isBlocked(),
                        contact.isFavorite(),
                        contact.getCreatedAt()
                ))
                .toList();
    }

    public List<ContactResponse> getFavoriteContacts(Long userId) {

        return contactRepository
                .findByUserIdAndIsFavoriteTrue(userId)
                .stream()
                .map(contact -> new ContactResponse(
                        contact.getId(),
                        contact.getName(),
                        contact.getPhone(),
                        contact.getEmail(),
                        contact.getCategory(),
                        contact.isBlocked(),
                        contact.isFavorite(),
                        contact.getCreatedAt()
                ))
                .toList();
    }
    // converts favorite contacts into API response format

    public List<ContactResponse> getBlockedContacts(Long userId) {

        return contactRepository
                .findByUserIdAndIsBlockedTrue(userId)
                .stream()
                .map(contact -> new ContactResponse(
                        contact.getId(),
                        contact.getName(),
                        contact.getPhone(),
                        contact.getEmail(),
                        contact.getCategory(),
                        contact.isBlocked(),
                        contact.isFavorite(),
                        contact.getCreatedAt()
                ))
                .toList();
    }
// converts blocked contacts into API response format




    public String exportToVcf(Long userId) {

        List<Contact> contacts =
                contactRepository.findByUserId(userId);

        List<VCard> vcards = new ArrayList<>();

        for (Contact contact : contacts) {

            VCard vcard = new VCard();

            vcard.setFormattedName(contact.getName());
            vcard.addTelephoneNumber(contact.getPhone());

            if (contact.getEmail() != null) {
                vcard.addEmail(contact.getEmail());
            }

            vcards.add(vcard);
        }

        return Ezvcard.write(vcards).go();
    }

    public void deleteContact(Long contactId, Long userId) {

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        // Ownership validation
        if (!contact.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to delete this contact");
        }

                List<Interaction> interactions = interactionRepository.findByContactId(contactId);
                if (!interactions.isEmpty()) {
                        interactionRepository.deleteAll(interactions);
                }

        contactRepository.delete(contact);
    }
}