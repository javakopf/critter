package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserService {

    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private PetRepository petRepository;


    @Autowired
    public UserService(CustomerRepository customerRepository, EmployeeRepository employeeRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }


    public Customer createNewCustomer(Customer customer) {
        customer = this.customerRepository.save(customer);
        return customer;
    }


    public List<Customer> getAllCustomers() {
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        return customers;
    }


    public Employee createNewEmployee(Employee employee) {
        return this.employeeRepository.save(employee);
    }

    public Employee getEmployee(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        return employee;
    }

    public Customer getOwnerByPetId(long petId) {
        Customer customer = null;
        Optional<Pet> petOpt = petRepository.findById(petId);
        if(petOpt.isPresent()){
             customer = petOpt.get().getOwner();
        }
        return customer;
    }

    public Employee updateEmployee(Employee employee) {
         employee = this.employeeRepository.save(employee);
        return employee;
    }

    public List<Employee> getEmployeeBySkills(Set<EmployeeSkill> skills, LocalDate dayOfWeek) {
        List<Employee> result = new ArrayList<>();
        Iterable<Employee> employees = employeeRepository.findAll();
        employees.forEach(
                (employee) ->
                {
                    if(employee.getSkills().containsAll(skills) &&
                            employee.getDaysAvailable().contains(dayOfWeek.getDayOfWeek()))
                    {
                        result.add(employee);
                    }
                }
        );
        return result;
    }

    public List<Pet> getPetsForCustomer(long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if(customerOpt.isPresent()){
            return customerOpt.get().getPets();
        }
        return new ArrayList<>();
    }

    public List<Employee> getAllEmployees() {
        List<Employee> result = new ArrayList<>();
        employeeRepository.findAll().forEach(
                (employee) ->
                {
                    result.add(employee);
                }
        );
        return result;
    }

    public void addPetToCustomer(Customer customer, Pet pet) {
        List<Pet> pets = customer.getPets();
        if (pets == null) {
            pets = new ArrayList<>();
        }
        pets.add(pet);
        customer.setPets(pets);
        customerRepository.save(customer);
    }

    public Customer getCustomerById(Long ownerId) {
        Optional<Customer> customerOpt = customerRepository.findById(ownerId);
        if(customerOpt.isPresent()){
            return customerOpt.get();
        }
        return null;
    }

    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}