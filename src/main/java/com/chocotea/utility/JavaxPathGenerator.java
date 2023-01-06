package com.chocotea.utility;

import com.chocotea.bean.postman.Auth;
import com.chocotea.bean.postman.DynamicVariables;
import com.chocotea.bean.postman.Info;
import com.chocotea.core.annotations.*;
import jakarta.ws.rs.*;

import javax.validation.constraints.NotNull;


@JavaxCollection(name = "susan more collection javaz", createTest = true, baseUrl = "https://example.com")
@Path(value = "/")
public class JavaxPathGenerator {

    //accept atomic/concurrent String do we can append to it continuously
    @POST
    @Path(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    @JavaxRequest(requestBean = Info.class, auth = Auth.Type.bearer, authValue = "ABCTEN")
    public String generate(
//                           @PathVariable( "resortID" )  int resortID,
                           @PathParam( "resortID" )  @ChocoRandom(dynamic = DynamicVariables.randomEmail) int resortID,
                           @QueryParam("id") @NotNull @ChocoRandom(dynamic = DynamicVariables.randomCatchPhrase) String id,
                           @QueryParam("ids") String ids,
                           Info resource, @HeaderParam("Authorization") int authorization){
        return null;
    }

//    @SpringRequest(requestBean = Info.class)
//    @GetMapping(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
//    public String oother(@PathVariable( "dayID" ) @NotNull @Min(1) @Max(1) int dayID){
//        return null;
//    }
}
