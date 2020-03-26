package com.nfinity.ll.testaz.domain.vets;

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

import com.nfinity.ll.testaz.domain.model.VetsEntity;
import com.nfinity.ll.testaz.domain.irepository.IVetSpecialtiesRepository;
import com.nfinity.ll.testaz.domain.irepository.IVetsRepository;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class VetsManagerTest {

	@InjectMocks
	VetsManager _vetsManager;
	
	@Mock
	IVetsRepository  _vetsRepository;
    
    @Mock
	IVetSpecialtiesRepository  _vetSpecialtiesRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static Integer ID=15;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_vetsManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findVetsById_IdIsNotNullAndIdExists_ReturnVets() {
		VetsEntity vets =mock(VetsEntity.class);

        Optional<VetsEntity> dbVets = Optional.of((VetsEntity) vets);
		Mockito.<Optional<VetsEntity>>when(_vetsRepository.findById(any(Integer.class))).thenReturn(dbVets);
		Assertions.assertThat(_vetsManager.findById(ID)).isEqualTo(vets);
	}

	@Test 
	public void findVetsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<VetsEntity>>when(_vetsRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
		Assertions.assertThat(_vetsManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void createVets_VetsIsNotNullAndVetsDoesNotExist_StoreVets() {

		VetsEntity vets =mock(VetsEntity.class);
		Mockito.when(_vetsRepository.save(any(VetsEntity.class))).thenReturn(vets);
		Assertions.assertThat(_vetsManager.create(vets)).isEqualTo(vets);
	}

	@Test
	public void deleteVets_VetsExists_RemoveVets() {

		VetsEntity vets =mock(VetsEntity.class);
		_vetsManager.delete(vets);
		verify(_vetsRepository).delete(vets);
	}

	@Test
	public void updateVets_VetsIsNotNullAndVetsExists_UpdateVets() {
		
		VetsEntity vets =mock(VetsEntity.class);
		Mockito.when(_vetsRepository.save(any(VetsEntity.class))).thenReturn(vets);
		Assertions.assertThat(_vetsManager.update(vets)).isEqualTo(vets);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<VetsEntity> vets = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_vetsRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(vets);
		Assertions.assertThat(_vetsManager.findAll(predicate,pageable)).isEqualTo(vets);
	}
	
}
