package com.sven.ce.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.sven.ce.util.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @NotNull(message = "{validation.username.empty}")
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
