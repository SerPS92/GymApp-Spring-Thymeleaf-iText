package com.example.GymApp.controller;

import com.example.GymApp.model.User;
import com.example.GymApp.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String list(Model model){
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user/users";
    }

    @GetMapping("/create")
    public String create(){
        return "user/create";
    }

    @PostMapping("/save")
    public String save(User user){
        userService.save(user);
        return "redirect:/user/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id")Integer id,
                       Model model){
        Optional<User> optionalUser = userService.findById(id);
        User user = new User();
        user = optionalUser.get();
        model.addAttribute("user", user);

        return "user/edit";
    }

    @PostMapping("/update")
    public String update(User user){
        userService.update(user);
        return "redirect:/user/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id")Integer id){
        userService.delete(id);
        return "redirect:/user/";
    }
}
