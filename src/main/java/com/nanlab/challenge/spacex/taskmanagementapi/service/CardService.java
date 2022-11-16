package com.nanlab.challenge.spacex.taskmanagementapi.service;

import com.nanlab.challenge.spacex.taskmanagementapi.dto.CardRequestDTO;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.Label;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.Member;

import java.util.List;

public interface CardService {
    String createCard(CardRequestDTO request);
    List<Label> getLabels();
    List<Member> getMembers();
}
