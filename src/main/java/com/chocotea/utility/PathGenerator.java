package com.chocotea.utility;

import com.chocotea.bean.postman.Auth;
import com.chocotea.bean.postman.DynamicVariables;
import com.chocotea.bean.postman.Language;
import com.chocotea.bean.testRequest;
import com.chocotea.bean.testResponse;
import com.chocotea.core.annotations.ChocoRandom;
import com.chocotea.core.annotations.SpringCollection;
import com.chocotea.core.annotations.SpringRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@SpringCollection(name = "susan more collection", createTest = true, baseUrl = "https://example.com")
public class PathGenerator {

    //accept atomic/concurrent String do we can append to it continuously
    @SpringRequest(request = testRequest.class, response = testResponse.class, auth = Auth.Type.bearer, authValue = "ABCTOKEN")
    @PostMapping(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    public String generate(
//                           @PathVariable( "resortID" )  int resortID,
            @PathVariable( "resortID" )  @ChocoRandom(dynamic = DynamicVariables.randomEmail) int resortID,
            @RequestParam("id") @NotNull @ChocoRandom(dynamic = DynamicVariables.randomCatchPhrase) String id,
            @RequestParam("ids") String ids,
            @RequestBody testRequest resource, @RequestHeader("Authorization") int authorization){
        return null;
    }

//    @SpringRequest(requestBean = Info.class)
    @GetMapping(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    public String oother(@PathVariable( "dayID" ) @NotNull @Min(1) @Max(1) int dayID){
        return null;
    }

    @GetMapping(value = "/data")
    @SpringRequest(auth = Auth.Type.bearer, authValue = "ABCTOKEN")
    public String ootherQ(@RequestParam( "dayID" ) @NotNull @Min(1) @Max(1) int dayID){
        return null;
    }

//    @GetMapping(value = "/data")
//    @SpringRequest(auth = Auth.Type.bearer, authValue = "ABCTOKEN", language = Language.text, request = String.class)
//    public String encodedtest(String name){
//        return null;
//    }
}
