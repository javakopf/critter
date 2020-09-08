package com.udacity.jdnd.course3.critter.user;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final UserConverter converter;

    @Autowired
    public UserController(UserService userService, UserConverter converter) {
        this.userService = userService;
        this.converter = converter;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = converter.convertToCustomerEntity(customerDTO, Lists.emptyList());
        customer = userService.createNewCustomer(customer);
      return  converter.convertToCustomerDTO(customer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer>  customers = userService.getAllCustomers();
        List<CustomerDTO> allCustomers = new ArrayList<>();
        customers.forEach(
                (customer) -> {
                    allCustomers.add(converter.convertToCustomerDTO(customer));
                }
            );
        return  allCustomers;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer = userService.getOwnerByPetId(petId);
        return converter.convertToCustomerDTO(customer) ;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = converter.convertToEmployeeEntity(employeeDTO);
        employee = userService.createNewEmployee(employee);
        return converter.convertToEmployeeDTO(employee);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = userService.getEmployee(employeeId);
        return converter.convertToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
         Employee employee = userService.getEmployee(employeeId);
        employee.setDaysAvailable(daysAvailable);
         userService.updateEmployee(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
     List<Employee> employees = userService.getEmployeeBySkills(employeeDTO.getSkills(),employeeDTO.getDate());
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        employees.forEach(
                (employee) -> {
                    employeeDTOS.add(converter.convertToEmployeeDTO(employee));
                }
        );
     return employeeDTOS;
    }

    @GetMapping("/employee/all")
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = userService.getAllEmployees();
        List<EmployeeDTO> allEmployees = new ArrayList<>();
        employees.forEach(
                (employee) -> {
                    allEmployees.add(converter.convertToEmployeeDTO(employee));
                }
        );
        return allEmployees;
    }
}
