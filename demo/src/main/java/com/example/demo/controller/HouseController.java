package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.House;
import com.example.demo.repository.HouseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/houses")
public class HouseController {
    private final HouseRepository houseRepository;

    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    // 民泊一覧
    @GetMapping
    public String index(@RequestParam(required = false) String keyword, @RequestParam(required = false) String area,
            @RequestParam(required = false) Integer price, @RequestParam(required = false) String order,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            Model model) {
        // 並び替え条件を組み立てる
        // priceASCなら価格の安い順で、それ以外なら新着順で並べ替える
        Sort sort = "priceAsc".equals(order)
                ? Sort.by(Sort.Direction.ASC, "price")
                : Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, 10, sort);

        Page<House> housePage;
        if (keyword != null && !keyword.isEmpty()) {
            housePage = houseRepository.findByNameContainingOrAddressContaining(keyword, keyword, pageable);

        } else if (area != null && !area.isEmpty()) {
            housePage = houseRepository.findByAddressContaining(area, pageable);

        } else if (price != null) {
            housePage = houseRepository.findByPriceLessThanEqual(price, pageable);

        } else {
            housePage = houseRepository.findAll(pageable);
        }
        model.addAttribute("housePage", housePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("area", area);
        model.addAttribute("price", price);

        return "houses/index";

    }

    // 民泊詳細画面
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        House house = houseRepository.findById(id).orElseThrow(() -> new RuntimeException("民泊が見つかりません"));

        model.addAttribute("house", house);
        return "houses/show";
    }
}
