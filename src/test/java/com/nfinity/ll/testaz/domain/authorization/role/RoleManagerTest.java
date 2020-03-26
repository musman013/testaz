package com.nfinity.ll.testaz.domain.authorization.role;

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
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.irepository.IRoleRepository;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
public class RoleManagerTest {
	
	@InjectMocks
	RoleManager roleManager;
	
	@Mock
	IRoleRepository _roleRepository;

	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(roleManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findRoleById_IdIsNotNullAndIdExists_ReturnARole() {
	
		RoleEntity roleEntity = mock(RoleEntity.class);
		Optional<RoleEntity> dbRole = Optional.of((RoleEntity) roleEntity);
		Mockito.<Optional<RoleEntity>>when(_roleRepository.findById(anyLong())).thenReturn(dbRole);
		Assertions.assertThat(roleManager.findById(ID)).isEqualTo(roleEntity);
	}

	@Test 
	public void findRoleById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.<Optional<RoleEntity>>when(_roleRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThat(roleManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findRoleByName_NameIsNotNullAndNameExists_ReturnARole() {
		RoleEntity roleEntity = mock(RoleEntity.class);

		Mockito.when(_roleRepository.findByRoleName(anyString())).thenReturn(roleEntity);
		Assertions.assertThat(roleManager.findByRoleName("Permission1")).isEqualTo(roleEntity);
	}

	@Test 
	public void findRoleByName_NameIsNotNullAndNameDoesNotExist_ReturnNull() {

		Mockito.when(_roleRepository.findByRoleName(anyString())).thenReturn(null);
		Assertions.assertThat(roleManager.findByRoleName("Permission")).isEqualTo(null);
	
	}
	@Test
	public void createRole_RoleIsNotNullAndRoleDoesNotExist_StoreARole() {

		RoleEntity roleEntity = mock(RoleEntity.class);
		Mockito.when(_roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);
		Assertions.assertThat(roleManager.create(roleEntity)).isEqualTo(roleEntity);
	}

	@Test
	public void deleteRole_RoleExists_RemoveARole() {

		RoleEntity roleEntity = mock(RoleEntity.class);
		roleManager.delete(roleEntity);
		verify(_roleRepository).delete(roleEntity);
	}

	@Test
	public void updateRole_RoleIsNotNullAndRoleExists_UpdateARole() {
		
		RoleEntity roleEntity = mock(RoleEntity.class);
		Mockito.when(_roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);
		Assertions.assertThat(roleManager.update(roleEntity)).isEqualTo(roleEntity);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<RoleEntity> role = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_roleRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(role);
		Assertions.assertThat(roleManager.findAll(predicate,pageable)).isEqualTo(role);

	}
	
}

