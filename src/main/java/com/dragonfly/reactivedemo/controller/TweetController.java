package com.dragonfly.reactivedemo.controller;

import com.dragonfly.reactivedemo.model.Tweet;
import com.dragonfly.reactivedemo.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
public class TweetController {
  private final TweetService tweetService;

  @Autowired
  public TweetController(TweetService tweetService) {
    this.tweetService = tweetService;
  }

  @GetMapping("/tweets")
  public Flux<Tweet> getTweets() {
    return tweetService.getTweets();
  }

  @PostMapping("/tweets")
  public Mono<Tweet> createTweet(@Valid @RequestBody Tweet tweet) {
    return tweetService.createTweet(tweet);
  }

  @GetMapping("/tweets/{id}")
  public Mono<ResponseEntity<Tweet>> getTweet(@PathVariable String id) {
    return tweetService.getTweet(id);
  }

  @PutMapping("/tweets/{id}")
  public Mono<ResponseEntity<Tweet>> updateTweet(@PathVariable String id,
                                                 @Valid @RequestBody Tweet tweet) {
    return tweetService.updateTweet(id, tweet);
  }

  @DeleteMapping("/tweets/{id}")
  public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable String id) {
    return tweetService.deleteTweet(id);
  }

  @GetMapping(value = "/stream/tweets", produces = TEXT_EVENT_STREAM_VALUE)
  public Flux<Tweet> streamTweets() {
    return tweetService.streamTweets();
  }
}
