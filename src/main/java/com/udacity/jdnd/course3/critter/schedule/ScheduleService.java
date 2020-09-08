package com.udacity.jdnd.course3.critter.schedule;


import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final PetRepository petRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, EmployeeRepository employeeRepository, PetRepository petRepository) {
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }


    public Schedule createNewSchedule(Schedule schedule,List<Long> employeeIds,List<Long> petIds) {
        List<Employee> employees = new ArrayList<>();
        employeeIds.forEach(empId -> employees.add(employeeRepository.findById(empId).get()));
        schedule.setEmployees(employees);
        List<Pet> pets = new ArrayList<>();
        petIds.forEach(petId -> pets.add(petRepository.findById(petId).get()));
        schedule.setPets(pets);
        schedule = scheduleRepository.save(schedule);
        return schedule;
    }


    public List<Schedule> getScheduleByPetId(long petId) {
        Optional<Pet> petOpt = petRepository.findById(petId);
        if(petOpt.isPresent()){
            return scheduleRepository.findScheduleByPetsIsContaining(petOpt.get());
        }
        return new ArrayList<>();
    }

    public List<Schedule> getScheduleByEmployeeId(long empId) {
        Optional<Employee> empOpt = employeeRepository.findById(empId);
        if(empOpt.isPresent()){
            Employee employee = empOpt.get();
            List<Schedule> schedules = scheduleRepository.findScheduleByEmployee(employee);
            return schedules;
        }
        return new ArrayList<>();
    }

}
