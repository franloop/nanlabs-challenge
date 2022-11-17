package com.nanlab.challenge.spacex.taskmanagementapi.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.BoardList;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.CardRequestDTO;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.Label;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.Member;
import com.nanlab.challenge.spacex.taskmanagementapi.exception.RemoteServiceException;
import com.nanlab.challenge.spacex.taskmanagementapi.service.CardService;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrelloServiceImpl implements CardService {

    private Logger logger = LoggerFactory.getLogger(TrelloServiceImpl.class);

    private static final String API_URL = "https://api.trello.com";
    private static final String ENDPOINT_CARD = "/1/cards";
    private static final String ENDPOINT_BOARD = "/1/boards/";

    @Value("${trello.api.key}")
    private final String API_KEY = "f78d7dd358f04bc24ef78992c725bd51";

    @Value("${trello.api.token}")
    private final String API_TOKEN = "ab63d44e75eb78b3bb3674bad294aa48b3f9d7f6b7ea60abb07e784d39e01d60";

    @Value("${trello.api.board}")
    private final String DEFAULT_BOARD = "6374410ebc236e01da0c419b";

    @Override
    public String createCard(CardRequestDTO request)
    {
        logger.info("Invoking external API for creating Card");

        String labelsIds = "";
        String membersIds = "";

        if (request.getLabels() != null)
            labelsIds = request.getLabels().stream().map(Label::getId).collect(Collectors.joining(","));

        if (request.getMembers() != null)
            membersIds = request.getMembers().stream().map(Member::getIdMember).collect(Collectors.joining(","));

        HttpResponse<JsonNode> response = Unirest.post(API_URL + ENDPOINT_CARD)
                .header("Accept", "application/json")
                .queryString("key", API_KEY)
                .queryString("token", API_TOKEN)
                .queryString("idList", request.getBoardList().getId())
                .queryString("name", request.getTitle())
                .queryString("desc", request.getDescription())
                .queryString("idLabels",labelsIds)
                .queryString("idMembers", membersIds)
                .asJson();

        logger.debug("Response status: {}", response.getStatus());

        if (response.isSuccess())
            return response.getBody().getObject().getString("id");

        return null;
    }

    public List<Label> getLabels() throws JsonProcessingException, RemoteServiceException {
        logger.info("Invoking external API for get labels");
        HttpResponse<JsonNode> response = Unirest.get(API_URL + ENDPOINT_BOARD + DEFAULT_BOARD + "/labels")
                .queryString("key", API_KEY)
                .queryString("token", API_TOKEN)
                .asJson();
        logger.debug("Response status: {}", response.getStatus());

        //TODO: see if can be variable instance (threadsafe)
        ObjectMapper mapper = new ObjectMapper();
        List<Label> list = null;

        if (response.isSuccess())
            list = Arrays.asList(mapper.readValue(response.getBody().toString(), Label[].class));
        else
            throw new RemoteServiceException(response.getStatus(), "Can not get Labels from External API.");

        return list;
    }

    public List<BoardList> getBoardList() throws JsonProcessingException, RemoteServiceException {
        logger.info("Invoking external API for get BoardList");
        HttpResponse<JsonNode> response = Unirest.get(API_URL + ENDPOINT_BOARD + DEFAULT_BOARD + "/lists")
                .queryString("key", API_KEY)
                .queryString("token", API_TOKEN)
                .asJson();

        logger.debug("Response status: {}", response.getStatus());

        //TODO: see if can be variable instance (threadsafe)
        ObjectMapper mapper = new ObjectMapper();

        List<BoardList> list = null;

        if (response.isSuccess())
            list = Arrays.asList(mapper.readValue(response.getBody().toString(), BoardList[].class));
        else
            throw new RemoteServiceException(response.getStatus(), "Can not get Labels from External API.");

        return list;
    }

    public List<Member> getMembers() throws JsonProcessingException, RemoteServiceException {
        logger.info("Invoking external API for get members of the board");

        HttpResponse<JsonNode> response = Unirest.get(API_URL + ENDPOINT_BOARD + DEFAULT_BOARD + "/memberships")
                .queryString("key", API_KEY)
                .queryString("token", API_TOKEN)
                .asJson();

        //TODO: see if can be variable instance (threadsafe)
        ObjectMapper mapper = new ObjectMapper();

        List<Member> list = null;
        if (response.isSuccess())
            list = Arrays.asList(mapper.readValue(response.getBody().toString(), Member[].class));
        else
            throw new RemoteServiceException(response.getStatus(), "Can not get members of board from External API.");

        return list;
    }
}
