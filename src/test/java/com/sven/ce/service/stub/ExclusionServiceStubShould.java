package com.sven.ce.service.stub;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.util.Pair;

public class ExclusionServiceStubShould
{

    @InjectMocks
    private ExclusionServiceStub underTest;
    private static final Set<Pair<String, String>> INVALID_DOB_SSN_SET = new HashSet<>();

    private static final Set<Pair<String, String>> VALID_DOB_SSN_SET = new HashSet<>();

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        underTest.init();

        INVALID_DOB_SSN_SET.add(Pair.of("2018-01-01", "111111111"));
        INVALID_DOB_SSN_SET.add(Pair.of("2018-02-01", "222222222"));
        INVALID_DOB_SSN_SET.add(Pair.of("2018-03-01", "333333333"));

        VALID_DOB_SSN_SET.add(Pair.of("2019-01-01", "111111111"));
        VALID_DOB_SSN_SET.add(Pair.of("2017-02-01", "222222222"));
        VALID_DOB_SSN_SET.add(Pair.of("2018-03-01", "333333334"));
    }

    @Test
    public void should_validate_return_false_if_invalid_dof_and_ssn()
    {

        List<Boolean> results = INVALID_DOB_SSN_SET.stream()
                        .map(s -> underTest.validate(s.getFirst(), s.getSecond()))
                        .collect(Collectors.toList());

        results.forEach(s -> assertThat(s).isEqualTo(false));
    }

    @Test
    public void should_validate_return_true_if_any_valid_dof_and_ssn()
    {

        List<Boolean> results = VALID_DOB_SSN_SET.stream()
                        .map(s -> underTest.validate(s.getFirst(), s.getSecond()))
                        .collect(Collectors.toList());

        results.forEach(s -> assertThat(s).isEqualTo(true));
    }

}
