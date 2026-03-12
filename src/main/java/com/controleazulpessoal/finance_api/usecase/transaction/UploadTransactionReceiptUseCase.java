package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.infrastructure.storage.S3StorageService;
import com.controleazulpessoal.finance_api.persistence.entity.Transaction;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.TransactionRepository;
import com.controleazulpessoal.finance_api.usecase.transaction.mapper.TransactionMapper;
import com.controleazulpessoal.finance_api.usecase.transaction.output.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadTransactionReceiptUseCase {

    private final TransactionRepository repository;
    private final S3StorageService s3StorageService;
    private final TransactionMapper mapper;

    @Transactional
    public TransactionDto execute(UUID transactionId, MultipartFile file) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = repository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Permission denied");
        }

        // Deleta o anterior se existir
        if (transaction.getAttachmentUrl() != null && !transaction.getAttachmentUrl().isEmpty()) {
            s3StorageService.deleteFile(transaction.getAttachmentUrl());
        }

        // Faz o novo upload na pasta "receipts"
        String newFileKey = s3StorageService.uploadFile(file, "receipts");

        transaction.setAttachmentUrl(newFileKey);
        return mapper.entityToDto(repository.save(transaction));
    }
}