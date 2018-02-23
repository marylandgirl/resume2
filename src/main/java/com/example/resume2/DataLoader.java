package com.example.resume2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public void run(String... strings) throws Exception {
        System.out.println("Loading data...");

        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        Role adminRole = roleRepository.findByRole("ADMIN");
        Role userRole = roleRepository.findByRole("USER");

        User user = new User("kjohnson@nasa.gov", "10101010", "Kathryn", "Johnson", true, "geekgirl");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        user = new User("charlesbrown@gmail.com", "snoopy", "Charles", "Brown", true, "chuck");
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);



    }
}
