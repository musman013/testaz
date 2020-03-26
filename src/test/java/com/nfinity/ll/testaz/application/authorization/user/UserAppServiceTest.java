package com.nfinity.ll.testaz.application.authorization.user;

import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchFields;
import com.nfinity.ll.testaz.application.authorization.user.dto.CreateUserInput;
import com.nfinity.ll.testaz.application.authorization.user.dto.FindUserByIdOutput;
import com.nfinity.ll.testaz.application.authorization.user.dto.UpdateUserInput;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.domain.authorization.permission.PermissionManager;
import com.nfinity.ll.testaz.application.authorization.permission.PermissionAppService;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.authorization.role.RoleManager;
import com.nfinity.ll.testaz.domain.authorization.user.UserManager;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.model.QUserEntity;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserAppServiceTest {

	@InjectMocks
	@Spy
	UserAppService userAppService;

	@Mock
	UserManager userManager;

	@Mock
	PermissionManager permissionManager;
	
	@Mock
	private PermissionAppService permissionsAppService;

	@Mock
	RoleManager roleManager;
	
	@Mock
	UserMapper userMapper;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;
    
	private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(userAppService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test 
	public void findUserById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(userManager.findById(anyLong())).thenReturn(null);	
		Assertions.assertThat(userAppService.findById(ID)).isEqualTo(null);	

	}

	@Test
	public void findUserById_IdIsNotNullAndUserExists_ReturnAUser() {

		UserEntity user = mock(UserEntity.class);

		Mockito.when(userManager.findById(anyLong())).thenReturn(user);
		Assertions.assertThat(userAppService.findById(ID)).isEqualTo(userMapper.userEntityToCreateUserOutput(user));
	}
	
	@Test 
	public void findWithAllFieldsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(userManager.findById(anyLong())).thenReturn(null);	
		Assertions.assertThat(userAppService.findWithAllFieldsById(ID)).isEqualTo(null);	

	}

	@Test
	public void findWithAllFieldsById_IdIsNotNullAndUserExists_ReturnAUser() {

		UserEntity user = mock(UserEntity.class);

		Mockito.when(userManager.findById(anyLong())).thenReturn(user);
		Assertions.assertThat(userAppService.findWithAllFieldsById(ID)).isEqualTo(userMapper.userEntityToFindUserWithAllFieldsByIdOutput(user));
	}

	@Test 
	public void findUserByName_NameIsNotNullAndUserDoesNotExist_ReturnNull() {

		Mockito.when(userManager.findByUserName(anyString())).thenReturn(null);	
		Assertions.assertThat(userAppService.findByUserName("User1")).isEqualTo(null);	

	}

	@Test
	public void findUserByName_NameIsNotNullAndUserExists_ReturnAUser() {

		UserEntity user = mock(UserEntity.class);

		Mockito.when(userManager.findByUserName(anyString())).thenReturn(user);
		Assertions.assertThat(userAppService.findByUserName("User1")).isEqualTo(userMapper.userEntityToCreateUserOutput(user));
	}

	@Test
	public void createUser_UserIsNotNullAndUserDoesNotExist_StoreAUser() {

		UserEntity userEntity = mock(UserEntity.class);
		CreateUserInput user=mock(CreateUserInput.class);
		RoleEntity foundRole = mock(RoleEntity.class);
		
		Mockito.when(roleManager.findById(anyLong())).thenReturn(foundRole);
		Mockito.when(userManager.create(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.when(userMapper.createUserInputToUserEntity(any(CreateUserInput.class))).thenReturn(userEntity);
		
		Assertions.assertThat(userAppService.create(user)).isEqualTo(userMapper.userEntityToCreateUserOutput(userEntity));
	}
	
	@Test
	public void createUser_UserIsNotNullAndUserDoesNotExistAndRoleIdIsNullAndRoleIdIsMandatory_ReturnNull() {

		CreateUserInput user = mock(CreateUserInput.class);
		
		//Mockito.when(user.getRoleId()).thenReturn(null);
		Assertions.assertThat(userAppService.create(user)).isEqualTo(null);
	}
	
	@Test
	public void createUser_UserIsNotNullAndUserDoesNotExistAndRoleIdIsNotNullAndRoleIdIsMandatoryAndRoleDoesNotExistIsNull_ReturnNull() {

		CreateUserInput user = mock(CreateUserInput.class);
		
		Mockito.when(roleManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(userAppService.create(user)).isEqualTo(null);
	}

	@Test
	public void deleteUser_UserIsNotNullAndUserExists_UserRemoved() {

		UserEntity user=mock(UserEntity.class);
        Mockito.when(userManager.findById(anyLong())).thenReturn(user);
		userAppService.delete(ID);
		verify(userManager).delete(user);
	}
	
	@Test
	public void updateUser_UserIsNotNullAndUserDoesNotExistAndChildIsNullAndChildIsMandatory_ReturnNull() {

		UpdateUserInput user = mock(UpdateUserInput.class);
		
		//Mockito.when(user.getRoleId()).thenReturn(null);
		Assertions.assertThat(userAppService.update(ID,user)).isEqualTo(null);
	}
	
	@Test
	public void updateUser_UserIsNotNullAndUserDoesNotExistAndChildIsNotNullAndChildIsMandatoryAndFindByIdIsNull_ReturnNull() {

		UpdateUserInput user = mock(UpdateUserInput.class);
		
		Mockito.when(roleManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(userAppService.update(ID,user)).isEqualTo(null);
	}

	@Test
	public void updateUser_UserIdIsNotNullAndUserExistsAndRoleIdIsNotNull_ReturnUpdatedUser() {

		UserEntity userEntity = mock(UserEntity.class);
		UpdateUserInput user=mock(UpdateUserInput.class);
		RoleEntity foundRole = mock(RoleEntity.class);
 
		Mockito.when(roleManager.findById(anyLong())).thenReturn(foundRole);
		Mockito.when(userMapper.updateUserInputToUserEntity(any(UpdateUserInput.class))).thenReturn(userEntity);
		Mockito.when(userManager.update(any(UserEntity.class))).thenReturn(userEntity);
		Assertions.assertThat(userAppService.update(ID,user)).isEqualTo(userMapper.userEntityToUpdateUserOutput(userEntity));

	}

	@Test
	public void Find_ListIsEmpty_ReturnList() throws Exception
	{
		List<UserEntity> list = new ArrayList<>();
		Page<UserEntity> foundPage = new PageImpl(list);
		Pageable pageable =mock(Pageable.class);

		List<FindUserByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(userAppService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(userManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(userAppService.find(search,pageable)).isEqualTo(output);

	}

	@Test
	public void Find_ListIsNotEmpty_ReturnList() throws Exception
	{
		List<UserEntity> list = new ArrayList<>();
		UserEntity user=mock(UserEntity.class);
		list.add(user);
		Page<UserEntity> foundPage = new PageImpl(list);
		Pageable pageable =mock(Pageable.class);
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		List<FindUserByIdOutput> output = new ArrayList<>();
		output.add(userMapper.userEntityToFindUserByIdOutput(user));
		
		Mockito.when(userAppService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(userManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(userAppService.find(search,pageable)).isEqualTo(output);

	}

	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder()
	{
		QUserEntity user = QUserEntity.userEntity;
		SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map map = new HashMap();
        map.put("firstName",searchFields);
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(user.firstName.eq("xyz"));
        Map searchMap = new HashMap();
        map.put("xyz",ID);
        
        Assertions.assertThat(userAppService.searchKeyValuePair(user, map,searchMap)).isEqualTo(builder);
	}

	@Test(expected = Exception.class)
	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("first");

		userAppService.checkProperties(list);
	}
	
	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("firstName");

		userAppService.checkProperties(list);
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception
	{
		Map<String,SearchFields> map = new HashMap<>();
		QUserEntity user = QUserEntity.userEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		fields.setFieldName("firstName");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
   		BooleanBuilder builder = new BooleanBuilder();
   		builder.or(user.firstName.eq("xyz"));
  	
    Assertions.assertThat(userAppService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void search_StringIsNull_ReturnNull() throws Exception
	{
		 Assertions.assertThat(userAppService.search(null)).isEqualTo(null);
	}

    @Test
    public void parseUserpermissionJoinColumn_StringIsNotNull_ReturnMap() {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("userId", "1");
		
		Assertions.assertThat(userAppService.parseUserpermissionJoinColumn("1")).isEqualTo(joinColumnMap);
		
	}
	
	@Test
	public void parseUserroleJoinColumn_StringIsNotNull_ReturnMap() {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("userId", "1");
		
		Assertions.assertThat(userAppService.parseUserroleJoinColumn("1")).isEqualTo(joinColumnMap);
		
	}

}