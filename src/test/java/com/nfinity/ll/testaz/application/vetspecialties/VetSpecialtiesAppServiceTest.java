package com.nfinity.ll.testaz.application.vetspecialties;

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

import com.nfinity.ll.testaz.domain.vetspecialties.*;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.application.vetspecialties.dto.*;
import com.nfinity.ll.testaz.domain.model.QVetSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesId;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
import com.nfinity.ll.testaz.domain.specialties.SpecialtiesManager;
import com.nfinity.ll.testaz.domain.model.VetsEntity;
import com.nfinity.ll.testaz.domain.vets.VetsManager;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class VetSpecialtiesAppServiceTest {

	@InjectMocks
	@Spy
	VetSpecialtiesAppService _appService;

	@Mock
	private VetSpecialtiesManager _vetSpecialtiesManager;
	
    @Mock
	private SpecialtiesManager  _specialtiesManager;
	
    @Mock
	private VetsManager  _vetsManager;
	
	@Mock
	private VetSpecialtiesMapper _mapper;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;
	

    @Mock
    private VetSpecialtiesId vetSpecialtiesId;
    
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
	public void findVetSpecialtiesById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_vetSpecialtiesManager.findById(any(VetSpecialtiesId.class))).thenReturn(null);
		Assertions.assertThat(_appService.findById(vetSpecialtiesId)).isEqualTo(null);
	}
	
	@Test
	public void findVetSpecialtiesById_IdIsNotNullAndIdExists_ReturnVetSpecialties() {

		VetSpecialtiesEntity vetSpecialties = mock(VetSpecialtiesEntity.class);
		Mockito.when(_vetSpecialtiesManager.findById(any(VetSpecialtiesId.class))).thenReturn(vetSpecialties);
		Assertions.assertThat(_appService.findById(vetSpecialtiesId)).isEqualTo(_mapper.vetSpecialtiesEntityToFindVetSpecialtiesByIdOutput(vetSpecialties));
	}
	
	 @Test 
    public void createVetSpecialties_VetSpecialtiesIsNotNullAndVetSpecialtiesDoesNotExist_StoreVetSpecialties() { 
 
       VetSpecialtiesEntity vetSpecialtiesEntity = mock(VetSpecialtiesEntity.class); 
       CreateVetSpecialtiesInput vetSpecialties = new CreateVetSpecialtiesInput();
   
		SpecialtiesEntity specialties= mock(SpecialtiesEntity.class);
        vetSpecialties.setSpecialtyId(Integer.valueOf(ID.intValue()));
		Mockito.when(_specialtiesManager.findById(
        any(Integer.class))).thenReturn(specialties);
		VetsEntity vets= mock(VetsEntity.class);
        vetSpecialties.setVetId(Integer.valueOf(ID.intValue()));
		Mockito.when(_vetsManager.findById(
        any(Integer.class))).thenReturn(vets);
		
        Mockito.when(_mapper.createVetSpecialtiesInputToVetSpecialtiesEntity(any(CreateVetSpecialtiesInput.class))).thenReturn(vetSpecialtiesEntity); 
        Mockito.when(_vetSpecialtiesManager.create(any(VetSpecialtiesEntity.class))).thenReturn(vetSpecialtiesEntity);
      
        Assertions.assertThat(_appService.create(vetSpecialties)).isEqualTo(_mapper.vetSpecialtiesEntityToCreateVetSpecialtiesOutput(vetSpecialtiesEntity)); 
    } 
    @Test
	public void createVetSpecialties_VetSpecialtiesIsNotNullAndVetSpecialtiesDoesNotExistAndChildIsNullAndChildIsNotMandatory_StoreVetSpecialties() {

		VetSpecialtiesEntity vetSpecialtiesEntity = mock(VetSpecialtiesEntity.class);
		CreateVetSpecialtiesInput vetSpecialties = mock(CreateVetSpecialtiesInput.class);
		
		Mockito.when(_mapper.createVetSpecialtiesInputToVetSpecialtiesEntity(any(CreateVetSpecialtiesInput.class))).thenReturn(vetSpecialtiesEntity);
		Mockito.when(_vetSpecialtiesManager.create(any(VetSpecialtiesEntity.class))).thenReturn(vetSpecialtiesEntity);
		Assertions.assertThat(_appService.create(vetSpecialties)).isEqualTo(_mapper.vetSpecialtiesEntityToCreateVetSpecialtiesOutput(vetSpecialtiesEntity));

	}
	
    @Test
	public void updateVetSpecialties_VetSpecialtiesIsNotNullAndVetSpecialtiesDoesNotExistAndChildIsNullAndChildIsNotMandatory_ReturnUpdatedVetSpecialties() {

		VetSpecialtiesEntity vetSpecialtiesEntity = mock(VetSpecialtiesEntity.class);
		UpdateVetSpecialtiesInput vetSpecialties = mock(UpdateVetSpecialtiesInput.class);
		
		Mockito.when(_mapper.updateVetSpecialtiesInputToVetSpecialtiesEntity(any(UpdateVetSpecialtiesInput.class))).thenReturn(vetSpecialtiesEntity);
		Mockito.when(_vetSpecialtiesManager.update(any(VetSpecialtiesEntity.class))).thenReturn(vetSpecialtiesEntity);
		Assertions.assertThat(_appService.update(vetSpecialtiesId,vetSpecialties)).isEqualTo(_mapper.vetSpecialtiesEntityToUpdateVetSpecialtiesOutput(vetSpecialtiesEntity));
	}
	
		
	@Test
	public void updateVetSpecialties_VetSpecialtiesIdIsNotNullAndIdExists_ReturnUpdatedVetSpecialties() {

		VetSpecialtiesEntity vetSpecialtiesEntity = mock(VetSpecialtiesEntity.class);
		UpdateVetSpecialtiesInput vetSpecialties= mock(UpdateVetSpecialtiesInput.class);
		
		Mockito.when(_mapper.updateVetSpecialtiesInputToVetSpecialtiesEntity(any(UpdateVetSpecialtiesInput.class))).thenReturn(vetSpecialtiesEntity);
		Mockito.when(_vetSpecialtiesManager.update(any(VetSpecialtiesEntity.class))).thenReturn(vetSpecialtiesEntity);
		Assertions.assertThat(_appService.update(vetSpecialtiesId,vetSpecialties)).isEqualTo(_mapper.vetSpecialtiesEntityToUpdateVetSpecialtiesOutput(vetSpecialtiesEntity));
	}
    
	@Test
	public void deleteVetSpecialties_VetSpecialtiesIsNotNullAndVetSpecialtiesExists_VetSpecialtiesRemoved() {

		VetSpecialtiesEntity vetSpecialties= mock(VetSpecialtiesEntity.class);
		Mockito.when(_vetSpecialtiesManager.findById(any(VetSpecialtiesId.class))).thenReturn(vetSpecialties);
		
		_appService.delete(vetSpecialtiesId); 
		verify(_vetSpecialtiesManager).delete(vetSpecialties);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<VetSpecialtiesEntity> list = new ArrayList<>();
		Page<VetSpecialtiesEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindVetSpecialtiesByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_vetSpecialtiesManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<VetSpecialtiesEntity> list = new ArrayList<>();
		VetSpecialtiesEntity vetSpecialties = mock(VetSpecialtiesEntity.class);
		list.add(vetSpecialties);
    	Page<VetSpecialtiesEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindVetSpecialtiesByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.vetSpecialtiesEntityToFindVetSpecialtiesByIdOutput(vetSpecialties));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_vetSpecialtiesManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QVetSpecialtiesEntity vetSpecialties = QVetSpecialtiesEntity.vetSpecialtiesEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.searchKeyValuePair(vetSpecialties,map,searchMap)).isEqualTo(builder);
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
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QVetSpecialtiesEntity vetSpecialties = QVetSpecialtiesEntity.vetSpecialtiesEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QVetSpecialtiesEntity.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
   
   //Specialties
	@Test
	public void GetSpecialties_IfVetSpecialtiesIdAndSpecialtiesIdIsNotNullAndVetSpecialtiesExists_ReturnSpecialties() {
		VetSpecialtiesEntity vetSpecialties = mock(VetSpecialtiesEntity.class);
		SpecialtiesEntity specialties = mock(SpecialtiesEntity.class);

		Mockito.when(_vetSpecialtiesManager.findById(any(VetSpecialtiesId.class))).thenReturn(vetSpecialties);
		Mockito.when(_vetSpecialtiesManager.getSpecialties(any(VetSpecialtiesId.class))).thenReturn(specialties);
		Assertions.assertThat(_appService.getSpecialties(vetSpecialtiesId)).isEqualTo(_mapper.specialtiesEntityToGetSpecialtiesOutput(specialties, vetSpecialties));
	}

	@Test 
	public void GetSpecialties_IfVetSpecialtiesIdAndSpecialtiesIdIsNotNullAndVetSpecialtiesDoesNotExist_ReturnNull() {
		Mockito.when(_vetSpecialtiesManager.findById(any(VetSpecialtiesId.class))).thenReturn(null);
		Assertions.assertThat(_appService.getSpecialties(vetSpecialtiesId)).isEqualTo(null);
	}
   
   //Vets
	@Test
	public void GetVets_IfVetSpecialtiesIdAndVetsIdIsNotNullAndVetSpecialtiesExists_ReturnVets() {
		VetSpecialtiesEntity vetSpecialties = mock(VetSpecialtiesEntity.class);
		VetsEntity vets = mock(VetsEntity.class);

		Mockito.when(_vetSpecialtiesManager.findById(any(VetSpecialtiesId.class))).thenReturn(vetSpecialties);
		Mockito.when(_vetSpecialtiesManager.getVets(any(VetSpecialtiesId.class))).thenReturn(vets);
		Assertions.assertThat(_appService.getVets(vetSpecialtiesId)).isEqualTo(_mapper.vetsEntityToGetVetsOutput(vets, vetSpecialties));
	}

	@Test 
	public void GetVets_IfVetSpecialtiesIdAndVetsIdIsNotNullAndVetSpecialtiesDoesNotExist_ReturnNull() {
		Mockito.when(_vetSpecialtiesManager.findById(any(VetSpecialtiesId.class))).thenReturn(null);
		Assertions.assertThat(_appService.getVets(vetSpecialtiesId)).isEqualTo(null);
	}
  
	@Test
	public void ParseVetSpecialtiesKey_KeysStringIsNotEmptyAndKeyValuePairExists_ReturnVetSpecialtiesId()
	{
		String keyString= "specialtyId:15,vetId:15";
	
		VetSpecialtiesId vetSpecialtiesId = new VetSpecialtiesId();
		vetSpecialtiesId.setSpecialtyId(Integer.valueOf(ID.intValue()));
		vetSpecialtiesId.setVetId(Integer.valueOf(ID.intValue()));

		Assertions.assertThat(_appService.parseVetSpecialtiesKey(keyString)).isEqualToComparingFieldByField(vetSpecialtiesId);
	}
	
	@Test
	public void ParseVetSpecialtiesKey_KeysStringIsEmpty_ReturnNull()
	{
		String keyString= "";
		Assertions.assertThat(_appService.parseVetSpecialtiesKey(keyString)).isEqualTo(null);
	}
	
	@Test
	public void ParseVetSpecialtiesKey_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
		String keyString= "specialtyId";

		Assertions.assertThat(_appService.parseVetSpecialtiesKey(keyString)).isEqualTo(null);
	}
	
}

