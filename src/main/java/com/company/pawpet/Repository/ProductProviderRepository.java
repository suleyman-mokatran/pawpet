package com.company.pawpet.Repository;

import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.ProductProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductProviderRepository extends JpaRepository<ProductProvider,Integer> {

    @Query(value = "SELECT * FROM Product_Provider WHERE company_id = :companyId", nativeQuery = true)
    List<ProductProvider> findProductProviderByCompany(@Param("companyId") int companyId);

    List<ProductProvider> findByRole(Role role);

    ProductProvider findByUsername(String username);
}
