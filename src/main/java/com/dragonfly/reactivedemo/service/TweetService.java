package com.dragonfly.reactivedemo.service;

import com.dragonfly.reactivedemo.model.Tweet;
import com.dragonfly.reactivedemo.repo.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@Service
public class TweetService {
  private final TweetRepository tweetRepository;

  @Autowired
  public TweetService(TweetRepository tweetRepository) {
    this.tweetRepository = tweetRepository;
  }

  public Flux<Tweet> getTweets() {
    return tweetRepository.findAll();
  }

  public Mono<Tweet> createTweet(final Tweet tweet) {
    return tweetRepository.save(tweet);
  }

  public Mono<ResponseEntity<Tweet>> getTweet(final String id) {
    return tweetRepository.findById(id)
        .map(savedTweet -> new ResponseEntity<>(savedTweet, OK))
        .defaultIfEmpty(new ResponseEntity<>(NOT_FOUND));
  }

  public Mono<ResponseEntity<Tweet>> updateTweet(final String id, final Tweet tweet) {
    return tweetRepository.findById(id)
        .flatMap(existingTweet -> {
          existingTweet.setText(tweet.getText());
          return tweetRepository.save(existingTweet);
        })
        .map(updatedTweet -> new ResponseEntity<>(updatedTweet, OK))
        .defaultIfEmpty(new ResponseEntity<>(NOT_FOUND));
  }

  public Mono<ResponseEntity<Void>> deleteTweet(final String id) {
    return tweetRepository.findById(id)
        .flatMap(existingTweet ->
            tweetRepository.delete(existingTweet).then(Mono.just(new ResponseEntity<Void>(OK)))
        )
        .defaultIfEmpty(new ResponseEntity<>(NOT_FOUND));
  }

  public Flux<Tweet> streamTweets() {
    return tweetRepository.findAll();
  }
}
