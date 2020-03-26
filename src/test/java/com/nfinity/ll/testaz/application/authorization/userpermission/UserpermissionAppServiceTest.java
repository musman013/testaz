package com.nfinity.ll.testaz.application.authorization.userpermission;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Spy;

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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.ll.testaz.domain.model.UserpermissionId;
import com.nfinity.ll.testaz.domain.authorization.userpermission.*;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.application.authorization.userpermission.dto.*;
import com.nfinity.ll.testaz.domain.model.QUserpermissionEntity;
import com.nfinity.ll.testaz.domain.model.UserpermissionEntity;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.authorization.user.UserManager;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.domain.authorization.permission.PermissionManager;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserpermissionAppServiceTest {
    @Spy
	@InjectMocks
	UserpermissionAppService _appService;

	@Mock
	private UserpermissionManager _userpermissionManager;
	
    @Mock
	private UserManager  _userManager;
	
    @Mock
	private PermissionManager  _permissionManager;
	
	@Mock
	private UserpermissionMapper _mapper;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;

	@Mock
	private UserpermissionId userPermissionId;
	
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
	public void findUserpermissionById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

    	Mockito.when(_userpermissionManager.findById(any(UserpermissionId.class))).thenReturn(null);
		Assertions.assertThat(_appService.findById(userPermissionId)).isEqualTo(null);
	}
	
	@Test
	public void findUserpermissionById_IdIsNotNullAndIdExists_ReturnUserpermission() {

		UserpermissionEntity userpermission = mock(UserpermissionEntity.class);
		Mockito.when(_userpermissionManager.findById(any(UserpermissionId.class))).thenReturn(userpermission);
		Assertions.assertThat(_appService.findById(userPermissionId)).isEqualTo(_mapper.userpermissionEntityToFindUserpermissionByIdOutput(userpermission));
	}
	
	@Test 
    public void createUserpermission_UserpermissionIsNotNullAndUserIdOrPermssionIdIsNotNullAndUserpermissionDoesNotExist_StoreUserpermission() { 
 
        UserpermissionEntity userpermissionEntity = new UserpermissionEntity(); 
        CreateUserpermissionInput userpermission = mock(CreateUserpermissionInput.class); 
        UserEntity userEntity = mock (UserEntity.class);
		PermissionEntity permissionEntity=mock(PermissionEntity.class);
		Mockito.when(_userManager.findById(any(Long.class))).thenReturn(userEntity);
		Mockito.when(_permissionManager.findById(anyLong())).thenReturn(permissionEntity);
        Mockito.when(_mapper.createUserpermissionInputToUserpermissionEntity(any(CreateUserpermissionInput.class))).thenReturn(userpermissionEntity); 
        Mockito.when(_userpermissionManager.create(any(UserpermissionEntity.class))).thenReturn(userpermissionEntity); 
        
        Assertions.assertThat(_appService.create(userpermission)).isEqualTo(_mapper.userpermissionEntityToCreateUserpermissionOutput(userpermissionEntity)); 
    } 

    @Test 
	public void createUserpermission_UserpermissionInputIsNotNullAndUserIdOrPermssionIdIsNotNullAndPermissionIsAlreadyAssigned_StoreUserpermission() { 

		UserpermissionEntity userpermissionEntity = new UserpermissionEntity(); 
		CreateUserpermissionInput userpermission = mock(CreateUserpermissionInput.class);
		UserEntity userEntity = mock (UserEntity.class);
		PermissionEntity permissionEntity=mock(PermissionEntity.class);
		
		Mockito.doReturn(true).when(_appService).checkIfPermissionAlreadyAssigned(any(UserEntity.class), any(PermissionEntity.class));
		_appService.checkIfPermissionAlreadyAssigned(userEntity,permissionEntity);  
		verify(_appService).checkIfPermissionAlreadyAssigned(userEntity,permissionEntity);
		Mockito.when(_mapper.createUserpermissionInputToUserpermissionEntity(any(CreateUserpermissionInput.class))).thenReturn(userpermissionEntity); 
		Mockito.when(_userManager.findById(any(Long.class))).thenReturn(userEntity);
		Mockito.when(_permissionManager.findById(anyLong())).thenReturn(permissionEntity);
		Mockito.when(_userpermissionManager.create(any(UserpermissionEntity.class))).thenReturn(userpermissionEntity); 
		Assertions.assertThat(_appService.create(userpermission)).isEqualTo(_mapper.userpermissionEntityToCreateUserpermissionOutput(null)); 
	} 


	@Test
	public void createUserpermission_UserpermissionIsNotNullAndUserpermissionDoesNotExistAndUserIdOrPermssionIdIsNull_ReturnNull() {

		CreateUserpermissionInput userpermission = new CreateUserpermissionInput();
		userpermission.setPermissionId(null);

		Assertions.assertThat(_appService.create(userpermission)).isEqualTo(null);
	}

	@Test
	public void createUserpermission_UserpermissionIsNotNullAndUserpermissionDoesNotExistAndUserIdOrPermssionIdIsNotNullAndUserOrPermissionDoesNotExist_ReturnNull() {

		CreateUserpermissionInput userpermission = mock(CreateUserpermissionInput.class);

		Mockito.when(_permissionManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.create(userpermission)).isEqualTo(null);
	}	
	

    @Test
	public void updateUserpermission_UserpermissionIsNotNullAndUserpermissionExistAndUserIdOrPermssionIdIsNull_ReturnNull() {

		UpdateUserpermissionInput userpermission = new UpdateUserpermissionInput();
		userpermission.setPermissionId(null);

		Assertions.assertThat(_appService.update(userPermissionId,userpermission)).isEqualTo(null);
	}

	@Test
	public void updateUserpermission_UserpermissionIsNotNullAndUserpermissionExistAndUserIdOrPermssionIdIsNotNullAndUserOrPermissionDoesNotExist_ReturnNull() {

		UpdateUserpermissionInput userpermission = mock(UpdateUserpermissionInput.class);

		Mockito.when(_permissionManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.update(userPermissionId,userpermission)).isEqualTo(null);
	}


	@Test
	public void updateUserpermission_UserpermissionIsNotNullAndUserpermissionDoesNotExistAndChildIsNotNullAndChildIsNotMandatory_ReturnUpdatedUserpermission() {

		UserpermissionEntity userpermissionEntity = new UserpermissionEntity();
		UpdateUserpermissionInput userpermission = mock(UpdateUserpermissionInput.class);
		PermissionEntity permissionEntity= mock(PermissionEntity.class);
		UserEntity userEntity = mock (UserEntity.class);
		userpermissionEntity.setPermission(permissionEntity);

		Mockito.when(_mapper.updateUserpermissionInputToUserpermissionEntity(any(UpdateUserpermissionInput.class))).thenReturn(userpermissionEntity);
		Mockito.when(_userManager.findById(any(Long.class))).thenReturn(userEntity);
		Mockito.when(_permissionManager.findById(anyLong())).thenReturn(permissionEntity); 
		Mockito.when(_userpermissionManager.update(any(UserpermissionEntity.class))).thenReturn(userpermissionEntity);

		Assertions.assertThat(_appService.update(userPermissionId,userpermission)).isEqualTo(_mapper.userpermissionEntityToUpdateUserpermissionOutput(userpermissionEntity));
	}
    
    @Test 
	public void checkIfPermissionAlreadyAssigned_UserEntityAndPermissionEntityIsNotNullAndUserPermissionSetIsEmpty_ReturnFalse() {

	    PermissionEntity permissionEntity= mock(PermissionEntity.class);
		UserEntity userEntity = mock (UserEntity.class);
	    Assertions.assertThat(_appService.checkIfPermissionAlreadyAssigned(userEntity, permissionEntity)).isEqualTo(false);

	}
	
	@Test 
	public void checkIfPermissionAlreadyAssigned_UserEntityAndPermissionEntityIsNotNullAndUserPermissionSetIsNotEmpty_ReturnTrue() {

    	PermissionEntity permissionEntity= new PermissionEntity();
		permissionEntity.setId(1L);
		
	    UserpermissionEntity userpermission= new UserpermissionEntity();
	    userpermission.setPermissionId(1L);  
		userpermission.setPermission(permissionEntity);
		
		Set<UserpermissionEntity> up= new HashSet<UserpermissionEntity>();
	    up.add(userpermission);
		UserEntity userEntity = mock (UserEntity.class);
	    Mockito.when(userEntity.getUserpermissionSet()).thenReturn(up);
		Assertions.assertThat(_appService.checkIfPermissionAlreadyAssigned(userEntity, permissionEntity)).isEqualTo(true);
	}
	
	@Test
	public void deleteUserpermission_UserpermissionIsNotNullAndUserpermissionExists_UserpermissionRemoved() {

		UserpermissionEntity userpermission= mock(UserpermissionEntity.class);
		Mockito.when(_userpermissionManager.findById(any(UserpermissionId.class))).thenReturn(userpermission);
		
		_appService.delete(userPermissionId); 
		verify(_userpermissionManager).delete(userpermission);
	}
	
	@Test
	public void Find_ListIsEmpty_ReturnList() throws Exception {

		List<UserpermissionEntity> list = new ArrayList<>();
		Page<UserpermissionEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindUserpermissionByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_userpermissionManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void Find_ListIsNotEmpty_ReturnList() throws Exception {

		List<UserpermissionEntity> list = new ArrayList<>();
		UserpermissionEntity userpermission = mock(UserpermissionEntity.class);
		list.add(userpermission);
    	Page<UserpermissionEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindUserpermissionByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.userpermissionEntityToFindUserpermissionByIdOutput(userpermission));
    	
    	Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_userpermissionManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
  @Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QUserpermissionEntity userpermission = QUserpermissionEntity.userpermissionEntity;
	    
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue(String.valueOf(ID));
		Map<String,SearchFields> map = new HashMap<>();
		map.put("permissionId",searchFields);
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(userpermission.permission.id.eq(ID));
		
		Map<String,String> searchMap = new HashMap<String,String>();
		searchMap.put("permissionId",String.valueOf(ID));
		
		Assertions.assertThat(_appService.searchKeyValuePair(userpermission,map,searchMap)).isEqualTo(builder);
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
		
		QUserpermissionEntity userpermission = QUserpermissionEntity.userpermissionEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue(String.valueOf(ID));
		search.setOperator("equals");
		
		Map<String,String> searchMap = new HashMap<String,String>();
		searchMap.put("permissionId",String.valueOf(ID));
		search.setJoinColumns(searchMap);

		fields.setOperator("equals");
		fields.setSearchValue(String.valueOf(ID));
		fields.setFieldName("permissionId");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
        
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(userpermission.permission.id.eq(ID));
		
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
		
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
	
    //User
	@Test
	public void GetUser_IfUserpermissionIdAndUserIdIsNotNullAndUserpermissionExists_ReturnUser() {
		UserpermissionEntity userpermission = mock(UserpermissionEntity.class);
		UserEntity user = mock(UserEntity.class);

		Mockito.when(_userpermissionManager.findById(any(UserpermissionId.class))).thenReturn(userpermission);
		Mockito.when(_userpermissionManager.getUser(any(UserpermissionId.class))).thenReturn(user);
		Assertions.assertThat(_appService.getUser(userPermissionId)).isEqualTo(_mapper.userEntityToGetUserOutput(user, userpermission));
	}

	@Test 
	public void GetUser_IfUserpermissionIdAndUserIdIsNotNullAndUserpermissionDoesNotExist_ReturnNull() {
		
		Mockito.when(_userpermissionManager.findById(any(UserpermissionId.class))).thenReturn(null);
		Assertions.assertThat(_appService.getUser(userPermissionId)).isEqualTo(null);
	}
 
   //Permission
	@Test
	public void GetPermission_IfUserpermissionIdAndPermissionIdIsNotNullAndUserpermissionExists_ReturnPermission() {
		UserpermissionEntity userpermission = mock(UserpermissionEntity.class);
		PermissionEntity permission = mock(PermissionEntity.class);

		Mockito.when(_userpermissionManager.findById(any(UserpermissionId.class))).thenReturn(userpermission);
		Mockito.when(_userpermissionManager.getPermission(any(UserpermissionId.class))).thenReturn(permission);
		Assertions.assertThat(_appService.getPermission(userPermissionId)).isEqualTo(_mapper.permissionEntityToGetPermissionOutput(permission, userpermission));
	}

	@Test 
	public void GetPermission_IfUserpermissionIdAndPermissionIdIsNotNullAndUserpermissionDoesNotExist_ReturnNull() {
		
		Mockito.when(_userpermissionManager.findById(any(UserpermissionId.class))).thenReturn(null);
		Assertions.assertThat(_appService.getPermission(userPermissionId)).isEqualTo(null);
	}
	
	@Test
	public void ParseUserPermissionKey_KeysStringIsNotEmptyAndKeyValuePairExists_ReturnUserpermissionId()
	{

		UserpermissionId userpermissionId = new UserpermissionId();
		userpermissionId.setPermissionId(Long.valueOf(ID));
        String keyString= "userId:15,permissionId:15";
    	userpermissionId.setUserId(Long.valueOf(ID));
		Assertions.assertThat(_appService.parseUserpermissionKey(keyString)).isEqualToComparingFieldByField(userpermissionId);
	}
	
	@Test
	public void ParseUserPermissionKey_KeysStringIsEmpty_ReturnNull()
	{
		
		String keyString= "";
		Assertions.assertThat(_appService.parseUserpermissionKey(keyString)).isEqualTo(null);
	}
	
	@Test
	public void ParseUserPermissionKey_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
		String keyString= "permissionId";
		Assertions.assertThat(_appService.parseUserpermissionKey(keyString)).isEqualTo(null);
	}
 
}

