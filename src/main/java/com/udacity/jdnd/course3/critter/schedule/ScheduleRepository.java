package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findScheduleByEmployeesContains(Employee employee);

    List<Schedule> findScheduleByPetsIsContaining(Pet pet);

    @Query("select p from Schedule p where :employee member of p.employees")
    List<Schedule> findScheduleByEmployee(Employee employee);

    @Query("select p from Schedule p where :pet member of p.pets")
    List<Schedule> findScheduleByPet(Pet pet);

}
