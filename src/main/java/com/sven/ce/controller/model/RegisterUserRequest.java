package com.sven.ce.controller.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.sven.ce.annotation.UniqueUserName;
import com.sven.ce.util.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserRequest
{

    @UniqueUserName
    @NotNull(message = "{validation.username.empty}")
    @Size(min = 1, message = "{validation.username.empty}")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "{validation.username.not-alphanumerical-or-have-whitespace}")
    private String username;

    @NotNull
    @Size(min = 4, message = "{validation.password.less-than-four-characters}")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).*$",
                    message = "{validation.password.not-contains-a-uppercase-character-or-a-number}")
    private String password;

    @DateTimeFormat(pattern = Constants.DOB_DATE_FORMAT_PATTERN)
    private Date dateOfBirth;

    @NotNull(message = "{validation.ssn.empty}")
    @Size(min = 1, message = "{validation.ssn.invalid}")
    private String ssn;
}
