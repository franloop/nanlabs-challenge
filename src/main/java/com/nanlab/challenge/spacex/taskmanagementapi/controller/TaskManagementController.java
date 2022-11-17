package com.nanlab.challenge.spacex.taskmanagementapi.controller;
import com.nanlab.challenge.spacex.taskmanagementapi.bo.CardBO;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.CardRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TaskManagementController 
{
    private Logger logger = LoggerFactory.getLogger(TaskManagementController.class);
    private final CardBO cardBO;

    @Autowired
    public TaskManagementController(CardBO cardBO) {
        this.cardBO = cardBO;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCard(@Valid @RequestBody CardRequestDTO cardRequestDTO) throws IOException {
        String idCard = cardBO.createCard(cardRequestDTO);

        if (idCard != null)
            return ResponseEntity.status(HttpStatus.CREATED).body("The card was created succesfully. ID: " + idCard);
        else
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    //Catch default validation exception and re-decorate response msj.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
            (error) ->
            {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });

        return errors;
    }

    //Catch default msj for InternalServer to decorate and hide internal traces
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public Map<String, String> handleValidationExceptions(Exception ex)
//    {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(
//                (error) ->
//                {
//                    String fieldName = ((FieldError) error).getField();
//                    String errorMessage = error.getDefaultMessage();
//                    errors.put(fieldName, errorMessage);
//                });
//
//        return errors;
//    }
}
