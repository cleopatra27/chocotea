package com.chocotea.bean;

import com.chocotea.bean.postman.DynamicVariables;
import com.chocotea.core.annotations.ChocoCurrencyTest;
import com.chocotea.core.annotations.ChocoRandom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class testRequest {

    @ChocoCurrencyTest(accept = {"NGN", "USD"})
    private String currency;

    @Size(min = 2, max = 6)
    private String nums;

    @Email
    private String email;

    private boolean deed;

    @NotNull
    @Size(min = 2, max = 6)
    private int num;

    @NotNull
    @NotBlank
    private  String reference;

    @ChocoRandom(dynamic = DynamicVariables.randomStreetAddress)
    private String address;
}
