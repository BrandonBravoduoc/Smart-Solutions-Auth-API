package com.smart_solutions_auth.api.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.smart_solutions_auth.api.client.CoreApiClient;
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
public class Validations {

    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private UserContactRepository userContactRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CommuneRepository communeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CoreApiClient coreApiClient;

    public String emailValidate(String email) {
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }
        return email.trim().toLowerCase();
    }

    public void passwordValidate(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)){
            throw new RuntimeException("Las contraseñas no coinciden.");
        }
    }

    public void contactValidate(String phoneNumber) {
        if(userContactRepository.existsByPhoneNumber(phoneNumber)){
            throw new RuntimeException("El número de teléfono ya está registrado.");
        }
    }

    public User validateCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas."));
      
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Credenciales inválidas.");
        }
        return user;
    }   

    public void checkRole(String nameRole){
        if(userRoleRepository.findByNameRole(nameRole).isPresent()){
            throw new RuntimeException("El rol " + nameRole + " ya existe.");
        }
    }

    public UserRole roleVerification(String nameRole){
        return userRoleRepository.findByNameRole(nameRole)
            .orElseThrow(() -> new RuntimeException("El rol " + nameRole + " no está registrado en el sistema"));
    }

    public Long getCurrentUserId() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (details == null) {
            throw new RuntimeException("Sesión no válida");
        }
        return (Long) details;
    }

    public Region requireRegion(Long id) {
        return regionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Region no encontrada."));
    }

    public Commune requireCommune(Long id) {
        return communeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comuna no encontrada."));
    }

    public Address requireAddress(Long id) {
        return addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dirección no encontrada."));
    }

    /**
     * Bloquea la desactivación/eliminación de una región, comuna o sucursal si hay algún
     * usuario asociado a ella a través de sus datos de contacto (dirección). Si alguno de
     * esos usuarios tiene además una suscripción activa (consultada en Core API), el mensaje
     * lo indica explícitamente.
     */
    public void assertNoUsersAssociated(List<UserContact> associatedContacts, String entityLabel) {
        if (associatedContacts.isEmpty()) {
            return;
        }

        boolean anyActiveSubscription = associatedContacts.stream()
            .anyMatch(contact -> coreApiClient.hasActiveSubscription(contact.getUser().getId()));

        String suffix = anyActiveSubscription ? " con una suscripción activa" : "";
        throw new RuntimeException(
            "No se puede desactivar esta " + entityLabel + " porque está asociada a un usuario" + suffix + ".");
    }

}
