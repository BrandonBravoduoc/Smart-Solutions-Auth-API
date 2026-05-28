package com.smart_solutions_auth.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.api.dto.user.UserContactDTO;
import com.smart_solutions_auth.api.model.UserContact;
import com.smart_solutions_auth.api.repository.UserContactRepository;
import com.smart_solutions_auth.api.util.Validations;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserContactService {

    @Autowired 
    private UserContactRepository userContactRepository;

    @Autowired 
    private Validations validations;

    @Autowired
    private CacheManager cacheManager;
    
    public UserContactDTO.Response updateUserContact(UserContactDTO.UpdateRequest dto) {

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

        Long userId = contact.getUser().getId();  

        if(cacheManager.getCache("user_contacts") != null){
            cacheManager.getCache("user_contacts").evict(userId);
        }

        if(cacheManager.getCache("users") != null){
            cacheManager.getCache("users").evict(email);
        }

        if(cacheManager.getCache("user_list") != null){
            cacheManager.getCache("user_list").clear();
        }

        return new UserContactDTO.Response(
            contact.getUser().getEmail(),
            contact.getName(),
            contact.getLastName(),
            contact.getPhoneNumber()
        );
    }

    
}
