package org.example.testassignment.repository;

import org.example.testassignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = true, value = "select * from users where id=:id")
    User findUserById(Long id);

    @Query("SELECT u FROM User u WHERE u.birthDate BETWEEN ?1 AND ?2 ORDER BY u.birthDate ASC")
    List<User> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate);
}
