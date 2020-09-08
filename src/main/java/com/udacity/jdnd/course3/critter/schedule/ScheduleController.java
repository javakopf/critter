package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private ScheduleService scheduleService;
    private ScheduleConverter scheduleConverter;
    private UserService userServiceService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, ScheduleConverter scheduleConverter, UserService userServiceService) {
        this.scheduleService = scheduleService;
        this.scheduleConverter = scheduleConverter;
        this.userServiceService = userServiceService;
    }


    @PutMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
         Schedule schedule = scheduleService.createNewSchedule(
                 scheduleConverter.convertToScheduleEntity(scheduleDTO),
                 scheduleDTO.getEmployeeIds(),
                 scheduleDTO.getPetIds());
         return scheduleConverter.convertToScheduleDTO(schedule);
    }


    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> allSchedules = scheduleService.getAllSchedules();
        return scheduleConverter.convertSchedulesDtoList(allSchedules);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
       return scheduleConverter.convertSchedulesDtoList( scheduleService.getScheduleByPetId(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleConverter.convertSchedulesDtoList(scheduleService.getScheduleByEmployeeId(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Pet> pets = userServiceService.getPetsForCustomer(customerId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        pets.forEach(pet -> scheduleDTOS.addAll(scheduleConverter.convertSchedulesDtoList(scheduleService.getScheduleByPetId(pet.getId())) ));
        return scheduleDTOS;
    }


}
