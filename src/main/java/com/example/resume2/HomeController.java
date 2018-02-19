package com.example.resume2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.size;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    ResumeRepository resumeRepository;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/secure")
    public String admin(){
        return "secure";
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistrationPage(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model){
        model.addAttribute("user",user);
        if(result.hasErrors()){
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message","User Account Successfully Created");
        }
        return "index";
    }

    @RequestMapping("/summary")
    public String showSummary(Model model, Principal principal){
        Resume resume;
        int resumeCount = size(resumeRepository.findAll());
        /*System.out.println("Kim the number of items in the repository is " + resumeCount);*/
        if ( resumeCount > 0) {
            resume = resumeRepository.findTopByOrderByIdDesc();
        } else {
            resume = new Resume();
        }
        model.addAttribute("resume",resume);
        return "summary";
    }

    @RequestMapping("/processsummary")
    public String showSummary(@Valid @ModelAttribute("resume") Resume resume,BindingResult result){
        if (result.hasErrors()) {
            return "summary";
        }
        resumeRepository.save(resume);
        return "redirect:/";
    }
}
