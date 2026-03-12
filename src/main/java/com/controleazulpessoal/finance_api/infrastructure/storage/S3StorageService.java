package com.controleazulpessoal.finance_api.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    // Agora aceita um prefixo (pasta)
    public String uploadFile(MultipartFile file, String folder) {
        String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            return fileName; // Retorna o caminho completo (key)
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo para upload no S3", e);
        }
    }

    public void deleteFile(String fileKey) {
        if (fileKey == null || fileKey.isEmpty()) return;
        try {
            s3Client.deleteObject(d -> d.bucket(bucketName).key(fileKey));
        } catch (Exception e) {
            System.err.println("Erro ao deletar arquivo no S3: " + e.getMessage());
        }
    }
}