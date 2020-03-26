package com.nfinity.ll.testaz.application.visits;

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

import com.nfinity.ll.testaz.domain.visits.*;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.application.visits.dto.*;
import com.nfinity.ll.testaz.domain.model.QVisitsEntity;
import com.nfinity.ll.testaz.domain.model.VisitsEntity;
import com.nfinity.ll.testaz.domain.model.PetsEntity;
import com.nfinity.ll.testaz.domain.pets.PetsManager;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class VisitsAppServiceTest {

	@InjectMocks
	@Spy
	VisitsAppService _appService;

	@Mock
	private VisitsManager _visitsManager;
	
    @Mock
	private PetsManager  _petsManager;
	
	@Mock
	private VisitsMapper _mapper;

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
	public void findVisitsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_visitsManager.findById(any(Integer.class))).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findVisitsById_IdIsNotNullAndIdExists_ReturnVisits() {

		VisitsEntity visits = mock(VisitsEntity.class);
		Mockito.when(_visitsManager.findById(any(Integer.class))).thenReturn(visits);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.visitsEntityToFindVisitsByIdOutput(visits));
	}
	
	 @Test 
    public void createVisits_VisitsIsNotNullAndVisitsDoesNotExist_StoreVisits() { 
 
       VisitsEntity visitsEntity = mock(VisitsEntity.class); 
       CreateVisitsInput visits = new CreateVisitsInput();
   
		PetsEntity pets= mock(PetsEntity.class);
        visits.setPetId(Integer.valueOf(ID.intValue()));
		Mockito.when(_petsManager.findById(
        any(Integer.class))).thenReturn(pets);
		
        Mockito.when(_mapper.createVisitsInputToVisitsEntity(any(CreateVisitsInput.class))).thenReturn(visitsEntity); 
        Mockito.when(_visitsManager.create(any(VisitsEntity.class))).thenReturn(visitsEntity);
      
        Assertions.assertThat(_appService.create(visits)).isEqualTo(_mapper.visitsEntityToCreateVisitsOutput(visitsEntity)); 
    } 
    @Test
	public void createVisits_VisitsIsNotNullAndVisitsDoesNotExistAndChildIsNullAndChildIsNotMandatory_StoreVisits() {

		VisitsEntity visitsEntity = mock(VisitsEntity.class);
		CreateVisitsInput visits = mock(CreateVisitsInput.class);
		
		Mockito.when(_mapper.createVisitsInputToVisitsEntity(any(CreateVisitsInput.class))).thenReturn(visitsEntity);
		Mockito.when(_visitsManager.create(any(VisitsEntity.class))).thenReturn(visitsEntity);
		Assertions.assertThat(_appService.create(visits)).isEqualTo(_mapper.visitsEntityToCreateVisitsOutput(visitsEntity));

	}
	
    @Test
	public void updateVisits_VisitsIsNotNullAndVisitsDoesNotExistAndChildIsNullAndChildIsNotMandatory_ReturnUpdatedVisits() {

		VisitsEntity visitsEntity = mock(VisitsEntity.class);
		UpdateVisitsInput visits = mock(UpdateVisitsInput.class);
		
		Mockito.when(_mapper.updateVisitsInputToVisitsEntity(any(UpdateVisitsInput.class))).thenReturn(visitsEntity);
		Mockito.when(_visitsManager.update(any(VisitsEntity.class))).thenReturn(visitsEntity);
		Assertions.assertThat(_appService.update(ID,visits)).isEqualTo(_mapper.visitsEntityToUpdateVisitsOutput(visitsEntity));
	}
	
		
	@Test
	public void updateVisits_VisitsIdIsNotNullAndIdExists_ReturnUpdatedVisits() {

		VisitsEntity visitsEntity = mock(VisitsEntity.class);
		UpdateVisitsInput visits= mock(UpdateVisitsInput.class);
		
		Mockito.when(_mapper.updateVisitsInputToVisitsEntity(any(UpdateVisitsInput.class))).thenReturn(visitsEntity);
		Mockito.when(_visitsManager.update(any(VisitsEntity.class))).thenReturn(visitsEntity);
		Assertions.assertThat(_appService.update(ID,visits)).isEqualTo(_mapper.visitsEntityToUpdateVisitsOutput(visitsEntity));
	}
    
	@Test
	public void deleteVisits_VisitsIsNotNullAndVisitsExists_VisitsRemoved() {

		VisitsEntity visits= mock(VisitsEntity.class);
		Mockito.when(_visitsManager.findById(any(Integer.class))).thenReturn(visits);
		
		_appService.delete(ID); 
		verify(_visitsManager).delete(visits);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<VisitsEntity> list = new ArrayList<>();
		Page<VisitsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindVisitsByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_visitsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<VisitsEntity> list = new ArrayList<>();
		VisitsEntity visits = mock(VisitsEntity.class);
		list.add(visits);
    	Page<VisitsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindVisitsByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.visitsEntityToFindVisitsByIdOutput(visits));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_visitsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QVisitsEntity visits = QVisitsEntity.visitsEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("description",searchFields);
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
         builder.and(visits.description.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(visits,map,searchMap)).isEqualTo(builder);
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
        list.add("description");
		_appService.checkProperties(list);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QVisitsEntity visits = QVisitsEntity.visitsEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setFieldName("description");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        builder.or(visits.description.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QVisitsEntity.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
   
   //Pets
	@Test
	public void GetPets_IfVisitsIdAndPetsIdIsNotNullAndVisitsExists_ReturnPets() {
		VisitsEntity visits = mock(VisitsEntity.class);
		PetsEntity pets = mock(PetsEntity.class);

		Mockito.when(_visitsManager.findById(any(Integer.class))).thenReturn(visits);
		Mockito.when(_visitsManager.getPets(any(Integer.class))).thenReturn(pets);
		Assertions.assertThat(_appService.getPets(ID)).isEqualTo(_mapper.petsEntityToGetPetsOutput(pets, visits));
	}

	@Test 
	public void GetPets_IfVisitsIdAndPetsIdIsNotNullAndVisitsDoesNotExist_ReturnNull() {
		Mockito.when(_visitsManager.findById(any(Integer.class))).thenReturn(null);
		Assertions.assertThat(_appService.getPets(ID)).isEqualTo(null);
	}
	
}

