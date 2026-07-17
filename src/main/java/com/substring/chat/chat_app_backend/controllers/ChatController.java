package com.substring.chat.chat_app_backend.controllers;

import com.substring.chat.chat_app_backend.config.AppConstants;
import com.substring.chat.chat_app_backend.entities.Message;
import com.substring.chat.chat_app_backend.entities.Room;
import com.substring.chat.chat_app_backend.payload.MessageRequest;
import com.substring.chat.chat_app_backend.repositories.RoomRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;




@Controller
@CrossOrigin(AppConstants.FRONT_END_BASE_URL)
public class ChatController {

    private RoomRepository roomRepository;

    private static final Map<String, Set<String>> roomUsers = new ConcurrentHashMap<>();

    public ChatController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    //for sending and receiving messsages

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request
    )

    {

        Room room = roomRepository.findByRoomId(request.getRoomId());

        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setMessageId(java.util.UUID.randomUUID().toString());
        message.setStatus("SENT");
        message.setFileName(request.getFileName());
        message.setFileUrl(request.getContent());
        message.setTimeStamp(LocalDateTime.now());
        message.setGender(request.getGender());

        if(room != null) {
            room.getMessages().add(message);
            roomRepository.save(room);
        } else{
            throw new RuntimeException("Room not found");
        }


        return message;
    }

    @MessageMapping("/typing/{roomId}")
    @SendTo("/topic/typing/{roomId}")
    public String typing(
            @DestinationVariable String roomId,
            String username
    ) {
        return username;
    }

    @MessageMapping("/join/{roomId}")
    @SendTo("/topic/users/{roomId}")
    public Set<String> joinRoom(
            @DestinationVariable String roomId,
            String username
    ) {

        roomUsers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
                .add(username);

        return roomUsers.get(roomId);
    }

    @MessageMapping("/leave/{roomId}")
    @SendTo("/topic/users/{roomId}")
    public Set<String> leaveRoom(
            @DestinationVariable String roomId,
            String username
    ) {

        Set<String> users = roomUsers.get(roomId);

        if (users != null) {
            users.remove(username);
        }

        return users;
    }
}
