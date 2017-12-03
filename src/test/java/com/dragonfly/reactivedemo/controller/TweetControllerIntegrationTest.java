package com.dragonfly.reactivedemo.controller;

import com.dragonfly.reactivedemo.model.Tweet;
import com.dragonfly.reactivedemo.repo.TweetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

import static java.util.Collections.singletonMap;

import static org.springframework.http.MediaType.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TweetControllerIntegrationTest {
  private static final String URI_TWEETS = "/tweets";
  private static final String URI_TWEET = "/tweets/{id}";
  private static final String ID_KEY = "id";
  private static final String ID_VALUE_NON_EXISTENT = "nonExistentId";
  private static final String UPDATED_TEXT = "Updated Tweet";
  private static final String BLANK_TEXT = "";

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  TweetRepository tweetRepository;

  @Test
  public void getTweets() throws Exception {
    webTestClient.get().uri(URI_TWEETS)
        .accept(APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(APPLICATION_JSON_UTF8)
        .expectBodyList(Tweet.class);
  }

  @Test
  public void createTweet() throws Exception {
    String text = "This is a Test Tweet";
    Tweet tweet = new Tweet(text);

    webTestClient.post().uri(URI_TWEETS)
        .contentType(APPLICATION_JSON_UTF8)
        .accept(APPLICATION_JSON_UTF8)
        .body(Mono.just(tweet), Tweet.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.text").isEqualTo(text);
  }

  @Test
  public void createTweetWithInvalidText() throws Exception {
    Tweet tweet = new Tweet(BLANK_TEXT);

    webTestClient.post().uri(URI_TWEETS)
        .contentType(APPLICATION_JSON_UTF8)
        .accept(APPLICATION_JSON_UTF8)
        .body(Mono.just(tweet), Tweet.class)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  public void getTweetById() throws Exception {
    Tweet tweet = tweetRepository.save(new Tweet("Hello, Universe!")).block();

    webTestClient.get()
        .uri(URI_TWEET, singletonMap(ID_KEY, tweet.getId()))
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .consumeWith(response -> assertThat(response.getResponseBody()).isNotNull());
  }

  @Test
  public void getTweetByNonExistentId() throws Exception {
    webTestClient.get()
        .uri(URI_TWEET, singletonMap(ID_KEY, ID_VALUE_NON_EXISTENT))
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  public void updateTweet() throws Exception {
    Tweet tweet = tweetRepository.save(new Tweet("Initial Tweet")).block();

    Tweet updatedTweet = new Tweet(UPDATED_TEXT);

    webTestClient.put()
        .uri(URI_TWEET, singletonMap(ID_KEY, tweet.getId()))
        .contentType(APPLICATION_JSON_UTF8)
        .accept(APPLICATION_JSON_UTF8)
        .body(Mono.just(updatedTweet), Tweet.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$.text").isEqualTo(UPDATED_TEXT);
  }

  @Test
  public void updateTweetWithInvalidText() throws Exception {
    Tweet tweet = tweetRepository.save(new Tweet("Tweet with some text")).block();

    Tweet invalidTweet = new Tweet(BLANK_TEXT);

    webTestClient.put()
        .uri(URI_TWEET, singletonMap(ID_KEY, tweet.getId()))
        .contentType(APPLICATION_JSON_UTF8)
        .accept(APPLICATION_JSON_UTF8)
        .body(Mono.just(invalidTweet), Tweet.class)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  public void updateTweetByNonExistentId() throws Exception {
    Tweet updatedTweet = new Tweet(UPDATED_TEXT);

    webTestClient.put()
        .uri(URI_TWEET, singletonMap(ID_KEY, ID_VALUE_NON_EXISTENT))
        .contentType(APPLICATION_JSON_UTF8)
        .accept(APPLICATION_JSON_UTF8)
        .body(Mono.just(updatedTweet), Tweet.class)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  public void deleteTweet() throws Exception {
    Tweet toBeDeleted = tweetRepository.save(new Tweet("To be deleted")).block();

    webTestClient.delete()
        .uri(URI_TWEET, singletonMap(ID_KEY, toBeDeleted.getId()))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void deleteTweetByNonExistentId() throws Exception {
    webTestClient.delete()
        .uri(URI_TWEET, singletonMap(ID_KEY, ID_VALUE_NON_EXISTENT))
        .exchange()
        .expectStatus().isNotFound();
  }
}