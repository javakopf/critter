package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
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


    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);
        customer = this.customerRepository.save(customer);
        CustomerDTO customerDTO1 = this.convertToDTO(customer);
        return customerDTO1;
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setNotes(customer.getNotes());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        List<Long> petIds = new ArrayList<>();
        customer.getPets().forEach((pet) -> {
            petIds.add(pet.getId());
        });
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }


    private Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = null;
        if (customerDTO.getId() > 0) {
            customer = new Customer(customerDTO.getId(), customerDTO.getName());
        } else {
            customer = new Customer();
            customer.setName(customerDTO.getName());
            customer.setPhoneNumber(customerDTO.getPhoneNumber());
            customer.setNotes(customerDTO.getNotes());
            List<Pet> pets = new ArrayList<>();
            if (customerDTO.getPetIds() != null && !customerDTO.getPetIds().isEmpty()) {
                customerDTO.getPetIds().forEach(
                        (petId) -> {
                            pets.add(petRepository.findById(petId).get());
                        }
                );
            }
            customer.setPets(pets);
        }
        return customer;
    }


    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> customersDTO = new ArrayList<>();
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        customers.forEach(
                (customer) -> {
                    customersDTO.add(convertToDTO(customer));
                }
        );
        return customersDTO;
    }

    public EmployeeDTO createNewEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEmployeeEntity(employeeDTO);
        employee = this.employeeRepository.save(employee);
        return this.convertToEmployeeDTO(employee);
    }

    private Employee convertToEmployeeEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        return  employee;
    }

    private EmployeeDTO convertToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee,employeeDTO);
        return  employeeDTO;
    }

    public EmployeeDTO getEmployee(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        return convertToEmployeeDTO(employee);
    }

    public CustomerDTO getOwnerByPetId(long petId) {
        CustomerDTO  result = null;
        Optional<Pet> petOpt = petRepository.findById(petId);
        if(petOpt.isPresent()){
            Customer customer = petOpt.get().getOwner();
            result = convertToDTO(customer);
        }
        return result;
    }

    public void updateEmployee(EmployeeDTO employeeDTO) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeDTO.getId());
        if(employeeOpt.isPresent()){
            Employee employee = convertToEmployeeEntity(employeeDTO);
            employee = this.employeeRepository.save(employee);
            employeeDTO = convertToEmployeeDTO(employee);
        }
    }

    public List<EmployeeDTO> getEmployeeBySills(Set<EmployeeSkill> skills) {
        List<EmployeeDTO> result = new ArrayList<>();
        Iterable<Employee> employees = employeeRepository.findAll();
        employees.forEach(
                (employee) ->
                {
                    if(employee.getSkills().containsAll(skills))
                    {
                        result.add(convertToEmployeeDTO(employee));
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
}