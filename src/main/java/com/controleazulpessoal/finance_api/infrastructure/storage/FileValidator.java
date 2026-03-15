package com.controleazulpessoal.finance_api.infrastructure.storage;

import com.controleazulpessoal.finance_api.exception.file.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileValidator {

    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024; // 5MB

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private static final List<String> ALLOWED_RECEIPT_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "application/pdf"
    );

    public static void validateImage(MultipartFile file) {
        validateSize(file);
        validateContentType(file, ALLOWED_IMAGE_TYPES, "Only JPEG, PNG and WEBP images are allowed.");
    }

    public static void validateReceipt(MultipartFile file) {
        validateSize(file);
        validateContentType(file, ALLOWED_RECEIPT_TYPES, "Only JPEG, PNG, WEBP and PDF files are allowed.");
    }

    private static void validateSize(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File must not be empty.");
        }
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new InvalidFileException("File size must not exceed 5MB.");
        }
    }

    private static void validateContentType(MultipartFile file, List<String> allowedTypes, String message) {
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new InvalidFileException(message);
        }
    }
}