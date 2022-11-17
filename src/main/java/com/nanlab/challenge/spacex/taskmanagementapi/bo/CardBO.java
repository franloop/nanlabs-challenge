package com.nanlab.challenge.spacex.taskmanagementapi.bo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.BoardList;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.CardRequestDTO;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.Label;
import com.nanlab.challenge.spacex.taskmanagementapi.dto.Member;
import com.nanlab.challenge.spacex.taskmanagementapi.exception.RemoteServiceException;
import com.nanlab.challenge.spacex.taskmanagementapi.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CardBO {
    private Logger logger = LoggerFactory.getLogger(CardBO.class);

    private final CardService cardService;

    private static final String BACKLOG_LIST = "Backlog";
    private static final String TODO_LIST = "TO-DO";

    @Autowired
    public CardBO(CardService cardService) {
        this.cardService = cardService;
    }

    //Creates a card based on the type and behavior needed.
    public String createCard(CardRequestDTO cardRequestDTO) throws RemoteServiceException, JsonProcessingException {
        //WARN: While the execution of the method, between the get of members and labels and the creation of the card,
        //those can be changed.

        //get all labels
        List<Label> labels = cardService.getLabels();

        //get all members
        List<Member> members = cardService.getMembers();

        //get all list
        List<BoardList> boardLists = cardService.getBoardList();

        //determinar tipo de card
        switch (cardRequestDTO.getType().toLowerCase())
        {
            case "issue":
                this.customizeIssue(cardRequestDTO, boardLists);
            break;
            case "bug":
                this.customizeBug(cardRequestDTO, labels, members, boardLists);
            break;
            case "task":
                this.customizeTask(cardRequestDTO, labels, boardLists);
            break;
            //Cause validations can not allow other values, this case wont exist.
            default:
                break;
        }

        return cardService.createCard(cardRequestDTO);
    }

    //issues must be on TO-DO list without assignament
    private void customizeIssue(CardRequestDTO cardRequestDTO, List<BoardList> boardLists)
    {
        //No need of customization as base construction makes an issue.
        cardRequestDTO.setBoardList(this.getBoardList(boardLists, CardBO.BACKLOG_LIST));
    }

    //task only have to be assigned to a label that match with the request category
    private void customizeTask(CardRequestDTO cardRequestDTO,  List<Label> labels, List<BoardList> boardLists)
    {
        cardRequestDTO.setLabels(
                Collections.singletonList(
                    this.getLabel(labels,cardRequestDTO.getCategory())
                )
        );

        cardRequestDTO.setBoardList(this.getBoardList(boardLists, CardBO.BACKLOG_LIST));
    }

    //method for customize the card with the bug behavior
    //Definition: must be assigned to random member and have title with random word and number
    //in the format: bug-{word}-{number}
    //Also need to have the Bug label
    private void customizeBug(CardRequestDTO cardRequestDTO, List<Label> labels,
                              List<Member> members,
                              List<BoardList> boardLists)
    {
        String[] descriptionWords = cardRequestDTO.getDescription().split(" ");

        String title = "Bug-"
                            + descriptionWords[this.getRandom(descriptionWords.length)]
                            + "-"
                            + this.getRandom(Integer.MAX_VALUE);

        cardRequestDTO.setTitle(title);

        cardRequestDTO.setLabels(
                Collections.singletonList(
                        this.getLabel(labels, "BUG")
                )
        );

        cardRequestDTO.setMembers(
                Collections.singletonList(
                        members.get(this.getRandom(members.size()))
                )
        );


        cardRequestDTO.setBoardList(this.getBoardList(boardLists, CardBO.TODO_LIST));
    }



    //filters the collections of label looking for anything that match with 'BUG' label
    private Label getLabel(List<Label> labels, String type)
    {
        if (labels != null && labels.size()>0)
        {
            //Looking for any label with the BUG name. If none found, return null (case not covered)
            return labels.stream().filter(
                    label -> label.getName().equalsIgnoreCase(type)
            ).findFirst().orElse(null);
        }

        return null;
    }

    //filters the collections of label looking for anything that match with 'BUG' label
    private BoardList getBoardList(List<BoardList> boardLists, String listName)
    {
        if (boardLists != null && boardLists.size()>0)
        {
            //Looking for the boardList with the listName name. If none found, return null (case not covered)
            return boardLists.stream().filter(
                    boardList -> boardList.getName().equalsIgnoreCase(listName)
            ).findFirst().orElse(null);
        }

        return null;
    }

    //Return an positive integer between 0 an max (exclusive) provided value.
    private Integer getRandom (Integer max){
        return new Random().nextInt(max);
    }
}
