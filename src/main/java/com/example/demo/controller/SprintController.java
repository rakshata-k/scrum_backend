package com.example.demo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.web.client.RestTemplate;

import com.example.demo.Repository.SprintRepository;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.model.Sprint;
import com.example.demo.model.Task;

@RestController
@RequestMapping("/scrum")
public class SprintController {
	
	@Autowired
	private SprintRepository sprintRepository;
	
	@Autowired
	private TaskRepository taskRepository;

	public SprintController(SprintRepository sprintRepository) {
		super();
		this.sprintRepository = sprintRepository;
	}
	
	@GetMapping("/sprints")
	Collection<Sprint> sprints(){
		return sprintRepository.findAll(); 
	}
	
	@GetMapping("/sprints/{name}")
	ResponseEntity<?> getSprint(@PathVariable String name){
		Optional<Sprint> sprint = sprintRepository.findByName(name);
		System.out.print(sprint);
		return sprint.map(response -> ResponseEntity.ok().body(response))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@PostMapping("/sprint")
	ResponseEntity<Sprint> createSprint(@Valid @RequestBody Sprint sprint) throws URISyntaxException
	{
		Sprint result = sprintRepository.save(sprint); 
		return ResponseEntity.created(new URI("/scrum/sprint/" + result.getName())).body(result); 
	}
	
	@PutMapping("/sprints/{name}")
	ResponseEntity<Sprint> updateSprint(@Valid @RequestBody Sprint sprints) throws URISyntaxException
	{
		Sprint result = sprintRepository.save(sprints);
		return ResponseEntity.ok().body(result);	
	}
	
	@DeleteMapping("/sprints/{name}")
	ResponseEntity<?> deleteSprint(@PathVariable String name){
		sprintRepository.deleteByName(name);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/sprints/{name}/tasks/{task}")
	ResponseEntity<Sprint> createSprint(@PathVariable String name, @PathVariable String task) throws URISyntaxException{
		Optional<Task> taskAssigned = taskRepository.findByName(task);
		final String URL = "http://localhost:8080/scrum" + "/sprints/" + name;
		System.out.println(URL);
		RestTemplate restTemplate = new RestTemplate();
	    Sprint sprintResult = restTemplate.getForObject(URL, Sprint.class);
		Set<Task> taskSet = sprintResult.getTask();
		taskSet.add(taskAssigned.get());
		sprintResult.setTask(taskSet);
		Sprint result = sprintRepository.save(sprintResult); //insert/update into table category
		return ResponseEntity.created(new URI("/scrum/sprint/" + result.getName())).body(result); 
	}
	
}
