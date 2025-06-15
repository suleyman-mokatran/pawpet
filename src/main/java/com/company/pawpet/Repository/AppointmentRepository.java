package com.company.pawpet.Repository;

import com.company.pawpet.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @Query(value = "Select booked from appointment where doctor_id =:doctorId", nativeQuery = true)
    List<Boolean> findBookingInsights(@Param("doctorId") int doctorId);

    @Query(
            value = "SELECT selected_date AS selectedDate, COUNT(*) AS count FROM Appointment WHERE booked = 1 AND doctor_id = :doctorId GROUP BY selected_date",
            nativeQuery = true
    )
    List<Map<String, Object>> findBookedDates(@Param("doctorId") int doctorId);

    @Query(value = "select distinct app_user_id from appointment where doctor_id = :doctorId && app_user_id IS NOT NULL",nativeQuery = true)
    List<Integer> numberOfDifferentUsers(@Param("doctorId") int doctorId);

    @Query(value = "select count(status) from appointment where doctor_id = :doctorId AND status = 'done'",nativeQuery = true)
    Integer numberOfAttendedUsers(@Param("doctorId") int doctorId);

    @Query(value = "select count(*) from appointment where booked = 1 AND doctor_id = :doctorId",nativeQuery = true)
    Integer findNumbersOfBookedAppointmentsByDoctor(@Param("doctorId") int doctorId);

    @Query(value = "select  type AS type  ,count(*) AS count from appointment where app_user_id = :userId group by type",nativeQuery = true)
    List<Map<String, Object>> findBookedAppointmentsType(@Param("userId") int userId);

}
