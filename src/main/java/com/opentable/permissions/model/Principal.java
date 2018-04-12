package com.opentable.permissions.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonSerialize
@Data
@AllArgsConstructor
@Builder
@SuppressWarnings("PMD.ImmutableField")
public class Principal {
    private String principalUrn;
    private String description;
    private Long principalId;

}
