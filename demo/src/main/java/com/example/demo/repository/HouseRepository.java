package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.House;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer>{

}
