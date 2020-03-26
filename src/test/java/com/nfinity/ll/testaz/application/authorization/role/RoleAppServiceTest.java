package com.nfinity.ll.testaz.application.authorization.role;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchFields;
import com.nfinity.ll.testaz.application.authorization.role.dto.*;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.application.authorization.permission.PermissionAppService;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.authorization.role.RoleManager;
import com.nfinity.ll.testaz.domain.model.QRoleEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoleAppServiceTest {

	@InjectMocks
	@Spy
	RoleAppService roleAppService;

	@Mock
	private RoleManager roleManager;

	@Mock
	private RoleMapper roleMapper;
	
	@Mock
	private PermissionAppService  permissionAppService;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;

	private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(roleAppService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test 
	public void findRoleById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(roleManager.findById(anyLong())).thenReturn(null);	
		Assertions.assertThat(roleAppService.findById(ID)).isEqualTo(null);	

	}

	@Test
	public void findRoleById_IdIsNotNullAndRoleExists_ReturnARole() {

		RoleEntity role = mock(RoleEntity.class);

		Mockito.when(roleManager.findById(anyLong())).thenReturn(role);
		Assertions.assertThat(roleAppService.findById(ID)).isEqualTo(roleMapper.roleEntityToCreateRoleOutput(role));
	}

	@Test 
	public void findRoleByName_NameIsNotNullAndRoleDoesNotExist_ReturnNull() {

		Mockito.when(roleManager.findByRoleName(anyString())).thenReturn(null);	
		Assertions.assertThat(roleAppService.findByRoleName("Role1")).isEqualTo(null);	
	}

	@Test
	public void findRoleByName_NameIsNotNullAndRoleExists_ReturnARole() {

		RoleEntity role = mock(RoleEntity.class);

		Mockito.when(roleManager.findByRoleName(anyString())).thenReturn(role);
		Assertions.assertThat(roleAppService.findByRoleName("Role1")).isEqualTo(roleMapper.roleEntityToCreateRoleOutput(role));
	}
 

	@Test
	public void createRole_RoleIsNotNullAndRoleDoesNotExist_StoreARole() {

		RoleEntity roleEntity = mock(RoleEntity.class);
		CreateRoleInput role=mock(CreateRoleInput.class);

		Mockito.when(roleMapper.createRoleInputToRoleEntity(any(CreateRoleInput.class))).thenReturn(roleEntity);
		Mockito.when(roleManager.create(any(RoleEntity.class))).thenReturn(roleEntity);
		Assertions.assertThat(roleAppService.create(role)).isEqualTo(roleMapper.roleEntityToCreateRoleOutput(roleEntity));
	}

	@Test
	public void deleteRole_RoleIsNotNullAndRoleExists_RoleRemoved() {

		RoleEntity role=mock(RoleEntity.class);
	
		Mockito.when(roleManager.findById(anyLong())).thenReturn(role);
		roleAppService.delete(ID);
		verify(roleManager,Mockito.times(1)).findById(anyLong());
	}

	@Test
	public void updateRole_RoleIdIsNotNullAndRoleExists_ReturnUpdatedRole() {

		RoleEntity roleEntity = mock(RoleEntity.class);
		UpdateRoleInput role=mock(UpdateRoleInput.class);
		
		Mockito.when(roleMapper.updateRoleInputToRoleEntity(any(UpdateRoleInput.class))).thenReturn(roleEntity);
		Mockito.when(roleManager.update(any(RoleEntity.class))).thenReturn(roleEntity);
		Assertions.assertThat(roleAppService.update(ID,role)).isEqualTo(roleMapper.roleEntityToUpdateRoleOutput(roleEntity));
	}
	
	@Test
	public void Find_ListIsEmpty_ReturnList() throws Exception
	{
		List<RoleEntity> list = new ArrayList<>();
		Page<RoleEntity> foundPage = new PageImpl(list);
		Pageable pageable =mock(Pageable.class);

		List<FindRoleByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(roleAppService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(roleManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(roleAppService.find(search,pageable)).isEqualTo(output);

	}

	@Test
	public void Find_ListIsNotEmpty_ReturnList() throws Exception
	{
		List<RoleEntity> list = new ArrayList<>();
		RoleEntity role=mock(RoleEntity.class);
		list.add(role);
		Page<RoleEntity> foundPage = new PageImpl(list);
		Pageable pageable =mock(Pageable.class);
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		List<FindRoleByIdOutput> output = new ArrayList<>();
		output.add(roleMapper.roleEntityToFindRoleByIdOutput(role));
		Mockito.when(roleAppService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(roleManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(roleAppService.find(search,pageable)).isEqualTo(output);

	}

    @Test 
	public void checkPermissionProperties_SearchListIsNotNull_ReturnKeyValueMap()throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("displayName");
		list.add("name");
		permissionAppService.checkProperties(list);
	}

	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder()
	{
		QRoleEntity role = QRoleEntity.roleEntity;
		SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
		Map map = new HashMap();
		map.put("name", searchFields);
		
		Map joinColMap = new HashMap();
		map.put("xyz", ID);

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(role.name.eq("xyz"));

		Assertions.assertThat(roleAppService.searchKeyValuePair(role, map,joinColMap)).isEqualTo(builder);
	}

	@Test(expected = Exception.class)
	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("xyz");

		roleAppService.checkProperties(list);
	}
	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("displayName");

		roleAppService.checkProperties(list);
	}

	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception
	{
		Map<String,SearchFields> map = new HashMap<>();
		QRoleEntity role = QRoleEntity.roleEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		fields.setFieldName("displayName");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(role.displayName.eq("xyz"));

		Assertions.assertThat(roleAppService.search(search)).isEqualTo(builder);

	}

	@Test
	public void search_StringIsNull_ReturnNull() throws Exception
	{
		Assertions.assertThat(roleAppService.search(null)).isEqualTo(null);
	}

	@Test
    public void parseRolepermissionJoinColumn_StringIsNotNull_ReturnMap() {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("roleId", "1");
		
		Assertions.assertThat(roleAppService.parseRolepermissionJoinColumn("1")).isEqualTo(joinColumnMap);
		
	}
	
	@Test
	public void parseUserroleJoinColumn_StringIsNotNull_ReturnMap() {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("roleId", "1");
		
		Assertions.assertThat(roleAppService.parseUserroleJoinColumn("1")).isEqualTo(joinColumnMap);
		
	}
}
