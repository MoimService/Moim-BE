package com.codeit.moim.repository;

import com.codeit.moim.domain.Contact;
import com.codeit.moim.domain.User;
import lombok.Builder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
