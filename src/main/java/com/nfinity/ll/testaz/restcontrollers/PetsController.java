package com.nfinity.ll.testaz.restcontrollers;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchUtils;
import com.nfinity.ll.testaz.commons.application.OffsetBasedPageRequest;
import com.nfinity.ll.testaz.commons.domain.EmptyJsonResponse;
import com.nfinity.ll.testaz.application.pets.PetsAppService;
import com.nfinity.ll.testaz.application.pets.dto.*;
import com.nfinity.ll.testaz.application.visits.VisitsAppService;
import com.nfinity.ll.testaz.application.visits.dto.FindVisitsByIdOutput;
import com.nfinity.ll.testaz.application.types.TypesAppService;
import com.nfinity.ll.testaz.application.owners.OwnersAppService;
import java.util.List;
import java.util.Map;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/pets")
public class PetsController {

	@Autowired
	private PetsAppService _petsAppService;
    
    @Autowired
	private VisitsAppService  _visitsAppService;
    
    @Autowired
	private TypesAppService  _typesAppService;
    
    @Autowired
	private OwnersAppService  _ownersAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	
    
    public PetsController(PetsAppService petsAppService, VisitsAppService visitsAppService, TypesAppService typesAppService, OwnersAppService ownersAppService,
	 LoggingHelper helper) {
		super();
		this._petsAppService = petsAppService;
    	this._visitsAppService = visitsAppService;
    	this._typesAppService = typesAppService;
    	this._ownersAppService = ownersAppService;
		this.logHelper = helper;
	}

    @PreAuthorize("hasAnyAuthority('PETSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreatePetsOutput> create(@RequestBody @Valid CreatePetsInput pets) {
		CreatePetsOutput output=_petsAppService.create(pets);
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete pets ------------
	@PreAuthorize("hasAnyAuthority('PETSENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
    FindPetsByIdOutput output = _petsAppService.findById(Integer.valueOf(id));
	if (output == null) {
		logHelper.getLogger().error("There does not exist a pets with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a pets with a id=%s", id));
	}
    _petsAppService.delete(Integer.valueOf(id));
    }
	
	// ------------ Update pets ------------
    @PreAuthorize("hasAnyAuthority('PETSENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdatePetsOutput> update(@PathVariable String id, @RequestBody @Valid UpdatePetsInput pets) {
	    FindPetsByIdOutput currentPets = _petsAppService.findById(Integer.valueOf(id));
			
		if (currentPets == null) {
			logHelper.getLogger().error("Unable to update. Pets with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
	    return new ResponseEntity(_petsAppService.update(Integer.valueOf(id),pets), HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('PETSENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindPetsByIdOutput> findById(@PathVariable String id) {
    FindPetsByIdOutput output = _petsAppService.findById(Integer.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('PETSENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_petsAppService.find(searchCriteria,Pageable));
	}
    
    @PreAuthorize("hasAnyAuthority('PETSENTITY_READ')")
	@RequestMapping(value = "/{id}/visits", method = RequestMethod.GET)
	public ResponseEntity getVisits(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_petsAppService.parseVisitsJoinColumn(id);
		if(joinColDetails== null)
		{
			logHelper.getLogger().error("Invalid Join Column");
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindVisitsByIdOutput> output = _visitsAppService.find(searchCriteria,pageable);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}   
 
    @PreAuthorize("hasAnyAuthority('PETSENTITY_READ')")
	@RequestMapping(value = "/{id}/types", method = RequestMethod.GET)
	public ResponseEntity<GetTypesOutput> getTypes(@PathVariable String id) {
    GetTypesOutput output= _petsAppService.getTypes(Integer.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('PETSENTITY_READ')")
	@RequestMapping(value = "/{id}/owners", method = RequestMethod.GET)
	public ResponseEntity<GetOwnersOutput> getOwners(@PathVariable String id) {
    GetOwnersOutput output= _petsAppService.getOwners(Integer.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}


}

