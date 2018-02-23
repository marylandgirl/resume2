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
        return "index";
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
        System.out.println("Kim you're in /summary requestmapping");
        model.addAttribute("resume",resume);
        return "summary";
    }

    @PostMapping("/processsummary")
    public String showSummary(@Valid @ModelAttribute("resume") Resume resume,BindingResult result){
        if (result.hasErrors()) {
              return "index";
        }
        System.out.println("Kim you're in /processsummary postmapping");
        resumeRepository.save(resume);
        return "redirect:/";
    }

    @RequestMapping("/contactinfo")
    public String contactInfo(Model model, Authentication principal){
        User user = userRepository.findByUsername(principal.getName());
        Resume resume = resumeRepository.findByUser(user);
        if (resume==null) {
            resume = new Resume();
            resume.setSummary("Blank");
        }
        resume.setUser(user);
        model.addAttribute("resume",resume);
        return "contactinfo";
    }

}
