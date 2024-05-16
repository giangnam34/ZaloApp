package com.essay.zaloapp.domain.payload.response.RoomChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatusOfUser {
    private String state;
    private String lastChanged;
}
