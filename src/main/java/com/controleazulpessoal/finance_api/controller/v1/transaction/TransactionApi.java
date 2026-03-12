package com.controleazulpessoal.finance_api.controller.v1.transaction;

import com.controleazulpessoal.finance_api.controller.v1.transaction.request.CreateTransactionRequest;
import com.controleazulpessoal.finance_api.controller.v1.transaction.request.UpdateTransactionRequest;
import com.controleazulpessoal.finance_api.response.Response;
import com.controleazulpessoal.finance_api.usecase.transaction.output.TransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Transaction", description = "Finance transactions management")
@RequestMapping(path = "/v1/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public interface TransactionApi {

    @PostMapping
    @Operation(description = "Create a new transaction for the authenticated user")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<Response<TransactionDto>> create(@RequestBody @Valid CreateTransactionRequest request);

    @GetMapping
    @Operation(description = "List all transactions of the authenticated user")
    ResponseEntity<Response<Page<TransactionDto>>> getAll(
            @PageableDefault(size = 20, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable);

    @DeleteMapping("/{id}")
    @Operation(description = "Delete a transaction")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    ResponseEntity<Void> delete(@PathVariable UUID id);

    @PutMapping("/{id}")
    @Operation(description = "Update an existing transaction")
    @ResponseStatus(value = HttpStatus.OK)
    ResponseEntity<Response<TransactionDto>> update(@PathVariable UUID id, @RequestBody UpdateTransactionRequest request);

    @PatchMapping("/{id}/receipt")
    @Operation(description = "Upload a receipt for a transaction")
    @ResponseStatus(value = HttpStatus.OK)
    ResponseEntity<Response<TransactionDto>> uploadReceipt(
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file);
}