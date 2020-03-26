package com.nfinity.ll.testaz.restcontrollers;

import com.nfinity.ll.testaz.application.authorization.userpermission.UserpermissionAppService;
import com.nfinity.ll.testaz.application.authorization.userrole.UserroleAppService;
import com.nfinity.ll.testaz.application.authorization.userrole.dto.FindUserroleByIdOutput;
import com.nfinity.ll.testaz.application.authorization.userpermission.dto.FindUserpermissionByIdOutput;
import com.nfinity.ll.testaz.application.authorization.user.UserAppService;
import com.nfinity.ll.testaz.application.authorization.user.dto.*;
import com.nfinity.ll.testaz.security.JWTAppService;
import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchUtils;
import com.nfinity.ll.testaz.commons.application.OffsetBasedPageRequest;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.nfinity.ll.testaz.commons.domain.EmptyJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserAppService _userAppService;
    
    @Autowired
	private UserpermissionAppService  _userpermissionAppService;
	
	@Autowired
	private UserroleAppService  _userroleAppService;
	
	@Autowired
    private PasswordEncoder pEncoder;
    
    @Autowired
 	private JWTAppService _jwtAppService;
 	

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	public UserController(UserAppService userAppService, UserpermissionAppService userpermissionAppService,
			UserroleAppService userroleAppService, PasswordEncoder pEncoder, JWTAppService jwtAppService, LoggingHelper logHelper) {
		super();
		this._userAppService = userAppService;
		this._userpermissionAppService = userpermissionAppService;
		this._userroleAppService = userroleAppService;
		this._jwtAppService = jwtAppService;
		this.pEncoder = pEncoder;
		this.logHelper = logHelper;
	}

	// CRUD Operations
	// ------------ Create a user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateUserOutput> create(@RequestBody @Valid CreateUserInput user) {
		 FindUserByNameOutput foundUser = _userAppService.findByUserName(user.getUserName());

	     if (foundUser != null) {
	     	logHelper.getLogger().error("There already exists a user with a name=%s", user.getUserName());
	        throw new EntityExistsException(
	        	String.format("There already exists a user with a name=%s", user.getUserName()));
	    }
	    user.setPassword(pEncoder.encode(user.getPassword()));
	    CreateUserOutput output=_userAppService.create(user);
		if(output==null)
		{
			logHelper.getLogger().error("No record found");
		throw new EntityNotFoundException(
			String.format("No record found"));
	    }
		
		return new ResponseEntity(output, HttpStatus.OK);
	}


	// ------------ Delete a user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
    	FindUserByIdOutput existing = _userAppService.findById(Long.valueOf(id));

        if (existing == null) {
        	logHelper.getLogger().error("There does not exist a user with a id=%s", id);
        	throw new EntityNotFoundException(
	        	String.format("There does not exist a user with a id=%s", id));
	     
        }
    	
		_userAppService.delete(Long.valueOf(id));
	}
	
	// ------------ Update user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateUserOutput> update(@PathVariable String id, @RequestBody @Valid UpdateUserInput user) {
    	FindUserWithAllFieldsByIdOutput currentUser = _userAppService.findWithAllFieldsById(Long.valueOf(id));
		
		if (currentUser == null) {
			logHelper.getLogger().error("Unable to update. User with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		user.setPassword(currentUser.getPassword());
		if(currentUser.getIsActive() && !user.getIsActive()) { 
            _jwtAppService.deleteAllUserTokens(currentUser.getUserName()); 
        } 
		
    return new ResponseEntity(_userAppService.update(Long.valueOf(id),user), HttpStatus.OK);
	}
	// ------------ Retrieve a user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindUserByIdOutput> findById(@PathVariable String id) {
    FindUserByIdOutput output = _userAppService.findById(Long.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_userAppService.find(searchCriteria,Pageable));
	}
   
    @PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(value = "/{userid}/userpermission", method = RequestMethod.GET)
	public ResponseEntity getUserpermission(@PathVariable String userid, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_userAppService.parseUserpermissionJoinColumn(userid);
		if(joinColDetails== null)
		{
			logHelper.getLogger().error("Invalid Join Column");
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindUserpermissionByIdOutput> output = _userpermissionAppService.find(searchCriteria,pageable);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}   
 
    @PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(value = "/{id}/userrole", method = RequestMethod.GET)
	public ResponseEntity getUserrole(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
		
		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit),sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_userAppService.parseUserroleJoinColumn(id);
		if(joinColDetails== null)
		{
			logHelper.getLogger().error("Invalid Join Column");
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindUserroleByIdOutput> output = _userroleAppService.find(searchCriteria,pageable);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}   


}