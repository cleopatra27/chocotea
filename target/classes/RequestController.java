//package org.example;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import javax.validation.constraints.*;
//
//import static org.example.ResponseBean.toSuccess;
//
//@SpringBootApplication
//@RestController
//@Validated
//@RequestMapping("/skiers")
//public class RequestController extends SpringBootServletInitializer {
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(RequestController.class);
//    }
//
//        public static void main(String[] args) {
//        SpringApplication.run(RequestController.class, args);
//    }
//
//
//    @PostMapping(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseBean create(@PathVariable( "resortID" ) @NotNull @Min(1) @Max(10) int resortID,
//                       @PathVariable( "seasonID" ) @NotNull @Min(2022) @Max(2022) int seasonID,
//                       @PathVariable( "dayID" ) @NotNull @Min(1) @Max(1) int dayID,
//                       @PathVariable( "skierID" ) @NotNull @Min(1) @Max(100000) int skierID,
//                       @RequestBody @Valid CreateSkiersBean resource) {
//
//        //validate path paramters
//        return toSuccess();
//    }
//
//}
