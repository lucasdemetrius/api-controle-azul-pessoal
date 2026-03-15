package com.controleazulpessoal.finance_api.controller.v1.transaction;

import com.controleazulpessoal.finance_api.controller.v1.transaction.request.CreateTransactionRequest;
import com.controleazulpessoal.finance_api.controller.v1.transaction.request.UpdateTransactionRequest;
import com.controleazulpessoal.finance_api.response.Response;
import com.controleazulpessoal.finance_api.usecase.transaction.*;
import com.controleazulpessoal.finance_api.usecase.transaction.output.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final ListTransactionsUseCase listTransactionsUseCase;
    private final ListTransactionsByPeriodUseCase listTransactionsByPeriodUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final UploadTransactionReceiptUseCase uploadTransactionReceiptUseCase;

    @Override
    public ResponseEntity<Response<TransactionDto>> create(CreateTransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.of(createTransactionUseCase.execute(request)));
    }

    @Override
    public ResponseEntity<Response<Page<TransactionDto>>> getAll(Pageable pageable) {
        return ResponseEntity.ok(Response.of(listTransactionsUseCase.execute(pageable)));
    }

    @Override
    public ResponseEntity<Response<Page<TransactionDto>>> getByPeriod(
            LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return ResponseEntity.ok(Response.of(listTransactionsByPeriodUseCase.execute(start, end, pageable)));
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        deleteTransactionUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Response<TransactionDto>> update(UUID id, UpdateTransactionRequest request) {
        return ResponseEntity.ok(Response.of(updateTransactionUseCase.execute(id, request)));
    }

    @Override
    public ResponseEntity<Response<TransactionDto>> uploadReceipt(UUID id, MultipartFile file) {
        return ResponseEntity.ok(Response.of(uploadTransactionReceiptUseCase.execute(id, file)));
    }
}