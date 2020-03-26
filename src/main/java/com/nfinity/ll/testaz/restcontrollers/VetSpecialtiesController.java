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
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesId;
import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchUtils;
import com.nfinity.ll.testaz.commons.application.OffsetBasedPageRequest;
import com.nfinity.ll.testaz.commons.domain.EmptyJsonResponse;
import com.nfinity.ll.testaz.application.vetspecialties.VetSpecialtiesAppService;
import com.nfinity.ll.testaz.application.vetspecialties.dto.*;
import com.nfinity.ll.testaz.application.specialties.SpecialtiesAppService;
import com.nfinity.ll.testaz.application.vets.VetsAppService;
import java.util.List;
import java.util.Map;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/vetSpecialties")
public class VetSpecialtiesController {

	@Autowired
	private VetSpecialtiesAppService _vetSpecialtiesAppService;
    
    @Autowired
	private SpecialtiesAppService  _specialtiesAppService;
    
    @Autowired
	private VetsAppService  _vetsAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	
    
    public VetSpecialtiesController(VetSpecialtiesAppService vetSpecialtiesAppService, SpecialtiesAppService specialtiesAppService, VetsAppService vetsAppService,
	 LoggingHelper helper) {
		super();
		this._vetSpecialtiesAppService = vetSpecialtiesAppService;
    	this._specialtiesAppService = specialtiesAppService;
    	this._vetsAppService = vetsAppService;
		this.logHelper = helper;
	}

    @PreAuthorize("hasAnyAuthority('VETSPECIALTIESENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateVetSpecialtiesOutput> create(@RequestBody @Valid CreateVetSpecialtiesInput vetSpecialties) {
		CreateVetSpecialtiesOutput output=_vetSpecialtiesAppService.create(vetSpecialties);
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete vetSpecialties ------------
	@PreAuthorize("hasAnyAuthority('VETSPECIALTIESENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	VetSpecialtiesId vetspecialtiesid =_vetSpecialtiesAppService.parseVetSpecialtiesKey(id);
	if(vetspecialtiesid == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindVetSpecialtiesByIdOutput output = _vetSpecialtiesAppService.findById(vetspecialtiesid);
	if (output == null) {
		logHelper.getLogger().error("There does not exist a vetSpecialties with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a vetSpecialties with a id=%s", id));
	}
	 _vetSpecialtiesAppService.delete(vetspecialtiesid);
    }
	
	// ------------ Update vetSpecialties ------------
    @PreAuthorize("hasAnyAuthority('VETSPECIALTIESENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateVetSpecialtiesOutput> update(@PathVariable String id, @RequestBody @Valid UpdateVetSpecialtiesInput vetSpecialties) {
		VetSpecialtiesId vetspecialtiesid =_vetSpecialtiesAppService.parseVetSpecialtiesKey(id);
		if(vetspecialtiesid == null)
		{
			logHelper.getLogger().error("Invalid id=%s", id);
			throw new EntityNotFoundException(
					String.format("Invalid id=%s", id));
		}
		FindVetSpecialtiesByIdOutput currentVetSpecialties = _vetSpecialtiesAppService.findById(vetspecialtiesid);
			
		if (currentVetSpecialties == null) {
			logHelper.getLogger().error("Unable to update. VetSpecialties with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(_vetSpecialtiesAppService.update(vetspecialtiesid,vetSpecialties), HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('VETSPECIALTIESENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindVetSpecialtiesByIdOutput> findById(@PathVariable String id) {
	VetSpecialtiesId vetspecialtiesid =_vetSpecialtiesAppService.parseVetSpecialtiesKey(id);
	if(vetspecialtiesid == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindVetSpecialtiesByIdOutput output = _vetSpecialtiesAppService.findById(vetspecialtiesid);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('VETSPECIALTIESENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_vetSpecialtiesAppService.find(searchCriteria,Pageable));
	}
    @PreAuthorize("hasAnyAuthority('VETSPECIALTIESENTITY_READ')")
	@RequestMapping(value = "/{id}/specialties", method = RequestMethod.GET)
	public ResponseEntity<GetSpecialtiesOutput> getSpecialties(@PathVariable String id) {
	VetSpecialtiesId vetspecialtiesid =_vetSpecialtiesAppService.parseVetSpecialtiesKey(id);
	if(vetspecialtiesid == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	GetSpecialtiesOutput output= _vetSpecialtiesAppService.getSpecialties(vetspecialtiesid);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('VETSPECIALTIESENTITY_READ')")
	@RequestMapping(value = "/{id}/vets", method = RequestMethod.GET)
	public ResponseEntity<GetVetsOutput> getVets(@PathVariable String id) {
	VetSpecialtiesId vetspecialtiesid =_vetSpecialtiesAppService.parseVetSpecialtiesKey(id);
	if(vetspecialtiesid == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	GetVetsOutput output= _vetSpecialtiesAppService.getVets(vetspecialtiesid);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}


}

