package com.example.taxiservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HereRouteService {

    @Value("${here.maps.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    /**
     * Рассчитывает маршрут между двумя точками с использованием HERE Routing API
     */
    public RouteResult calculateRoute(Double startLat, Double startLng, Double endLat, Double endLng) {
        String url = "https://router.hereapi.com/v8/routes?transportMode=car&origin={startLat},{startLng}&destination={endLat},{endLng}&return=summary&apiKey={apiKey}";

        Map<String, Object> params = new HashMap<>();
        params.put("startLat", startLat);
        params.put("startLng", startLng);
        params.put("endLat", endLat);
        params.put("endLng", endLng);
        params.put("apiKey", apiKey);

        ResponseEntity<HereRouteResponse> response = restTemplate.getForEntity(url, HereRouteResponse.class, params);
        HereRouteResponse routeData = response.getBody();

        if (routeData != null && !routeData.getRoutes().isEmpty()) {
            HereRouteResponse.Route route = routeData.getRoutes().get(0);
            HereRouteResponse.Section section = route.getSections().get(0);

            double distance = section.getSummary().getLength() / 1000.0; // Meters to kilometers
            int duration = (int) Math.ceil(section.getSummary().getDuration() / 60.0); // Seconds to minutes

            return new RouteResult(distance, duration);
        }

        throw new RuntimeException("Unable to calculate route");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteResult {
        private double distance; // in kilometers
        private int duration; // in minutes
    }

    // Response classes for HERE API
    @Data
    static class HereRouteResponse {
        private List<Route> routes;

        @Data
        static class Route {
            private List<Section> sections;
        }

        @Data
        static class Section {
            private Summary summary;
        }

        @Data
        static class Summary {
            private double length; // in meters
            private double duration; // in seconds
        }
    }
}