package com.nanlab.challenge.spacex.taskmanagementapi.controller;
import com.nanlab.challenge.spacex.taskmanagementapi.bo.CardBO;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.CardRequestDTO;
import com.nanlab.challenge.spacex.taskmanagementapi.service.CardService;
import com.nanlab.challenge.spacex.taskmanagementapi.service.impl.TrelloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TaskManagementController 
{

    private final CardBO cardBO;

    private final TrelloServiceImpl trelloService;
    @Autowired
    public TaskManagementController(CardBO cardBO, TrelloServiceImpl trelloService) {
        this.cardBO = cardBO;
        this.trelloService = trelloService;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCard(@Valid @RequestBody CardRequestDTO cardRequestDTO)
    {
       // trelloService.getLabels();

        String idCard = cardBO.createCard(cardRequestDTO);

        if (idCard != null)
            return ResponseEntity.status(HttpStatus.CREATED).body("The card was created succesfully. ID: " + idCard);
        else
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    //Catch default valitacion exception and re-decorate response msj.
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
}
