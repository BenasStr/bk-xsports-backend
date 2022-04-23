package com.ktu.xsports.api.config;

import com.ktu.xsports.api.converter.TricksToIdsConverter;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.dto.response.TrickResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelMapping {

    private final TricksToIdsConverter tricksToIdsConverter;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper
                .typeMap(Trick.class, TrickResponse.class)
                .addMappings(
                        mapper -> mapper.using(tricksToIdsConverter)
                                .map(Trick::getTrickParents, TrickResponse::setTrickParentsIds))
                .addMappings(
                        mapper -> mapper.using(tricksToIdsConverter)
                                .map(Trick::getTrickChildren, TrickResponse::setTrickChildrenIds));

        return modelMapper;
    }
}
