package com.socalite.scoalite.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Embeddable
public class PhoneNumber {
    @Column
    private String number;

    public PhoneNumber(String number) {
        this.number = number;
    }
}

