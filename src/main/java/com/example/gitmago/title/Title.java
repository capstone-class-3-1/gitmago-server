package com.example.gitmago.title;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Title {
    private String name;
    private int level;
    private String imageUrl;
    private boolean obtained; //획득여부
    private LocalDateTime obtainedAt; //언제 획득했는지

    private TitleType type;

}

