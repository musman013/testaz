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
import com.nfinity.ll.testaz.domain.model.RolepermissionId;
import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchUtils;
import com.nfinity.ll.testaz.commons.application.OffsetBasedPageRequest;
import com.nfinity.ll.testaz.commons.domain.EmptyJsonResponse;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.nfinity.ll.testaz.application.authorization.rolepermission.RolepermissionAppService;
import com.nfinity.ll.testaz.application.authorization.rolepermission.dto.*;

@RestController
@RequestMapping("/rolepermission")
public class RolepermissionController {

	@Autowired
	private RolepermissionAppService _rolepermissionAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	public RolepermissionController(RolepermissionAppService rolepermissionAppService, LoggingHelper helper) {
		super();
		this._rolepermissionAppService = rolepermissionAppService;
		this.logHelper = helper;
	}
    
    @PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateRolepermissionOutput> create(@RequestBody @Valid CreateRolepermissionInput rolepermission) {
		CreateRolepermissionOutput output=_rolepermissionAppService.create(rolepermission);
		if(output==null)
		{
			logHelper.getLogger().error("No record found");
		throw new EntityNotFoundException(
				String.format("No record found"));
	    }
		
		_rolepermissionAppService.deleteUserTokens(output.getRoleId());
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete rolepermission ------------
	@PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
		RolepermissionId rolepermissionId =_rolepermissionAppService.parseRolepermissionKey(id);
		if(rolepermissionId == null)
		{
			logHelper.getLogger().error("Invalid id=%s", id);
			throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
		}
		FindRolepermissionByIdOutput output = _rolepermissionAppService.findById(rolepermissionId);
		if (output == null) {
			logHelper.getLogger().error("There does not exist a rolepermission with a id=%s", id);
			throw new EntityNotFoundException(
				String.format("There does not exist a rolepermission with a id=%s", id));
		}
	
		_rolepermissionAppService.deleteUserTokens(output.getRoleId());
		
		_rolepermissionAppService.delete(rolepermissionId);
    }
	
	// ------------ Update rolepermission ------------
	@PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateRolepermissionOutput> update(@PathVariable String id, @RequestBody @Valid UpdateRolepermissionInput rolepermission) {
		RolepermissionId rolepermissionId =_rolepermissionAppService.parseRolepermissionKey(id);
		if(rolepermissionId == null)
		{
			logHelper.getLogger().error("Invalid id=%s", id);
			throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
		}
		FindRolepermissionByIdOutput currentRolepermission = _rolepermissionAppService.findById(rolepermissionId);
		
		if (currentRolepermission == null) {
			logHelper.getLogger().error("Unable to update. Rolepermission with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		_rolepermissionAppService.deleteUserTokens(rolepermissionId.getRoleId());
		
		return new ResponseEntity(_rolepermissionAppService.update(rolepermissionId,rolepermission), HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindRolepermissionByIdOutput> findById(@PathVariable String id) {
		RolepermissionId rolepermissionId =_rolepermissionAppService.parseRolepermissionKey(id);
		if(rolepermissionId == null)
		{
			logHelper.getLogger().error("Invalid id=%s", id);
			throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
		}
		FindRolepermissionByIdOutput output = _rolepermissionAppService.findById(rolepermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
//		if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_rolepermissionAppService.find(searchCriteria,Pageable));
	}

    @PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{id}/permission", method = RequestMethod.GET)
	public ResponseEntity<GetPermissionOutput> getPermission(@PathVariable String id) {
		RolepermissionId rolepermissionId =_rolepermissionAppService.parseRolepermissionKey(id);
		if(rolepermissionId == null)
		{
			logHelper.getLogger().error("Invalid id=%s", id);
			throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
		}
		GetPermissionOutput output= _rolepermissionAppService.getPermission(rolepermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
  
    @PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{id}/role", method = RequestMethod.GET)
	public ResponseEntity<GetRoleOutput> getRole(@PathVariable String id) {
		RolepermissionId rolepermissionId =_rolepermissionAppService.parseRolepermissionKey(id);
		if(rolepermissionId == null)
		{
			logHelper.getLogger().error("Invalid id=%s", id);
			throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
		}
		GetRoleOutput output= _rolepermissionAppService.getRole(rolepermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
  
}

