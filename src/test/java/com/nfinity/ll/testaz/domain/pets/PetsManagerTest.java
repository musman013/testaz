package com.nfinity.ll.testaz.domain.pets;

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

import com.nfinity.ll.testaz.domain.model.PetsEntity;
import com.nfinity.ll.testaz.domain.irepository.IVisitsRepository;
import com.nfinity.ll.testaz.domain.irepository.ITypesRepository;
import com.nfinity.ll.testaz.domain.model.TypesEntity;
import com.nfinity.ll.testaz.domain.irepository.IOwnersRepository;
import com.nfinity.ll.testaz.domain.model.OwnersEntity;
import com.nfinity.ll.testaz.domain.irepository.IPetsRepository;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class PetsManagerTest {

	@InjectMocks
	PetsManager _petsManager;
	
	@Mock
	IPetsRepository  _petsRepository;
    
    @Mock
	IVisitsRepository  _visitsRepository;
    
    @Mock
	ITypesRepository  _typesRepository;
    
    @Mock
	IOwnersRepository  _ownersRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static Integer ID=15;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_petsManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findPetsById_IdIsNotNullAndIdExists_ReturnPets() {
		PetsEntity pets =mock(PetsEntity.class);

        Optional<PetsEntity> dbPets = Optional.of((PetsEntity) pets);
		Mockito.<Optional<PetsEntity>>when(_petsRepository.findById(any(Integer.class))).thenReturn(dbPets);
		Assertions.assertThat(_petsManager.findById(ID)).isEqualTo(pets);
	}

	@Test 
	public void findPetsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<PetsEntity>>when(_petsRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
		Assertions.assertThat(_petsManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void createPets_PetsIsNotNullAndPetsDoesNotExist_StorePets() {

		PetsEntity pets =mock(PetsEntity.class);
		Mockito.when(_petsRepository.save(any(PetsEntity.class))).thenReturn(pets);
		Assertions.assertThat(_petsManager.create(pets)).isEqualTo(pets);
	}

	@Test
	public void deletePets_PetsExists_RemovePets() {

		PetsEntity pets =mock(PetsEntity.class);
		_petsManager.delete(pets);
		verify(_petsRepository).delete(pets);
	}

	@Test
	public void updatePets_PetsIsNotNullAndPetsExists_UpdatePets() {
		
		PetsEntity pets =mock(PetsEntity.class);
		Mockito.when(_petsRepository.save(any(PetsEntity.class))).thenReturn(pets);
		Assertions.assertThat(_petsManager.update(pets)).isEqualTo(pets);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<PetsEntity> pets = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_petsRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(pets);
		Assertions.assertThat(_petsManager.findAll(predicate,pageable)).isEqualTo(pets);
	}
	
    //Types
	@Test
	public void getTypes_if_PetsIdIsNotNull_returnTypes() {

		PetsEntity pets = mock(PetsEntity.class);
		TypesEntity types = mock(TypesEntity.class);
		
        Optional<PetsEntity> dbPets = Optional.of((PetsEntity) pets);
		Mockito.<Optional<PetsEntity>>when(_petsRepository.findById(any(Integer.class))).thenReturn(dbPets);
		Mockito.when(pets.getTypes()).thenReturn(types);
		Assertions.assertThat(_petsManager.getTypes(ID)).isEqualTo(types);

	}
	
    //Owners
	@Test
	public void getOwners_if_PetsIdIsNotNull_returnOwners() {

		PetsEntity pets = mock(PetsEntity.class);
		OwnersEntity owners = mock(OwnersEntity.class);
		
        Optional<PetsEntity> dbPets = Optional.of((PetsEntity) pets);
		Mockito.<Optional<PetsEntity>>when(_petsRepository.findById(any(Integer.class))).thenReturn(dbPets);
		Mockito.when(pets.getOwners()).thenReturn(owners);
		Assertions.assertThat(_petsManager.getOwners(ID)).isEqualTo(owners);

	}
	
}
