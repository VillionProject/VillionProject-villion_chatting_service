package org.example.villion_chatting_service.User;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document // mongoDB
public class User {
//    #2
    @Id
    private String nickName;
    private String fullName;
    private Status status;
}
