package com.ibmr.sdm.api_clinica.Exceptions.ExceptionHandler;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL =
            "Ocorreu um erro interno inesperado. Tente novamente ou contate o administrador.";

    // 1.  VAI CAPTURA ERROS DE VALIDAÇÃO DE CAMPOS (DTO - @NotBlank, @NotNull)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto.";

        List<Problem.Field> problemFields = ex.getBindingResult().getAllErrors().stream()
                .map(objectError -> {
                    String name = objectError.getObjectName();
                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }
                    return Problem.Field.builder()
                            .name(name)
                            .userMessage(objectError.getDefaultMessage())
                            .build();
                })
                .collect(Collectors.toList());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .fields(problemFields)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    // 2. VAI  CAPTURA A NOSSA EXCEÇÃO CUSTOMIZADA (Animal/Veterinário não encontrado)
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Object> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    // 3. VAI CAPTURA ERROS DE BANCO DE DADOS (Ex: CPF ou CRMV já existentes)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;

        String detail = "Ocorreu um erro de integridade de dados. Um registro único já está cadastrado ou há violação de chave.";
        String msg = ex.getMostSpecificCause().getMessage().toLowerCase();

        // Adaptado para o contexto de clínica veterinária
        if (msg.contains("crmv")) {
            detail = "Este CRMV já está cadastrado no sistema!";
        } else if (msg.contains("cpf")) {
            detail = "Este CPF já está cadastrado no sistema!";
        } else if (msg.contains("email")) {
            detail = "Este e-mail já está em uso!";
        }

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    // 4. VAI CAPTURA QUALQUER OUTRA EXCEÇÃO NÃO TRATADA
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;

        Problem problem = createProblemBuilder(status, problemType, MSG_ERRO_GENERICA_USUARIO_FINAL)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status, ProblemType problemType, String detail) {
        return Problem.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }
}
