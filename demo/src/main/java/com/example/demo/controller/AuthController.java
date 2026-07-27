package com.example.demo.controller;

import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.entity.User;
import com.example.demo.event.SignupEventPublisher;
import com.example.demo.form.SignupForm;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final UserService userService;
    private final SignupEventPublisher signupEventPublisher;

    AuthController(UserService userService, SignupEventPublisher signupEventPublisher) {
        this.userService = userService;
        this.signupEventPublisher = signupEventPublisher;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        // メールアドレス重複登録防止
        if (userService.isEmailRegistered(signupForm.getEmail())) {
            FieldError error = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです");
            bindingResult.addError(error);

        }
        // パスワードとパスワード確認用が一致していることの確認
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError error = new FieldError(bindingResult.getObjectName(), "email", "パスワードが一致しません");
            bindingResult.addError(error);

        }

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        User createdUser = userService.create(signupForm);
        String requestUrl = new String(httpServletRequest.getRequestURL());
        signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
        redirectAttributes.addFlashAttribute("successMessage",
                "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックしてください");
        return "redirect:/";

    }
}
