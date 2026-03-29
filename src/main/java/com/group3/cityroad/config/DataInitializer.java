package com.group3.cityroad.config;

import com.group3.cityroad.entity.Administrator;
import com.group3.cityroad.entity.BranchOffice;
import com.group3.cityroad.entity.Mayor;
import com.group3.cityroad.repository.BranchOfficeRepository;
import com.group3.cityroad.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BranchOfficeRepository branchOfficeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(BranchOfficeRepository branchOfficeRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.branchOfficeRepository = branchOfficeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (branchOfficeRepository.count() == 0) {
            BranchOffice branch1 = new BranchOffice();
            branch1.setName("North District Office");
            branch1.setSuburb("North");
            branch1.setAreaJurisdiction("Northern City Jurisdiction");

            BranchOffice branch2 = new BranchOffice();
            branch2.setName("South District Office");
            branch2.setSuburb("South");
            branch2.setAreaJurisdiction("Southern City Jurisdiction");

            BranchOffice branch3 = new BranchOffice();
            branch3.setName("Central District Office");
            branch3.setSuburb("Central");
            branch3.setAreaJurisdiction("Central City Jurisdiction");

            branchOfficeRepository.save(branch1);
            branchOfficeRepository.save(branch2);
            branchOfficeRepository.save(branch3);
            
            System.out.println("Inserted mock Branch Offices into the database.");
        }

        if (userRepository.findByUsername("mayorAdmin").isEmpty()) {
            Mayor mayor = new Mayor();
            mayor.setUsername("mayorAdmin");
            mayor.setName("City Mayor");
            mayor.setPasswordHash(passwordEncoder.encode("mayor123"));
            userRepository.save(mayor);
            System.out.println("Inserted default Mayor account (mayorAdmin / mayor123).");
        }

        if (userRepository.findByUsername("adminUser").isEmpty()) {
            Administrator admin = new Administrator();
            admin.setUsername("adminUser");
            admin.setName("System Administrator");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            userRepository.save(admin);
            System.out.println("Inserted default Administrator account (adminUser / admin123).");
        }
    }
}
