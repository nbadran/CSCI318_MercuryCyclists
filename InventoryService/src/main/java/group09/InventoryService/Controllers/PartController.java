package group09.InventoryService.Controllers;

import group09.InventoryService.Entities.Part;
import group09.InventoryService.Services.PartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@ResponseBody
@RequestMapping(path = "/part")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }


    @GetMapping
    List<Part> getParts(){
        return partService.getParts();
    }

    @GetMapping(path = "{id}")
    List<Part> getPart(@PathVariable("id") Long id)
    {
        return partService.getPart(id);
    }

    @PostMapping
    Part createPart(@RequestBody Part part)
    {
        return partService.createPart(part);
    }

    @PutMapping(path = "{id}")
    public Optional<Part> updatePart(@RequestBody Part newPart, @PathVariable("id") Long id)
    {
        return partService.updatePart(newPart, id);
    }

    @DeleteMapping(path = "{id}")
    public void deletePart(@PathVariable("id") Long id)
    {
        partService.deletePart(id);
    }

    //Use Case #6 - Look up supplier by part
    @PutMapping("/addSupplierToPart/{supplierId}/{partId}")
    public void addSupplierToPart(@PathVariable("supplierId") Long supplierId, @PathVariable("partId") Long partId)
    {
        partService.addSupplierToPart(partId, supplierId);
    }
    //Use Case #6 - Look up supplier by part
    @GetMapping(path = "lookUpSupplierByPart/{partId}")
    public ResponseEntity<Object> lookUpSupplierByPart(@PathVariable("partId") Long partId)
    {
        return partService.lookUpSupplierByPart(partId);
    }

    //validate products
    @GetMapping("/partsValidate/{partId}")
    public boolean validatePart(@PathVariable Long partId){
        return partService.validateID(partId);
    }



}