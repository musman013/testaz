package com.nfinity.ll.testaz.application.authorization.permission;

import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchFields;
import com.nfinity.ll.testaz.commons.search.SearchUtils;
import com.nfinity.ll.testaz.application.authorization.permission.dto.*;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.domain.authorization.permission.IPermissionManager;
import com.nfinity.ll.testaz.domain.model.QPermissionEntity;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Service
@Validated
public class PermissionAppService implements IPermissionAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
    
    @Autowired
	private IPermissionManager _permissionManager;

	@Autowired
	private PermissionMapper mapper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreatePermissionOutput create(CreatePermissionInput input) {

		PermissionEntity permission = mapper.createPermissionInputToPermissionEntity(input);
		PermissionEntity createdPermission = _permissionManager.create(permission);
		
		return mapper.permissionEntityToCreatePermissionOutput(createdPermission);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Permission", key = "#p0")
	public UpdatePermissionOutput update(Long permissionId, UpdatePermissionInput input) {
	
		PermissionEntity permission = mapper.updatePermissionInputToPermissionEntity(input);
		PermissionEntity updatedPermission = _permissionManager.update(permission);

		return mapper.permissionEntityToUpdatePermissionOutput(updatedPermission);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Permission", key = "#p0")
	public void delete(Long permissionId) {

		PermissionEntity existing = _permissionManager.findById(permissionId) ; 
		_permissionManager.delete(existing);

	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Permission", key = "#p0")
	public FindPermissionByIdOutput findById(Long permissionId) {

		PermissionEntity foundPermission = _permissionManager.findById(permissionId);
		if (foundPermission == null)  
			return null ; 
 	   
 	   FindPermissionByIdOutput output=mapper.permissionEntityToFindPermissionByIdOutput(foundPermission); 
		return output;
	}

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = "Permission", key = "#p0")
	public FindPermissionByNameOutput findByPermissionName(String permissionName) {

		PermissionEntity foundPermission = _permissionManager.findByPermissionName(permissionName);
		if (foundPermission == null) {
			return null;
		}
		return mapper.permissionEntityToFindPermissionByNameOutput(foundPermission);

	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Permission")
	public List<FindPermissionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<PermissionEntity> foundPermission = _permissionManager.findAll(search(search), pageable);
		List<PermissionEntity> permissionList = foundPermission.getContent();
		Iterator<PermissionEntity> permissionIterator = permissionList.iterator(); 
		List<FindPermissionByIdOutput> output = new ArrayList<>();

		while (permissionIterator.hasNext()) {
			output.add(mapper.permissionEntityToFindPermissionByIdOutput(permissionIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QPermissionEntity permission= QPermissionEntity.permissionEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(permission, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
		if(!(
		
		 list.get(i).replace("%20","").trim().equals("displayName") ||
		 list.get(i).replace("%20","").trim().equals("id") ||
		 list.get(i).replace("%20","").trim().equals("name") ||
		 list.get(i).replace("%20","").trim().equals("rolepermission") ||
		 list.get(i).replace("%20","").trim().equals("userpermission")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QPermissionEntity permission, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("displayName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(permission.displayName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(permission.displayName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(permission.displayName.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("name")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(permission.name.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(permission.name.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(permission.name.ne(details.getValue().getSearchValue()));
			}
		}
		return builder;
	}
	
	public Map<String,String> parseRolepermissionJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("permissionId", keysString);
		return joinColumnMap;
		
	}
	
	
	public Map<String,String> parseUserpermissionJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("permissionId", keysString);
		return joinColumnMap;
		
	}
}
