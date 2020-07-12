package com.dan.travel_agent.service;

import com.dan.travel_agent.models.City;
import com.dan.travel_agent.models.Link;
import org.springframework.stereotype.Component;

@Component
public class CityMessageCreator implements MessageCreator<City> {
    private static final String END_OF_LINE = "\n";

    @Override
    public String createMessage(City city) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(city.getCityName().toUpperCase() + END_OF_LINE + END_OF_LINE);
        stringBuilder.append(city.getCityDescription() + END_OF_LINE + END_OF_LINE);
        stringBuilder.append(getLinksInfo(city));
        return stringBuilder.toString();
    }

    private static String getLinksInfo(City city) {
        if (city.getLinks().isEmpty()) return "";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Useful links :" + END_OF_LINE);
        city.getLinks().stream()
                .map(CityMessageCreator::getLinkInfo)
                .forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    private static String getLinkInfo(Link link) {
        return link.getLink() + " - " + link.getLinkDescription() + END_OF_LINE + END_OF_LINE;
    }
}
