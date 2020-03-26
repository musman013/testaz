package com.nfinity.ll.testaz.restcontrollers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
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
import com.nfinity.ll.testaz.application.authorization.role.RoleAppService;
import com.nfinity.ll.testaz.application.authorization.role.dto.CreateRoleInput;
import com.nfinity.ll.testaz.application.authorization.role.dto.FindRoleByIdOutput;
import com.nfinity.ll.testaz.application.authorization.role.dto.FindRoleByNameOutput;
import com.nfinity.ll.testaz.application.authorization.role.dto.UpdateRoleInput;
import com.nfinity.ll.testaz.application.authorization.rolepermission.RolepermissionAppService;
import com.nfinity.ll.testaz.application.authorization.userrole.UserroleAppService;
import com.nfinity.ll.testaz.domain.irepository.IRoleRepository;
import com.nfinity.ll.testaz.domain.model.RoleEntity;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class RoleControllerTest {

	private static final String DEFAULT_ROLE_NAME = "R1";
	private static final String DEFAULT_DISPLAY_NAME = "D1";
	
	@Autowired
    private SortHandlerMethodArgumentResolver sortArgumentResolver;
	
	@Autowired 
	private IRoleRepository role_repository;

	@SpyBean
	private RoleAppService roleAppService;
	
	@SpyBean
	private UserroleAppService userroleAppService;
	
	@SpyBean
	private RolepermissionAppService rolepermissionAppService;
	
	@SpyBean
	private LoggingHelper logHelper;
	
	@Mock
	private Logger loggerMock;
	
	private RoleEntity role;
	
	private MockMvc mvc;
	
	@Autowired 
	private CacheManager cacheManager; 
	
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
		em.createNativeQuery("drop table sample.role CASCADE").executeUpdate();
		em.getTransaction().commit();
	}
	
	public void evictAllCaches(){ 
	    for(String name : cacheManager.getCacheNames()){
	        cacheManager.getCache(name).clear(); 
	    } 
	}
	public static RoleEntity createEntity() {
		RoleEntity role = new RoleEntity();
		role.setName(DEFAULT_ROLE_NAME);
		role.setId(1L);
		role.setDisplayName(DEFAULT_DISPLAY_NAME);

		return role;
	}

	public static CreateRoleInput createRoleInput() {
		CreateRoleInput role = new CreateRoleInput();
		role.setName("newRole");
		role.setDisplayName("newRole");

		return role;
	}
	
	public static RoleEntity createNewEntity() {
		RoleEntity role = new RoleEntity();
		role.setName("R2");
		role.setId(2L);
		role.setDisplayName("D2");

		return role;
	}

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        evictAllCaches();
        final RoleController roleController = new RoleController(roleAppService,logHelper,userroleAppService,rolepermissionAppService);
        
        when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
		
		
        this.mvc = MockMvcBuilders.standaloneSetup(roleController)
        		.setCustomArgumentResolvers(sortArgumentResolver)
        		.setControllerAdvice()
                .build();
        	
    }

	@Before
	public void initTest() {
	
		role= createEntity();
		List<RoleEntity> list= role_repository.findAll();
	    if(!list.contains(role))
		   role=role_repository.save(role);

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
		 mvc.perform(get("/role/" + role.getId())
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

	      mvc.perform(get("/role/15")
	    		  .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	
	}    
	@Test
	public void CreateRole_RoleAlreadyExists_ThrowEntityExistsException() throws Exception {

		FindRoleByNameOutput output= new FindRoleByNameOutput();
	    output.setId(3L);
	    output.setName("R1");
	    output.setDisplayName("D2");
	    doReturn(output).when(roleAppService).findByRoleName(anyString());

	    CreateRoleInput role = createRoleInput();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(role);
       
       org.assertj.core.api.Assertions.assertThatThrownBy(() ->
        mvc.perform(post("/role")
        		.contentType(MediaType.APPLICATION_JSON).content(json))
         .andExpect(status().isOk())
         ).hasCause(new EntityExistsException("There already exists a role with name=" + role.getName()));
      
	}    
	
	@Test
	public void CreateRole_RoleDoesNotExist_ReturnStatusOk() throws Exception {
	
	    doReturn(null).when(roleAppService).findByRoleName(anyString());
		CreateRoleInput role = createRoleInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(role);
      
		 mvc.perform(post("/role").contentType(MediaType.APPLICATION_JSON).content(json))
		  .andExpect(status().isOk());
		 
	
	}  
	
	@Test
	public void DeleteRole_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {
	   
		doReturn(null).when(roleAppService).findById(anyLong());
     	 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/role/21")
     			 .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a role with a id=21"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
		
		Long id = role_repository.save(createNewEntity()).getId();
		
		FindRoleByIdOutput output= new FindRoleByIdOutput();
	    output.setId(id.longValue());
	    output.setName("R2");
	    output.setDisplayName("d2");

	    Mockito.when(roleAppService.findById(id.longValue())).thenReturn(output);
	    
     	 mvc.perform(delete("/role/"+id.toString())
     			 .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isNoContent());
	}  
	
	@Test
	public void UpdateRole_RoleDoesNotExist_ReturnStatusNotFound() throws Exception {
		
		doReturn(null).when(roleAppService).findById(anyLong());
       
        UpdateRoleInput role = new UpdateRoleInput();
        role.setId(21L);
 		role.setName("R116");
 		role.setDisplayName("D299");
 		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(role);
        mvc.perform(put("/role/21").contentType(MediaType.APPLICATION_JSON).content(json))
		  .andExpect(status().isNotFound());
     
	}    
	
	@Test
	public void UpdateRole_RoleExists_ReturnStatusOk() throws Exception {
		Long id = role_repository.save(createNewEntity()).getId();
		FindRoleByIdOutput output= new FindRoleByIdOutput();
	    output.setId(id.longValue());
	    output.setName("R2");
	    output.setDisplayName("d2");
	    doReturn(output).when(roleAppService).findById(anyLong());
        UpdateRoleInput role = new UpdateRoleInput();
        role.setId(id.longValue());
		role.setName("R116");
		role.setDisplayName("D299");
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(role);
        System.out.println("id " +id);
        mvc.perform(put("/role/"+id).contentType(MediaType.APPLICATION_JSON).content(json))
	    .andExpect(status().isOk());

        RoleEntity e= createEntity();
        e.setId(id);
        role_repository.delete(e);
	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
		
		 mvc.perform(get("/role?search=id[equals]=1&limit=10&offset=1")
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}    
	
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
		 
		 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/role?search=roleid[equals]=1&limit=10&offset=1")
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property roleid not found!"));
	
	} 
	
	@Test
	public void GetUserrole_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("roleid", "1");
		Mockito.when(roleAppService.parseUserroleJoinColumn(any(String.class))).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/role/2/userrole?search=roleid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property roleid not found!"));
	
	}    
	
	@Test
	public void GetUserrole_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("roleid", "1");
		Mockito.when(roleAppService.parseUserroleJoinColumn(any(String.class))).thenReturn(joinCol);

		mvc.perform(get("/role/2/userrole?search=roleId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetUserrole_searchIsNotEmpty() throws Exception {
	
		Mockito.when(roleAppService.parseUserroleJoinColumn(any(String.class))).thenReturn(null);
		mvc.perform(get("/role/2/userrole?search=roleid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
	
	@Test
	public void GetRolepermission_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("roleid", "1");
		Mockito.when(roleAppService.parseRolepermissionJoinColumn(any(String.class))).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/role/2/rolepermission?search=roleid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property roleid not found!"));
	
	}    
	
	@Test
	public void GetRolepermission_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("roleid", "1");
		Mockito.when(roleAppService.parseRolepermissionJoinColumn(any(String.class))).thenReturn(joinCol);

		mvc.perform(get("/role/2/rolepermission?search=roleId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetRolepermission_searchIsNotEmpty() throws Exception {
	
		Mockito.when(roleAppService.parseRolepermissionJoinColumn(any(String.class))).thenReturn(null);
		mvc.perform(get("/role/2/rolepermission?search=roleid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    

}
