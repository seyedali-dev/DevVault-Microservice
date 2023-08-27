package com.dev.vault.projectservice.feign.client;

import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectErrorDecoder implements ErrorDecoder {

    @Override
    @SneakyThrows
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        log.error("❌ url: {{}} ❌", response.request().url());
        log.error("❌ dst: {{}} ❌", s);

        DevVaultException devVaultException = objectMapper.readValue(
                response.body().asInputStream(),
                DevVaultException.class
        );
        return new DevVaultException(
                devVaultException.getMessage(),
                devVaultException.getHttpStatus(),
                response.status()
        );
    }

}
