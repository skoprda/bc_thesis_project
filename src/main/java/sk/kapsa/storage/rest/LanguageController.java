package sk.kapsa.storage.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import sk.kapsa.storage.mongo.LanguageRepository;
import sk.kapsa.storage.mongo.entities.Language;

@RestController
public class LanguageController {
	
	public static final Logger logger = LoggerFactory.getLogger(LanguageController.class);
	@Autowired
	private LanguageRepository languageRepository;
	
	@GetMapping("/languages")
	public ResponseEntity<List<Language>> getAll(){
		List<Language> languages = languageRepository.findAll();
		if(languages.isEmpty()) {
			logger.warn("There are no languages in the database");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			logger.info("Languages found");
			return new ResponseEntity<>(languages, HttpStatus.OK);
		}
	}
	
	@GetMapping("/languages/{id}")
	public ResponseEntity<Language> getById(@PathVariable String id) {
		Language language = languageRepository.findById(id).get();
		if(language == null) {
			logger.error("There is no language with such id: " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			logger.info("Language found");
			return new ResponseEntity<>(language, HttpStatus.OK);
		}
	}
	
	
}
