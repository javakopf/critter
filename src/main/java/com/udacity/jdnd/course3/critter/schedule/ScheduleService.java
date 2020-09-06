package com.udacity.jdnd.course3.critter.schedule;


import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
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

    public List<ScheduleDTO> getAllSchedules() {
        return convertSchedulesDtoList(scheduleRepository.findAll());
    }

    public ScheduleDTO createNewSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = convertToScheduleEntity(scheduleDTO);
        List<Employee> employees = new ArrayList<>();
        scheduleDTO.getEmployeeIds().forEach(empId -> employees.add(employeeRepository.findById(empId).get()));
        schedule.setEmployees(employees);
        List<Pet> pets = new ArrayList<>();
        scheduleDTO.getPetIds().forEach(petId -> pets.add(petRepository.findById(petId).get()));
        schedule.setPets(pets);
        schedule = scheduleRepository.save(schedule);
        return convertToScheduleDTO(schedule);
    }

    private Schedule convertToScheduleEntity(ScheduleDTO dto){
        Schedule entity = new Schedule();
        BeanUtils.copyProperties(dto,entity);
        return entity;
    }


    private ScheduleDTO convertToScheduleDTO(Schedule entity){
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(entity,dto);
        List<Long> empIds = new ArrayList<>();
        entity.getEmployees().forEach(employee -> empIds.add(employee.getId()));
        dto.setEmployeeIds(empIds);
        List<Long> petIds = new ArrayList<>();
        entity.getPets().forEach(pet -> petIds.add(pet.getId()));
        dto.setPetIds(petIds);
        return dto;
    }

    public List<ScheduleDTO> getScheduleByPetId(long petId) {
        Optional<Pet> petOpt = petRepository.findById(petId);
        if(petOpt.isPresent()){
            return convertSchedulesDtoList(scheduleRepository.findScheduleByPetsIsContaining(petOpt.get()));
        }
        return new ArrayList<>();
    }
    public List<ScheduleDTO> getScheduleByEmployeeId(long empId) {
        Optional<Employee> empOpt = employeeRepository.findById(empId);
        if(empOpt.isPresent()){
            Employee employee = empOpt.get();
            List<Schedule> employees = scheduleRepository.findScheduleByEmployee(employee);
            if(employees.isEmpty())scheduleRepository.findScheduleByEmployee(employee);

            return convertSchedulesDtoList(employees);
        }
        return new ArrayList<>();
    }

    private List<ScheduleDTO> convertSchedulesDtoList(Iterable<Schedule> schedules){
        List<ScheduleDTO> result = new ArrayList<>();
        schedules.forEach(schedule -> result.add(convertToScheduleDTO(schedule)));
        return result;
    }
}
