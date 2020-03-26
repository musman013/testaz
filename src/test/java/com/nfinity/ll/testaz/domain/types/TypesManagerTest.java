package com.nfinity.ll.testaz.domain.types;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.ll.testaz.domain.model.TypesEntity;
import com.nfinity.ll.testaz.domain.irepository.IPetsRepository;
import com.nfinity.ll.testaz.domain.irepository.ITypesRepository;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class TypesManagerTest {

	@InjectMocks
	TypesManager _typesManager;
	
	@Mock
	ITypesRepository  _typesRepository;
    
    @Mock
	IPetsRepository  _petsRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static Integer ID=15;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_typesManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findTypesById_IdIsNotNullAndIdExists_ReturnTypes() {
		TypesEntity types =mock(TypesEntity.class);

        Optional<TypesEntity> dbTypes = Optional.of((TypesEntity) types);
		Mockito.<Optional<TypesEntity>>when(_typesRepository.findById(any(Integer.class))).thenReturn(dbTypes);
		Assertions.assertThat(_typesManager.findById(ID)).isEqualTo(types);
	}

	@Test 
	public void findTypesById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<TypesEntity>>when(_typesRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
		Assertions.assertThat(_typesManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void createTypes_TypesIsNotNullAndTypesDoesNotExist_StoreTypes() {

		TypesEntity types =mock(TypesEntity.class);
		Mockito.when(_typesRepository.save(any(TypesEntity.class))).thenReturn(types);
		Assertions.assertThat(_typesManager.create(types)).isEqualTo(types);
	}

	@Test
	public void deleteTypes_TypesExists_RemoveTypes() {

		TypesEntity types =mock(TypesEntity.class);
		_typesManager.delete(types);
		verify(_typesRepository).delete(types);
	}

	@Test
	public void updateTypes_TypesIsNotNullAndTypesExists_UpdateTypes() {
		
		TypesEntity types =mock(TypesEntity.class);
		Mockito.when(_typesRepository.save(any(TypesEntity.class))).thenReturn(types);
		Assertions.assertThat(_typesManager.update(types)).isEqualTo(types);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<TypesEntity> types = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_typesRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(types);
		Assertions.assertThat(_typesManager.findAll(predicate,pageable)).isEqualTo(types);
	}
	
}
