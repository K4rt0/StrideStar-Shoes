package com.stores.stridestar.controllers;

import com.stores.stridestar.models.User;
import com.stores.stridestar.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "/main-site/user/login";
    }


    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User()); // Thêm một đối tượng User mới vào model
        return "/main-site/user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, // Validate đối tượng User
                           @NotNull BindingResult bindingResult, // Kết quả của quá trình validate
                           Model model) {
        if (bindingResult.hasErrors()) { // Kiểm tra nếu có lỗi validate
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "/main-site/user/register"; // Trả về lại view "register" nếu có lỗi
        }
        userService.save(user); // Lưu người dùng vào cơ sở dữ liệu
        userService.setDefaultRole(user.getUsername()); // Gán vai trò mặc định cho người dùng
        return "redirect:/login"; // Chuyển hướng người dùng tới trang "login"
    }

    @GetMapping("/account")
    public String getAccountDetail(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        model.addAttribute("user", user);
        return "/main-site/user/account-detail";
    }

    @PostMapping("/account")
    public String updateAccountDetail(@ModelAttribute("user") User userForm, Model model, Authentication authentication) {
        try {
            // Lấy thông tin người dùng hiện tại
            User currentUser = userService.findByUsername(authentication.getName()).orElseThrow();

            // Cập nhật thông tin người dùng hiện tại dựa trên dữ liệu từ form
            currentUser.setFullName(userForm.getFullName());
            currentUser.setEmail(userForm.getEmail());
            currentUser.setPhoneNumber(userForm.getPhoneNumber());
            currentUser.setAddress(userForm.getAddress());

            // Cập nhật mật khẩu mới nếu có
            if (userForm.getNewPassword() != null && !userForm.getNewPassword().isEmpty()) {
                if (!userForm.getNewPassword().equals(userForm.getConfirmPassword())) {
                    model.addAttribute("error", "Passwords do not match");
                    model.addAttribute("user", currentUser);
                    return "/main-site/user/account-detail";
                }
                currentUser.setNewPassword(userForm.getNewPassword());
                currentUser.setConfirmPassword(userForm.getConfirmPassword());
            }

            // Lưu thông tin đã cập nhật vào cơ sở dữ liệu
            userService.updateUser(currentUser);

            return "redirect:/account-detail?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", userForm);
            return "/main-site/user/account-detail";
        }
    }
}
