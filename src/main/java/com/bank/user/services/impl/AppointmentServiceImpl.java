package com.bank.user.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.user.dao.AppointmentDao;
import com.bank.user.domains.Appointment;
import com.bank.user.services.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentDao appointmentDao;

	@Override
	public Appointment createAppointment(Appointment appointment) {
		return appointmentDao.save(appointment);
	}

	@Override
	public List<Appointment> findAll() {
		return appointmentDao.findAll();
	}

	@Override
	public Appointment findAppointment(Long id) {
		return appointmentDao.findOne(id);
	}

	@Override
	public void confirmAppointment(Long id) {
		Appointment appointment = appointmentDao.findOne(id);
		appointment.setConfirmed(true);
		appointmentDao.save(appointment);
	}

}
