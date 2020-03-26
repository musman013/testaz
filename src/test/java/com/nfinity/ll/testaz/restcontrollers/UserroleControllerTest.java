package com.nfinity.ll.testaz.restcontrollers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.annotation.PostConstruct;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.nfinity.ll.testaz.security.JWTAppService;
import com.nfinity.ll.testaz.application.authorization.userrole.UserroleAppService;
import com.nfinity.ll.testaz.application.authorization.userrole.dto.CreateUserroleInput;
import com.nfinity.ll.testaz.application.authorization.userrole.dto.FindUserroleByIdOutput;
import com.nfinity.ll.testaz.application.authorization.userrole.dto.UpdateUserroleInput;

import com.nfinity.ll.testaz.application.authorization.user.UserAppService;
import com.nfinity.ll.testaz.application.authorization.user.dto.FindUserByIdOutput;
import com.nfinity.ll.testaz.domain.irepository.IRoleRepository;
import com.nfinity.ll.testaz.domain.irepository.IUserRepository;
import com.nfinity.ll.testaz.domain.irepository.IUserroleRepository;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.model.UserroleEntity;
import com.nfinity.ll.testaz.domain.model.UserroleId;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class UserroleControllerTest {

	private static final Long DEFAULT_ROLE_ID = 1L;
	private static final Long DEFAULT_USER_ID = 1L;

	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IUserroleRepository userroleRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IRoleRepository roleRepository;

	@SpyBean
	private UserAppService userAppService;

	@SpyBean
	private UserroleAppService userroleAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;
	
	@SpyBean
	private JWTAppService jwtAppService;
    
	private UserroleEntity userrole;

	private MockMvc mvc;
	
	@Autowired
	EntityManagerFactory emf;
	
	static EntityManagerFactory emfs;

	@Autowired 
	private CacheManager cacheManager; 

	public void evictAllCaches(){ 
		for(String name : cacheManager.getCacheNames()){
			cacheManager.getCache(name).clear(); 
		} 
	}

    @PostConstruct
	public void init() {
	this.emfs = emf;
	}

	@AfterClass
	public static void cleanup() {
	EntityManager em = emfs.createEntityManager();
	try {
		  em.getTransaction().begin(); 
		  em.createNativeQuery("drop table sample.userrole CASCADE").executeUpdate();
		  em.createNativeQuery("drop table sample.role CASCADE").executeUpdate();
		  em.createNativeQuery("drop table sample.f_user CASCADE").executeUpdate();
		  em.getTransaction().commit();
		} catch(Exception ex) {
		  em.getTransaction().rollback();
		  throw ex;
		}
	}
	
	public UserroleEntity createEntity() {
		UserEntity user=createUserEntity();
		RoleEntity role =createRoleEntity();
		
		if(!userRepository.findAll().contains(user))
		{
			user=userRepository.save(user);
		}

		if(!roleRepository.findAll().contains(role))
		{
			role=roleRepository.save(role);
		}
		
		UserroleEntity userrole = new UserroleEntity();
		userrole.setRoleId(role.getId());
		userrole.setUserId(user.getId());
		userrole.setUser(user);
		userrole.setRole(role);
		

		return userrole;
	}

	public static CreateUserroleInput createUserroleInput() {
		CreateUserroleInput userrole = new CreateUserroleInput();
		userrole.setRoleId(3L);
		userrole.setUserId(3L);
		
		return userrole;
	}

	public static UserEntity createUserEntity() {
		UserEntity user = new UserEntity();
	    user.setId(DEFAULT_USER_ID);
		user.setUserName("u1");
		user.setEmailAddress("u1@g.com");
		user.setFirstName("U1");
		user.setLastName("1");
		user.setIsActive(true);
		user.setPassword("secret");

		return user;
	}

	public static RoleEntity createRoleEntity() {
		RoleEntity role = new RoleEntity();
		role.setDisplayName("D1");
		role.setId(DEFAULT_ROLE_ID);
		role.setName("R1");

		return role;

	}

	public FindUserByIdOutput createUserByIdOuput()
	{
		FindUserByIdOutput user = new FindUserByIdOutput();
		user.setId(4L);
		user.setIsActive(true);
		user.setUserName("u4");
		user.setEmailAddress("u4@gh.com");
		user.setFirstName("U4");
		user.setLastName("4");

		return user;
	}

	public UserroleEntity createNewEntityForDelete() {
		UserEntity user =createUserEntity();
		user.setUserName("u2");
		user.setPassword("secret");
		user.setIsActive(true);
		user.setFirstName("U2");
		user.setEmailAddress("u2@gil.com");
		user.setId(2L);
		user=userRepository.save(user);
		
		RoleEntity role = createRoleEntity();
		role.setName("R2");
		role.setId(2L);
		role.setDisplayName("D2");
		role=roleRepository.save(role);
		
		UserroleEntity userrole = new UserroleEntity();
		userrole.setRoleId(role.getId());
		userrole.setUserId(user.getId());
		userrole.setUser(user);
		userrole.setRole(role);

		return userrole;
	}

	public UserroleEntity createNewEntityForUpdate() {
		
		UserEntity user =createUserEntity();
		user.setUserName("u5");
		user.setFirstName("U5");
		user.setEmailAddress("u5@gma.com");
		user.setId(5L);
		user.setPassword("secret");
		user.setIsActive(true);
		user=userRepository.save(user);
		
		RoleEntity role = createRoleEntity();
		role.setName("R5");
		role.setId(5L);
		role.setDisplayName("D5");
		role=roleRepository.save(role);
		UserroleEntity userrole = new UserroleEntity();
		userrole.setRoleId(role.getId());
		userrole.setUserId(user.getId());
		userrole.setUser(user);
		userrole.setRole(role);

		return userrole;
	}
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		evictAllCaches();
		final UserroleController userroleController = new UserroleController(userroleAppService, userAppService, jwtAppService, logHelper);

		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());


		this.mvc = MockMvcBuilders.standaloneSetup(userroleController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();

	}

	@Before
	public void initTest() {

		userrole= createEntity();

		List<UserroleEntity> list= userroleRepository.findAll();
		System.out.println(list);
		if(!list.contains(userrole))
		{
			userrole=userroleRepository.save(userrole);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/userrole/roleId:"+ userrole.getRoleId() + ",userId:"+ userrole.getUserId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		mvc.perform(get("/userrole/roleId:32,userId:32")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void CreateUserrole_UserDoesNotExists_ThrowEntityNotFoundException() throws Exception {

		CreateUserroleInput userrole = createUserroleInput();
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userrole);
      
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->
		mvc.perform(post("/userrole")
				.contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk())
				).hasCause(new EntityNotFoundException("No record found"));

	}    

	@Test
	public void CreateUserrole_UserroleDoesNotExist_ReturnStatusOk() throws Exception {

		UserEntity user = new UserEntity();
        user.setId(3L);
	    user.setUserName("u3");
	    user.setEmailAddress("u3@g.com");
	    user.setFirstName("U");
	    user.setLastName("3");
	    user.setIsActive(true);
	    user.setPassword("secret");

		user=userRepository.save(user);

		RoleEntity role = new RoleEntity();
		role.setDisplayName("D3");
		role.setId(3L);
		role.setName("R3");
		role=roleRepository.save(role);
	
		CreateUserroleInput userrole = createUserroleInput();
		userrole.setRoleId(role.getId());
		userrole.setUserId(user.getId());
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userrole);

		mvc.perform(post("/userrole").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}  

	@Test
	public void DeleteUserrole_IdIsNotParseable_ThrowEntityNotFoundException() throws Exception {

		doReturn(null).when(userroleAppService).findById(new UserroleId(32L,32L));
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/userrole/21")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=21"));

	}  

	@Test
	public void DeleteUserrole_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

		doReturn(null).when(userroleAppService).findById(new UserroleId(32L,32L));
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/userrole/roleId:32,userId:32")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a userrole with a id=roleId:32,userId:32"));

	}

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {

		UserroleEntity up = userroleRepository.save(createNewEntityForDelete());
		FindUserroleByIdOutput output= new FindUserroleByIdOutput();
	    output.setUserId(up.getUserId());
		output.setRoleId(up.getRoleId());

		doReturn(output).when(userroleAppService).findById(new UserroleId(up.getRoleId(),up.getUserId()));
	
		Mockito.when(userAppService.findById(up.getUserId()
		)).thenReturn(createUserByIdOuput());
		doNothing().when(jwtAppService).deleteAllUserTokens(anyString());

		mvc.perform(delete("/userrole/roleId:"+up.getRoleId() +",userId:" + up.getUserId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());

        userRepository.delete(up.getUser());
	    roleRepository.delete(up.getRole());
	}  

	@Test
	public void UpdateUserrole_IdIsNotParseable_ThrowEntityNotFoundException() throws Exception {

		UpdateUserroleInput userrole = new UpdateUserroleInput();
		userrole.setUserId(21L);
		userrole.setRoleId(21L);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userrole);

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(put("/userrole/21")
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=21"));

	}  

	@Test
	public void UpdateUserrole_IdIsNotValid_ReturnNotFound() throws Exception {
		UpdateUserroleInput userrole = new UpdateUserroleInput();
		userrole.setUserId(32L);
		userrole.setRoleId(32L);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userrole);
		doReturn(null).when(userroleAppService).findById(new UserroleId(32L,32L));
		
		mvc.perform(put("/userrole/roleId:32,userId:32")
     			 .contentType(MediaType.APPLICATION_JSON).content(json))
		  .andExpect(status().isNotFound());
	}

	@Test
	public void UpdateUserrole_UserroleExists_ReturnStatusOk() throws Exception {

		UserroleEntity up = userroleRepository.save(createNewEntityForUpdate());
		FindUserroleByIdOutput output= new FindUserroleByIdOutput();
		output.setUserId(up.getUserId());
		output.setRoleId(up.getRoleId());

		UpdateUserroleInput userrole = new UpdateUserroleInput();
		userrole.setUserId(up.getUserId());
		userrole.setRoleId(up.getRoleId());

		doReturn(output).when(userroleAppService).findById(new UserroleId(up.getRoleId(),up.getUserId()));
		Mockito.when(userAppService.findById(up.getUserId())).thenReturn(createUserByIdOuput());
		doNothing().when(jwtAppService).deleteAllUserTokens(anyString());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userrole);
      

		mvc.perform(put("/userrole/roleId:"+up.getRoleId() + ",userId:" + up.getUserId())
		.contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		UserroleEntity entity= new UserroleEntity();
		entity.setUserId(up.getUserId());
		entity.setRoleId(up.getRoleId());
		userroleRepository.delete(entity);
		userRepository.delete(up.getUser());
		roleRepository.delete(up.getRole());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/userrole?search=roleId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/userrole?search=abcc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abcc not found!"));

	} 

	@Test
	public void GetUser_IdIsNotEmptyAndIdIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/userrole/roleId:33/user")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=roleId:33"));

	}    

	@Test
	public void GetUser_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {

		mvc.perform(get("/userrole/roleId:99,userId:99/user")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void GetUser_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {

		mvc.perform(get("/userrole/roleId:"+ userrole.getRoleId() + ",userId:"+ userrole.getUserId() + "/user")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void GetRole_IdIsNotEmptyAndIdIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/userrole/roleId:33/role")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=roleId:33"));

	}    

	@Test
	public void GetRole_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {

		mvc.perform(get("/userrole/roleId:99,userId:99/role")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void GetRole_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {

		mvc.perform(get("/userrole/roleId:"+ userrole.getRoleId() + ",userId:"+ userrole.getUserId() + "/role")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

}
