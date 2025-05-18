package com.company.pawpet.Repository;

import com.company.pawpet.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query(value = "SELECT * FROM Appointment WHERE doctor_id = :doctorId", nativeQuery = true)
    List<Appointment> findAppointmentsByDoctor(@Param("doctorId") int doctorId);

    @Query(value = "SELECT * FROM Appointment WHERE booked = 1 && doctor_id = :doctorId", nativeQuery = true)
    List<Appointment> findBookedAppointmentsByDoctor(@Param("doctorId") int doctorId);

    @Query(value = "SELECT * FROM Appointment WHERE booked = 1 && service_id = :serviceId", nativeQuery = true)
    List<Appointment> findBookedAppointmentsByService(@Param("serviceId") int serviceId);

    @Query(value = "SELECT * FROM Appointment WHERE service_id = :serviceId", nativeQuery = true)
    List<Appointment> findAppointmentsByService(@Param("serviceId") int serviceId);

    @Query(value = "SELECT * FROM Appointment WHERE price = :price", nativeQuery = true)
    List<Appointment> findAppointmentsByPrice(@Param("price") float price);

    @Query(value = "SELECT * FROM Appointment WHERE status = :status", nativeQuery = true)
    List<Appointment> findAppointmentsByStatus(@Param("status") String status);

    @Query(value = "SELECT * FROM Appointment WHERE app_user_id = :appUserId", nativeQuery = true)
    List<Appointment> findAppointmentsByUser(@Param("appUserId") int appUserId);

    @Query(value = "SELECT a.*, u.* FROM appointment a JOIN appusers u ON a.app_user_id = u.app_user_id WHERE a.appointment_id = :appointmentId", nativeQuery = true)
    List<?> findAppointmentById(@Param("appointmentId") int appointmentId);

    @Query("SELECT a.selectedDate FROM Appointment a WHERE a.booked = true AND a.selectedDate >= CURRENT_DATE AND a.doctor.id = :doctorId")
    List<LocalDate> findUpcomingBookedDates(@Param("doctorId") int doctorId);


}
