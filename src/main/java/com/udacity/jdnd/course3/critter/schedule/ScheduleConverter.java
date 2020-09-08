package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleConverter {

    public Schedule convertToScheduleEntity(ScheduleDTO dto){
        Schedule entity = new Schedule();
        BeanUtils.copyProperties(dto,entity);
        return entity;
    }


    public ScheduleDTO convertToScheduleDTO(Schedule entity){
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


    public List<ScheduleDTO> convertSchedulesDtoList(Iterable<Schedule> schedules){
        List<ScheduleDTO> result = new ArrayList<>();
        schedules.forEach(schedule -> result.add(convertToScheduleDTO(schedule)));
        return result;
    }
}
