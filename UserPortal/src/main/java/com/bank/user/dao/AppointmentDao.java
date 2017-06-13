package com.bank.user.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bank.user.domains.Appointment;

public interface AppointmentDao extends CrudRepository<Appointment, Long>{

	List<Appointment> findAll();
}
