package com.nfinity.ll.testaz.domain.owners;

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

import com.nfinity.ll.testaz.domain.model.OwnersEntity;
import com.nfinity.ll.testaz.domain.irepository.IPetsRepository;
import com.nfinity.ll.testaz.domain.irepository.IOwnersRepository;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class OwnersManagerTest {

	@InjectMocks
	OwnersManager _ownersManager;
	
	@Mock
	IOwnersRepository  _ownersRepository;
    
    @Mock
	IPetsRepository  _petsRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static Integer ID=15;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_ownersManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findOwnersById_IdIsNotNullAndIdExists_ReturnOwners() {
		OwnersEntity owners =mock(OwnersEntity.class);

        Optional<OwnersEntity> dbOwners = Optional.of((OwnersEntity) owners);
		Mockito.<Optional<OwnersEntity>>when(_ownersRepository.findById(any(Integer.class))).thenReturn(dbOwners);
		Assertions.assertThat(_ownersManager.findById(ID)).isEqualTo(owners);
	}

	@Test 
	public void findOwnersById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<OwnersEntity>>when(_ownersRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
		Assertions.assertThat(_ownersManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void createOwners_OwnersIsNotNullAndOwnersDoesNotExist_StoreOwners() {

		OwnersEntity owners =mock(OwnersEntity.class);
		Mockito.when(_ownersRepository.save(any(OwnersEntity.class))).thenReturn(owners);
		Assertions.assertThat(_ownersManager.create(owners)).isEqualTo(owners);
	}

	@Test
	public void deleteOwners_OwnersExists_RemoveOwners() {

		OwnersEntity owners =mock(OwnersEntity.class);
		_ownersManager.delete(owners);
		verify(_ownersRepository).delete(owners);
	}

	@Test
	public void updateOwners_OwnersIsNotNullAndOwnersExists_UpdateOwners() {
		
		OwnersEntity owners =mock(OwnersEntity.class);
		Mockito.when(_ownersRepository.save(any(OwnersEntity.class))).thenReturn(owners);
		Assertions.assertThat(_ownersManager.update(owners)).isEqualTo(owners);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<OwnersEntity> owners = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_ownersRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(owners);
		Assertions.assertThat(_ownersManager.findAll(predicate,pageable)).isEqualTo(owners);
	}
	
}
