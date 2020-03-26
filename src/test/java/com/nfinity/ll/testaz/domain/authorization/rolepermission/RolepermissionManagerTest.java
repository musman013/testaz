package com.nfinity.ll.testaz.domain.authorization.rolepermission;

import static org.mockito.ArgumentMatchers.any;
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

import com.nfinity.ll.testaz.domain.model.RolepermissionEntity;
import com.nfinity.ll.testaz.domain.model.RolepermissionId;
import com.nfinity.ll.testaz.domain.irepository.IPermissionRepository;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.domain.irepository.IRoleRepository;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.irepository.IRolepermissionRepository;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class RolepermissionManagerTest {
	
	@InjectMocks
	RolepermissionManager _rolepermissionManager;
	
	@Mock
	IRolepermissionRepository  _rolepermissionRepository;
    
    @Mock
	IPermissionRepository  _permissionRepository;
    
    @Mock
    RolepermissionId rolepermissionId;
    
    @Mock
	IRoleRepository  _roleRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_rolepermissionManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findRolepermissionById_IdIsNotNullAndIdExists_ReturnRolepermission() {
		RolepermissionEntity rolepermission =mock(RolepermissionEntity.class);
		
		Optional<RolepermissionEntity> dbRolepermission = Optional.of((RolepermissionEntity) rolepermission);
		Mockito.<Optional<RolepermissionEntity>>when(_rolepermissionRepository.findById(any(RolepermissionId.class))).thenReturn(dbRolepermission);

		Assertions.assertThat(_rolepermissionManager.findById(rolepermissionId)).isEqualTo(rolepermission);
	}

	@Test 
	public void findRolepermissionById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
    
	Mockito.<Optional<RolepermissionEntity>>when(_rolepermissionRepository.findById(any(RolepermissionId.class))).thenReturn(Optional.empty());
	Assertions.assertThat(_rolepermissionManager.findById(rolepermissionId)).isEqualTo(null);
	}
	
	@Test
	public void createRolepermission_RolepermissionIsNotNullAndRolepermissionDoesNotExist_StoreRolepermission() {

		RolepermissionEntity rolepermission =mock(RolepermissionEntity.class);
		Mockito.when(_rolepermissionRepository.save(any(RolepermissionEntity.class))).thenReturn(rolepermission);
		Assertions.assertThat(_rolepermissionManager.create(rolepermission)).isEqualTo(rolepermission);
	}

	@Test
	public void deleteRolepermission_RolepermissionExists_RemoveRolepermission() {

		RolepermissionEntity rolepermission =mock(RolepermissionEntity.class);
		_rolepermissionManager.delete(rolepermission);
		verify(_rolepermissionRepository).delete(rolepermission);
	}

	@Test
	public void updateRolepermission_RolepermissionIsNotNullAndRolepermissionExists_UpdateRolepermission() {
		
		RolepermissionEntity rolepermission =mock(RolepermissionEntity.class);
		Mockito.when(_rolepermissionRepository.save(any(RolepermissionEntity.class))).thenReturn(rolepermission);
		Assertions.assertThat(_rolepermissionManager.update(rolepermission)).isEqualTo(rolepermission);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<RolepermissionEntity> rolepermission = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_rolepermissionRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(rolepermission);
		Assertions.assertThat(_rolepermissionManager.findAll(predicate,pageable)).isEqualTo(rolepermission);
	}
	
   //Permission
	@Test
	public void getPermission_ifRolepermissionIdIsNotNull_returnPermission() {

		RolepermissionEntity rolepermission = mock(RolepermissionEntity.class);
		PermissionEntity permission = mock(PermissionEntity.class);
		
		Optional<RolepermissionEntity> dbRolepermission = Optional.of((RolepermissionEntity) rolepermission);
		Mockito.<Optional<RolepermissionEntity>>when(_rolepermissionRepository.findById(any(RolepermissionId.class))).thenReturn(dbRolepermission);
	
		Mockito.when(rolepermission.getPermission()).thenReturn(permission);
		Assertions.assertThat(_rolepermissionManager.getPermission(rolepermissionId)).isEqualTo(permission);

	}
	
   //Role
	@Test
	public void getRole_ifRolepermissionIdIsNotNull_returnRole() {

		RolepermissionEntity rolepermission = mock(RolepermissionEntity.class);
		RoleEntity role = mock(RoleEntity.class);
		
		Optional<RolepermissionEntity> dbRolepermission = Optional.of((RolepermissionEntity) rolepermission);
		Mockito.<Optional<RolepermissionEntity>>when(_rolepermissionRepository.findById(any(RolepermissionId.class))).thenReturn(dbRolepermission);
		
		Mockito.when(rolepermission.getRole()).thenReturn(role);
		Assertions.assertThat(_rolepermissionManager.getRole(rolepermissionId)).isEqualTo(role);

	}
	
}
