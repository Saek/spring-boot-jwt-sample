package com.example.springboottest.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.springboottest.model.ApplicationUser;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
	ApplicationUser findByUsername(String username);
}
