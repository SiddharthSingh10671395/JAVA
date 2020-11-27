package io.javabrains.moviecatalogservice.resources;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserRatingInfo {


    @Autowired
    private RestTemplate restTemplate;
        //Like this many properties are there...to see how many last avlues to look .,,how many errros are allowed
    //how masny second to sleep before coming back up
    @HystrixCommand(fallbackMethod = "getFallbackUserRating",

    commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="500")
    })
    public UserRating getUserRating(String userId) {
        UserRating userRating=  restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
        return userRating;
    }
    public UserRating getFallbackUserRating(String userId){
        UserRating userRating=new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(Arrays.asList(new Rating("0",0)));
        return userRating;
    }
}
