package ru.kata.spring.boot_security.demo.controller;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.Model.User;
import ru.kata.spring.boot_security.demo.Model.Role;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@Transactional
public class MyController {
    @Autowired
    private UserServiceImp userService;
    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;


    @GetMapping("/admin/admin")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String us=auth.getName();
        model.addAttribute("user", userService.findUserByName(us));
        model.addAttribute("newuser", new User());
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("listRoles", listRoles);
        return "admin/admin";
    }

    @PostMapping("/admin/admin")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            userService.deleteUser(userId);
        }
        return "redirect:/admin/admin";
    }

    @GetMapping("/user")
    public String userdt(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String us=auth.getName();
        User user = userService.findUserByName(us);
        if (user == null) {user = new User();}
        model.addAttribute("user", user);
        return "user";
    }

    @RequestMapping (value = "/admin/edit", method= RequestMethod.GET)
    public String edit(ModelMap model, @RequestParam Long id) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("listRoles", listRoles);
        return "admin/edit";
    }

    @RequestMapping(value="/admin/edit", method=RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute User model,
                             @RequestParam(value="action", required=true) String action) {
        switch(action) {
            case "save":
                userService.saveUser(model);
                break;
            case "cancel":
                // do stuff
                break;
            case "newthing":
                // do stuff
                break;
            default:
                // do stuff
                break;
        }
        return new ModelAndView( "redirect:/admin/admin");
    }

    @RequestMapping (value = "/admin/delete", method= RequestMethod.GET)
    public String del(ModelMap model, @RequestParam Long id) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("listRoles", listRoles);
        return "admin/delete";
    }

    @RequestMapping(value="delete", method= RequestMethod.POST)
    public String deleteItem(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/admin";
    }
    @RequestMapping(value = "/admin/adminadd", method=RequestMethod.GET)
    public String add(ModelMap model) {
        User user = new User();
        model.addAttribute("user", user);
        return "admin/adminadd";
    }

    @RequestMapping(value="/admin/adminadd", method=RequestMethod.POST)
    public String addNewOrder(@ModelAttribute User model) {
        userService.saveUser(model);
        return "redirect:/admin/admin";
    }
}

