package group09.ProcurementService.Controllers;

import group09.ProcurementService.Entities.Contact;
import group09.ProcurementService.Services.ContactService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/supplierContacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    List<Contact> getContacts(){
        return contactService.getContacts();
    }

    @GetMapping(path = "{id}")
    List<Contact> getContact(@PathVariable("id") Long id)
    {
        return contactService.getContact(id);
    }

    @PostMapping
    Contact createContact(@RequestBody Contact contact)
    {
        return contactService.createContact(contact);
    }

    @PutMapping(path = "{id}")
    public Optional<Contact> updateContact(@RequestBody Contact newContact, @PathVariable("id") Long id)
    {
        return contactService.updateContact(newContact, id);
    }

    @DeleteMapping(path = "{id}")
    public void deleteContact(@PathVariable("id") Long id)
    {
        contactService.deleteContact(id);
    }
}


