package com.nfinity.ll.testaz.domain.visits;

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

import com.nfinity.ll.testaz.domain.model.VisitsEntity;
import com.nfinity.ll.testaz.domain.irepository.IPetsRepository;
import com.nfinity.ll.testaz.domain.model.PetsEntity;
import com.nfinity.ll.testaz.domain.irepository.IVisitsRepository;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class VisitsManagerTest {

	@InjectMocks
	VisitsManager _visitsManager;
	
	@Mock
	IVisitsRepository  _visitsRepository;
    
    @Mock
	IPetsRepository  _petsRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static Integer ID=15;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_visitsManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findVisitsById_IdIsNotNullAndIdExists_ReturnVisits() {
		VisitsEntity visits =mock(VisitsEntity.class);

        Optional<VisitsEntity> dbVisits = Optional.of((VisitsEntity) visits);
		Mockito.<Optional<VisitsEntity>>when(_visitsRepository.findById(any(Integer.class))).thenReturn(dbVisits);
		Assertions.assertThat(_visitsManager.findById(ID)).isEqualTo(visits);
	}

	@Test 
	public void findVisitsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<VisitsEntity>>when(_visitsRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
		Assertions.assertThat(_visitsManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void createVisits_VisitsIsNotNullAndVisitsDoesNotExist_StoreVisits() {

		VisitsEntity visits =mock(VisitsEntity.class);
		Mockito.when(_visitsRepository.save(any(VisitsEntity.class))).thenReturn(visits);
		Assertions.assertThat(_visitsManager.create(visits)).isEqualTo(visits);
	}

	@Test
	public void deleteVisits_VisitsExists_RemoveVisits() {

		VisitsEntity visits =mock(VisitsEntity.class);
		_visitsManager.delete(visits);
		verify(_visitsRepository).delete(visits);
	}

	@Test
	public void updateVisits_VisitsIsNotNullAndVisitsExists_UpdateVisits() {
		
		VisitsEntity visits =mock(VisitsEntity.class);
		Mockito.when(_visitsRepository.save(any(VisitsEntity.class))).thenReturn(visits);
		Assertions.assertThat(_visitsManager.update(visits)).isEqualTo(visits);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<VisitsEntity> visits = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_visitsRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(visits);
		Assertions.assertThat(_visitsManager.findAll(predicate,pageable)).isEqualTo(visits);
	}
	
    //Pets
	@Test
	public void getPets_if_VisitsIdIsNotNull_returnPets() {

		VisitsEntity visits = mock(VisitsEntity.class);
		PetsEntity pets = mock(PetsEntity.class);
		
        Optional<VisitsEntity> dbVisits = Optional.of((VisitsEntity) visits);
		Mockito.<Optional<VisitsEntity>>when(_visitsRepository.findById(any(Integer.class))).thenReturn(dbVisits);
		Mockito.when(visits.getPets()).thenReturn(pets);
		Assertions.assertThat(_visitsManager.getPets(ID)).isEqualTo(pets);

	}
	
}
