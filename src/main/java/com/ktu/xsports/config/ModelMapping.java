package com.ktu.xsports.config;

import com.ktu.xsports.api.converter.publish.PublishToPublishResponse;
import com.ktu.xsports.api.converter.trick.TrickVariantToTrickBasicResponseConverter;
import com.ktu.xsports.api.converter.trick.TrickVariantToTrickResponseConverter;
import com.ktu.xsports.api.domain.Publish;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.dto.response.publish.PublishResponse;
import com.ktu.xsports.api.dto.response.trick.TrickBasicResponse;
import com.ktu.xsports.api.dto.response.trick.TrickExtendedResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelMapping {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        trickToTrickResponseMapping(modelMapper);
        publishToPublishResponse(modelMapper);

        return modelMapper;
    }

    private void trickToTrickResponseMapping(ModelMapper modelMapper) {
        modelMapper.typeMap(TrickVariant.class, TrickExtendedResponse.class)
            .addMappings(new TrickVariantToTrickResponseConverter());

        modelMapper.typeMap(TrickVariant.class, TrickBasicResponse.class)
            .addMappings(new TrickVariantToTrickBasicResponseConverter());
    }

    private void publishToPublishResponse(ModelMapper modelMapper) {
        modelMapper.typeMap(Publish.class, PublishResponse.class)
            .addMappings(new PublishToPublishResponse());
    }
}
