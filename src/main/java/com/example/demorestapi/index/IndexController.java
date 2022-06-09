package com.example.demorestapi.index;

import com.example.demorestapi.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class IndexController {

	@GetMapping
	public RepresentationModel index() {
		var index = new RepresentationModel<>();
		index.add(linkTo(EventController.class).withRel("events"));
		return index;
	}

}
