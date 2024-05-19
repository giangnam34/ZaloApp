package com.essay.zaloapp.domain.payload.response.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileData {
    private String name;
    private Long size;
    private String type;
    private Boolean audio;
    private Float duration;
    private String url;
    private String preview;
    private Long progress;
}
