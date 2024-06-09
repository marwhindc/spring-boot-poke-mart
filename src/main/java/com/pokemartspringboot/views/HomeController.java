package com.pokemartspringboot.views;

import java.io.FileNotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;

import com.pokemartspringboot.reports.ReportService;
import com.pokemartspringboot.user.UserService;

//@Controller
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class HomeController {

    UserService userService;
    ReportService reportService;

    //@GetMapping
    //public String viewHomePage(Model model) {
    //    model.addAttribute("users", userService.findAll());
    //    return "index";
    //}

    @GetMapping("/report")
    public String generateReport() throws JRException, FileNotFoundException {
        reportService.exportReport();
        return "File generated!";
    }

}
