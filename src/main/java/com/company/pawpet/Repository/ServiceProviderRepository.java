package com.company.pawpet.Repository;

import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider,Integer> {

    @Query(value = "SELECT * FROM Service_Provider WHERE company_id = :companyId", nativeQuery = true)
    List<ServiceProvider> findServiceProviderByCompany(@Param("companyId") int companyId);

    List<ServiceProvider> findByRole(Role role);

    ServiceProvider findByUsername(String username);

    @Query(value = "SELECT available_days FROM service_provider_available_days WHERE available_days_key = :day AND service_provider_app_user_id = :spId", nativeQuery = true)
    String findByDayAndSpId(@Param("day") String day, @Param("spId") int spId);
}
