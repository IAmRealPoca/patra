package com.prc391.patra.users;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class User {
    @Id
    private String id;
    private String username;
    private String passHash;
    private String name;

    private boolean enabled;

    private List<Long> roles;
}
