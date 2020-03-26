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
import java.util.Properties;

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
import com.nfinity.ll.testaz.application.authorization.permission.PermissionAppService;
import com.nfinity.ll.testaz.application.authorization.permission.dto.CreatePermissionInput;
import com.nfinity.ll.testaz.application.authorization.permission.dto.FindPermissionByIdOutput;
import com.nfinity.ll.testaz.application.authorization.permission.dto.FindPermissionByNameOutput;
import com.nfinity.ll.testaz.application.authorization.permission.dto.UpdatePermissionInput;
import com.nfinity.ll.testaz.application.authorization.rolepermission.RolepermissionAppService;
import com.nfinity.ll.testaz.application.authorization.userpermission.UserpermissionAppService;
import com.nfinity.ll.testaz.domain.irepository.IPermissionRepository;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class PermissionControllerTest {

	private static final String DEFAULT_PERMISSION_NAME = "R1";
	private static final String DEFAULT_DISPLAY_NAME = "D1";
	
	@Autowired
    private SortHandlerMethodArgumentResolver sortArgumentResolver;
	
	@Autowired 
	private IPermissionRepository permission_repository;

	@SpyBean
	private PermissionAppService permissionAppService;
	
	@SpyBean
	private UserpermissionAppService userpermissionAppService; 
	
	@SpyBean
	private RolepermissionAppService rolepermissionAppService;
	
	@SpyBean
	private LoggingHelper logHelper;
	
	@Mock
	private Logger loggerMock;
	
	private PermissionEntity permission;
	
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
		em.createNativeQuery("drop table sample.permission CASCADE").executeUpdate();
		em.getTransaction().commit();
	}
	
	public static PermissionEntity createEntity() {
		PermissionEntity permission = new PermissionEntity();
		permission.setName(DEFAULT_PERMISSION_NAME);
		permission.setId(1L);
		permission.setDisplayName(DEFAULT_DISPLAY_NAME);

		return permission;
	}

	public static CreatePermissionInput createPermissionInput() {
		CreatePermissionInput permission = new CreatePermissionInput();
		permission.setName("newPermission");
		permission.setDisplayName("newPermission");

		return permission;
	}
	
	public static PermissionEntity createNewEntity() {
		PermissionEntity permission = new PermissionEntity();
		permission.setName("R2");
		permission.setId(2L);
		permission.setDisplayName("D2");

		return permission;
	}

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        evictAllCaches();
        final PermissionController permissionController = new PermissionController(permissionAppService,logHelper,userpermissionAppService,rolepermissionAppService);
        
        when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
		
		
        this.mvc = MockMvcBuilders.standaloneSetup(permissionController)
        		.setCustomArgumentResolvers(sortArgumentResolver)
        		.setControllerAdvice()
                .build();
        	
    }

	@Before
	public void initTest() {
	
		permission= createEntity();
		List<PermissionEntity> list= permission_repository.findAll();
	    if(!list.contains(permission))
		   permission=permission_repository.save(permission);

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
		 mvc.perform(get("/permission/" + permission.getId())
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

	      mvc.perform(get("/permission/15")
	    		  .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	
	}    

	@Test
	public void CreatePermission_PermissionAlreadyExists_ThrowEntityExistsException() throws Exception {

		FindPermissionByNameOutput output= new FindPermissionByNameOutput();
	    output.setId(3L);
	    output.setName("R1");
	    output.setDisplayName("D2");
	    doReturn(output).when(permissionAppService).findByPermissionName(anyString());

	    CreatePermissionInput permission = createPermissionInput();
	    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(permission);
       
       org.assertj.core.api.Assertions.assertThatThrownBy(() ->
        mvc.perform(post("/permission")
        		.contentType(MediaType.APPLICATION_JSON).content(json))
         .andExpect(status().isOk())
         ).hasCause(new EntityExistsException("There already exists a permission with name=" + permission.getName()));
      
	}    
	
	@Test
	public void CreatePermission_PermissionDoesNotExist_ReturnStatusOk() throws Exception {
	
	    doReturn(null).when(permissionAppService).findByPermissionName(anyString());
		CreatePermissionInput permission = createPermissionInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(permission);
      
		 mvc.perform(post("/permission").contentType(MediaType.APPLICATION_JSON).content(json))
		  .andExpect(status().isOk());
		 
	
	}  
	
	@Test
	public void DeletePermission_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {
	   
		doReturn(null).when(permissionAppService).findById(anyLong());
     	 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/permission/21")
     			 .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a permission with a id=21"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
		
		Long id = permission_repository.save(createNewEntity()).getId();
		
		FindPermissionByIdOutput output= new FindPermissionByIdOutput();
	    output.setId(id.longValue());
	    output.setName("R2");
	    output.setDisplayName("d2");

	    Mockito.when(permissionAppService.findById(id.longValue())).thenReturn(output);
	    
     	 mvc.perform(delete("/permission/"+id.toString())
     			 .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isNoContent());
	}  
	
	@Test
	public void UpdatePermission_PermissionDoesNotExist_ReturnStatusNotFound() throws Exception {
		
		doReturn(null).when(permissionAppService).findById(anyLong());
       
        UpdatePermissionInput permission = new UpdatePermissionInput();
        permission.setId(21L);
 		permission.setName("R116");
 		permission.setDisplayName("D299");
 		
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(permission);
		
        mvc.perform(put("/permission/21").contentType(MediaType.APPLICATION_JSON).content(json))
		  .andExpect(status().isNotFound());
     
	}    
	
	@Test
	public void UpdatePermission_PermissionExists_ReturnStatusOk() throws Exception {
		Long id = permission_repository.save(createNewEntity()).getId();
		FindPermissionByIdOutput output= new FindPermissionByIdOutput();
	    output.setId(id.longValue());
	    output.setName("R2");
	    output.setDisplayName("d2");
	    doReturn(output).when(permissionAppService).findById(anyLong());
        UpdatePermissionInput permission = new UpdatePermissionInput();
        permission.setId(id.longValue());
		permission.setName("R116");
		permission.setDisplayName("D299");
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(permission);
	
        mvc.perform(put("/permission/"+id).contentType(MediaType.APPLICATION_JSON).content(json))
	    .andExpect(status().isOk());

        PermissionEntity e= createEntity();
        e.setId(id);
        permission_repository.delete(e);
	}    
	
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
		
		 mvc.perform(get("/permission?search=id[equals]=1&limit=10&offset=1")
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}    
	
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
		 
		 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/permission?search=permissionid[equals]=1&limit=10&offset=1")
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property permissionid not found!"));
	
	} 
	
	@Test
	public void GetUserpermission_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("permissionid", "1");
		Mockito.when(permissionAppService.parseUserpermissionJoinColumn(any(String.class))).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/permission/2/userpermission?search=permissionid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property permissionid not found!"));
	
	}    
	
	@Test
	public void GetUserpermission_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("permissionid", "1");
		Mockito.when(permissionAppService.parseUserpermissionJoinColumn(any(String.class))).thenReturn(joinCol);

		mvc.perform(get("/permission/2/userpermission?search=permissionId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetUserpermission_searchIsNotEmpty() throws Exception {
	
		Mockito.when(permissionAppService.parseUserpermissionJoinColumn(any(String.class))).thenReturn(null);
		mvc.perform(get("/permission/2/userpermission?search=permissionid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
	
	@Test
	public void GetRolepermission_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("permissionid", "1");
		Mockito.when(permissionAppService.parseRolepermissionJoinColumn(any(String.class))).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/permission/2/rolepermission?search=permissionid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property permissionid not found!"));
	
	}    
	
	@Test
	public void GetRolepermission_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("permissionid", "1");
		Mockito.when(permissionAppService.parseRolepermissionJoinColumn(any(String.class))).thenReturn(joinCol);

		mvc.perform(get("/permission/2/rolepermission?search=permissionId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetRolepermission_searchIsNotEmpty() throws Exception {
	
		Mockito.when(permissionAppService.parseRolepermissionJoinColumn(any(String.class))).thenReturn(null);
		mvc.perform(get("/permission/2/rolepermission?search=permissionid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
	
}
