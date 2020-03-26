package com.nfinity.ll.testaz.application.types;

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

import com.nfinity.ll.testaz.domain.types.*;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.application.types.dto.*;
import com.nfinity.ll.testaz.domain.model.QTypesEntity;
import com.nfinity.ll.testaz.domain.model.TypesEntity;
import com.nfinity.ll.testaz.domain.model.PetsEntity;
import com.nfinity.ll.testaz.domain.pets.PetsManager;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class TypesAppServiceTest {

	@InjectMocks
	@Spy
	TypesAppService _appService;

	@Mock
	private TypesManager _typesManager;
	
    @Mock
	private PetsManager  _petsManager;
	
	@Mock
	private TypesMapper _mapper;

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
	public void findTypesById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_typesManager.findById(any(Integer.class))).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findTypesById_IdIsNotNullAndIdExists_ReturnTypes() {

		TypesEntity types = mock(TypesEntity.class);
		Mockito.when(_typesManager.findById(any(Integer.class))).thenReturn(types);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.typesEntityToFindTypesByIdOutput(types));
	}
	
	 @Test 
    public void createTypes_TypesIsNotNullAndTypesDoesNotExist_StoreTypes() { 
 
       TypesEntity typesEntity = mock(TypesEntity.class); 
       CreateTypesInput types = new CreateTypesInput();
   
		
        Mockito.when(_mapper.createTypesInputToTypesEntity(any(CreateTypesInput.class))).thenReturn(typesEntity); 
        Mockito.when(_typesManager.create(any(TypesEntity.class))).thenReturn(typesEntity);
      
        Assertions.assertThat(_appService.create(types)).isEqualTo(_mapper.typesEntityToCreateTypesOutput(typesEntity)); 
    } 
	@Test
	public void updateTypes_TypesIdIsNotNullAndIdExists_ReturnUpdatedTypes() {

		TypesEntity typesEntity = mock(TypesEntity.class);
		UpdateTypesInput types= mock(UpdateTypesInput.class);
		
		Mockito.when(_mapper.updateTypesInputToTypesEntity(any(UpdateTypesInput.class))).thenReturn(typesEntity);
		Mockito.when(_typesManager.update(any(TypesEntity.class))).thenReturn(typesEntity);
		Assertions.assertThat(_appService.update(ID,types)).isEqualTo(_mapper.typesEntityToUpdateTypesOutput(typesEntity));
	}
    
	@Test
	public void deleteTypes_TypesIsNotNullAndTypesExists_TypesRemoved() {

		TypesEntity types= mock(TypesEntity.class);
		Mockito.when(_typesManager.findById(any(Integer.class))).thenReturn(types);
		
		_appService.delete(ID); 
		verify(_typesManager).delete(types);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<TypesEntity> list = new ArrayList<>();
		Page<TypesEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindTypesByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_typesManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<TypesEntity> list = new ArrayList<>();
		TypesEntity types = mock(TypesEntity.class);
		list.add(types);
    	Page<TypesEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindTypesByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.typesEntityToFindTypesByIdOutput(types));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_typesManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QTypesEntity types = QTypesEntity.typesEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("name",searchFields);
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
         builder.and(types.name.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(types,map,searchMap)).isEqualTo(builder);
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
        list.add("name");
		_appService.checkProperties(list);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QTypesEntity types = QTypesEntity.typesEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setFieldName("name");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        builder.or(types.name.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QTypesEntity.class), any(HashMap.class), any(HashMap.class));
        
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
		joinColumnMap.put("typeId", keyString);
		Assertions.assertThat(_appService.parsePetsJoinColumn(keyString)).isEqualTo(joinColumnMap);
	}
}

