package com.example.demo.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.House;
import com.example.demo.form.HouseEditForm;
import com.example.demo.form.HouseRegisterForm;
import com.example.demo.repository.HouseRepository;

import jakarta.transaction.Transactional;

@Service

public class HouseService {
    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Transactional
    public void create(HouseRegisterForm houseRegisterForm) {
        House house = new House();
        MultipartFile imageFile = houseRegisterForm.getImageFile();

        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();
            if (imageName == null) {
                imageName = "unknown";
            }
            String hashedImageName = generateNewFileName(imageName);
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
            copyImageFile(imageFile, filePath);
            house.setImageName(hashedImageName);

        }
        house.setName(houseRegisterForm.getName());
        house.setDescription(houseRegisterForm.getDescription());
        house.setPrice(houseRegisterForm.getPrice());
        house.setCapacity(houseRegisterForm.getCapacity());
        house.setPostalCode(houseRegisterForm.getPostalCode());
        house.setAddress(houseRegisterForm.getAddress());
        house.setPhoneNumber(houseRegisterForm.getPhoneNumber());

        houseRepository.save(house);
    }

    // UUIDを使って生成したファイル名を返す
    public String generateNewFileName(String fileName) {
        String extension = "";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            extension = fileName.substring(dotIndex);

        }
        return UUID.randomUUID() + extension;

    }

    // 画像ファイルを指定しファイルにコピーする
    public void copyImageFile(MultipartFile imageFile, Path filePath) {
        try {
            Files.copy(imageFile.getInputStream(), filePath);

        } catch (IOException e) {
            throw new UncheckedIOException("画像の保存に失敗しました", e);

        }
    }

    // 民泊を更新する
    @Transactional
    public void update(HouseEditForm houseEditForm) {
        House house = houseRepository.findById(houseEditForm.getId()).orElseThrow();
        MultipartFile imageFile = houseEditForm.getImageFile();
        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();
            String hashedImageName = generateNewFileName(imageName);
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
            copyImageFile(imageFile, filePath);
            house.setImageName(hashedImageName);
        }
        house.setName(houseEditForm.getName());
        house.setDescription(houseEditForm.getDescription());
        house.setPrice(houseEditForm.getPrice());
        house.setCapacity(houseEditForm.getCapacity());
        house.setPostalCode(houseEditForm.getPostalCode());
        house.setAddress(houseEditForm.getAddress());
        house.setPhoneNumber(houseEditForm.getPhoneNumber());

        houseRepository.save(house);
    }

}
