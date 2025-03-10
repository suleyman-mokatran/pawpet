package com.company.pawpet.Repository;

import com.company.pawpet.Enum.Role;
import com.company.pawpet.Model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    AppUser findByUsername(String username);

    List<AppUser> findByRole(Role role);

}
