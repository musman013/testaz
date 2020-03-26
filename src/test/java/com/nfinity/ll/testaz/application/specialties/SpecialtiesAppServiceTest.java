package com.nfinity.ll.testaz.application.specialties;

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

import com.nfinity.ll.testaz.domain.specialties.*;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.application.specialties.dto.*;
import com.nfinity.ll.testaz.domain.model.QSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.vetspecialties.VetSpecialtiesManager;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class SpecialtiesAppServiceTest {

	@InjectMocks
	@Spy
	SpecialtiesAppService _appService;

	@Mock
	private SpecialtiesManager _specialtiesManager;
	
    @Mock
	private VetSpecialtiesManager  _vetSpecialtiesManager;
	
	@Mock
	private SpecialtiesMapper _mapper;

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
	public void findSpecialtiesById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_specialtiesManager.findById(any(Integer.class))).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findSpecialtiesById_IdIsNotNullAndIdExists_ReturnSpecialties() {

		SpecialtiesEntity specialties = mock(SpecialtiesEntity.class);
		Mockito.when(_specialtiesManager.findById(any(Integer.class))).thenReturn(specialties);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.specialtiesEntityToFindSpecialtiesByIdOutput(specialties));
	}
	
	 @Test 
    public void createSpecialties_SpecialtiesIsNotNullAndSpecialtiesDoesNotExist_StoreSpecialties() { 
 
       SpecialtiesEntity specialtiesEntity = mock(SpecialtiesEntity.class); 
       CreateSpecialtiesInput specialties = new CreateSpecialtiesInput();
   
		
        Mockito.when(_mapper.createSpecialtiesInputToSpecialtiesEntity(any(CreateSpecialtiesInput.class))).thenReturn(specialtiesEntity); 
        Mockito.when(_specialtiesManager.create(any(SpecialtiesEntity.class))).thenReturn(specialtiesEntity);
      
        Assertions.assertThat(_appService.create(specialties)).isEqualTo(_mapper.specialtiesEntityToCreateSpecialtiesOutput(specialtiesEntity)); 
    } 
	@Test
	public void updateSpecialties_SpecialtiesIdIsNotNullAndIdExists_ReturnUpdatedSpecialties() {

		SpecialtiesEntity specialtiesEntity = mock(SpecialtiesEntity.class);
		UpdateSpecialtiesInput specialties= mock(UpdateSpecialtiesInput.class);
		
		Mockito.when(_mapper.updateSpecialtiesInputToSpecialtiesEntity(any(UpdateSpecialtiesInput.class))).thenReturn(specialtiesEntity);
		Mockito.when(_specialtiesManager.update(any(SpecialtiesEntity.class))).thenReturn(specialtiesEntity);
		Assertions.assertThat(_appService.update(ID,specialties)).isEqualTo(_mapper.specialtiesEntityToUpdateSpecialtiesOutput(specialtiesEntity));
	}
    
	@Test
	public void deleteSpecialties_SpecialtiesIsNotNullAndSpecialtiesExists_SpecialtiesRemoved() {

		SpecialtiesEntity specialties= mock(SpecialtiesEntity.class);
		Mockito.when(_specialtiesManager.findById(any(Integer.class))).thenReturn(specialties);
		
		_appService.delete(ID); 
		verify(_specialtiesManager).delete(specialties);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<SpecialtiesEntity> list = new ArrayList<>();
		Page<SpecialtiesEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindSpecialtiesByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_specialtiesManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<SpecialtiesEntity> list = new ArrayList<>();
		SpecialtiesEntity specialties = mock(SpecialtiesEntity.class);
		list.add(specialties);
    	Page<SpecialtiesEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindSpecialtiesByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.specialtiesEntityToFindSpecialtiesByIdOutput(specialties));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_specialtiesManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QSpecialtiesEntity specialties = QSpecialtiesEntity.specialtiesEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("name",searchFields);
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
         builder.and(specialties.name.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(specialties,map,searchMap)).isEqualTo(builder);
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
		QSpecialtiesEntity specialties = QSpecialtiesEntity.specialtiesEntity;
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
        builder.or(specialties.name.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QSpecialtiesEntity.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
	
	@Test
	public void ParseVetSpecialtiesJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
	    Map<String,String> joinColumnMap = new HashMap<String,String>();
		String keyString= "15";
		joinColumnMap.put("specialtyId", keyString);
		Assertions.assertThat(_appService.parseVetSpecialtiesJoinColumn(keyString)).isEqualTo(joinColumnMap);
	}
}

