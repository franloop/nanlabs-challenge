package com.nanlab.challenge.spacex.taskmanagementapi.dto;

import lombok.Data;
import org.springframework.lang.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class CardRequestDTO {
    private String id;

    @Nullable
    private String title;

    @NotBlank(message = "Type must be defined. Available options are: issue, bug, task")
    @Pattern(message = "Wrong type. Available options are: issue, bug, task", regexp = "issue|bug|task")
    private String type;

    private String description;

    private List<Member> members;

    private List<Label> labels;
}