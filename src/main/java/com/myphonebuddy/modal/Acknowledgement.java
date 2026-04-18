package com.myphonebuddy.modal;

import com.myphonebuddy.enums.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Acknowledgement {

    private String message;

    @Builder.Default
    private MessageType type = MessageType.INFO;

}
