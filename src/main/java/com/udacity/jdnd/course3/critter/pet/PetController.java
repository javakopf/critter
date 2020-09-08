package com.udacity.jdnd.course3.critter.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;
    private final PetConverter petConverter;

    @Autowired
    public PetController(PetService petService,PetConverter petConverter) {
        this.petService = petService;
        this.petConverter = petConverter;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
         Pet pet = petConverter.convertToEntity(petDTO);
         Pet newPet = petService.addNewPet(pet, petDTO.getOwnerId());
         if(newPet == null){
             throw new RuntimeException("Pet needs owner");
         }
        return petConverter.convertToDTO(newPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return petConverter.convertToDTO(petService.getPet(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<PetDTO> petList = new ArrayList<>();
        petService.getAllPets().forEach(pet -> petList.add(petConverter.convertToDTO(pet)));
        return petList;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<PetDTO> petList = new ArrayList<>();
        petService.getPetsByOwner(ownerId).forEach(pet -> petList.add(petConverter.convertToDTO(pet)));
        return petList;
    }
}
