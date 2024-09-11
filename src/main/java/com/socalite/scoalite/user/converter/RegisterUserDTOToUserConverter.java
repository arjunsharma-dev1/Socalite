package com.socalite.scoalite.user.converter;

import com.socalite.scoalite.utils.Converter;
import com.socalite.scoalite.utils.ToDTOConverter;
import com.socalite.scoalite.user.dto.RegisterUserRequestDTO;
import com.socalite.scoalite.user.model.*;

@Converter
public class RegisterUserDTOToUserConverter implements ToDTOConverter<RegisterUserRequestDTO, User> {

    @Override
    public User convert(RegisterUserRequestDTO input) {
        var name = new Name()
                .setFirstName(input.getFirstName())
                .setLastName(input.getLastName())
                .setMiddleName(input.getMiddleName());

        var contact = new Contact()
                .setEmailAddress(new EmailAddress(input.getEmailAddress()))
                .setPhoneNumber(new PhoneNumber(input.getPhoneNumber()));

        return new User()
                .setUsername(input.getUsername())
                .setName(name)
                .setContact(contact);
    }
}
