package com.nfinity.ll.testaz.restcontrollers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import java.util.Date;

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
import com.nfinity.ll.testaz.application.authorization.userpermission.UserpermissionAppService;
import com.nfinity.ll.testaz.application.authorization.userpermission.dto.CreateUserpermissionInput;
import com.nfinity.ll.testaz.application.authorization.userpermission.dto.FindUserpermissionByIdOutput;
import com.nfinity.ll.testaz.application.authorization.userpermission.dto.UpdateUserpermissionInput;
import com.nfinity.ll.testaz.application.authorization.rolepermission.RolepermissionAppService;
import com.nfinity.ll.testaz.application.authorization.user.UserAppService;
import com.nfinity.ll.testaz.application.authorization.user.dto.FindUserByIdOutput;
import com.nfinity.ll.testaz.application.authorization.userrole.UserroleAppService;
import com.nfinity.ll.testaz.domain.irepository.IPermissionRepository;
import com.nfinity.ll.testaz.domain.irepository.IUserRepository;
import com.nfinity.ll.testaz.domain.irepository.IUserpermissionRepository;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.security.JWTAppService;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.model.UserpermissionEntity;
import com.nfinity.ll.testaz.domain.model.UserpermissionId;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class UserpermissionControllerTest {

	private static final Long DEFAULT_PERMISSION_ID = 1L;
	private static final Long DEFAULT_USER_ID = 1L;
	private static final Boolean DEFAULT_REVOKED=true;
	
	@Autowired
    private SortHandlerMethodArgumentResolver sortArgumentResolver;
	
	@Autowired 
	private IUserpermissionRepository userpermissionRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IPermissionRepository permissionRepository;

	@SpyBean
	private UserAppService userAppService;
	
	@SpyBean
	private UserpermissionAppService userpermissionAppService;
	
	@SpyBean
	private UserroleAppService userroleAppService;
	
	@SpyBean
	private RolepermissionAppService rolepermissionAppService;
	
	@SpyBean
	private LoggingHelper logHelper;
	
	@SpyBean
	private JWTAppService jwtAppService;
	
	@Mock
	private Logger loggerMock;
	
	private UserpermissionEntity userpermission;
	
	private MockMvc mvc;
	
	@Autowired 
	private CacheManager cacheManager; 
	
	public void evictAllCaches(){ 
	    for(String name : cacheManager.getCacheNames()){
	        cacheManager.getCache(name).clear(); 
	    } 
	}
    
    @Autowired
	EntityManagerFactory emf;
	
    static EntityManagerFactory emfs;
	
	@PostConstruct
	public void init() {
	this.emfs = emf;
	}

	@AfterClass
	public static void cleanup() {
		EntityManager em = emfs.createEntityManager();
		em.getTransaction().begin();
		
		em.createNativeQuery("drop table sample.userpermission CASCADE").executeUpdate();
		em.createNativeQuery("drop table sample.permission CASCADE").executeUpdate();
		em.createNativeQuery("drop table sample.f_user CASCADE").executeUpdate();
		em.getTransaction().commit();
	}
    
	public UserpermissionEntity createEntity() {
		UserEntity user=createUserEntity();
		PermissionEntity permission =createPermissionEntity();
		
		if(!userRepository.findAll().contains(user))
		{
		user=userRepository.save(user);
		}

		if(!permissionRepository.findAll().contains(permission))
		{
			permission=permissionRepository.save(permission);
		}
		
		UserpermissionEntity userpermission = new UserpermissionEntity();
		userpermission.setPermissionId(permission.getId());
		userpermission.setUserId(user.getId());

		userpermission.setRevoked(DEFAULT_REVOKED);
		userpermission.setUser(user);
		userpermission.setPermission(permission);
		return userpermission;
	}

	public static CreateUserpermissionInput createUserpermissionInput() {
		CreateUserpermissionInput userpermission = new CreateUserpermissionInput();
		userpermission.setPermissionId(3L);
		userpermission.setUserId(3L);
		userpermission.setRevoked(DEFAULT_REVOKED);
		
		return userpermission;
	}
	
	public static UserEntity createUserEntity() {
		UserEntity user = new UserEntity();
		user.setId(DEFAULT_USER_ID);
	    user.setUserName("u1");
	    user.setEmailAddress("u1@g.com");
	    user.setFirstName("U");
	    user.setLastName("1");
	    user.setPassword("secret");
	    user.setIsActive(true);
		
		return user;
	}
	
	public static PermissionEntity createPermissionEntity() {
		PermissionEntity permission = new PermissionEntity();
	    permission.setDisplayName("D1");
	    permission.setId(DEFAULT_PERMISSION_ID);
	    permission.setName("P1");
	    
	    return permission;
	}
	
	public FindUserByIdOutput createUserByIdOuput()
	{
		FindUserByIdOutput user = new FindUserByIdOutput();
	
	    user.setId(1L);
	    user.setIsActive(true);
	    user.setUserName("u1");
	    user.setEmailAddress("u1@g.com");
	    user.setFirstName("U");
	    user.setLastName("1");
	  
		return user;
	}
	
	public UserpermissionEntity createNewEntityForDelete() {
		UserEntity user =createUserEntity();
		user.setUserName("u2");
		user.setPassword("secret");
		user.setIsActive(true);
		user.setFirstName("U2");
		user.setEmailAddress("u2@gil.com");
		user.setId(2L);
		user=userRepository.save(user);
		
		PermissionEntity permission = createPermissionEntity();
		permission.setDisplayName("D2");
		permission.setName("P2");
		permission.setId(2L);
		permission=permissionRepository.save(permission);
		
		UserpermissionEntity userpermission = new UserpermissionEntity();
		userpermission.setPermissionId(permission.getId());
		userpermission.setUserId(user.getId());
		userpermission.setUser(user);
		userpermission.setPermission(permission);

		return userpermission;
	}

	public UserpermissionEntity createNewEntityForUpdate() {
		
		UserEntity user =createUserEntity();
		user.setUserName("u5");
		user.setFirstName("U5");
		user.setEmailAddress("u5@gma.com");
		user.setId(5L);
		user.setIsActive(true);
		user.setPassword("secret");
		user=userRepository.save(user);
		
		PermissionEntity permission = createPermissionEntity();
		permission.setName("P5");
		permission.setDisplayName("D5");
		permission.setId(5L);
		permission=permissionRepository.save(permission);
		
		UserpermissionEntity userpermission = new UserpermissionEntity();
		userpermission.setPermissionId(permission.getId());
		userpermission.setUserId(user.getId());
		userpermission.setUser(user);
		userpermission.setPermission(permission);

		return userpermission;
	}
	

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        evictAllCaches();
        final UserpermissionController userpermissionController = new UserpermissionController(userpermissionAppService, userAppService, jwtAppService, logHelper);
        
        when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
		
        this.mvc = MockMvcBuilders.standaloneSetup(userpermissionController)
        		.setCustomArgumentResolvers(sortArgumentResolver)
        		.setControllerAdvice()
                .build();
        	
    }

	@Before
	public void initTest() {
	
		userpermission= createEntity();
		
		List<UserpermissionEntity> list= userpermissionRepository.findAll();
		System.out.println(list);
	    if(!list.contains(userpermission)){
	    	userpermission=userpermissionRepository.save(userpermission);
		}
	}
	
	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {

		 mvc.perform(get("/userpermission/permissionId:"+ userpermission.getPermissionId() + ",userId:"+ userpermission.getUserId())
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

	      mvc.perform(get("/userpermission/permissionId:32,userId:32")
	    		  .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
	
	@Test
	public void CreateUserpermission_UserDoesNotExists_ThrowEntityNotFoundException() throws Exception {
       
		CreateUserpermissionInput userpermission = createUserpermissionInput();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userpermission);
      
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
        mvc.perform(post("/userpermission")
        		.contentType(MediaType.APPLICATION_JSON).content(json))
         .andExpect(status().isOk())
         ).hasCause(new EntityNotFoundException("No record found"));
      
	}    
	
	@Test
	public void CreateUserpermission_UserpermissionDoesNotExist_ReturnStatusOk() throws Exception {
	
		UserEntity user = new UserEntity();
        user.setId(3L);
	    user.setUserName("u3");
	    user.setEmailAddress("u3@g.com");
	    user.setFirstName("U");
	    user.setLastName("3");
	    user.setPassword("secret");
	    user.setIsActive(true);
	    user=userRepository.save(user);
	    
	    PermissionEntity permission = new PermissionEntity();
	    permission.setDisplayName("D3");
	    permission.setId(3L);
	    permission.setName("P3");
	    permission=permissionRepository.save(permission);
        
		CreateUserpermissionInput userpermission = createUserpermissionInput();
		userpermission.setPermissionId(permission.getId());
		userpermission.setRevoked(false);
		userpermission.setUserId(user.getId());
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userpermission);
      
		 mvc.perform(post("/userpermission").contentType(MediaType.APPLICATION_JSON).content(json))
		  .andExpect(status().isOk());
	
	}  
	
	@Test
	public void DeleteUserpermission_IdIsNotParseable_ThrowEntityNotFoundException() throws Exception {
	   
	   doReturn(null).when(userpermissionAppService).findById(new UserpermissionId(32L, 32L));
     	 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/userpermission/21")
     			 .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=21"));

	}  
	
	@Test
	public void DeleteUserpermission_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {
	   
		doReturn(null).when(userpermissionAppService).findById(new UserpermissionId(32L, 32L));
		
     	 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/userpermission/permissionId:32,userId:32")
     			 .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a userpermission with a id=permissionId:32,userId:32"));

	}

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
		
		UserpermissionEntity up = userpermissionRepository.save(createNewEntityForDelete());
        
		FindUserpermissionByIdOutput output= new FindUserpermissionByIdOutput();
	    output.setPermissionId(up.getPermissionId());
	    output.setUserId(up.getUserId());
	   
	     doReturn(output).when(userpermissionAppService).findById(new UserpermissionId(up.getPermissionId(),up.getUserId()));
	     Mockito.when(userAppService.findById(up.getUserId())).thenReturn(createUserByIdOuput());
	     doNothing().when(jwtAppService).deleteAllUserTokens(anyString());
	     
	     mvc.perform(delete("/userpermission/permissionId:"+up.getPermissionId() + ",userId:" + up.getUserId())
     			 .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isNoContent());
		  
		  userRepository.delete(up.getUser());
	      permissionRepository.delete(up.getPermission());
     	 
	}  
	
	@Test
	public void UpdateUserpermission_IdIsNotParseable_ThrowEntityNotFoundException() throws Exception {
	   
		UpdateUserpermissionInput userpermission = new UpdateUserpermissionInput();
		userpermission.setUserId(21L);
	    userpermission.setPermissionId(21L);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userpermission);
		
    	 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(put("/userpermission/21")
    			 .contentType(MediaType.APPLICATION_JSON).content(json))
		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=21"));

	}  
	
	@Test
	public void UpdateUserpermission_IdIsNotValid_ReturnNotFound() throws Exception {
		UpdateUserpermissionInput userpermission = new UpdateUserpermissionInput();
		userpermission.setUserId(32L);
	    userpermission.setPermissionId(32L);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userpermission);
		doReturn(null).when(userpermissionAppService).findById(new UserpermissionId(32L,32L));
		 
     	mvc.perform(put("/userpermission/permissionId:32,userId:32")
     			 .contentType(MediaType.APPLICATION_JSON).content(json))
		  .andExpect(status().isNotFound());

	}
	
	@Test
	public void UpdateUserpermission_UserpermissionExists_ReturnStatusOk() throws Exception {
		
		UserpermissionEntity up = userpermissionRepository.save(createNewEntityForUpdate());
        
		FindUserpermissionByIdOutput output= new FindUserpermissionByIdOutput();
		output.setUserId(up.getUserId());
	    output.setPermissionId(up.getPermissionId());

        UpdateUserpermissionInput userpermission = new UpdateUserpermissionInput();
		userpermission.setUserId(up.getUserId());
		userpermission.setPermissionId(up.getPermissionId());
	
		doReturn(output).when(userpermissionAppService).findById(new UserpermissionId(up.getPermissionId(),up.getUserId()));
    	Mockito.when(userAppService.findById(up.getUserId())).thenReturn(createUserByIdOuput());
     	doNothing().when(jwtAppService).deleteAllUserTokens(anyString());
    	
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userpermission);
     
        mvc.perform(put("/userpermission/permissionId:"+up.getPermissionId() + ",userId:" + up.getUserId())
        .contentType(MediaType.APPLICATION_JSON).content(json))
	    .andExpect(status().isOk());

        UserpermissionEntity entity= new UserpermissionEntity();
		entity.setUserId(up.getUserId());
        entity.setPermissionId(up.getPermissionId());
        userpermissionRepository.delete(entity);
        userRepository.delete(up.getUser());
		permissionRepository.delete(up.getPermission());
	}    
	
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
		
		 mvc.perform(get("/userpermission?search=permissionId[equals]=1&limit=10&offset=1")
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}    
	
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
		 
		 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/userpermission?search=abcc[equals]=1&limit=10&offset=1")
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abcc not found!"));
	
	} 
	
	@Test
	public void GetUser_IdIsNotEmptyAndIdIsNotValid_ThrowException() throws Exception {
	
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/userpermission/permissionId:33/user")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=permissionId:33"));
	
	}    
	
	@Test
	public void GetUser_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
	
	    mvc.perform(get("/userpermission/permissionId:99,userId:99/user")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	
	}    
	
	@Test
	public void GetUser_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/userpermission/permissionId:"+ userpermission.getPermissionId() + ",userId:"+ userpermission.getUserId()+ "/user")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetPermission_IdIsNotEmptyAndIdIsNotValid_ThrowException() throws Exception {
	
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/userpermission/permissionId:33/permission")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=permissionId:33"));
	
	}    
	
	@Test
	public void GetPermission_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
	
	    mvc.perform(get("/userpermission/permissionId:99,userId:99/permission")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	
	}    
	
	@Test
	public void GetPermission_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/userpermission/permissionId:"+ userpermission.getPermissionId() + ",userId:"+ userpermission.getUserId()+ "/permission")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
}

