package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {
    private final PetRepository petRepository;
    private final UserService customerService;

    @Autowired
    public PetService(PetRepository petRepository,UserService customerService) {
        this.petRepository = petRepository;
        this.customerService = customerService;
    }

    public Pet addNewPet(Pet pet,long ownerId){
        if(ownerId != 0 ){
            Customer customer =customerService.getCustomerById(ownerId);
            if(customer!=null){
                pet.setOwner(customer);
                pet = petRepository.save(pet);
                if(!customer.getPets().contains(pet)){
                    customer.addPet(pet);
                    customerService.updateCustomer(customer);
                }
                return pet;
            }
        }
        return null;
    }


    public Pet getPet(long petId) {
        Optional<Pet> petOpt = petRepository.findById(petId);
        if(petOpt.isPresent()){
            return petOpt.get();
        }
        return null;
    }
    public List<Pet> getPetsByOwner(long ownerId) {
        Customer customer = customerService.getCustomerById(ownerId);
        if(customer == null){
            return Lists.emptyList();
        }
        return customer.getPets();
    }

    public List<Pet> getAllPets() {
        return  petRepository.findAll();
    }
}
