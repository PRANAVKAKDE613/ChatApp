package com.substring.chat.chat_app_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {

    private String sender;
    private String content;

    private String fileName;
    private String fileUrl;
    private String messageType;

    private LocalDateTime timeStamp;
    private String messageId;
    private String status;
    private String profileImage;
    private String gender;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timeStamp = LocalDateTime.now();
        this.messageType = "TEXT";
        this.status = "SENT";
    }
}