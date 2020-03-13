package com.example.demo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.TaskRepository;
import com.example.demo.model.Task;

@RestController
@RequestMapping("/scrum")
public class TaskController {
	
	@Autowired
	private TaskRepository taskRepository;

	public TaskController(TaskRepository taskRepository) {
		super();
		this.taskRepository = taskRepository;
	}
	
	@GetMapping("/tasks")
	Collection<Task> tasks(){
		return taskRepository.findAll();
	}
	
	@GetMapping("/tasks/{name}")
	ResponseEntity<?> getTask(@PathVariable String name){
		Optional<Task> task = taskRepository.findByName(name);
		System.out.print(task);
		return task.map(response -> ResponseEntity.ok().body(response))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@PostMapping("/task")
	ResponseEntity<Task> createTask(@Valid @RequestBody Task task) throws URISyntaxException{
		Task result = taskRepository.save(task); //insert into table category
		return ResponseEntity.created(new URI("/scrum/task/" + result.getName())).body(result); 
	}
	
	@PutMapping("/tasks/{name}")
	ResponseEntity<Task> updateTask(@Valid @RequestBody Task tasks) throws URISyntaxException
	{
		
		Task result = taskRepository.save(tasks);
		return ResponseEntity.ok().body(result);
				
	}
	
	@DeleteMapping("/tasks/{name}")
	ResponseEntity<?> deleteTask(@PathVariable String name){
		taskRepository.deleteByName(name);
		return ResponseEntity.ok().build();
	}
	
}
