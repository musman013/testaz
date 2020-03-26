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
import com.nfinity.ll.testaz.application.types.TypesAppService;
import com.nfinity.ll.testaz.application.types.dto.*;
import com.nfinity.ll.testaz.application.pets.PetsAppService;
import com.nfinity.ll.testaz.application.pets.dto.FindPetsByIdOutput;
import java.util.List;
import java.util.Map;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/types")
public class TypesController {

	@Autowired
	private TypesAppService _typesAppService;
    
    @Autowired
	private PetsAppService  _petsAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	
    
    public TypesController(TypesAppService typesAppService, PetsAppService petsAppService,
	 LoggingHelper helper) {
		super();
		this._typesAppService = typesAppService;
    	this._petsAppService = petsAppService;
		this.logHelper = helper;
	}

    @PreAuthorize("hasAnyAuthority('TYPESENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateTypesOutput> create(@RequestBody @Valid CreateTypesInput types) {
		CreateTypesOutput output=_typesAppService.create(types);
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete types ------------
	@PreAuthorize("hasAnyAuthority('TYPESENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
    FindTypesByIdOutput output = _typesAppService.findById(Integer.valueOf(id));
	if (output == null) {
		logHelper.getLogger().error("There does not exist a types with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a types with a id=%s", id));
	}
    _typesAppService.delete(Integer.valueOf(id));
    }
	
	// ------------ Update types ------------
    @PreAuthorize("hasAnyAuthority('TYPESENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateTypesOutput> update(@PathVariable String id, @RequestBody @Valid UpdateTypesInput types) {
	    FindTypesByIdOutput currentTypes = _typesAppService.findById(Integer.valueOf(id));
			
		if (currentTypes == null) {
			logHelper.getLogger().error("Unable to update. Types with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
	    return new ResponseEntity(_typesAppService.update(Integer.valueOf(id),types), HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('TYPESENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindTypesByIdOutput> findById(@PathVariable String id) {
    FindTypesByIdOutput output = _typesAppService.findById(Integer.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('TYPESENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_typesAppService.find(searchCriteria,Pageable));
	}
    
    @PreAuthorize("hasAnyAuthority('TYPESENTITY_READ')")
	@RequestMapping(value = "/{id}/pets", method = RequestMethod.GET)
	public ResponseEntity getPets(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_typesAppService.parsePetsJoinColumn(id);
		if(joinColDetails== null)
		{
			logHelper.getLogger().error("Invalid Join Column");
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindPetsByIdOutput> output = _petsAppService.find(searchCriteria,pageable);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}   
 


}

