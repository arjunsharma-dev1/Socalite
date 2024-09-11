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
public class Name {
        @Column(nullable = false)
        private String firstName;

        @Column
        private String middleName;

        @Column(nullable = false)
        private String lastName;

        public Name(String firstName, String middleName, String lastName) {
                this.firstName = firstName;
                this.middleName = middleName;
                this.lastName = lastName;
        }
}
