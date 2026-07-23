package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.House;
import com.example.demo.repository.HouseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/admin/houses")
public class HouseController {
    private final HouseRepository houseRepository;

    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @GetMapping
    public String index(Model model,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
            @RequestParam(name = "keyword", required = false) String keyword) {
        Page<House> housePage;

        if (keyword != null && !keyword.isEmpty()) {
            housePage = houseRepository.findByNameContaining(keyword, pageable);
        } else {
            housePage = houseRepository.findAll(pageable);
        }
        model.addAttribute("housePage", housePage);
        model.addAttribute("keyword", keyword);

        return "admin/houses/index";
    }

    // 民泊の詳細画面
    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "民泊が見つかりません"));
        model.addAttribute("house", house);

        return "admin/houses/show";
    }
}
