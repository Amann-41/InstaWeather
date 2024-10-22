package com.example.InstaWeather.controllers;

import com.example.InstaWeather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class WeatherController {

    @Value("${api.key}")
    private String apiKey;

    @GetMapping("/")
    public String getIndex() {
        return "index";  // Serves index.html from the templates folder
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam("city") String city, Model model) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
        RestTemplate restTemplate = new RestTemplate();

        WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);
        if (weatherResponse != null) {
            model.addAttribute("city", weatherResponse.getName());
            model.addAttribute("country", weatherResponse.getSys().getCountry());
            model.addAttribute("weatherDescription", weatherResponse.getWeather().get(0).getDescription());
            model.addAttribute("temperature", weatherResponse.getMain().getTemp());
            model.addAttribute("humidity", weatherResponse.getMain().getHumidity());
            model.addAttribute("windSpeed", weatherResponse.getWind().getSpeed());  // Ensure Wind class is correct
            String weatherIcon = "wi wi-own" + weatherResponse.getWeather().get(0).getId();
            model.addAttribute("weatherIcon", weatherIcon);
        } else {
            model.addAttribute("error", "City not found");
        }
        return "weather";  // Serves weather.html from the templates folder
    }
}