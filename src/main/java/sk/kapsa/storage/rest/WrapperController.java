package sk.kapsa.storage.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sk.kapsa.storage.mongo.entities.Wrapper;
import sk.kapsa.storage.mongo.services.WrapperMalformedException;
import sk.kapsa.storage.mongo.services.WrapperService;

@RestController
public class WrapperController {

	@Autowired
	private WrapperService wrapperService; 
	
	
	/**
	 * this method is called by Exago after clicking on "Create new annotation"
	 * @param url indicating where is user located
	 * @return best fit wrapper for given url
	 */
	@GetMapping("/get-wrapper")
	public ResponseEntity<Wrapper> getByUrl(@RequestParam(value = "url", required = true) String url) {
		Wrapper wrapper = wrapperService.getWrapperByUrl(url);
		if (wrapper == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(wrapper, HttpStatus.OK);
		}
	}
	
	/**
	 * this method is called by Exago after sending a wrapper to the server
	 * @param wrapper that is going to be saved
	 * @return that wrapper with assigned id and source id 
	 */
	@PutMapping("/put-wrapper")
	public ResponseEntity<Wrapper> save(@RequestBody Wrapper wrapper) {
		try {
			Wrapper saved = wrapperService.save(wrapper);
			return new ResponseEntity<>(saved, HttpStatus.OK);
		} catch (WrapperMalformedException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
}
