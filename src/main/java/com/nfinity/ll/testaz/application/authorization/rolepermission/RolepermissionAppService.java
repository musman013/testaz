package com.nfinity.ll.testaz.application.authorization.rolepermission;

import com.nfinity.ll.testaz.application.authorization.rolepermission.dto.*;
import com.nfinity.ll.testaz.domain.authorization.rolepermission.IRolepermissionManager;
import com.nfinity.ll.testaz.domain.model.QRolepermissionEntity;
import com.nfinity.ll.testaz.domain.model.RolepermissionEntity;
import com.nfinity.ll.testaz.domain.model.RolepermissionId;
import com.nfinity.ll.testaz.domain.model.UserroleEntity;
import com.nfinity.ll.testaz.domain.authorization.permission.IPermissionManager;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.domain.authorization.role.IRoleManager;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import org.springframework.cache.annotation.*;
import com.nfinity.ll.testaz.security.JWTAppService;
import java.util.Set;
import java.util.Map;
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
public class RolepermissionAppService implements IRolepermissionAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IRolepermissionManager _rolepermissionManager;
  
    @Autowired
	private IPermissionManager _permissionManager;
    
    @Autowired
	private IRoleManager _roleManager;
    
	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private RolepermissionMapper mapper;
	
	@Autowired
 	private JWTAppService _jwtAppService;
    
    @Transactional(propagation = Propagation.REQUIRED)
	public CreateRolepermissionOutput create(CreateRolepermissionInput input) {

		RolepermissionEntity rolepermission = mapper.createRolepermissionInputToRolepermissionEntity(input);
		if(input.getPermissionId()!=null && input.getRoleId()!=null){
			PermissionEntity foundPermission = _permissionManager.findById(input.getPermissionId());
			RoleEntity foundRole = _roleManager.findById(input.getRoleId());
			
			if(foundPermission!=null && foundRole!=null) {
				if(!checkIfPermissionAlreadyAssigned(foundRole, foundPermission))
				{
					rolepermission.setPermission(foundPermission);
					rolepermission.setRole(foundRole);
				}
				else return null;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
		RolepermissionEntity createdRolepermission = _rolepermissionManager.create(rolepermission);
		return mapper.rolepermissionEntityToCreateRolepermissionOutput(createdRolepermission);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Rolepermission", key = "#p0")
	public UpdateRolepermissionOutput update(RolepermissionId rolepermissionId , UpdateRolepermissionInput input) {

		RolepermissionEntity rolepermission = mapper.updateRolepermissionInputToRolepermissionEntity(input);

		if(input.getPermissionId()!=null && input.getRoleId()!=null){
			PermissionEntity foundPermission = _permissionManager.findById(input.getPermissionId());
			RoleEntity foundRole = _roleManager.findById(input.getRoleId());

			if(foundPermission!=null && foundRole!=null) {
				if(checkIfPermissionAlreadyAssigned(foundRole, foundPermission))
				{
					rolepermission.setPermission(foundPermission);
					rolepermission.setRole(foundRole);
				}
				else return null;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
		
		RolepermissionEntity updatedRolepermission = _rolepermissionManager.update(rolepermission);
		return mapper.rolepermissionEntityToUpdateRolepermissionOutput(updatedRolepermission);
	}
	
	public boolean checkIfPermissionAlreadyAssigned(RoleEntity foundRole,PermissionEntity foundPermission)
	{
		Set<RolepermissionEntity> rolePermission = foundRole.getRolepermissionSet();
		 
		Iterator pIterator = rolePermission.iterator();
			while (pIterator.hasNext()) { 
				RolepermissionEntity pe = (RolepermissionEntity) pIterator.next();
				if (pe.getPermission() == foundPermission ) {
					return true;
				}
			}
			
		return false;
	}
	
	public void deleteUserTokens(Long roleId)
    {
    	RoleEntity role =  _roleManager.findById(roleId);
		Set<UserroleEntity> userRole = role.getUserroleSet();
		for (UserroleEntity ur : userRole)
		{
			if(ur.getUser() != null)
				_jwtAppService.deleteAllUserTokens(ur.getUser().getUserName()); 
		}

    }
    
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Rolepermission", key = "#p0")
	public void delete(RolepermissionId rolepermissionId ) {

		RolepermissionEntity existing = _rolepermissionManager.findById(rolepermissionId) ; 
		_rolepermissionManager.delete(existing);
	
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Rolepermission", key = "#p0")
	public FindRolepermissionByIdOutput findById(RolepermissionId rolepermissionId) {

		RolepermissionEntity foundRolepermission = _rolepermissionManager.findById(rolepermissionId);
		if (foundRolepermission == null)  
			return null ; 
 	   
 	   FindRolepermissionByIdOutput output=mapper.rolepermissionEntityToFindRolepermissionByIdOutput(foundRolepermission); 
		return output;
	}
    //Permission
	// ReST API Call - GET /rolepermission/1/permission
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Rolepermission", key="#p0")
	public GetPermissionOutput getPermission(RolepermissionId rolepermissionId ) {

		RolepermissionEntity foundRolepermission = _rolepermissionManager.findById(rolepermissionId);
		if (foundRolepermission == null) {
			logHelper.getLogger().error("There does not exist a rolepermission wth a id=%s", rolepermissionId);
			return null;
		}
		PermissionEntity re = _rolepermissionManager.getPermission(rolepermissionId);
		return mapper.permissionEntityToGetPermissionOutput(re, foundRolepermission);
	}
    
    //Role
	// ReST API Call - GET /rolepermission/1/role
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Rolepermission", key="#p0")
	public GetRoleOutput getRole(RolepermissionId rolepermissionId ) {

		RolepermissionEntity foundRolepermission = _rolepermissionManager.findById(rolepermissionId);
		if (foundRolepermission == null) {
			logHelper.getLogger().error("There does not exist a rolepermission wth a id=%s", rolepermissionId);
			return null;
		}
		RoleEntity re = _rolepermissionManager.getRole(rolepermissionId);
		return mapper.roleEntityToGetRoleOutput(re, foundRolepermission);
	}
    

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Rolepermission")
	public List<FindRolepermissionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<RolepermissionEntity> foundRolepermission = _rolepermissionManager.findAll(search(search), pageable);
		List<RolepermissionEntity> rolepermissionList = foundRolepermission.getContent();
		Iterator<RolepermissionEntity> rolepermissionIterator = rolepermissionList.iterator(); 
		List<FindRolepermissionByIdOutput> output = new ArrayList<>();

		while (rolepermissionIterator.hasNext()) {
			output.add(mapper.rolepermissionEntityToFindRolepermissionByIdOutput(rolepermissionIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QRolepermissionEntity rolepermission= QRolepermissionEntity.rolepermissionEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(rolepermission, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
		if(!(
		
		 list.get(i).replace("%20","").trim().equals("permission") ||
		 list.get(i).replace("%20","").trim().equals("permissionId") ||
		 list.get(i).replace("%20","").trim().equals("role") ||
		 list.get(i).replace("%20","").trim().equals("roleId")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QRolepermissionEntity rolepermission, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("permissionId")) {
		    builder.and(rolepermission.permission.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("roleId")) {
		    builder.and(rolepermission.role.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public RolepermissionId parseRolepermissionKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		RolepermissionId rolepermissionId = new RolepermissionId();
		
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
		
		rolepermissionId.setPermissionId(Long.valueOf(keyMap.get("permissionId")));
		rolepermissionId.setRoleId(Long.valueOf(keyMap.get("roleId")));
		return rolepermissionId;
		
	}	
	
	
}


