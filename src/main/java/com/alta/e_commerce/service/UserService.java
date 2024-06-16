package com.alta.e_commerce.service;

import com.alta.e_commerce.entity.User;
import com.alta.e_commerce.model.*;
import com.alta.e_commerce.repository.UserRepository;
import com.cloudinary.api.exceptions.BadRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * method untuk maping dari entity ke userresponse
     * @param user
     * @return
     */

    private UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .username(user.getIdentifier())
                .phone(user.getPhone())
                .image(user.getImage())
                .build();
    }

    @Transactional
    public UserResponse update(UpdateUserRequest request){
        // cek id user ke db
        User user = userRepository.findById(request.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "user tidak ditemukan"));

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setIdentifier(request.getIdentifier());
        user.setPhone(request.getPhone());
        if (request.getFile() != null && !request.getFile().isEmpty()) {
            String imageUrl = cloudinaryService.UploadFile(request.getFile(), "user_profile");
            if (imageUrl == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal mengunggah foto profil");
            }
            user.setImage(imageUrl);
        }

        userRepository.save(user);

        return toUserResponse(user);
    }

    @Transactional
    public void delete(String userId){
        // cek id user ke db
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "user tidak ditemukan"));

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(String userId){
        // baca data user by id user ke db
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "user tidak ditemukan"));

        return toUserResponse(user);
    }

    /**
     * untuk menggunakan specification, tambahkan extend JpaSpecificationExecutor di repo nya User
     * @param request
     * @return
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllOrSearch(SearchUserRequest request){
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // jika name ada di query param
            if (Objects.nonNull(request.getName())){
                predicates.add(
                        criteriaBuilder.like(root.get("name"), "%"+request.getName()+"%")
                );
            }

            // jika email ada di query param
            if (Objects.nonNull(request.getEmail())){
                predicates.add(
                        criteriaBuilder.like(root.get("email"), "%"+request.getEmail()+"%")
                );
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit());
        Page<User> users = userRepository.findAll(specification, pageable);
        List<UserResponse> userResponses = users.getContent().stream()
                .map(this::toUserResponse)
                .toList();

        return new PageImpl<>(userResponses, pageable, users.getTotalElements());
    }

    @Transactional(readOnly = true)
    public String getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));;
        return user.getId();
    }

}