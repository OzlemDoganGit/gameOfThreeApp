package com.takeaway.gameofthree.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@ApiModel(description = "Represents Game Of Three NickName")
@Data
@FieldDefaults(makeFinal = false, level = AccessLevel.PROTECTED)
@JsonTypeName("NickName")
@NoArgsConstructor
public class NickNameDTO implements Serializable {
    
    private static final long serialVersionUID = -405825017125717316L;
    
    String playerNickName;
}
