package com.example.resume2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.core.Authentication;

import javax.validation.Valid;
import java.security.Principal;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.size;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    UserRepository userRepository;


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
        User user = userRepository.findByUsername(principal.getName());
        try {
            if (resumeRepository.findByUser(user).equals(null)) {
                resume = new Resume();
            } else {
                resume = resumeRepository.findByUser(user);
            }
        } catch (NullPointerException e) {
            resume = new Resume();
        }

        model.addAttribute("resume",resume);
        return "summary";
    }

    @PostMapping("/processsummary")
    public String showSummary(@Valid @ModelAttribute("resume") Resume resume,BindingResult result){
        if (result.hasErrors()) {
            System.out.println("Kim there were errors " + result.getAllErrors());
            String str = resume.getSummary();
            return "index";
        }
        System.out.println("Kim the id for this resume is "  + resume.getId());

        resumeRepository.save(resume);
        return "redirect:/";
    }
}
