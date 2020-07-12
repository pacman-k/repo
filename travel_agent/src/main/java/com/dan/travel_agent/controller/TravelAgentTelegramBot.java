package com.dan.travel_agent.controller;

import com.dan.travel_agent.models.BotCommand;
import com.dan.travel_agent.models.City;
import com.dan.travel_agent.service.CityService;
import com.dan.travel_agent.service.MessageCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Component
public class TravelAgentTelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LogManager.getLogger(TravelAgentTelegramBot.class);
    private static final String COMMANDS_PATH = "classpath:commands.json";
    private static final String MESSAGE_ON_MISSING = "is missing :( \n Try something else \uD83D\uDE07";

    private final String botUsername;
    private final String botToken;

    private Set<BotCommand> commands;
    private CityService cityService;
    private MessageCreator<City> cityMessageCreator;

    public TravelAgentTelegramBot(@Value("${telegram.bot.username}") String botUsername,
                                  @Value("${telegram.bot.token}") String botToken,
                                  @Autowired CityService cityService,
                                  @Autowired MessageCreator<City> cityMessageCreator) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.cityService = cityService;
        this.cityMessageCreator = cityMessageCreator;
    }


    @PostConstruct
    private void postConstruct() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.commands = objectMapper.readValue(
                    ResourceUtils.getFile(COMMANDS_PATH),
                    new TypeReference<Set<BotCommand>>() {});
        } catch (IOException e) {
            this.commands = Collections.emptySet();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message requestMessage = update.getMessage();
        createAndSendMessage(requestMessage);
    }

    private void createAndSendMessage(Message requestMessage) {
        String text = trimText(requestMessage.getText());
        Long chatId = requestMessage.getChatId();
        if (text.isEmpty()) return;
        if (text.startsWith("/")) {
            sendCommandValue(chatId, text);
            return;
        }
        sendCityInfo(chatId, text);
    }

    private void sendCommandValue(Long chatId, String commandName) {
        String commandValue = commands.stream()
                .filter(command -> command.isThatCommand(commandName))
                .map(BotCommand::getValue)
                .findFirst().orElseGet(BotCommand::getDefaultValue);
        sendMessage(chatId, commandValue);
    }

    private void sendCityInfo(Long chatId, String cityName) {
        Optional<City> city = cityService.findCityByName(cityName);
        String cityInfo = city
                .map(cityMessageCreator::createMessage)
                .orElse(String.format("'%s' %s", cityName, MESSAGE_ON_MISSING));
        sendMessage(chatId, cityInfo);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.log(
                    Level.ERROR,
                    String.format("Cant send command '%s' to chat with id %d", text, chatId),
                    e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private static String trimText(String text) {
        return text == null ? "" : text.trim();
    }
}
