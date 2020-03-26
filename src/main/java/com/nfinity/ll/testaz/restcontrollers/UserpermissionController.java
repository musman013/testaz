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
import com.nfinity.ll.testaz.domain.model.UserpermissionId;
import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchUtils;
import com.nfinity.ll.testaz.commons.application.OffsetBasedPageRequest;
import com.nfinity.ll.testaz.commons.domain.EmptyJsonResponse;
import com.nfinity.ll.testaz.security.JWTAppService;
import com.nfinity.ll.testaz.application.authorization.user.UserAppService;
import com.nfinity.ll.testaz.application.authorization.userpermission.UserpermissionAppService;
import com.nfinity.ll.testaz.application.authorization.userpermission.dto.*;
import com.nfinity.ll.testaz.application.authorization.user.dto.*;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/userpermission")
public class UserpermissionController {

	@Autowired
	private UserpermissionAppService _userpermissionAppService;
	
	@Autowired
	private UserAppService _userAppService;

	@Autowired
	private LoggingHelper logHelper;
	
	@Autowired
 	private JWTAppService _jwtAppService;
    
	@Autowired
	private Environment env;
	
	public UserpermissionController(UserpermissionAppService userpermissionAppService, UserAppService userAppService,
			JWTAppService jwtAppService, LoggingHelper helper) {
	
		this._userpermissionAppService = userpermissionAppService;
		this._userAppService = userAppService;
		this._jwtAppService = jwtAppService;
		this.logHelper = helper;
	}
    
    @PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateUserpermissionOutput> create(@RequestBody @Valid CreateUserpermissionInput userpermission) {
		CreateUserpermissionOutput output=_userpermissionAppService.create(userpermission);
		if(output==null)
		{
			logHelper.getLogger().error("No record found");
		throw new EntityNotFoundException(
				String.format("No record found"));
	    }
	    
		FindUserByIdOutput foundUser =_userAppService.findById(output.getUserId());
	   _jwtAppService.deleteAllUserTokens(foundUser.getUserName());  
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete userrpermission ------------
	@PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	UserpermissionId userpermissionId =_userpermissionAppService.parseUserpermissionKey(id);
	if(userpermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindUserpermissionByIdOutput output = _userpermissionAppService.findById(userpermissionId);
	if (output == null) {
		logHelper.getLogger().error("There does not exist a userpermission with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a userpermission with a id=%s", id));
	}
	 _userpermissionAppService.delete(userpermissionId);
	 
	 FindUserByIdOutput foundUser =_userAppService.findById(output.getUserId());
	 _jwtAppService.deleteAllUserTokens(foundUser.getUserName());  
    }
	
	// ------------ Update userpermission ------------
	@PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateUserpermissionOutput> update(@PathVariable String id, @RequestBody @Valid UpdateUserpermissionInput userpermission) {
	UserpermissionId userpermissionId =_userpermissionAppService.parseUserpermissionKey(id);
	if(userpermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindUserpermissionByIdOutput currentUserpermission = _userpermissionAppService.findById(userpermissionId);
		
    if (currentUserpermission == null) {
	   logHelper.getLogger().error("Unable to update. Userpermission with id {} not found.", id);
	   return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
	}
		
    FindUserByIdOutput foundUser =_userAppService.findById(currentUserpermission.getUserId());
	_jwtAppService.deleteAllUserTokens(foundUser.getUserName());  
    	
	return new ResponseEntity(_userpermissionAppService.update(userpermissionId,userpermission), HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindUserpermissionByIdOutput> findById(@PathVariable String id) {
	UserpermissionId userpermissionId =_userpermissionAppService.parseUserpermissionKey(id);
	if(userpermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindUserpermissionByIdOutput output = _userpermissionAppService.findById(userpermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_userpermissionAppService.find(searchCriteria,Pageable));
	}

    @PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
	public ResponseEntity<GetUserOutput> getUser(@PathVariable String id) {
	UserpermissionId userpermissionId =_userpermissionAppService.parseUserpermissionKey(id);
	if(userpermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	GetUserOutput output= _userpermissionAppService.getUser(userpermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
  
    @PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{id}/permission", method = RequestMethod.GET)
	public ResponseEntity<GetPermissionOutput> getPermission(@PathVariable String id) {
	UserpermissionId userpermissionId =_userpermissionAppService.parseUserpermissionKey(id);
	if(userpermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	GetPermissionOutput output= _userpermissionAppService.getPermission(userpermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
  

}

