package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.House;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer> {
    public Page<House> findByNameContaining(String keyword, Pageable pageable);
    public Page<House> findByNameContainingOrAddressContaining(String nameKeyword, String addressKeyword, Pageable pageable);
    public Page<House> findByAddressContaining(String addressKeyword, Pageable pageable);
    public Page<House> findByPriceLessThanEqual(Integer price, Pageable pageable);

    public List<House> findTop10ByOrderByCreatedAtDesc();

}
