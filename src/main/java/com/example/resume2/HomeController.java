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

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("user") User user, BindingResult result) {
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
    public String showSummary(Model model, Authentication principal){
        User user = userRepository.findByUsername(principal.getName());
        Resume resume = resumeRepository.findByUser(user);
        if (resume==null) {
            resume = new Resume();
            resume.setLastName(user.getLastName());
            resume.setFirstName(user.getFirstName());
            resume.setEmail(user.getEmail());
            resume.setUser(user);
        }

        System.out.println("Kim your in the catch block");

        System.out.println("Kim the values in resume are " + '\n' + resume.getEmail() + '\n' + resume.getLastName() + '\n' + resume.getFirstName());
        /*resumeRepository.save(resume);*/
        System.out.println("Kim the values in resume are " + '\n' + resume.getId() + '\n' + resume.getEmail() + '\n' + resume.getLastName() + '\n' + resume.getFirstName());
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

        resumeRepository.save(resume);
        return "redirect:/";
    }
}
