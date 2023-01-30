package group09.ProcurementService.Services;

import group09.ProcurementService.Entities.Contact;
import group09.ProcurementService.Repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getContacts(){
        return contactRepository.findAll();
    }
    public List<Contact> getContact(Long id)
    {
        return contactRepository.findAllById(Collections.singleton(id));
    }

    public Contact createContact(Contact contact){
        return contactRepository.save(contact);
    }

    public Optional<Contact> updateContact(Contact newContact, Long id)
    {
        return contactRepository.findById(id)
                .map(contact -> {
                    contact.setName(newContact.getName());
                    contact.setEmail(newContact.getEmail());
                    contact.setPhone(newContact.getEmail());
                    contact.setPosition(newContact.getPosition());
                    return contactRepository.save(contact);
                });
    }

    public void deleteContact(Long id)
    {
        contactRepository.deleteById(id);
    }
}

