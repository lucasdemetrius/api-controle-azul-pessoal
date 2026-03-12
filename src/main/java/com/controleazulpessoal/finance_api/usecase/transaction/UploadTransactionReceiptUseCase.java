package com.controleazulpessoal.finance_api.usecase.transaction;

import com.controleazulpessoal.finance_api.exception.ForbiddenActionException;
import com.controleazulpessoal.finance_api.exception.transaction.TransactionAccessDeniedException;
import com.controleazulpessoal.finance_api.exception.transaction.TransactionNotFoundException;
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

        // Ajuste: Substituído por TransactionNotFoundException
        Transaction transaction = repository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new TransactionAccessDeniedException();
        }

        if (transaction.getAttachmentUrl() != null && !transaction.getAttachmentUrl().isEmpty()) {
            s3StorageService.deleteFile(transaction.getAttachmentUrl());
        }

        String newFileKey = s3StorageService.uploadFile(file, "receipts");

        transaction.setAttachmentUrl(newFileKey);
        return mapper.entityToDto(repository.save(transaction));
    }
}