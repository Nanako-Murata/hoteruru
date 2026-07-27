package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.form.SignupForm;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ユーザー登録前の確認 validation
    public void validateSignup(SignupForm signupForm, BindingResult bindingResult) {
        // メール重複確認
        if (isEmailRegistered(signupForm.getEmail())) {
            bindingResult.rejectValue("email", null, "すでに登録済みのメールアドレスです");
        }

        // パスワード確認
        if (!isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            bindingResult.rejectValue("password", null, "パスワードが一致しません");
        }
    }

    // validate signupの結果を受けてのユーザーの登録
    @Transactional
    public User create(SignupForm signupForm) {
        User user = new User();
        Role role = roleRepository.findByName("ROLE_GENERAL");

        user.setName(signupForm.getName());
        user.setFurigana(signupForm.getFurigana());
        user.setPostalCode(signupForm.getPostalCode());
        user.setAddress(signupForm.getAddress());
        user.setPhoneNumber(signupForm.getPhoneNumber());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setRole(role);
        user.setEnabled(false);// 初期値falseに設定

        return userRepository.save(user);

    }

    // メール重複確認
    public boolean isEmailRegistered(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;

    }

    // パスワードと確認用パスワードが一致するか確認するmethod
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

}
