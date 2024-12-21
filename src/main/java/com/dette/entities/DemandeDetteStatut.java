package com.dette.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString()

public enum DemandeDetteStatut {
    EN_ATTENTE,
    VALIDEE,
    REFUSEE,
    ANNULEE,
    RELANCEE
}