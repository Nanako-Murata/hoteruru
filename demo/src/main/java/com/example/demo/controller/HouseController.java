package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.House;
import com.example.demo.repository.HouseRepository;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/admin/houses")
public class HouseController {
    private final HouseRepository houseRepository;
    
    public HouseController(HouseRepository houseRepository){
        this.houseRepository=houseRepository;
    }

    @GetMapping("/")
    public String index(Model model){
        List<House> houses = houseRepository.findAll();
        model.addAttribute("houses", houses);
        return "admin/houses/index";

    }
    }