package com.alta.e_commerce.service;

import com.alta.e_commerce.entity.Image;
import com.alta.e_commerce.entity.Product;
import com.alta.e_commerce.entity.User;
import com.alta.e_commerce.model.*;
import com.alta.e_commerce.repository.ImageRepository;
import com.alta.e_commerce.repository.ProductRepository;
import com.alta.e_commerce.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private CloudinaryService cloudinaryService;

    private String getUserIdFromToken(String token) {
        return jwtService.extractClaim(token, claims -> token);
    }

    /**
     * method untuk mapping dari entity ke useresponse
     * @param product
     * @return
     */
    private ProductResponse toProductResponse(Product product, List<Image> images){
        List<String> imageUrls = images.stream().map(Image::getUrl).collect(Collectors.toList());

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .thumbnail(product.getThumbnail())
                .imageUrls(imageUrls)
                .userId(product.getUser().getId())
                .build();
    }

    @Transactional
    public ProductResponse create(ProductRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setUser(user);

        // Save images
        List<Image> images = new ArrayList<>();
        for (MultipartFile imageFile : request.getImageUrls()) {
            String imageUrl = cloudinaryService.UploadFile(imageFile,"product_image");
            Image image = new Image();
            image.setId(UUID.randomUUID().toString());
            image.setUrl(imageUrl);
            image.setProduct(product);
            images.add(image);

            if (product.getThumbnail() == null){
                product.setThumbnail(imageUrl);
            }
        }

        productRepository.save(product);
        imageRepository.saveAll(images);

        return toProductResponse(product, images);
    }

    @Transactional
    public void delete(String productId, String userId) {
        Product product = productRepository.findByIdAndUserId(productId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produk tidak ditemukan"));

        productRepository.delete(product);
    }

    @Transactional
    public ProductResponse update(UpdateProductRequest request, String productId, String userId){
        Product product = productRepository.findByIdAndUserId(productId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produk tidak ditemukan"));

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getPrice() != 0) {
            product.setPrice(request.getPrice());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }

        productRepository.save(product);

        List<Image> images = new ArrayList<>();

        if (request.getImageUrls() != null){
            for (MultipartFile imageFile : request.getImageUrls()) {
                String imageUrl = cloudinaryService.UploadFile(imageFile,"product_image");
                if (imageUrl == null) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal mengunggah foto profil");
                }
                Image image = new Image();
                image.setId(UUID.randomUUID().toString());
                image.setUrl(imageUrl);
                image.setProduct(product);
                images.add(image);
            }
            imageRepository.saveAll(images);
        }

        return toProductResponse(product, images);
    }
    /**
     * @param request
     * @return
     */
    @Transactional
    public Page<ProductResponse> getAllOrSearch(SearchProductRequest request){
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

           if (Objects.nonNull(request.getName())){
                predicates.add(
                        criteriaBuilder.like(root.get("name"), "%"+request.getName()+"%")
                );
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit());
        Page<Product> products = productRepository.findAll(specification,pageable);
        List<ProductResponse> productResponses = products.getContent().stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .userId(product.getUser().getId())
                        .build())
                .toList();

        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }


    @Transactional(readOnly = true)
    public ProductResponse getById(String productId){

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "produk tidak ditemukan"));

        List<Image> image = imageRepository.findByProductId(productId);

        return toProductResponse(product,image);
    }
}
