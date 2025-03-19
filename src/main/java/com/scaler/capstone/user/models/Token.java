package com.scaler.capstone.user.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Token extends BaseModel {
    private String tokenValue;
    private User user;
    private Date expiryDate;
}