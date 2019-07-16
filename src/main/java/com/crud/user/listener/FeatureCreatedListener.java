package com.crud.user.listener;

import com.crud.user.event.FeatureCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Component
public class FeatureCreatedListener implements ApplicationListener<FeatureCreatedEvent> {

    @Override
    public void onApplicationEvent(FeatureCreatedEvent recursoCriadoEvent) {
        HttpServletResponse response = recursoCriadoEvent.getResponse();
        Integer codigo = recursoCriadoEvent.getId();

        adicionarHeaderLocation(response, codigo);
    }

    @EventListener
    public void handleResponseLocationHeader(FeatureCreatedEvent event) {
        HttpServletResponse response = event.getResponse();
        Integer idRecurso = event.getId();

        URI locationURI = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(idRecurso)
                .toUri();

        response.addHeader("Location", locationURI.toASCIIString());
    }

    private void adicionarHeaderLocation(HttpServletResponse response, Integer codigo) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(codigo).toUri();
        response.setHeader("Location", uri.toASCIIString());
    }
}
