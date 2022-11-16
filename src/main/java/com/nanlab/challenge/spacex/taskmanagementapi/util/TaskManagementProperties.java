package com.nanlab.challenge.spacex.taskmanagementapi.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Data
public class TaskManagementProperties {

    @Value("${trello.api.key}")
    private String API_KEY;

    @Value("${trello.api.token}")
    private String API_TOKEN;

    @Value("${trello.api.board}")
    private String DEFAULT_BOARD;
}
