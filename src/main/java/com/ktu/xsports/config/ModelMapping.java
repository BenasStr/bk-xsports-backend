package com.ktu.xsports.config;

import com.ktu.xsports.api.converter.trick.ProgressToStatusConverter;
import com.ktu.xsports.api.converter.trick.TricksToIdsConverter;
import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.dto.response.CategoryResponse;
import com.ktu.xsports.api.dto.response.TrickResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;

@Configuration
@RequiredArgsConstructor
public class ModelMapping {

    private final TricksToIdsConverter tricksToIdsConverter;
    private final ProgressToStatusConverter progressToStatusConverter;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        trickToTrickResponseMapping(modelMapper);

        return modelMapper;
    }

    private void trickToTrickResponseMapping(ModelMapper modelMapper) {
        modelMapper
                .typeMap(Trick.class, TrickResponse.class)
                .addMappings(
                        mapper -> mapper.using(tricksToIdsConverter)
                                .map(Trick::getTrickParents, TrickResponse::setTrickParentsIds))
                .addMappings(
                        mapper -> mapper.using(tricksToIdsConverter)
                                .map(Trick::getTrickChildren, TrickResponse::setTrickChildrenIds))
                .addMappings(
                        mapper -> mapper.using(progressToStatusConverter)
                                .map(Trick::getProgress, TrickResponse::setStatus)
                );
    }
}
