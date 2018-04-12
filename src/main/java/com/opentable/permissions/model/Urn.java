package com.opentable.permissions.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Optional;

@JsonSerialize
public class Urn {

    private final String nameSpace;
    private final String nameSpaceSS;

    public Urn (String namespace, String nameSpaceSS) {
        this.nameSpace = namespace;
        this.nameSpaceSS = nameSpaceSS;
    }

    public String toString() {
        return "urn:" + nameSpace + ":" + nameSpaceSS;

    }

    public static Optional<Urn> fromString (String urn) {
        String[] splitUrn = urn.trim().split(":");
        if (splitUrn.length != 3 || !splitUrn[0].equalsIgnoreCase("urn")) {
            return Optional.empty();
        }

        return Optional.of(new Urn(splitUrn[1],splitUrn[2]));
    }
}
