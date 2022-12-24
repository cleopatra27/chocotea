package org.example.utility;

import org.example.bean.postman.Auth;
import org.example.bean.postman.DynamicVariables;
import org.example.bean.postman.Info;
import org.example.core.annotations.ChocoRandom;
import org.example.core.annotations.SpringCollection;
import org.example.core.annotations.SpringRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@SpringCollection(name = "susan more collection", createTest = true, baseUrl = "https://example.com")
public class PathGenerator {

    //accept atomic/concurrent String do we can append to it continuously
    @SpringRequest(requestBean = Info.class, auth = Auth.Type.bearer, authValue = "ABCTOKEN")
    @PostMapping(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    public String generate(
//                           @PathVariable( "resortID" )  int resortID,
//                           @PathVariable( "resortID" )  int resortID,

                           @RequestParam("id") @NotNull @ChocoRandom(dynamic = DynamicVariables.randomCatchPhrase) String id,
                           @RequestParam("ids") String ids,
                           @RequestBody Info resource, @RequestHeader("Authorization") int authorization){
        return null;
    }

//    @SpringRequest(requestBean = Info.class)
    @GetMapping(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    public String oother(@PathVariable( "dayID" ) @NotNull @Min(1) @Max(1) int dayID){
        return null;
    }
}
