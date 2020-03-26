package com.nfinity.ll.testaz.domain.authorization.user;

import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.irepository.IRoleRepository;
import com.nfinity.ll.testaz.domain.irepository.IUserRepository;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;
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

import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
public class UserManagerTest {

	@InjectMocks
	UserManager userManager;

	@Mock
	IUserRepository _userRepository;

	@Mock
	IRoleRepository _roleRepository;

	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(userManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findUserById_IdIsNotNullAndIdExists_ReturnAUser() {
		UserEntity userEntity = mock(UserEntity.class);
		Optional<UserEntity> dbUser = Optional.of((UserEntity) userEntity);
		Mockito.<Optional<UserEntity>>when(_userRepository.findById(anyLong())).thenReturn(dbUser);
		Assertions.assertThat(userManager.findById(ID)).isEqualTo(userEntity);
	}

	@Test 
	public void findUserById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.<Optional<UserEntity>>when(_userRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThat(userManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findUserByName_NameIsNotNullAndNameExists_ReturnAUser() {
		UserEntity userEntity = mock(UserEntity.class);

		Mockito.when(_userRepository.findByUserName(anyString())).thenReturn(userEntity);
		Assertions.assertThat(userManager.findByUserName("User1")).isEqualTo(userEntity);
	}

	@Test 
	public void findUserByName_NameIsNotNullAndNameDoesNotExist_ReturnNull() {

		Mockito.when(_userRepository.findByUserName(anyString())).thenReturn(null);
		Assertions.assertThat(userManager.findByUserName("User1")).isEqualTo(null);
	
	}
	@Test
	public void createUser_UserIsNotNullAndUserDoesNotExist_StoreAUser() {

		UserEntity userEntity = mock(UserEntity.class);
		Mockito.when(_userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		Assertions.assertThat(userManager.create(userEntity)).isEqualTo(userEntity);
	}

	@Test
	public void deleteUser_UserExists_RemoveAUser() {

		UserEntity userEntity = mock(UserEntity.class);
		userManager.delete(userEntity);
		verify(_userRepository).delete(userEntity);
	}

	@Test
	public void updateUser_UserIsNotNullAndUserExists_UpdateAUser() {
		
		UserEntity userEntity = mock(UserEntity.class);
		Mockito.when(_userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		Assertions.assertThat(userManager.update(userEntity)).isEqualTo(userEntity);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<UserEntity> user = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_userRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(user);
		Assertions.assertThat(userManager.findAll(predicate,pageable)).isEqualTo(user);

	}

	
}