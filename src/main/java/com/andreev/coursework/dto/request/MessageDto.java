package com.andreev.coursework.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MessageDto {
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateSend;

    public String getContent() {
        return content;
    }

    public Date getDateSend() {
        return dateSend;
    }
}
