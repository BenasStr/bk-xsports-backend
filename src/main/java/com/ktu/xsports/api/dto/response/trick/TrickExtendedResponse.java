package com.ktu.xsports.api.dto.response.trick;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TrickExtendedResponse extends TrickBasicResponse {
    String video;
    String description;
    List<TrickBasicResponse> trickParents;
    List<TrickBasicResponse> trickChildren;
    List<TrickBasicResponse> trickVariants;
}