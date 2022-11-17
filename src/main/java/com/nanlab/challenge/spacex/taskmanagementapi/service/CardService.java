package com.nanlab.challenge.spacex.taskmanagementapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.BoardList;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.CardRequestDTO;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.Label;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.Member;
import com.nanlab.challenge.spacex.taskmanagementapi.exception.RemoteServiceException;

import java.util.List;

public interface CardService {
    String createCard(CardRequestDTO request);
    List<Label> getLabels() throws JsonProcessingException, RemoteServiceException;
    List<BoardList> getBoardList() throws JsonProcessingException, RemoteServiceException;
    List<Member> getMembers() throws JsonProcessingException, RemoteServiceException;
}
