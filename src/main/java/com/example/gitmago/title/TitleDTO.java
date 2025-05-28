package com.example.gitmago.title;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TitleDTO {
    private String titleName;
    private int level;
    private TitleType type;
}
