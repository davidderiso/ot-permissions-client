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
public class RLCPermission {

    private String permissionUrn;
    private String description;
    private Long permissionId;

}


