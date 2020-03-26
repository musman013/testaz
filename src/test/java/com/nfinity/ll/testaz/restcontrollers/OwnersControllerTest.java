package com.nfinity.ll.testaz.restcontrollers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

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
import com.nfinity.ll.testaz.application.owners.OwnersAppService;
import com.nfinity.ll.testaz.application.owners.dto.*;
import com.nfinity.ll.testaz.domain.irepository.IOwnersRepository;
import com.nfinity.ll.testaz.domain.model.OwnersEntity;
import com.nfinity.ll.testaz.application.pets.PetsAppService;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class OwnersControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IOwnersRepository owners_repository;
	
	@SpyBean
	private OwnersAppService ownersAppService;
    
    @SpyBean
	private PetsAppService petsAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private OwnersEntity owners;

	private MockMvc mvc;
	
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
		em.createNativeQuery("drop table sample.owners CASCADE").executeUpdate();
		em.getTransaction().commit();
	}

	@Autowired 
	private CacheManager cacheManager; 
	
	public void evictAllCaches(){ 
		for(String name : cacheManager.getCacheNames()) {
			cacheManager.getCache(name).clear(); 
		} 
	}

	public OwnersEntity createEntity() {
	
		OwnersEntity owners = new OwnersEntity();
  		owners.setAddress("1");
  		owners.setCity("1");
  		owners.setFirstName("1");
		owners.setId(1);
  		owners.setLastName("1");
  		owners.setTelephone("1");
		
		return owners;
	}

	public CreateOwnersInput createOwnersInput() {
	
	    CreateOwnersInput owners = new CreateOwnersInput();
  		owners.setAddress("2");
  		owners.setCity("2");
  		owners.setFirstName("2");
  		owners.setLastName("2");
  		owners.setTelephone("2");
	    
		
		
		return owners;
	}

	public OwnersEntity createNewEntity() {
		OwnersEntity owners = new OwnersEntity();
  		owners.setAddress("3");
  		owners.setCity("3");
  		owners.setFirstName("3");
		owners.setId(3);
  		owners.setLastName("3");
  		owners.setTelephone("3");
		return owners;
	}
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		evictAllCaches();
		final OwnersController ownersController = new OwnersController(ownersAppService,petsAppService,
	logHelper);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(ownersController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		owners= createEntity();
		List<OwnersEntity> list= owners_repository.findAll();
		if(!list.contains(owners)) {
			owners=owners_repository.save(owners);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/owners/" + owners.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		mvc.perform(get("/owners/111")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    
	@Test
	public void CreateOwners_OwnersDoesNotExist_ReturnStatusOk() throws Exception {
		CreateOwnersInput owners = createOwnersInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(owners);

		mvc.perform(post("/owners").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeleteOwners_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(ownersAppService).findById(111);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/owners/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a owners with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 OwnersEntity entity =  createNewEntity();
		
		entity = owners_repository.save(entity);

		FindOwnersByIdOutput output= new FindOwnersByIdOutput();
  		output.setId(entity.getId());
        Mockito.when(ownersAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/owners/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateOwners_OwnersDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(ownersAppService).findById(111);

		UpdateOwnersInput owners = new UpdateOwnersInput();
  		owners.setAddress("111");
  		owners.setCity("111");
  		owners.setFirstName("111");
		owners.setId(111);
  		owners.setLastName("111");
  		owners.setTelephone("111");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(owners);
		mvc.perform(put("/owners/111").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void UpdateOwners_OwnersExists_ReturnStatusOk() throws Exception {
		OwnersEntity entity =  createNewEntity();
		entity = owners_repository.save(entity);
		FindOwnersByIdOutput output= new FindOwnersByIdOutput();
  		output.setAddress(entity.getAddress());
  		output.setCity(entity.getCity());
  		output.setFirstName(entity.getFirstName());
  		output.setId(entity.getId());
  		output.setLastName(entity.getLastName());
  		output.setTelephone(entity.getTelephone());
        Mockito.when(ownersAppService.findById(entity.getId())).thenReturn(output);
        
		UpdateOwnersInput owners = new UpdateOwnersInput();
  		owners.setId(entity.getId());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(owners);
	
		mvc.perform(put("/owners/" + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		OwnersEntity de = createEntity();
		de.setId(entity.getId());
		owners_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/owners?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/owners?search=ownersid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property ownersid not found!"));

	} 
	
	@Test
	public void GetPets_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("ownerId", "1");

		Mockito.when(ownersAppService.parsePetsJoinColumn("ownerId")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/owners/1/pets?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetPets_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("ownerId", "1");
		
        Mockito.when(ownersAppService.parsePetsJoinColumn("ownerId")).thenReturn(joinCol);
		mvc.perform(get("/owners/1/pets?search=ownerId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetPets_searchIsNotEmpty() throws Exception {
	
		Mockito.when(ownersAppService.parsePetsJoinColumn(anyString())).thenReturn(null);
		mvc.perform(get("/owners/1/pets?search=ownerid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
    

}
