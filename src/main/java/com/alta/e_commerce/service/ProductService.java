package com.alta.e_commerce.service;

import com.alta.e_commerce.entity.Image;
import com.alta.e_commerce.entity.Product;
import com.alta.e_commerce.entity.User;
import com.alta.e_commerce.model.ProductRequest;
import com.alta.e_commerce.model.ProductResponse;
import com.alta.e_commerce.repository.ImageRepository;
import com.alta.e_commerce.repository.ProductRepository;
import com.alta.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
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
                .imageUrls(imageUrls)
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

        productRepository.save(product);

        // Save images
        List<Image> images = new ArrayList<>();
        for (MultipartFile imageFile : request.getImageUrls()) {
            String imageUrl = cloudinaryService.UploadFile(imageFile,"product_image");
            Image image = new Image();
            image.setId(UUID.randomUUID().toString());
            image.setUrl(imageUrl);
            image.setProduct(product);
            images.add(image);
        }
        imageRepository.saveAll(images);

        return toProductResponse(product, images);
    }

    @Transactional
    public void delete(String productId, String userId) {
        Product product = productRepository.findByIdAndUserId(productId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produk tidak ditemukan"));

        productRepository.delete(product);
    }

}
