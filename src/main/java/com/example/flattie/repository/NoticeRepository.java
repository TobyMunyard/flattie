package com.example.flattie.repository;

import org.springframework.stereotype.Repository;

import com.example.flattie.model.Notice;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    public Notice getNoticeById(Long id);
}
