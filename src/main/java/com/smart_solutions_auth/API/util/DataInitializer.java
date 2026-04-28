package com.smart_solutions_auth.API.util;


import com.smart_solutions_auth.API.model.User;
import com.smart_solutions_auth.API.model.UserContact;
import com.smart_solutions_auth.API.model.UserRole;
import com.smart_solutions_auth.API.repository.UserRepository;
import com.smart_solutions_auth.API.repository.UserContactRepository;
import com.smart_solutions_auth.API.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserContactRepository contactRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Crear Roles si no existen
        UserRole adminRole = createRoleIfNotFound("ADMINISTRADOR");
        createRoleIfNotFound("CLIENTE");

        // 2. Crear Usuario Administrador inicial si no existe ningún usuario
        if (userRepository.findByEmail("admin@smart.com").isEmpty()) {
            
            // Crear entidad Usuario
            User adminUser = new User();
            adminUser.setEmail("admin@smart.com");
            adminUser.setPassword(passwordEncoder.encode("admin1234")); // Cambiar en producción
            adminUser.setUserRole(adminRole);
            adminUser.setAsset(true);
            
            User savedAdmin = userRepository.save(adminUser);

            // Crear datos de contacto ficticios
            UserContact adminContact = new UserContact();
            adminContact.setName("Admin");
            adminContact.setLastName("Sistema");
            adminContact.setPhoneNumber("999999999");
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
