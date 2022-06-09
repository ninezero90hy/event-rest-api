package com.example.demorestapi.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/file", produces = MediaTypes.HAL_JSON_VALUE)
public class FileController {

	Logger log = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileService fileService;

	@PostMapping(value = "/uploadFile")
	public ResponseEntity<String> uploadFile(MultipartFile file) throws IllegalStateException, IOException {
		fileService.store(file);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@GetMapping(value = "download")
	public ResponseEntity<Resource> serveFile(@RequestParam(value = "filename") String filename) {

		Resource file = fileService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping(value = "deleteAll")
	public ResponseEntity<String> deleteAll() {
		fileService.deleteAll();
		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@GetMapping("fileList")
	public ResponseEntity<List<FileData>> getListFiles() {
		List<FileData> fileInfos = fileService.loadAll()
				.map(path -> {
					FileData data = new FileData();
					String filename = path.getFileName().toString();
					data.setFilename(filename);
					data.setUrl(MvcUriComponentsBuilder.fromMethodName(FileController.class,
							"serveFile", filename).build().toString());
					try {
						data.setSize(Files.size(path));
					} catch (IOException e) {
						log.error(e.getMessage());
					}
					return data;
				})
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

}
