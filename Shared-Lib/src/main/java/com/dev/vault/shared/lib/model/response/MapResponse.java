package com.dev.vault.shared.lib.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapResponse {

    private Map<Object, Object> mapResponse = new HashMap<>();

}
