package com.nfinity.ll.testaz.application.vets;

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

import com.nfinity.ll.testaz.domain.vets.*;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.application.vets.dto.*;
import com.nfinity.ll.testaz.domain.model.QVetsEntity;
import com.nfinity.ll.testaz.domain.model.VetsEntity;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.vetspecialties.VetSpecialtiesManager;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class VetsAppServiceTest {

	@InjectMocks
	@Spy
	VetsAppService _appService;

	@Mock
	private VetsManager _vetsManager;
	
    @Mock
	private VetSpecialtiesManager  _vetSpecialtiesManager;
	
	@Mock
	private VetsMapper _mapper;

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
	public void findVetsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_vetsManager.findById(any(Integer.class))).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findVetsById_IdIsNotNullAndIdExists_ReturnVets() {

		VetsEntity vets = mock(VetsEntity.class);
		Mockito.when(_vetsManager.findById(any(Integer.class))).thenReturn(vets);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.vetsEntityToFindVetsByIdOutput(vets));
	}
	
	 @Test 
    public void createVets_VetsIsNotNullAndVetsDoesNotExist_StoreVets() { 
 
       VetsEntity vetsEntity = mock(VetsEntity.class); 
       CreateVetsInput vets = new CreateVetsInput();
   
		
        Mockito.when(_mapper.createVetsInputToVetsEntity(any(CreateVetsInput.class))).thenReturn(vetsEntity); 
        Mockito.when(_vetsManager.create(any(VetsEntity.class))).thenReturn(vetsEntity);
      
        Assertions.assertThat(_appService.create(vets)).isEqualTo(_mapper.vetsEntityToCreateVetsOutput(vetsEntity)); 
    } 
	@Test
	public void updateVets_VetsIdIsNotNullAndIdExists_ReturnUpdatedVets() {

		VetsEntity vetsEntity = mock(VetsEntity.class);
		UpdateVetsInput vets= mock(UpdateVetsInput.class);
		
		Mockito.when(_mapper.updateVetsInputToVetsEntity(any(UpdateVetsInput.class))).thenReturn(vetsEntity);
		Mockito.when(_vetsManager.update(any(VetsEntity.class))).thenReturn(vetsEntity);
		Assertions.assertThat(_appService.update(ID,vets)).isEqualTo(_mapper.vetsEntityToUpdateVetsOutput(vetsEntity));
	}
    
	@Test
	public void deleteVets_VetsIsNotNullAndVetsExists_VetsRemoved() {

		VetsEntity vets= mock(VetsEntity.class);
		Mockito.when(_vetsManager.findById(any(Integer.class))).thenReturn(vets);
		
		_appService.delete(ID); 
		verify(_vetsManager).delete(vets);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<VetsEntity> list = new ArrayList<>();
		Page<VetsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindVetsByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_vetsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<VetsEntity> list = new ArrayList<>();
		VetsEntity vets = mock(VetsEntity.class);
		list.add(vets);
    	Page<VetsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindVetsByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.vetsEntityToFindVetsByIdOutput(vets));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_vetsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QVetsEntity vets = QVetsEntity.vetsEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("firstName",searchFields);
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
         builder.and(vets.firstName.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(vets,map,searchMap)).isEqualTo(builder);
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
        list.add("firstName");
        list.add("lastName");
		_appService.checkProperties(list);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QVetsEntity vets = QVetsEntity.vetsEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setFieldName("firstName");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        builder.or(vets.firstName.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QVetsEntity.class), any(HashMap.class), any(HashMap.class));
        
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
		joinColumnMap.put("vetId", keyString);
		Assertions.assertThat(_appService.parseVetSpecialtiesJoinColumn(keyString)).isEqualTo(joinColumnMap);
	}
}

