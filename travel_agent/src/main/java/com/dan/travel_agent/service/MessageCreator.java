package com.dan.travel_agent.service;

public interface MessageCreator<T> {

    String createMessage(T object);
}
