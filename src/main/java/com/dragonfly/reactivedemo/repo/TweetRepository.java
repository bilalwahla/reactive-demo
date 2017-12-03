package com.dragonfly.reactivedemo.repo;

import com.dragonfly.reactivedemo.model.Tweet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TweetRepository extends ReactiveMongoRepository<Tweet, String> {
}
