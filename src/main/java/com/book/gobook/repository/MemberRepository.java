package com.book.gobook.repository;

import com.book.gobook.model.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Integer> {

    Optional<Members> findById(String id);
    Members findByNameAndPhoneAndEmail(String name, String phone, String email);
    Members findByIdAndEmail(String id, String email);

    Optional<Members> findByQrCode(String qrCodeValue);
}
