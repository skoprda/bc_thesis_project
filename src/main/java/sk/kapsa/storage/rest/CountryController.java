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

import sk.kapsa.storage.mongo.CountryRepository;
import sk.kapsa.storage.mongo.entities.Country;

@RestController
public class CountryController {
	
	public static final Logger logger = LoggerFactory.getLogger(CountryController.class);
	@Autowired
	private CountryRepository countryRepository;

	@GetMapping("/countries")
	public ResponseEntity<List<Country>> getAll() {
		List<Country> countries = countryRepository.findAll();
		if (countries.isEmpty()) {
			logger.warn("There are no countries in the database");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			logger.info("Countries found");
			return new ResponseEntity<>(countries, HttpStatus.OK);
		}
	}

	@GetMapping("/countries/{id}")
	public ResponseEntity<Country> getById(@PathVariable String id) {
		Country country = countryRepository.findById(id).get();
		if(country == null) {
			logger.error("There is no country with such id: " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			logger.info("Country found");
			return new ResponseEntity<>(country, HttpStatus.OK);
		}
	}
}
