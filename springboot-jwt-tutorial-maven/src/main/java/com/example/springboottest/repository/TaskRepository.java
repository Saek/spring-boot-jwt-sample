package com.example.springboottest.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.springboottest.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
	public List<Task> findAll();
}
