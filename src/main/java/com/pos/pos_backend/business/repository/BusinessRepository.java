package com.pos.pos_backend.business.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.pos_backend.business.entity.Business;

public interface BusinessRepository
        extends JpaRepository<Business, Long> {
}