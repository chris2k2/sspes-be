package de.cweyermann.ssps.backend.logic;

import de.cweyermann.ssps.backend.dtos.BffResult;
import de.cweyermann.ssps.backend.repo.Match;
import de.cweyermann.ssps.backend.repo.Repository;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetAllMatches {

    private final Repository repo;
    private final ModelMapper modelMapper;

    public GetAllMatches(Repository repo) {
        this.repo = repo;
        this.modelMapper = new ModelMapper();
    }

    public List<BffResult.Match> allFromDb() {
        List<Match> matchesInDb = repo.getAll();

        List<BffResult.Match> matches = matchesInDb
                .stream()
                .map(m -> modelMapper.map(m, BffResult.Match.class))
                .map(m -> m.withNiceDate())
                .sorted(Comparator.comparing(BffResult.Match::getDate))
                .collect(Collectors.toList());
        Collections.reverse(matches);

        return matches;
    }
}
