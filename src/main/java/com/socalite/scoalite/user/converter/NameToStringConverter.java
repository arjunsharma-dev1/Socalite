package com.socalite.scoalite.user.converter;

import com.socalite.scoalite.utils.Converter;
import com.socalite.scoalite.utils.ToDTOConverter;
import com.socalite.scoalite.user.model.Name;

import java.util.Objects;

@Converter
public class NameToStringConverter implements ToDTOConverter<Name, String> {
    @Override
    public String convert(Name input) {
        var sb = new StringBuilder();
        var firstName = input.getFirstName();
        if (Objects.nonNull(firstName)) {
            sb.append(firstName);
        }
        var middleName = input.getMiddleName();
        if (Objects.nonNull(middleName)) {
            sb.append(middleName);
        }
        var lastName = input.getLastName();
        if (Objects.nonNull(lastName)) {
            sb.append(lastName);
        }
        return sb.toString();
    }
}