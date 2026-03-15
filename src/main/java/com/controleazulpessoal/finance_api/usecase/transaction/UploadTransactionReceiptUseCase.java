package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.exception.transaction.TransactionAccessDeniedException;
import com.controleazulpessoal.finance_api.exception.transaction.TransactionNotFoundException;
import com.controleazulpessoal.finance_api.infrastructure.storage.FileValidator;
import com.controleazulpessoal.finance_api.infrastructure.storage.S3StorageService;
import com.controleazulpessoal.finance_api.persistence.entity.Transaction;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.TransactionRepository;
import com.controleazulpessoal.finance_api.usecase.transaction.mapper.TransactionMapper;
import com.controleazulpessoal.finance_api.usecase.transaction.output.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadTransactionReceiptUseCase {

    private final TransactionRepository repository;
    private final S3StorageService s3StorageService;
    private final TransactionMapper mapper;

    @Transactional
    public TransactionDto execute(UUID transactionId, MultipartFile file) {
        FileValidator.validateReceipt(file);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Uploading receipt for transaction: {}, user: {}", transactionId, user.getId());

        Transaction transaction = repository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);

        if (!transaction.getUser().getId().equals(user.getId())) {
            log.warn("User: {} attempted to upload receipt for transaction: {} owned by another user", user.getId(), transactionId);
            throw new TransactionAccessDeniedException();
        }

        if (transaction.getAttachmentUrl() != null && !transaction.getAttachmentUrl().isEmpty()) {
            log.info("Deleting old receipt for transaction: {}", transactionId);
            s3StorageService.deleteFile(transaction.getAttachmentUrl());
        }

        String newFileKey = s3StorageService.uploadFile(file, "receipts");
        log.info("Receipt uploaded successfully for transaction: {}. key: {}", transactionId, newFileKey);

        transaction.setAttachmentUrl(newFileKey);
        return mapper.entityToDto(repository.save(transaction));
    }
}