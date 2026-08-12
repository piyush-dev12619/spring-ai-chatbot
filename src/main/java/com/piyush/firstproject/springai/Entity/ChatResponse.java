package com.piyush.firstproject.springai.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String title;
    private String content;
    private Integer createdYear;
}
