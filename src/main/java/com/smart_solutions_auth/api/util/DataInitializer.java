package com.smart_solutions_auth.api.util;



import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.smart_solutions_auth.api.model.entity.Address;
import com.smart_solutions_auth.api.model.entity.Commune;
import com.smart_solutions_auth.api.model.entity.Region;
import com.smart_solutions_auth.api.model.entity.User;
import com.smart_solutions_auth.api.model.entity.UserContact;
import com.smart_solutions_auth.api.model.entity.UserRole;
import com.smart_solutions_auth.api.repository.AddressRepository;
import com.smart_solutions_auth.api.repository.CommuneRepository;
import com.smart_solutions_auth.api.repository.RegionRepository;
import com.smart_solutions_auth.api.repository.UserContactRepository;
import com.smart_solutions_auth.api.repository.UserRepository;
import com.smart_solutions_auth.api.repository.UserRoleRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserContactRepository contactRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegionRepository regionRepository;
    private final CommuneRepository communeRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        UserRole adminRole = createRoleIfNotFound("ADMINISTRADOR");
        createRoleIfNotFound("CLIENTE");

        if (userRepository.findByEmail("admin@smart.com").isEmpty()) {
            
            User adminUser = new User();
            adminUser.setEmail("admin@smart.com");
            adminUser.setPassword(passwordEncoder.encode("admin1234")); 
            adminUser.setUserRole(adminRole);
            adminUser.setAsset(true);
            
            User savedAdmin = userRepository.save(adminUser);

            Region newRegion = new Region();
            newRegion.setRegionName("santiago");

            regionRepository.save(newRegion);

            Commune newCommune = new Commune();
            newCommune.setCommuneName("cerrillos");
            newCommune.setRegion(newRegion);

            communeRepository.save(newCommune);

            Address newAddress = new Address();
            newAddress.setSucursalName("Plaza oeste");
            newAddress.setStreet("Colo Colo");
            newAddress.setNumber("921");
            newAddress.setCommune(newCommune);

            addressRepository.save(newAddress);

            UserContact adminContact = new UserContact();
            adminContact.setName("Admin");
            adminContact.setLastName("Sistema");
            adminContact.setPhoneNumber("999999999");
            adminContact.setUserAddress(newAddress);
            adminContact.setUser(savedAdmin);            

            contactRepository.save(adminContact);
            
            System.out.println("--> Configuración inicial: Roles y Usuario Admin creados con éxito.");
        }
    }

    private UserRole createRoleIfNotFound(String roleName) {
        return roleRepository.findByNameRole(roleName)
                .orElseGet(() -> {
                    UserRole newRole = new UserRole();
                    newRole.setNameRole(roleName);
                    return roleRepository.save(newRole);
                });
    }
}
