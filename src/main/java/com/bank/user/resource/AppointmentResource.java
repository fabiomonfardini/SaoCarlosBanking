package com.bank.user.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bank.user.domains.Appointment;
import com.bank.user.services.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
@PreAuthorize("hasRole('ADMIN')")
public class AppointmentResource {

    @Autowired
    private AppointmentService appointmentService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Appointment> findAppointmentList() {
        List<Appointment> appointmentList = appointmentService.findAll();

        return appointmentList;
    }

    @RequestMapping(value = "/{id}/confirm")
    public void confirmAppointment(@PathVariable("id") Long id) {
        appointmentService.confirmAppointment(id);
    }
}
