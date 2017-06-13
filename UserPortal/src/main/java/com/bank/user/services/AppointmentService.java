package com.bank.user.services;

import java.util.List;

import com.bank.user.domains.Appointment;

public interface AppointmentService {

	Appointment createAppointment(Appointment appointment);
	
	List<Appointment> findAll();
	
	Appointment findAppointment(Long id);
	
	void confirmAppointment(Long id);

}
