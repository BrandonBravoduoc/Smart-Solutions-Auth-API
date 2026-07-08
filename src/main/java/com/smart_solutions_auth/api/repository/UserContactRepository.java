package com.smart_solutions_auth.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart_solutions_auth.api.model.entity.UserContact;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserContact> findByUserEmail(String email);

    Optional<UserContact> findByUserId(Long userId);

    Optional<UserContact> findByPhoneNumber(String phoneNumber);

    List<UserContact> findByUserAddressId(Long addressId);

    List<UserContact> findByUserAddress_Commune_Id(Long communeId);

    List<UserContact> findByUserAddress_Commune_Region_Id(Long regionId);

}
