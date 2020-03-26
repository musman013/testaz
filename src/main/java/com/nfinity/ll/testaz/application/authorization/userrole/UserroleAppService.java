package com.nfinity.ll.testaz.application.authorization.userrole;

import com.nfinity.ll.testaz.application.authorization.userrole.dto.*;
import com.nfinity.ll.testaz.domain.authorization.userrole.IUserroleManager;
import com.nfinity.ll.testaz.domain.model.QUserroleEntity;
import com.nfinity.ll.testaz.domain.model.UserroleEntity;
import com.nfinity.ll.testaz.domain.model.UserroleId;
import com.nfinity.ll.testaz.domain.authorization.user.UserManager;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.authorization.role.RoleManager;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import org.springframework.cache.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.*;

@Service
@Validated
public class UserroleAppService implements IUserroleAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IUserroleManager _userroleManager;
	
    @Autowired
	private UserManager _userManager;
    
    @Autowired
	private RoleManager _roleManager;
    
	@Autowired
	private UserroleMapper mapper;

	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateUserroleOutput create(CreateUserroleInput input) {

		UserroleEntity userrole = mapper.createUserroleInputToUserroleEntity(input);
	  	if(input.getUserId()!=null || input.getRoleId()!=null)
	  	{
			UserEntity foundUser = _userManager.findById(input.getUserId());
	        RoleEntity foundRole = _roleManager.findById(input.getRoleId());
		
		    if(foundUser!=null || foundRole!=null)
		    {			
				if(!checkIfRoleAlreadyAssigned(foundUser, foundRole))
				{
					userrole.setRole(foundRole);
					userrole.setUser(foundUser);
				}
				else return null;
		     }
		     else return null;
		}
		else return null;
		
		UserroleEntity createdUserrole = _userroleManager.create(userrole);
		return mapper.userroleEntityToCreateUserroleOutput(createdUserrole);
	
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Userrole", key = "#p0")
	public UpdateUserroleOutput update(UserroleId userroleId , UpdateUserroleInput input) {

		UserroleEntity userrole = mapper.updateUserroleInputToUserroleEntity(input);
	  	if(input.getUserId()!=null || input.getRoleId()!=null)
	  	{
			UserEntity foundUser = _userManager.findById(input.getUserId());
	        RoleEntity foundRole = _roleManager.findById(input.getRoleId());
		
		    if(foundUser!=null || foundRole!=null)
		    {			
				if(checkIfRoleAlreadyAssigned(foundUser, foundRole))
				{
					userrole.setRole(foundRole);
					userrole.setUser(foundUser);
				}
				else return null;
		     }
		     else return null;
		}
		else return null;
		
		UserroleEntity updatedUserrole = _userroleManager.create(userrole);
		return mapper.userroleEntityToUpdateUserroleOutput(updatedUserrole);
	}
	
	public boolean checkIfRoleAlreadyAssigned(UserEntity foundUser,RoleEntity foundRole)
	{
		Set<UserroleEntity> userRole = foundUser.getUserroleSet();
		 
		Iterator rIterator = userRole.iterator();
			while (rIterator.hasNext()) { 
				UserroleEntity ur = (UserroleEntity) rIterator.next();
				if (ur.getRole() == foundRole) {
					return true;
				}
			}
			
		return false;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Userrole", key = "#p0")
	public void delete(UserroleId userroleId) {

		UserroleEntity existing = _userroleManager.findById(userroleId) ; 
		_userroleManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Userrole", key = "#p0")
	public FindUserroleByIdOutput findById(UserroleId userroleId) {

		UserroleEntity foundUserrole = _userroleManager.findById(userroleId);
		if (foundUserrole == null)  
			return null ; 
 	   
 	    FindUserroleByIdOutput output=mapper.userroleEntityToFindUserroleByIdOutput(foundUserrole); 
		return output;
	}
    //User
	// ReST API Call - GET /userrole/1/user
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Userrole", key="#p0")
	public GetUserOutput getUser(UserroleId userroleId) {

		UserroleEntity foundUserrole = _userroleManager.findById(userroleId);
		if (foundUserrole == null) {
			logHelper.getLogger().error("There does not exist a userrole wth a id=%s", userroleId);
			return null;
		}
		UserEntity re = _userroleManager.getUser(userroleId);
		return mapper.userEntityToGetUserOutput(re, foundUserrole);
	}
	
    //Role
	// ReST API Call - GET /userrole/1/role
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Userrole", key="#p0")
	public GetRoleOutput getRole(UserroleId userroleId) {

		UserroleEntity foundUserrole = _userroleManager.findById(userroleId);
		if (foundUserrole == null) {
			logHelper.getLogger().error("There does not exist a userrole wth a id=%s", userroleId);
			return null;
		}
		RoleEntity re = _userroleManager.getRole(userroleId);
		return mapper.roleEntityToGetRoleOutput(re, foundUserrole);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Userrole")
	public List<FindUserroleByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<UserroleEntity> foundUserrole = _userroleManager.findAll(search(search), pageable);
		List<UserroleEntity> userroleList = foundUserrole.getContent();
		Iterator<UserroleEntity> userroleIterator = userroleList.iterator(); 
		List<FindUserroleByIdOutput> output = new ArrayList<>();

		while (userroleIterator.hasNext()) {
			output.add(mapper.userroleEntityToFindUserroleByIdOutput(userroleIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QUserroleEntity userrole= QUserroleEntity.userroleEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(userrole, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
		if(!(
    	list.get(i).replace("%20","").trim().equals("userId")||
		list.get(i).replace("%20","").trim().equals("role") ||
		list.get(i).replace("%20","").trim().equals("roleId") ||
		list.get(i).replace("%20","").trim().equals("user"))) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QUserroleEntity userrole, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("userId")) {
		    builder.and(userrole.user.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("roleId")) {
		    builder.and(userrole.role.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public UserroleId parseUserroleKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		UserroleId userroleId = new UserroleId();
		
		Map<String,String> keyMap = new HashMap<String,String>();
		if(keyEntries.length > 1) {
			for(String keyEntry: keyEntries)
			{
				String[] keyEntryArr = keyEntry.split(":");
				if(keyEntryArr.length > 1) {
					keyMap.put(keyEntryArr[0], keyEntryArr[1]);					
				}
				else {
					return null;
				}
			}
		}
		else {
			return null;
		}
		
		userroleId.setRoleId(Long.valueOf(keyMap.get("roleId")));
        userroleId.setUserId(Long.valueOf(keyMap.get("userId")));
		return userroleId;
		
	}	
	
}


