package com.nfinity.ll.testaz.application.authorization.userpermission;

import com.nfinity.ll.testaz.application.authorization.userpermission.dto.*;
import com.nfinity.ll.testaz.domain.authorization.userpermission.IUserpermissionManager;
import com.nfinity.ll.testaz.domain.model.QUserpermissionEntity;
import com.nfinity.ll.testaz.domain.model.UserpermissionEntity;
import com.nfinity.ll.testaz.domain.model.UserpermissionId;
import com.nfinity.ll.testaz.domain.authorization.user.IUserManager;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.authorization.permission.IPermissionManager;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.domain.model.RolepermissionEntity;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import org.springframework.cache.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Validated
public class UserpermissionAppService implements IUserpermissionAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IUserpermissionManager _userpermissionManager;
  
    @Autowired
	private IUserManager _userManager;
    
    @Autowired
	private IPermissionManager _permissionManager;
    
	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private UserpermissionMapper mapper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateUserpermissionOutput create(CreateUserpermissionInput input) {

		UserpermissionEntity userpermission = mapper.createUserpermissionInputToUserpermissionEntity(input);
	  	
    	if(input.getUserId()!=null || input.getPermissionId()!=null)
		{
		UserEntity foundUser = _userManager.findById(input.getUserId());
		PermissionEntity foundPermission = _permissionManager.findById(input.getPermissionId());
		
		if(foundUser!=null || foundPermission!=null)
		{			
				if(!checkIfPermissionAlreadyAssigned(foundUser, foundPermission))
				{
					userpermission.setPermission(foundPermission);
					userpermission.setUser(foundUser);
					userpermission.setRevoked(input.getRevoked());
				}
				else return null;
		}
		else return null;
		}
		else return null;
		
		UserpermissionEntity createdUserpermission = _userpermissionManager.create(userpermission);
		return mapper.userpermissionEntityToCreateUserpermissionOutput(createdUserpermission);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Userpermission", key = "#p0")
	public UpdateUserpermissionOutput update(UserpermissionId userpermissionId , UpdateUserpermissionInput input) {

		UserpermissionEntity userpermission = mapper.updateUserpermissionInputToUserpermissionEntity(input);
	  	
		if(input.getUserId()!=null || input.getPermissionId()!=null)
		{
			UserEntity foundUser = _userManager.findById(input.getUserId());
			PermissionEntity foundPermission = _permissionManager.findById(input.getPermissionId());
		
			if(foundUser!=null || foundPermission!=null)
			{			
				if(checkIfPermissionAlreadyAssigned(foundUser, foundPermission))
				{
					userpermission.setPermission(foundPermission);
					userpermission.setUser(foundUser);
					userpermission.setRevoked(input.getRevoked());
				}
				else return null;
			}
			else return null;
		}
		else return null;
		
		UserpermissionEntity updatedUserpermission = _userpermissionManager.update(userpermission);
		
		return mapper.userpermissionEntityToUpdateUserpermissionOutput(updatedUserpermission);
	}
	
	public boolean checkIfPermissionAlreadyAssigned(UserEntity foundUser,PermissionEntity foundPermission)
	{
		
		Set<UserpermissionEntity> userPermission = foundUser.getUserpermissionSet();
		 
		Iterator pIterator = userPermission.iterator();
			while (pIterator.hasNext()) { 
				UserpermissionEntity pe = (UserpermissionEntity) pIterator.next();
				if (pe.getPermission() == foundPermission ) {
					return true;
				}
			}
			
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Userpermission", key = "#p0")
	public void delete(UserpermissionId userpermissionId) {

		UserpermissionEntity existing = _userpermissionManager.findById(userpermissionId) ; 
		_userpermissionManager.delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Userpermission", key = "#p0")
	public FindUserpermissionByIdOutput findById(UserpermissionId userpermissionId ) {

		UserpermissionEntity foundUserpermission = _userpermissionManager.findById(userpermissionId);
		if (foundUserpermission == null)  
			return null ; 
 	   
 	   FindUserpermissionByIdOutput output=mapper.userpermissionEntityToFindUserpermissionByIdOutput(foundUserpermission); 
		return output;
	}
    //User
	// ReST API Call - GET /userpermission/1/user
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Userpermission", key="#p0")
	public GetUserOutput getUser(UserpermissionId userpermissionId ) {

		UserpermissionEntity foundUserpermission = _userpermissionManager.findById(userpermissionId);
		if (foundUserpermission == null) {
			logHelper.getLogger().error("There does not exist a userpermission wth a id=%s", userpermissionId);
			return null;
		}
		UserEntity re = _userpermissionManager.getUser(userpermissionId);
		return mapper.userEntityToGetUserOutput(re, foundUserpermission);
	}
    
    //Permission
	// ReST API Call - GET /userpermission/1/permission
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Userpermission", key="#p0")
	public GetPermissionOutput getPermission(UserpermissionId userpermissionId ) {

		UserpermissionEntity foundUserpermission = _userpermissionManager.findById(userpermissionId);
		if (foundUserpermission == null) {
			logHelper.getLogger().error("There does not exist a userpermission wth a id=%s", userpermissionId);
			return null;
		}
		
		PermissionEntity re = _userpermissionManager.getPermission(userpermissionId);
		return mapper.permissionEntityToGetPermissionOutput(re, foundUserpermission);
	}
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Userpermission")
	public List<FindUserpermissionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<UserpermissionEntity> foundUserpermission = _userpermissionManager.findAll(search(search), pageable);
		List<UserpermissionEntity> userpermissionList = foundUserpermission.getContent();
		Iterator<UserpermissionEntity> userpermissionIterator = userpermissionList.iterator(); 
		List<FindUserpermissionByIdOutput> output = new ArrayList<>();

		while (userpermissionIterator.hasNext()) {
			output.add(mapper.userpermissionEntityToFindUserpermissionByIdOutput(userpermissionIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QUserpermissionEntity userpermission= QUserpermissionEntity.userpermissionEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(userpermission, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
		if(!(
    	list.get(i).replace("%20","").trim().equals("userId")||
		 list.get(i).replace("%20","").trim().equals("permission") ||
		 list.get(i).replace("%20","").trim().equals("permissionId") ||
		 list.get(i).replace("%20","").trim().equals("user")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QUserpermissionEntity userpermission, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        
        if(joinCol != null && joinCol.getKey().equals("userId")) {
		    builder.and(userpermission.user.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("permissionId")) {
		    builder.and(userpermission.permission.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public UserpermissionId parseUserpermissionKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		UserpermissionId userpermissionId = new UserpermissionId();
		
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
		
		userpermissionId.setPermissionId(Long.valueOf(keyMap.get("permissionId")));
        userpermissionId.setUserId(Long.valueOf(keyMap.get("userId")));
		return userpermissionId;
	}	
	
	
}


