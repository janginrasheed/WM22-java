package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private int roleId;
}
