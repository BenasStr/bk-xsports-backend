package com.ktu.xsports.api.service.sport;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SportServiceImpl implements SportsService {

    private final SportRepository sportRepository;

    @Override
    public List<Sport> findSports() {
        return sportRepository.findAll();
    }

    @Override
    public byte[] downloadSportImage(long sportId) {
        Sport sport = sportRepository.findById(sportId).get();
        return null; //fileStore.download(path, sport.getPhoto());
    }

    @Override
    public Optional<Sport> findSportById(long id) {
        return sportRepository.findById(id);
    }

    @Override
    public Optional<Sport> createSport(Sport sport) {
        return Optional.of(sportRepository.save(sport));
    }

//    @Override
//    public String uploadImage(MultipartFile file) {
////        if (file.isEmpty()) {
////            throw new IllegalStateException("Cannot upload empty file");
////        }
////        if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType() ).contains(file.getContentType())) {
////            throw new IllegalStateException("File must be an image");
////        }
////        Map<String, String> metadata = new HashMap<>();
////        metadata.put("Content-Type", file.getContentType());
////        metadata.put("Content-Length", String.valueOf(file.getSize()));
////
////        //String path = String.format("%s/%s", BucketName.IMAGE.getBucketName(), "sports");
////        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
//
////        try {
////            //fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
////        } catch (IOException e) {
////            throw new IllegalStateException(e);
////        }
//
////        return fileName;
//    }

    @Override
    public Optional<Sport> updateSport(Sport sport, long id) {
        sport.setId(id);
        if(sportRepository.findById(id).isPresent()) {
            return Optional.of(sportRepository.save(sport));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Sport> removeSport(long id) {
        Optional<Sport> deletedSport = sportRepository.findById(id);
        if(deletedSport.isPresent()) {
            sportRepository.delete(deletedSport.get());
            return deletedSport;
        }
        return Optional.empty();
    }
}