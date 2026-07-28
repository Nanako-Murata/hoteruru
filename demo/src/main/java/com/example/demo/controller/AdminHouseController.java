package com.example.demo.controller;

import com.example.demo.service.HouseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.House;
import com.example.demo.form.HouseEditForm;
import com.example.demo.form.HouseRegisterForm;
import com.example.demo.repository.HouseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin/houses")
public class AdminHouseController {
    private final HouseService houseService;
    private final HouseRepository houseRepository;

    public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
        this.houseRepository = houseRepository;
        this.houseService = houseService;
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

    // 民泊の新規追加フォームの表示
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("houseRegisterForm", new HouseRegisterForm());
        return "admin/houses/register";
    }

    // 民泊の登録
    @PostMapping("/create")
    public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // TODO: process POST request
        if (bindingResult.hasErrors()) {
            return "admin/houses/register";
        }
        houseService.create(houseRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "民泊を予約しました");
        return "redirect:/admin/houses";

    }

    // 民泊の編集画面の表示
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "民宿が見つかりません"));
        String imageName = house.getImageName();
        HouseEditForm houseEditForm = new HouseEditForm(house.getId(), house.getName(), null, house.getDescription(),
                house.getPrice(), house.getCapacity(), house.getPostalCode(), house.getAddress(),
                house.getPhoneNumber());
        model.addAttribute("imageName", imageName);
        model.addAttribute("houseEditForm", houseEditForm);
        return "admin/houses/edit";
    }

    // 民泊の情報編集
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        // TODO: process POST request
        if (bindingResult.hasErrors()) {
            return "admin/houses/edit";
        }
        houseService.update(houseEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "民泊情報を編集しました");

        return "redirect:/admin/houses";
    }

    // 民泊を削除する
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        houseRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "民泊を削除しました");
        return "redirect:/admin/houses";
    }
}
