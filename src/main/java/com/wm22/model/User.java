package com.wm22.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
