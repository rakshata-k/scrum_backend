package com.example.demo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Repository.TaskRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Task;
import com.example.demo.model.User;

@RestController
@RequestMapping("/scrum")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskRepository taskRepository;

	public UserController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	@GetMapping("/users")
	Collection<User> users(){
		return userRepository.findAll();
	}
	
	@GetMapping("/users/{username}")
	ResponseEntity<?> getUser(@PathVariable String username){
		Optional<User> user = userRepository.findByUsername(username);
		System.out.print(user);
		return user.map(response -> ResponseEntity.ok().body(response))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@PostMapping("/users/{username}/tasks/{task}")
	ResponseEntity<User> createUser(@PathVariable String username, @PathVariable String task) throws URISyntaxException{
		Optional<Task> taskAssigned = taskRepository.findByName(task);
		final String URL = "http://localhost:8080/scrum" + "/users/" + username;
		System.out.println(URL);
		RestTemplate restTemplate = new RestTemplate();
	    User userResult = restTemplate.getForObject(URL, User.class);
		Set<Task> taskSet = userResult.getTask();
		taskSet.add(taskAssigned.get());
		userResult.setTask(taskSet);
		User result = userRepository.save(userResult); //insert/update into table category
		return ResponseEntity.created(new URI("/scrum/user/" + result.getUsername())).body(result); 
	}

}
