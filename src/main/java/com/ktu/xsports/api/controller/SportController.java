package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.dto.request.SportRequest;
import com.ktu.xsports.api.dto.response.SportResponse;
import com.ktu.xsports.api.service.JwtService;
import com.ktu.xsports.api.service.SportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sports")
@Slf4j
public class SportController {

    private final SportService sportService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findSports() {
        log.info("finding sports");
        List<Sport> sports = sportService.findSports();
        List<SportResponse> sportsResponse = sports.stream().map(
                s -> modelMapper.map(s, SportResponse.class)
        ).toList();
        return ResponseEntity.ok(Map.of("data", sportsResponse));
    }

    @GetMapping("/my_list")
    public ResponseEntity<?> findMySports(@RequestHeader("Authorization") String token) {
        log.info("finding my sports");
        String email = jwtService.extractUsername(token);
        List<Sport> sports = sportService.findMySports(email);
        List<SportResponse> sportResponse = sports.stream().map(
            sport -> modelMapper.map(sport, SportResponse.class)
        ).toList();
        return ResponseEntity.ok(Map.of("data", sportResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSport(@PathVariable long id)
    {
        log.info("finding sport by id");
        Optional<Sport> sport = sportService.findSportById(id);

        return ResponseEntity.of(
                sport.map(s -> Map.of("data", modelMapper.map(s, SportResponse.class))));
    }

    @PostMapping()
    public ResponseEntity<?> createSport(@RequestBody @Valid SportRequest sportRequest) {
        log.info("posting sports");
        Sport sport = sportRequest.toSport();
        Optional<Sport> newSport = sportService.createSport(sport);
        return ResponseEntity.of(
                newSport.map(s -> Map.of("data", modelMapper.map(s, SportResponse.class))));
    }

    @PostMapping("/my_list")
    public ResponseEntity<?> addMySport(
        @RequestHeader("Authorization") String token,
        @RequestParam("sportId") int sportId) {
        log.info("adding to my list");
        String email = jwtService.extractUsername(token);
        sportService.addSportToUserList(sportId, email);

        return ResponseEntity.ok("");
    }

//    @PostMapping(
//            path = "/upload/image",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    )
//    public ResponseEntity<?> uploadSportImage(
//            @RequestParam("file") MultipartFile file
//    ) {
////        String path = sportService.uploadImage(file);
////        return ResponseEntity.ok(Map.of("data", path));
//    }

    @GetMapping("{sportId}/download/image")
    public byte[] downloadSportImage(@PathVariable long sportId) {
        return sportService.downloadSportImage(sportId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSport(
            @RequestBody @Valid SportRequest sportRequest,
            @PathVariable long id
    ) {
        log.info("updating sport");
        Sport sport = sportRequest.toSport();
        Optional<Sport> updatedSport = sportService.updateSport(sport, id);
        return ResponseEntity.of(
                updatedSport.map(s -> Map.of("data", modelMapper.map(s, SportResponse.class))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSport(@PathVariable long id) {
        log.info("deleting sport");
        Optional<Sport> deletedSport = sportService.removeSport(id);
        return ResponseEntity.of(
                deletedSport.map( s ->
                        Map.of("data", modelMapper.map(s, SportResponse.class))));
    }

    @DeleteMapping("/my_list")
    public ResponseEntity<?> deleteMySport(
            @RequestHeader("Authorization") String token,
            @RequestParam("sportId") int sportId
    ) {
        log.info("removing sport from my list");
        String email = jwtService.extractUsername(token);
        sportService.removeMyListSport(sportId, email);
        return ResponseEntity.ok("");
    }

}
