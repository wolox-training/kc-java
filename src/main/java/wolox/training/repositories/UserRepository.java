package wolox.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE (CAST(:startDate AS date) IS NULL OR u.birthdate >= :startDate)"
            + " AND (CAST(:endDate AS date) IS NULL OR u.birthdate <= :endDate)"
            + " AND (:name IS NULL OR LOWER(u.name) LIKE %:name%)")
    List<User> findByBirthdateBetweenAndNameContainingIgnoreCase(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("name") String name);

    @Query("SELECT u FROM User u WHERE (CAST(:startDate AS date) IS NULL OR u.birthdate >= :startDate)"
            + " AND (CAST(:endDate AS date) IS NULL OR u.birthdate <= :endDate)"
            + " AND (:name IS NULL OR LOWER(u.name) LIKE %:name%)"
            + " AND (:username IS NULL OR LOWER(u.username) LIKE %:username%)")
    Optional<User> findAll(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("name") String name, @Param("username") String username);


}
