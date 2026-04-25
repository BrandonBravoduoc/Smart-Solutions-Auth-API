package com.smart_solutions_auth.API.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.dto.user.UserContactDTO;
import com.smart_solutions_auth.API.model.UserContact;
import com.smart_solutions_auth.API.repository.UserContactRepository;
import com.smart_solutions_auth.API.util.Validations;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserContactService {

    @Autowired 
    private UserContactRepository userContactRepository;

    @Autowired 
    private Validations validations;
    
    public UserContactDTO.Response upateUserContact(UserContactDTO.UpdateRequest dto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserContact contact = userContactRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        if(!contact.getPhoneNumber().equals(dto.phone())){
            validations.contactValidate(dto.phone());
        }
        
        contact.setName(dto.name());
        contact.setLastName(dto.lastName());
        contact.setPhoneNumber(dto.phone());

        userContactRepository.save(contact);

        return new UserContactDTO.Response(
            contact.getUser().getEmail(),
            contact.getName(),
            contact.getLastName(),
            contact.getPhoneNumber()
        );
    }

    
}
