package com.opentable.permissions.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize
@SuppressWarnings("PMD.ImmutableField")
public class PrincipalRolesResponse {

    private String principal;
    private String context;
    private List<String> roles;
}
