package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Boolean existsByEmail(String email);
    List<User> getUsersByRoleEntityId(Long roleId);

    @Query("SELECT COUNT(u) FROM User u WHERE u.roleEntity.name = 'customer'")
    Long countCustomers();

//    @Query("SELECT u\n" +
//            "FROM users u\n"+
//            "WHERE \t(:roleId IS NULL OR :roleId = 0 OR :roleId = u.role_id)\n"
////            "   AND (:email IS NULL OR :email = '' OR u.email LIKE CONCAT('%', :email, '%'))\n" +
////            "   AND (:phoneNumber IS NULL OR :phoneNumber = '' OR phone_number LIKE CONCAT('%', :phoneNumber, '%'))\n" +
////            "   AND (:keyword IS NULL OR :keyword = '' OR u.full_name LIKE CONCAT('%', :keyword, '%') OR u.address LIKE CONCAT('%', :keyword, '%')"
//    )
//    Page<User> searchUser(
//            @Param("roleId") Long roleId,
////            @Param("email") String email,
////            @Param("phoneNumber") String phoneNumber,
////            @Param("keyword") String keyword,
//            Pageable pageable
//    );

    @Query("SELECT u FROM User u WHERE " +
            "(:fullName = '' OR u.fullName LIKE %:fullName%) " +
            "AND (:roleId = 0 OR u.roleEntity.id = :roleId) " +
            "AND (:phoneNumber = '' OR u.phoneNumber = :phoneNumber)"+
            "AND (:email = '' OR u.email LIKE %:email%)"
    )
    Page<User> searchUsers(@Param("fullName") String fullName,
                           @Param("roleId") Long roleId,
                           @Param("phoneNumber") String phoneNumber,
                           @Param("email") String email,
                           Pageable pageable);
}