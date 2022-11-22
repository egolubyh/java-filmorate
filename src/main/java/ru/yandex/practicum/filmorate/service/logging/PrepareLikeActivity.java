package ru.yandex.practicum.filmorate.service.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrepareLikeActivity implements PrepareActivityServise {

    @Autowired
    private final ObjectMapper objectMapper;

    @Override
    public Map<String, Long> prepareActivityAttributes(HttpServletRequest request) {
        Map<String, String> attributeMap =
                objectMapper.convertValue(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE), Map.class);

        return Map.ofEntries(
                    Map.entry("userId", Long.parseLong(attributeMap.get("userId"))),
                    Map.entry("entityId", Long.parseLong(attributeMap.get("id")))
            );
    }
}
