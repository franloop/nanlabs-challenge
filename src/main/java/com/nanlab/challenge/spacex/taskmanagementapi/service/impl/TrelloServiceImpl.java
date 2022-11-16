package com.nanlab.challenge.spacex.taskmanagementapi.service.impl;

import com.nanlab.challenge.spacex.taskmanagementapi.dto.CardRequestDTO;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.Label;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.Member;
import com.nanlab.challenge.spacex.taskmanagementapi.service.CardService;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrelloServiceImpl implements CardService {

    Logger logger = LoggerFactory.getLogger(TrelloServiceImpl.class);

    static final String API_URL = "https://api.trello.com";
    static final String ENDPOINT_CARD = "/1/cards";
    static final String ENDPOINT_BOARD = "/1/boards/";

    //TODO:Externalizar
    static final String TODO_LIST_ID = "6374410ebc236e01da0c41a2";
    static final String API_KEY = "f78d7dd358f04bc24ef78992c725bd51";
    static final String API_TOKEN = "ab63d44e75eb78b3bb3674bad294aa48b3f9d7f6b7ea60abb07e784d39e01d60";
    static final String DEFAULT_BOARD = "6374410ebc236e01da0c419b";

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
                .queryString("idList", TODO_LIST_ID)
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

    public List<Label> getLabels()
    {
        logger.info("Invoking external API for get labels");
        HttpResponse<JsonNode> response = Unirest.get(API_URL + ENDPOINT_BOARD + DEFAULT_BOARD + "/labels")
                .queryString("key", API_KEY)
                .queryString("token", API_TOKEN)
                .asJson();

        logger.debug("Response status: {}", response.getStatus());

        List<Label> labels = new ArrayList<>();

        for(Object obj: response.getBody().getArray()){
            JSONObject jsonObject = (JSONObject) obj;
            labels.add(new Label(jsonObject.getString("id"),jsonObject.getString("name")));
        }

        return labels;
    }

    public List<Member> getMembers()
    {
        logger.info("Invoking external API for get members of the board");

        HttpResponse<JsonNode> response = Unirest.get(API_URL + ENDPOINT_BOARD + DEFAULT_BOARD + "/memberships")
                .queryString("key", API_KEY)
                .queryString("token", API_TOKEN)
                .asJson();

        List<Member> members = new ArrayList<>();

        for(Object obj: response.getBody().getArray())
        {
            JSONObject jsonObject = (JSONObject)obj;
            members.add(new Member(jsonObject.getString("idMember")));
        }

        return members;
    }

    //Return id of the TO-DO list if exist
//    public String getTodoList()
//    {
//
//    }
}
