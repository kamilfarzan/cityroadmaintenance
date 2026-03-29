package com.group3.cityroad.config;

import com.group3.cityroad.entity.Administrator;
import com.group3.cityroad.entity.BranchOffice;
import com.group3.cityroad.entity.CityService;
import com.group3.cityroad.entity.Machine;
import com.group3.cityroad.entity.Manpower;
import com.group3.cityroad.entity.Mayor;
import com.group3.cityroad.entity.RawMaterial;
import com.group3.cityroad.repository.BranchOfficeRepository;
import com.group3.cityroad.repository.CityServiceRepository;
import com.group3.cityroad.repository.ResourceRepository;
import com.group3.cityroad.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BranchOfficeRepository branchOfficeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CityServiceRepository cityServiceRepository;
    private final ResourceRepository resourceRepository;

    public DataInitializer(BranchOfficeRepository branchOfficeRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           CityServiceRepository cityServiceRepository,
                           ResourceRepository resourceRepository) {
        this.branchOfficeRepository = branchOfficeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cityServiceRepository = cityServiceRepository;
        this.resourceRepository = resourceRepository;
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

        if (cityServiceRepository.count() == 0) {
            CityService s1 = new CityService("Central Public Library", "Education", "City's largest collection of books.", "Downtown Square", "1-800-LIBRARY");
            s1.setTiming("9AM - 8PM");
            CityService s2 = new CityService("Water & Sanitation Dept", "Utility", "Handles water billing and sewage lines.", "100 Utility Ave", "1-800-WATER");
            s2.setTiming("8AM - 5PM");
            CityService s3 = new CityService("Parks and Recreation", "Public Spaces", "Manages all city parks and public pools.", "Greenway Park", "1-800-PARKS");
            s3.setTiming("6AM - 10PM");
            
            cityServiceRepository.save(s1);
            cityServiceRepository.save(s2);
            cityServiceRepository.save(s3);
            System.out.println("Inserted mock City Services into the database.");
        }

        if (resourceRepository.count() == 0) {
            // Manpower
            resourceRepository.save(new Manpower("General Engineers", "Engineers", 5));
            resourceRepository.save(new Manpower("Field Labourers", "Labourers", 20));
            resourceRepository.save(new Manpower("Heavy Operators", "Operators", 8));
            resourceRepository.save(new Manpower("Site Inspector", "Inspector", 3));
            resourceRepository.save(new Manpower("Safety Officer", "Safety Officer", 4));
            
            // Machinery
            resourceRepository.save(new Machine("Excavators", "Excavators", "EX-001", 3));
            resourceRepository.save(new Machine("Rollers", "Rollers", "RL-002", 4));
            resourceRepository.save(new Machine("Mixer Truck", "Mixer Truck", "MT-003", 2));
            resourceRepository.save(new Machine("Water Tanker", "Water Tanker", "WT-004", 2));
            resourceRepository.save(new Machine("Sprayer", "Sprayer", "SP-005", 2));
            resourceRepository.save(new Machine("Jackhammer", "Jackhammer", "JH-006", 10));
            resourceRepository.save(new Machine("Sealer", "Sealer", "SL-007", 3));
            
            // Raw Materials
            resourceRepository.save(new RawMaterial("Hot Asphalt", "Asphalt", 50, 5000f));
            resourceRepository.save(new RawMaterial("Portland Cement", "Cement", 100, 2000f));
            resourceRepository.save(new RawMaterial("Road Paint", "Paint", 40, 400f));
            resourceRepository.save(new RawMaterial("Industrial Adhesives", "Adhesives", 30, 150f));
            
            System.out.println("Inserted precise mock Resources into the database.");
        }
    }
}
