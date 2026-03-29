package com.group3.cityroad.service;

import com.group3.cityroad.entity.*;
import com.group3.cityroad.enums.RoleEnum;
import com.group3.cityroad.exception.InvalidCredentialsException;
import com.group3.cityroad.repository.BranchOfficeRepository;
import com.group3.cityroad.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final BranchOfficeRepository branchOfficeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, 
                                 BranchOfficeRepository branchOfficeRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.branchOfficeRepository = branchOfficeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Username not found"));
                
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Incorrect password");
        }
        
        return user;
    }

    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public User register(String username, String name, String password, RoleEnum role, Map<String, String> extraFields) {
        if (usernameExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        String hash = passwordEncoder.encode(password);
        User newUser = null;

        switch (role) {
            case RESIDENT:
                String address = extraFields.getOrDefault("address", "");
                String phone = extraFields.getOrDefault("phone", "");
                String area = extraFields.getOrDefault("area", "");
                newUser = new Resident(username, name, hash, address, phone, area);
                break;
            case SUPERVISOR:
                String branchIdStr = extraFields.get("branchOfficeId");
                if (branchIdStr == null || branchIdStr.isEmpty()) {
                    throw new IllegalArgumentException("Supervisor must belong to a Branch Office");
                }
                Long branchId = Long.parseLong(branchIdStr);
                BranchOffice branch = branchOfficeRepository.findById(branchId)
                        .orElseThrow(() -> new IllegalArgumentException("Branch office not found"));
                        
                newUser = new Supervisor(username, name, hash, branch);
                break;
            case ADMINISTRATOR:
                Administrator admin = new Administrator();
                admin.setUsername(username);
                admin.setName(name);
                admin.setPasswordHash(hash);
                newUser = admin;
                break;
            case MAYOR:
                Mayor mayor = new Mayor();
                mayor.setUsername(username);
                mayor.setName(name);
                mayor.setPasswordHash(hash);
                newUser = mayor;
                break;
        }

        if (newUser == null) {
            throw new IllegalArgumentException("Invalid role provided");
        }

        return userRepository.save(newUser);
    }
}
