package com.nfinity.ll.testaz.application.authorization.role;

import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchFields;
import com.nfinity.ll.testaz.commons.search.SearchUtils;
import com.nfinity.ll.testaz.application.authorization.role.dto.*;
import com.nfinity.ll.testaz.domain.authorization.role.IRoleManager;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.model.QRoleEntity;
import com.querydsl.core.BooleanBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.*;
import org.apache.commons.lang3.StringUtils;

@Service
@Validated
public class RoleAppService implements IRoleAppService{

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IRoleManager _roleManager;

	@Autowired
	private RoleMapper mapper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateRoleOutput create(CreateRoleInput input) {

		RoleEntity role = mapper.createRoleInputToRoleEntity(input);
		RoleEntity createdRole = _roleManager.create(role);

		return mapper.roleEntityToCreateRoleOutput(createdRole);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Role", key = "#p0")
	public UpdateRoleOutput update(Long roleId, UpdateRoleInput input) {

		RoleEntity role = mapper.updateRoleInputToRoleEntity(input);
		RoleEntity updatedRole = _roleManager.update(role);
		
		return mapper.roleEntityToUpdateRoleOutput(updatedRole);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Role", key = "#p0")
	public void delete(Long roleId) {

		RoleEntity existing = _roleManager.findById(roleId) ; 
		_roleManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Role", key = "#p0")
	public FindRoleByIdOutput findById(Long roleId) {

		RoleEntity foundRole = _roleManager.findById(roleId);
		if (foundRole == null)  
			return null ; 
 	   
 	   FindRoleByIdOutput output=mapper.roleEntityToFindRoleByIdOutput(foundRole); 
		return output;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = "Role", key = "#p0")
	public FindRoleByNameOutput findByRoleName(String roleName) {

		RoleEntity foundRole = _roleManager.findByRoleName(roleName);

		if (foundRole == null) {
			return null;
		}
		return mapper.roleEntityToFindRoleByNameOutput(foundRole);
	}

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Role")
	public List<FindRoleByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<RoleEntity> foundRole = _roleManager.findAll(search(search), pageable);
		List<RoleEntity> roleList = foundRole.getContent();
		Iterator<RoleEntity> roleIterator = roleList.iterator(); 
		List<FindRoleByIdOutput> output = new ArrayList<>();

		while (roleIterator.hasNext()) {
			output.add(mapper.roleEntityToFindRoleByIdOutput(roleIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QRoleEntity role= QRoleEntity.roleEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(role, map,search.getJoinColumns());
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
		 list.get(i).replace("%20","").trim().equals("user")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}

	public BooleanBuilder searchKeyValuePair(QRoleEntity role, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("displayName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(role.displayName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(role.displayName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(role.displayName.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("name")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(role.name.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(role.name.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(role.name.ne(details.getValue().getSearchValue()));
			}
		}
		return builder;
	}

	public Map<String,String> parseRolepermissionJoinColumn(String keysString) {
	
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("roleId", keysString);
		return joinColumnMap;
	}

	public Map<String,String> parseUserroleJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("roleId", keysString);
		return joinColumnMap;
	}
	
}