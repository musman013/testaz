package com.nfinity.ll.testaz.application.owners;

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

import com.nfinity.ll.testaz.domain.owners.*;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.application.owners.dto.*;
import com.nfinity.ll.testaz.domain.model.QOwnersEntity;
import com.nfinity.ll.testaz.domain.model.OwnersEntity;
import com.nfinity.ll.testaz.domain.model.PetsEntity;
import com.nfinity.ll.testaz.domain.pets.PetsManager;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class OwnersAppServiceTest {

	@InjectMocks
	@Spy
	OwnersAppService _appService;

	@Mock
	private OwnersManager _ownersManager;
	
    @Mock
	private PetsManager  _petsManager;
	
	@Mock
	private OwnersMapper _mapper;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;
	

    private static Integer ID=15;
    
	 
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
	public void findOwnersById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_ownersManager.findById(any(Integer.class))).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findOwnersById_IdIsNotNullAndIdExists_ReturnOwners() {

		OwnersEntity owners = mock(OwnersEntity.class);
		Mockito.when(_ownersManager.findById(any(Integer.class))).thenReturn(owners);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.ownersEntityToFindOwnersByIdOutput(owners));
	}
	
	 @Test 
    public void createOwners_OwnersIsNotNullAndOwnersDoesNotExist_StoreOwners() { 
 
       OwnersEntity ownersEntity = mock(OwnersEntity.class); 
       CreateOwnersInput owners = new CreateOwnersInput();
   
		
        Mockito.when(_mapper.createOwnersInputToOwnersEntity(any(CreateOwnersInput.class))).thenReturn(ownersEntity); 
        Mockito.when(_ownersManager.create(any(OwnersEntity.class))).thenReturn(ownersEntity);
      
        Assertions.assertThat(_appService.create(owners)).isEqualTo(_mapper.ownersEntityToCreateOwnersOutput(ownersEntity)); 
    } 
	@Test
	public void updateOwners_OwnersIdIsNotNullAndIdExists_ReturnUpdatedOwners() {

		OwnersEntity ownersEntity = mock(OwnersEntity.class);
		UpdateOwnersInput owners= mock(UpdateOwnersInput.class);
		
		Mockito.when(_mapper.updateOwnersInputToOwnersEntity(any(UpdateOwnersInput.class))).thenReturn(ownersEntity);
		Mockito.when(_ownersManager.update(any(OwnersEntity.class))).thenReturn(ownersEntity);
		Assertions.assertThat(_appService.update(ID,owners)).isEqualTo(_mapper.ownersEntityToUpdateOwnersOutput(ownersEntity));
	}
    
	@Test
	public void deleteOwners_OwnersIsNotNullAndOwnersExists_OwnersRemoved() {

		OwnersEntity owners= mock(OwnersEntity.class);
		Mockito.when(_ownersManager.findById(any(Integer.class))).thenReturn(owners);
		
		_appService.delete(ID); 
		verify(_ownersManager).delete(owners);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<OwnersEntity> list = new ArrayList<>();
		Page<OwnersEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindOwnersByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_ownersManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<OwnersEntity> list = new ArrayList<>();
		OwnersEntity owners = mock(OwnersEntity.class);
		list.add(owners);
    	Page<OwnersEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindOwnersByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.ownersEntityToFindOwnersByIdOutput(owners));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_ownersManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QOwnersEntity owners = QOwnersEntity.ownersEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("address",searchFields);
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
         builder.and(owners.address.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(owners,map,searchMap)).isEqualTo(builder);
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
        list.add("address");
        list.add("city");
        list.add("firstName");
        list.add("lastName");
        list.add("telephone");
		_appService.checkProperties(list);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QOwnersEntity owners = QOwnersEntity.ownersEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setFieldName("address");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        builder.or(owners.address.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QOwnersEntity.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
	
	@Test
	public void ParsePetsJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
	    Map<String,String> joinColumnMap = new HashMap<String,String>();
		String keyString= "15";
		joinColumnMap.put("ownerId", keyString);
		Assertions.assertThat(_appService.parsePetsJoinColumn(keyString)).isEqualTo(joinColumnMap);
	}
}

