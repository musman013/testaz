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
import com.nfinity.ll.testaz.application.vets.VetsAppService;
import com.nfinity.ll.testaz.application.vets.dto.*;
import com.nfinity.ll.testaz.application.vetspecialties.VetSpecialtiesAppService;
import com.nfinity.ll.testaz.application.vetspecialties.dto.FindVetSpecialtiesByIdOutput;
import java.util.List;
import java.util.Map;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/vets")
public class VetsController {

	@Autowired
	private VetsAppService _vetsAppService;
    
    @Autowired
	private VetSpecialtiesAppService  _vetSpecialtiesAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	
    
    public VetsController(VetsAppService vetsAppService, VetSpecialtiesAppService vetSpecialtiesAppService,
	 LoggingHelper helper) {
		super();
		this._vetsAppService = vetsAppService;
    	this._vetSpecialtiesAppService = vetSpecialtiesAppService;
		this.logHelper = helper;
	}

    @PreAuthorize("hasAnyAuthority('VETSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateVetsOutput> create(@RequestBody @Valid CreateVetsInput vets) {
		CreateVetsOutput output=_vetsAppService.create(vets);
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete vets ------------
	@PreAuthorize("hasAnyAuthority('VETSENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
    FindVetsByIdOutput output = _vetsAppService.findById(Integer.valueOf(id));
	if (output == null) {
		logHelper.getLogger().error("There does not exist a vets with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a vets with a id=%s", id));
	}
    _vetsAppService.delete(Integer.valueOf(id));
    }
	
	// ------------ Update vets ------------
    @PreAuthorize("hasAnyAuthority('VETSENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateVetsOutput> update(@PathVariable String id, @RequestBody @Valid UpdateVetsInput vets) {
	    FindVetsByIdOutput currentVets = _vetsAppService.findById(Integer.valueOf(id));
			
		if (currentVets == null) {
			logHelper.getLogger().error("Unable to update. Vets with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
	    return new ResponseEntity(_vetsAppService.update(Integer.valueOf(id),vets), HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('VETSENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindVetsByIdOutput> findById(@PathVariable String id) {
    FindVetsByIdOutput output = _vetsAppService.findById(Integer.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('VETSENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_vetsAppService.find(searchCriteria,Pageable));
	}
    
    @PreAuthorize("hasAnyAuthority('VETSENTITY_READ')")
	@RequestMapping(value = "/{id}/vetSpecialties", method = RequestMethod.GET)
	public ResponseEntity getVetSpecialties(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_vetsAppService.parseVetSpecialtiesJoinColumn(id);
		if(joinColDetails== null)
		{
			logHelper.getLogger().error("Invalid Join Column");
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindVetSpecialtiesByIdOutput> output = _vetSpecialtiesAppService.find(searchCriteria,pageable);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}   
 


}

