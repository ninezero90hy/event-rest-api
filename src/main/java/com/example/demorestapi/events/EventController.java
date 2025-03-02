package com.example.demorestapi.events;

import com.example.demorestapi.accounts.Account;
import com.example.demorestapi.accounts.CurrentUser;
import com.example.demorestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

	private final EventRepository eventRepository;

	private final ModelMapper modelMapper;

	private final EventValidator eventValidator;

	public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
		this.eventRepository = eventRepository;
		this.modelMapper = modelMapper;
		this.eventValidator = eventValidator;
	}

	// test
	@PostMapping
	public ResponseEntity createEvent(@RequestBody @Validated EventDto eventDto,
			Errors errors,
			@CurrentUser Account currentUser) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(refineErrors(errors));
		}

		eventValidator.validate(eventDto, errors);
		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		Event event = modelMapper.map(eventDto, Event.class);
		event.update();
		event.setManager(currentUser);
		Event newEvent = this.eventRepository.save(event);

		var selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
		URI createdUri = selfLinkBuilder.toUri();
		EventResource eventResource = new EventResource(event);
		eventResource.add(linkTo(EventController.class).withRel("query-events"));
		eventResource.add(selfLinkBuilder.withRel("update-event"));
		return ResponseEntity.created(createdUri).body(eventResource);
	}

	public LinkedList<LinkedHashMap<String, String>> refineErrors(Errors errors) {
		LinkedList errorList = new LinkedList<LinkedHashMap<String, String>>();
		errors.getFieldErrors().forEach(e -> {
			LinkedHashMap<String, String> error = new LinkedHashMap<>();
			error.put(e.getField(), e.getDefaultMessage());
			errorList.push(error);
		});
		return errorList;
	}

	@GetMapping
	public ResponseEntity queryEvents(Pageable pageable,
			PagedResourcesAssembler<Event> assembler,
			@CurrentUser Account account) {
		Page<Event> page = this.eventRepository.findAll(pageable);
		var pagedResources = assembler.toModel(page, e -> new EventResource(e));
		if (account != null) {
			pagedResources.add(linkTo(EventController.class).withRel("create-event"));
		}
		return ResponseEntity.ok(pagedResources);
	}

	@GetMapping("/{id}")
	public ResponseEntity getEvent(@PathVariable Integer id,
			@CurrentUser Account currentUser) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		if (optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Event event = optionalEvent.get();
		EventResource eventResource = new EventResource(event);
		if (event.getManager().equals(currentUser)) {
			eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
		}

		return ResponseEntity.ok(eventResource);
	}

	@PutMapping("/{id}")
	public ResponseEntity updateEvent(@PathVariable Integer id,
			@RequestBody @Valid EventDto eventDto,
			Errors errors,
			@CurrentUser Account currentUser) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		if (optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		this.eventValidator.validate(eventDto, errors);
		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		Event existingEvent = optionalEvent.get();
		if (!existingEvent.getManager().equals(currentUser)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		this.modelMapper.map(eventDto, existingEvent);
		Event savedEvent = this.eventRepository.save(existingEvent);

		EventResource eventResource = new EventResource(savedEvent);

		return ResponseEntity.ok(eventResource);
	}

	@PostMapping("/test")
	public ResponseEntity test(@CurrentUser Account currentUser) {
		EventDto event = EventDto.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
				.closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
				.beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
				.endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 스타텁 팩토리")
				.build();

		return new ResponseEntity<>(event, HttpStatus.OK);
	}

	private ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}

}