package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.dto.request.SportRequest;
import com.ktu.xsports.api.dto.response.SportResponse;
import com.ktu.xsports.api.service.sport.internal.SportCreator;
import com.ktu.xsports.api.service.sport.internal.SportRemover;
import com.ktu.xsports.api.service.sport.internal.SportRetriever;
import com.ktu.xsports.api.service.sport.internal.SportUpdater;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("sports")
public class SportController {

    private final SportRetriever sportRetriever;
    private final SportUpdater sportUpdater;
    private final SportCreator sportCreator;
    private final SportRemover sportRemover;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findSports() {
        List<Sport> sports = sportRetriever.findSports();
        List<SportResponse> sportsResponse = sports.stream().map(
                s -> modelMapper.map(s, SportResponse.class)
        ).toList();
        return ResponseEntity.ok(Map.of("data",sportsResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSport(@PathVariable long id)
    {
        Optional<Sport> sport = sportRetriever.findSportById(id);

        return ResponseEntity.of(
                sport.map(s -> Map.of("data", modelMapper.map(s, SportResponse.class))));
    }

    @PostMapping()
    public ResponseEntity<?> createSport(
            @RequestBody @Valid SportRequest sportRequest
    ) {
        Sport sport = sportRequest.toSport();
        Optional<Sport> newSport = sportCreator.createSport(sport);
        return ResponseEntity.of(
                newSport.map(s -> Map.of("data", modelMapper.map(s, SportResponse.class))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(
            @RequestBody @Valid SportRequest sportRequest,
            @PathVariable long id
    ) {
        Sport sport = sportRequest.toSport();
        Optional<Sport> updatedSport = sportUpdater.updateSport(sport, id);
        return ResponseEntity.of(
                updatedSport.map(s -> Map.of("data", modelMapper.map(s, SportResponse.class))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable long id) {
        Optional<Sport> deletedSport = sportRemover.removeSport(id);
        return ResponseEntity.of(
                deletedSport.map( s ->
                        Map.of("data", modelMapper.map(s, SportResponse.class))));
    }

}
