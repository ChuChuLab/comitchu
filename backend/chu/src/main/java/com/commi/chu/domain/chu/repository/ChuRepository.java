package com.commi.chu.domain.chu.repository;

import com.commi.chu.domain.chu.entity.Chu;
import com.commi.chu.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChuRepository extends JpaRepository<Chu, Integer> {

    Optional<Chu> findByUser(User user);

}
