package com.nfinity.ll.testaz.application.authorization.userrole;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.ll.testaz.domain.authorization.userrole.*;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.application.authorization.userrole.dto.*;
import com.nfinity.ll.testaz.domain.model.QUserroleEntity;
import com.nfinity.ll.testaz.domain.model.UserroleEntity;
import com.nfinity.ll.testaz.domain.model.UserroleId;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.authorization.user.UserManager;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.authorization.role.RoleManager;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserroleAppServiceTest {
    @Spy
	@InjectMocks
	UserroleAppService _appService;

	@Mock
	private UserroleManager _userroleManager;
	
    @Mock
	private UserManager  _userManager;
	
    @Mock
	private RoleManager  _roleManager;
	
	@Mock
	private UserroleMapper _mapper;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;

	@Mock
	private UserroleId userRoleId;
	
	private static Long ID=15L;
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(_appService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findUserroleById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_userroleManager.findById(any(UserroleId.class))).thenReturn(null);
		Assertions.assertThat(_appService.findById(userRoleId)).isEqualTo(null);
	}
	
	@Test
	public void findUserroleById_IdIsNotNullAndIdExists_ReturnUserrole() {

		UserroleEntity userrole = mock(UserroleEntity.class);
		Mockito.when(_userroleManager.findById(any(UserroleId.class))).thenReturn(userrole);
		Assertions.assertThat(_appService.findById(userRoleId)).isEqualTo(_mapper.userroleEntityToFindUserroleByIdOutput(userrole));
	}
	
	@Test 
    public void createUserrole_UserroleIsNotNullAndUserIdOrPermssionIdIsNotNullAndUserroleDoesNotExist_StoreUserrole() { 
 
        UserroleEntity userroleEntity = new UserroleEntity(); 
        CreateUserroleInput userrole = mock(CreateUserroleInput.class); 
        UserEntity userEntity = mock (UserEntity.class);
		RoleEntity roleEntity=mock(RoleEntity.class);
		Mockito.when(_userManager.findById(any(Long.class))).thenReturn(userEntity);
		Mockito.when(_roleManager.findById(anyLong())).thenReturn(roleEntity);
        Mockito.when(_mapper.createUserroleInputToUserroleEntity(any(CreateUserroleInput.class))).thenReturn(userroleEntity); 
        Mockito.when(_userroleManager.create(any(UserroleEntity.class))).thenReturn(userroleEntity); 
        
        Assertions.assertThat(_appService.create(userrole)).isEqualTo(_mapper.userroleEntityToCreateUserroleOutput(userroleEntity)); 

    } 

    @Test 
	public void createUserrole_UserroleInputIsNotNullAndUserIdOrPermssionIdIsNotNullAndRoleIsAlreadyAssigned_StoreUserrole() { 

		UserroleEntity userroleEntity = new UserroleEntity(); 
		CreateUserroleInput userrole = mock(CreateUserroleInput.class);
		UserEntity userEntity = mock (UserEntity.class);
		RoleEntity roleEntity=mock(RoleEntity.class);
		
		Mockito.doReturn(true).when(_appService).checkIfRoleAlreadyAssigned(any(UserEntity.class), any(RoleEntity.class));
		_appService.checkIfRoleAlreadyAssigned(userEntity,roleEntity);  
		verify(_appService).checkIfRoleAlreadyAssigned(userEntity,roleEntity);
		Mockito.when(_mapper.createUserroleInputToUserroleEntity(any(CreateUserroleInput.class))).thenReturn(userroleEntity); 
		Mockito.when(_userManager.findById(any(Long.class))).thenReturn(userEntity);
		Mockito.when(_roleManager.findById(anyLong())).thenReturn(roleEntity);
		Mockito.when(_userroleManager.create(any(UserroleEntity.class))).thenReturn(userroleEntity); 
		
		Assertions.assertThat(_appService.create(userrole)).isEqualTo(_mapper.userroleEntityToCreateUserroleOutput(null)); 
	} 

	@Test
	public void createUserrole_UserroleIsNotNullAndUserroleDoesNotExistAndUserIdOrPermssionIdIsNull_ReturnNull() {

		CreateUserroleInput userrole = new CreateUserroleInput();
		userrole.setRoleId(null);

		Assertions.assertThat(_appService.create(userrole)).isEqualTo(null);
	}

	@Test
	public void createUserrole_UserroleIsNotNullAndUserroleDoesNotExistAndUserIdOrPermssionIdIsNotNullAndUserOrRoleDoesNotExist_ReturnNull() {

		CreateUserroleInput userrole = mock(CreateUserroleInput.class);

		Mockito.when(_roleManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.create(userrole)).isEqualTo(null);
	}	
	
    @Test
	public void updateUserrole_UserroleIsNotNullAndUserroleExistAndUserIdOrPermssionIdIsNull_ReturnNull() {

		UpdateUserroleInput userrole = new UpdateUserroleInput();
		userrole.setRoleId(null);

		Assertions.assertThat(_appService.update(userRoleId,userrole)).isEqualTo(null);
	}

	@Test
	public void updateUserrole_UserroleIsNotNullAndUserroleExistAndUserIdOrPermssionIdIsNotNullAndUserOrRoleDoesNotExist_ReturnNull() {

		UpdateUserroleInput userrole = mock(UpdateUserroleInput.class);

		Mockito.when(_roleManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.update(userRoleId,userrole)).isEqualTo(null);
	}

	@Test
	public void updateUserrole_UserroleIsNotNullAndUserroleDoesNotExistAndChildIsNotNullAndChildIsNotMandatory_ReturnUpdatedUserrole() {

		UserroleEntity userroleEntity = new UserroleEntity();
		UpdateUserroleInput userrole = mock(UpdateUserroleInput.class);
		RoleEntity roleEntity= mock(RoleEntity.class);
		UserEntity userEntity = mock (UserEntity.class);
		userroleEntity.setRole(roleEntity);

		Mockito.when(_mapper.updateUserroleInputToUserroleEntity(any(UpdateUserroleInput.class))).thenReturn(userroleEntity);
		Mockito.when(_userManager.findById(any(Long.class))).thenReturn(userEntity);
		Mockito.when(_roleManager.findById(anyLong())).thenReturn(roleEntity); 
		Mockito.when(_userroleManager.update(any(UserroleEntity.class))).thenReturn(userroleEntity);
		
		Assertions.assertThat(_appService.update(userRoleId,userrole)).isEqualTo(_mapper.userroleEntityToUpdateUserroleOutput(userroleEntity));
	}
    
    @Test 
	public void checkIfRoleAlreadyAssigned_UserEntityAndRoleEntityIsNotNullAndUserRoleSetIsEmpty_ReturnFalse() {

	    RoleEntity roleEntity= mock(RoleEntity.class);
		UserEntity userEntity = mock (UserEntity.class);
	    Assertions.assertThat(_appService.checkIfRoleAlreadyAssigned(userEntity, roleEntity)).isEqualTo(false);
	}
	
	@Test 
	public void checkIfRoleAlreadyAssigned_UserEntityAndRoleEntityIsNotNullAndUserRoleSetIsNotEmpty_ReturnTrue() {

    	RoleEntity roleEntity= new RoleEntity();
		roleEntity.setId(1L);
		
	    UserroleEntity userrole= new UserroleEntity();
	    userrole.setRoleId(1L);  
		userrole.setRole(roleEntity);
		
		Set<UserroleEntity> up= new HashSet<UserroleEntity>();
	    up.add(userrole);
		UserEntity userEntity = mock (UserEntity.class);
	    Mockito.when(userEntity.getUserroleSet()).thenReturn(up);
		Assertions.assertThat(_appService.checkIfRoleAlreadyAssigned(userEntity, roleEntity)).isEqualTo(true);
	}
	
	@Test
	public void deleteUserrole_UserroleIsNotNullAndUserroleExists_UserroleRemoved() {

		UserroleEntity userrole= mock(UserroleEntity.class);
		Mockito.when(_userroleManager.findById(any(UserroleId.class))).thenReturn(userrole);
		
		_appService.delete(userRoleId); 
		verify(_userroleManager).delete(userrole);
	}
	

	
	@Test
	public void Find_ListIsEmpty_ReturnList() throws Exception {

		List<UserroleEntity> list = new ArrayList<>();
		Page<UserroleEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindUserroleByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_userroleManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void Find_ListIsNotEmpty_ReturnList() throws Exception {

		List<UserroleEntity> list = new ArrayList<>();
		UserroleEntity userrole = mock(UserroleEntity.class);
		list.add(userrole);
    	Page<UserroleEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindUserroleByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.userroleEntityToFindUserroleByIdOutput(userrole));
    	
    	Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_userroleManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
  @Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QUserroleEntity userrole = QUserroleEntity.userroleEntity;
	    
	  SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue(String.valueOf(ID));
		Map<String,SearchFields> map = new HashMap<>();
		map.put("roleId",searchFields);
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(userrole.role.id.eq(ID));
		
		Map<String,String> searchMap = new HashMap<String,String>();
		searchMap.put("roleId",String.valueOf(ID));
		
		Assertions.assertThat(_appService.searchKeyValuePair(userrole,map,searchMap)).isEqualTo(builder);
	}
	
	@Test (expected = Exception.class)
	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("xyz");
		_appService.checkProperties(list);
	}
	
	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception {
		List<String> list = new ArrayList<>();
		_appService.checkProperties(list);
	}
	
  @Test
	public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
		QUserroleEntity userrole = QUserroleEntity.userroleEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue(String.valueOf(ID));
		search.setOperator("equals");
		
		Map<String,String> searchMap = new HashMap<String,String>();
		searchMap.put("roleId",String.valueOf(ID));
		search.setJoinColumns(searchMap);

		fields.setOperator("equals");
		fields.setSearchValue(String.valueOf(ID));
		fields.setFieldName("roleId");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
        
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(userrole.role.id.eq(ID));
		
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
	
   //User
	@Test
	public void GetUser_IfUserroleIdAndUserIdIsNotNullAndUserroleExists_ReturnUser() {
		UserroleEntity userrole = mock(UserroleEntity.class);
		UserEntity user = mock(UserEntity.class);

		Mockito.when(_userroleManager.findById(any(UserroleId.class))).thenReturn(userrole);
		Mockito.when(_userroleManager.getUser(any(UserroleId.class))).thenReturn(user);
		Assertions.assertThat(_appService.getUser(userRoleId)).isEqualTo(_mapper.userEntityToGetUserOutput(user, userrole));
	}

	@Test 
	public void GetUser_IfUserroleIdAndUserIdIsNotNullAndUserroleDoesNotExist_ReturnNull() {
		UserroleEntity userrole = mock(UserroleEntity.class);

		Mockito.when(_userroleManager.findById(any(UserroleId.class))).thenReturn(null);
		Assertions.assertThat(_appService.getUser(userRoleId)).isEqualTo(null);
	}
 
   //Role
	@Test
	public void GetRole_IfUserroleIdAndRoleIdIsNotNullAndUserroleExists_ReturnRole() {
		UserroleEntity userrole = mock(UserroleEntity.class);
		RoleEntity role = mock(RoleEntity.class);

		Mockito.when(_userroleManager.findById(any(UserroleId.class))).thenReturn(userrole);
		Mockito.when(_userroleManager.getRole(any(UserroleId.class))).thenReturn(role);
		Assertions.assertThat(_appService.getRole(userRoleId)).isEqualTo(_mapper.roleEntityToGetRoleOutput(role, userrole));
	}

	@Test 
	public void GetRole_IfUserroleIdAndRoleIdIsNotNullAndUserroleDoesNotExist_ReturnNull() {
		UserroleEntity userrole = mock(UserroleEntity.class);

		Mockito.when(_userroleManager.findById(any(UserroleId.class))).thenReturn(null);
		Assertions.assertThat(_appService.getRole(userRoleId)).isEqualTo(null);
	}
 
    @Test
	public void ParseUserRoleKey_KeysStringIsNotEmptyAndKeyValuePairExists_ReturnUserroleId()
	{

		UserroleId userroleId = new UserroleId();
		userroleId.setRoleId(Long.valueOf(ID));
        String keyString= "userId:15,roleId:15";
    	userroleId.setUserId(Long.valueOf(ID));
		Assertions.assertThat(_appService.parseUserroleKey(keyString)).isEqualToComparingFieldByField(userroleId);
	}
	
	@Test
	public void ParseUserRoleKey_KeysStringIsEmpty_ReturnNull()
	{
		
		String keyString= "";
		Assertions.assertThat(_appService.parseUserroleKey(keyString)).isEqualTo(null);
	}
	
	@Test
	public void ParseUserRoleKey_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
		String keyString= "roleId";
		Assertions.assertThat(_appService.parseUserroleKey(keyString)).isEqualTo(null);
	}

}

