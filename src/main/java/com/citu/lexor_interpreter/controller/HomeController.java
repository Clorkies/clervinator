package com.citu.lexor_interpreter.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/Language_Specifications_of_Lexor.pdf")
	public ResponseEntity<Resource> languageSpecificationsPdf() {
		Resource resource = new ClassPathResource("Language Specification of LEXOR.pdf");
		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_PDF)
			.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"Language_Specifications_of_Lexor.pdf\"")
			.body(resource);
	}
}
