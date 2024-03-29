package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.SportRequest;
import com.ktu.xsports.api.dto.response.SportResponse;
import com.ktu.xsports.api.service.SportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.ktu.xsports.api.util.ApiVersionPrefix.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + "/sports")
@Slf4j
public class SportController {

    private final SportService sportService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findSports(
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "") String publishStatus,
        @AuthenticationPrincipal User user
    ) {
        log.info("finding sports");
        List<Sport> sports = sportService.findSports(search, publishStatus, user);
        List<SportResponse> sportsResponse = sports.stream().map(
                s -> modelMapper.map(s, SportResponse.class)
        ).toList();
        return ResponseEntity.ok(Map.of("data", sportsResponse));
    }

    @GetMapping("/my_list")
    public ResponseEntity<?> findMySports(
        @AuthenticationPrincipal User user
    ) {
        log.info("finding my sports");
        List<Sport> sports = sportService.findMySports(user.getId());
        List<SportResponse> sportResponse = sports.stream().map(
            sport -> modelMapper.map(sport, SportResponse.class)
        ).toList();
        return ResponseEntity.ok(Map.of("data", sportResponse));
    }

    @GetMapping("/my_list/explore")
    public ResponseEntity<?> findAllExploreTricks(@AuthenticationPrincipal User user) {
        log.info("Finding explorable sports.");
        List<Sport> sports = sportService.findExploreSports(user.getId());
        List<SportResponse> sportResponse = sports.stream().map(
            sport -> modelMapper.map(sport, SportResponse.class)
        ).toList();
        return ResponseEntity.ok(Map.of("data", sportResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSport(@PathVariable long id)
    {
        log.info("finding sport by id");
        Sport sport = sportService.findSportById(id);
        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(sport, SportResponse.class))
        );
    }

    @PostMapping()
    public ResponseEntity<?> createSport(@RequestBody @Valid SportRequest sportRequest) {
        log.info("Creating sport");
        Sport sport = sportService.createSport(sportRequest.toSport());
        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(sport, SportResponse.class))
        );
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadSportImage(
        @RequestParam("file") MultipartFile image,
        @PathVariable int id
    ) {
        log.info("User is updating sport picture");
        Sport sport = sportService.updateSportImage(id, image);
        return ResponseEntity.ok(Map.of("data", sport.getPhotoUrl()));
    }

    @PostMapping("/my_list")
    public ResponseEntity<?> addMySport(
        @AuthenticationPrincipal User user,
        @RequestParam("sportId") int sportId) {
        log.info("adding to my list");
        sportService.addSportToUserList(sportId, user.getEmail());
        return ResponseEntity.ok("");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSport(
        @RequestBody @Valid SportRequest sportRequest,
        @PathVariable long id
    ) {
        log.info("updating sport");
        Sport sport = sportService.updateSport(sportRequest.toSport(), id);
        return ResponseEntity.ok(
                Map.of("data", modelMapper.map(sport, SportResponse.class))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSport(@PathVariable Long id) {
        log.info("deleting sport");
        sportService.removeSport(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/my_list")
    public ResponseEntity<?> deleteMySport(
        @AuthenticationPrincipal User user,
        @RequestParam("sportId") int sportId
    ) {
        log.info("removing sport from my list");
        sportService.removeMyListSport(sportId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
