package com.socalite.scoalite.user.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_emailaddress", columnNames = {"email"}),
        @UniqueConstraint(name = "unique_phonenumber", columnNames = {"number"}),
})
public class Contact {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ContactSequenceGenerator")
        @SequenceGenerator(name = "ContactSequenceGenerator", sequenceName = "contact_seq", initialValue = 1, allocationSize = 1)
        Long id;

        @Column
        @Embedded
        EmailAddress emailAddress;

        @Column
        @Embedded
        PhoneNumber phoneNumber;

        public Contact(Long id, EmailAddress emailAddress, PhoneNumber phoneNumber) {
                this.id = id;
                this.emailAddress = emailAddress;
                this.phoneNumber = phoneNumber;
        }
}