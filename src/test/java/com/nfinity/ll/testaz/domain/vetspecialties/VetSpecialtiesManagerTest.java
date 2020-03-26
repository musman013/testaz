package com.nfinity.ll.testaz.domain.vetspecialties;

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

import com.nfinity.ll.testaz.domain.model.VetSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.irepository.ISpecialtiesRepository;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
import com.nfinity.ll.testaz.domain.irepository.IVetsRepository;
import com.nfinity.ll.testaz.domain.model.VetsEntity;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesId;
import com.nfinity.ll.testaz.domain.irepository.IVetSpecialtiesRepository;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class VetSpecialtiesManagerTest {

	@InjectMocks
	VetSpecialtiesManager _vetSpecialtiesManager;
	
	@Mock
	IVetSpecialtiesRepository  _vetSpecialtiesRepository;
    
    @Mock
	ISpecialtiesRepository  _specialtiesRepository;
    
    @Mock
	IVetsRepository  _vetsRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	@Mock
	private VetSpecialtiesId vetSpecialtiesId;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_vetSpecialtiesManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findVetSpecialtiesById_IdIsNotNullAndIdExists_ReturnVetSpecialties() {
		VetSpecialtiesEntity vetSpecialties =mock(VetSpecialtiesEntity.class);

        Optional<VetSpecialtiesEntity> dbVetSpecialties = Optional.of((VetSpecialtiesEntity) vetSpecialties);
		Mockito.<Optional<VetSpecialtiesEntity>>when(_vetSpecialtiesRepository.findById(any(VetSpecialtiesId.class))).thenReturn(dbVetSpecialties);
		Assertions.assertThat(_vetSpecialtiesManager.findById(vetSpecialtiesId)).isEqualTo(vetSpecialties);
	}

	@Test 
	public void findVetSpecialtiesById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<VetSpecialtiesEntity>>when(_vetSpecialtiesRepository.findById(any(VetSpecialtiesId.class))).thenReturn(Optional.empty());
		Assertions.assertThat(_vetSpecialtiesManager.findById(vetSpecialtiesId)).isEqualTo(null);
	}
	
	@Test
	public void createVetSpecialties_VetSpecialtiesIsNotNullAndVetSpecialtiesDoesNotExist_StoreVetSpecialties() {

		VetSpecialtiesEntity vetSpecialties =mock(VetSpecialtiesEntity.class);
		Mockito.when(_vetSpecialtiesRepository.save(any(VetSpecialtiesEntity.class))).thenReturn(vetSpecialties);
		Assertions.assertThat(_vetSpecialtiesManager.create(vetSpecialties)).isEqualTo(vetSpecialties);
	}

	@Test
	public void deleteVetSpecialties_VetSpecialtiesExists_RemoveVetSpecialties() {

		VetSpecialtiesEntity vetSpecialties =mock(VetSpecialtiesEntity.class);
		_vetSpecialtiesManager.delete(vetSpecialties);
		verify(_vetSpecialtiesRepository).delete(vetSpecialties);
	}

	@Test
	public void updateVetSpecialties_VetSpecialtiesIsNotNullAndVetSpecialtiesExists_UpdateVetSpecialties() {
		
		VetSpecialtiesEntity vetSpecialties =mock(VetSpecialtiesEntity.class);
		Mockito.when(_vetSpecialtiesRepository.save(any(VetSpecialtiesEntity.class))).thenReturn(vetSpecialties);
		Assertions.assertThat(_vetSpecialtiesManager.update(vetSpecialties)).isEqualTo(vetSpecialties);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<VetSpecialtiesEntity> vetSpecialties = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_vetSpecialtiesRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(vetSpecialties);
		Assertions.assertThat(_vetSpecialtiesManager.findAll(predicate,pageable)).isEqualTo(vetSpecialties);
	}
	
    //Specialties
	@Test
	public void getSpecialties_if_VetSpecialtiesIdIsNotNull_returnSpecialties() {

		VetSpecialtiesEntity vetSpecialties = mock(VetSpecialtiesEntity.class);
		SpecialtiesEntity specialties = mock(SpecialtiesEntity.class);
		
        Optional<VetSpecialtiesEntity> dbVetSpecialties = Optional.of((VetSpecialtiesEntity) vetSpecialties);
		Mockito.<Optional<VetSpecialtiesEntity>>when(_vetSpecialtiesRepository.findById(any(VetSpecialtiesId.class))).thenReturn(dbVetSpecialties);
		Mockito.when(vetSpecialties.getSpecialties()).thenReturn(specialties);
		Assertions.assertThat(_vetSpecialtiesManager.getSpecialties(vetSpecialtiesId)).isEqualTo(specialties);

	}
	
    //Vets
	@Test
	public void getVets_if_VetSpecialtiesIdIsNotNull_returnVets() {

		VetSpecialtiesEntity vetSpecialties = mock(VetSpecialtiesEntity.class);
		VetsEntity vets = mock(VetsEntity.class);
		
        Optional<VetSpecialtiesEntity> dbVetSpecialties = Optional.of((VetSpecialtiesEntity) vetSpecialties);
		Mockito.<Optional<VetSpecialtiesEntity>>when(_vetSpecialtiesRepository.findById(any(VetSpecialtiesId.class))).thenReturn(dbVetSpecialties);
		Mockito.when(vetSpecialties.getVets()).thenReturn(vets);
		Assertions.assertThat(_vetSpecialtiesManager.getVets(vetSpecialtiesId)).isEqualTo(vets);

	}
	
}
