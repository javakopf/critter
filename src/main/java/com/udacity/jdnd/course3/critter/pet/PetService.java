package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    private PetRepository petRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public PetService(PetRepository petRepository,CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public PetDTO addNewPet(PetDTO petDTO){
        Pet pet = convertToEntity(petDTO);
        Customer customer = customerRepository.findById(petDTO.getOwnerId()).get();
        pet.setOwner(customer);
        pet = petRepository.save(pet);
        customer.getPets().add(pet);
        customerRepository.save(customer);
        return convertToDTO(pet);
    }

    private PetDTO convertToDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet,petDTO);
        petDTO.setOwnerId(pet.getOwner().getId());
        return petDTO;
    }

    private Pet convertToEntity(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO,pet);
        return pet;
    }

    public PetDTO getPet(long petId) {
        Optional<Pet> petOpt = petRepository.findById(petId);
        if(petOpt.isPresent()){
            Pet pet = petOpt.get();
            return convertToDTO(pet);
        }
        return new PetDTO();
    }

    public List<PetDTO> getPetsByOwner(long ownerId) {
      Customer customer = customerRepository.findById(ownerId).get();
      List<PetDTO> pets = new ArrayList<>();
        customer.getPets().forEach(
                (pet) -> {
                    pets.add(convertToDTO(pet));
                }
        );
      return pets;
    }


    public List<PetDTO> getAllPets() {
        List<PetDTO> pets = new ArrayList<>();
        petRepository.findAll().forEach(pet ->pets.add(convertToDTO(pet)) );
        return pets;
    }
}
