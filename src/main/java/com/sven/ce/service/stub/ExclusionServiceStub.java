package com.sven.ce.service.stub;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.sven.ce.service.ExclusionService;

//@formatter:off
/**
 * stub implementation of {@link ExclusionService}.
 * 
 * <p> 
 * The following dob-ssn combination are invalid
 * "2018-01-01" - "111111111"
 * "2018-02-01" - "222222222"
 * "2018-03-01" - "333333333"
 */
//@formatter:on
@Profile("local")
@Service
public class ExclusionServiceStub implements ExclusionService
{

    private static final Set<Pair<String, String>> INVALID_DOB_SSN_SET = new HashSet<>();

    @PostConstruct
    public void init()
    {
        INVALID_DOB_SSN_SET.add(Pair.of("2018-01-01", "111111111"));
        INVALID_DOB_SSN_SET.add(Pair.of("2018-02-01", "222222222"));
        INVALID_DOB_SSN_SET.add(Pair.of("2018-03-01", "333333333"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(String dob, String ssn)
    {

        return INVALID_DOB_SSN_SET.stream()
                        .noneMatch(s -> s.getFirst().equalsIgnoreCase(dob) && s.getSecond().equalsIgnoreCase(ssn));
    }

}
