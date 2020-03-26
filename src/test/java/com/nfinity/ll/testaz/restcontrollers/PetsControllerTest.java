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
import com.nfinity.ll.testaz.application.pets.PetsAppService;
import com.nfinity.ll.testaz.application.pets.dto.*;
import com.nfinity.ll.testaz.domain.irepository.IPetsRepository;
import com.nfinity.ll.testaz.domain.model.PetsEntity;
import com.nfinity.ll.testaz.domain.irepository.ITypesRepository;
import com.nfinity.ll.testaz.domain.model.TypesEntity;
import com.nfinity.ll.testaz.domain.irepository.IOwnersRepository;
import com.nfinity.ll.testaz.domain.model.OwnersEntity;
import com.nfinity.ll.testaz.application.visits.VisitsAppService;    
import com.nfinity.ll.testaz.application.types.TypesAppService;    
import com.nfinity.ll.testaz.application.owners.OwnersAppService;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class PetsControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IPetsRepository pets_repository;
	
	@Autowired 
	private ITypesRepository typesRepository;
	
	@Autowired 
	private IOwnersRepository ownersRepository;
	
	@SpyBean
	private PetsAppService petsAppService;
    
    @SpyBean
	private VisitsAppService visitsAppService;
    
    @SpyBean
	private TypesAppService typesAppService;
    
    @SpyBean
	private OwnersAppService ownersAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private PetsEntity pets;

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
		em.createNativeQuery("drop table sample.pets CASCADE").executeUpdate();
		em.createNativeQuery("drop table sample.types CASCADE").executeUpdate();
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

	public PetsEntity createEntity() {
		TypesEntity types = createTypesEntity();
		if(!typesRepository.findAll().contains(types))
		{
			types=typesRepository.save(types);
		}
		OwnersEntity owners = createOwnersEntity();
		if(!ownersRepository.findAll().contains(owners))
		{
			owners=ownersRepository.save(owners);
		}
	
		PetsEntity pets = new PetsEntity();
		pets.setBirthDate(new Date());
		pets.setId(1);
  		pets.setName("1");
		pets.setTypes(types);
		pets.setOwners(owners);
		
		return pets;
	}

	public CreatePetsInput createPetsInput() {
	
	    CreatePetsInput pets = new CreatePetsInput();
		pets.setBirthDate(new Date());
  		pets.setName("2");
	    
		
		TypesEntity types = new TypesEntity();
		types.setId(2);
  		types.setName("2");
		types=typesRepository.save(types);
		pets.setTypeId(types.getId());
		
		OwnersEntity owners = new OwnersEntity();
  		owners.setAddress("2");
  		owners.setCity("2");
  		owners.setFirstName("2");
		owners.setId(2);
  		owners.setLastName("2");
  		owners.setTelephone("2");
		owners=ownersRepository.save(owners);
		pets.setOwnerId(owners.getId());
		
		
		return pets;
	}

	public PetsEntity createNewEntity() {
		PetsEntity pets = new PetsEntity();
		pets.setBirthDate(new Date());
		pets.setId(3);
  		pets.setName("3");
		return pets;
	}
	
	public TypesEntity createTypesEntity() {
		TypesEntity types = new TypesEntity();
		types.setId(1);
  		types.setName("1");
		return types;
		 
	}
	public OwnersEntity createOwnersEntity() {
		OwnersEntity owners = new OwnersEntity();
  		owners.setAddress("1");
  		owners.setCity("1");
  		owners.setFirstName("1");
		owners.setId(1);
  		owners.setLastName("1");
  		owners.setTelephone("1");
		return owners;
		 
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		evictAllCaches();
		final PetsController petsController = new PetsController(petsAppService,visitsAppService,typesAppService,ownersAppService,
	logHelper);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(petsController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		pets= createEntity();
		List<PetsEntity> list= pets_repository.findAll();
		if(!list.contains(pets)) {
			pets=pets_repository.save(pets);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/pets/" + pets.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		mvc.perform(get("/pets/111")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    
	@Test
	public void CreatePets_PetsDoesNotExist_ReturnStatusOk() throws Exception {
		CreatePetsInput pets = createPetsInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pets);

		mvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeletePets_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(petsAppService).findById(111);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/pets/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a pets with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 PetsEntity entity =  createNewEntity();
		TypesEntity types = new TypesEntity();
		types.setId(3);
  		types.setName("3");
		types=typesRepository.save(types);
		
		entity.setTypes(types);
		OwnersEntity owners = new OwnersEntity();
  		owners.setAddress("3");
  		owners.setCity("3");
  		owners.setFirstName("3");
		owners.setId(3);
  		owners.setLastName("3");
  		owners.setTelephone("3");
		owners=ownersRepository.save(owners);
		
		entity.setOwners(owners);
		
		entity = pets_repository.save(entity);

		FindPetsByIdOutput output= new FindPetsByIdOutput();
  		output.setId(entity.getId());
        Mockito.when(petsAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/pets/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdatePets_PetsDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(petsAppService).findById(111);

		UpdatePetsInput pets = new UpdatePetsInput();
		pets.setBirthDate(new Date());
		pets.setId(111);
  		pets.setName("111");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pets);
		mvc.perform(put("/pets/111").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void UpdatePets_PetsExists_ReturnStatusOk() throws Exception {
		PetsEntity entity =  createNewEntity();
		TypesEntity types = new TypesEntity();
		types.setId(5);
  		types.setName("5");
		types=typesRepository.save(types);
		entity.setTypes(types);
		OwnersEntity owners = new OwnersEntity();
  		owners.setAddress("5");
  		owners.setCity("5");
  		owners.setFirstName("5");
		owners.setId(5);
  		owners.setLastName("5");
  		owners.setTelephone("5");
		owners=ownersRepository.save(owners);
		entity.setOwners(owners);
		entity = pets_repository.save(entity);
		FindPetsByIdOutput output= new FindPetsByIdOutput();
  		output.setBirthDate(entity.getBirthDate());
  		output.setId(entity.getId());
  		output.setName(entity.getName());
        Mockito.when(petsAppService.findById(entity.getId())).thenReturn(output);
        
		UpdatePetsInput pets = new UpdatePetsInput();
  		pets.setId(entity.getId());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pets);
	
		mvc.perform(put("/pets/" + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		PetsEntity de = createEntity();
		de.setId(entity.getId());
		pets_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/pets?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/pets?search=petsid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property petsid not found!"));

	} 
	
	@Test
	public void GetVisits_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("petId", "1");

		Mockito.when(petsAppService.parseVisitsJoinColumn("petId")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/pets/1/visits?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetVisits_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("petId", "1");
		
        Mockito.when(petsAppService.parseVisitsJoinColumn("petId")).thenReturn(joinCol);
		mvc.perform(get("/pets/1/visits?search=petId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetVisits_searchIsNotEmpty() throws Exception {
	
		Mockito.when(petsAppService.parseVisitsJoinColumn(anyString())).thenReturn(null);
		mvc.perform(get("/pets/1/visits?search=petid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
	@Test
	public void GetTypes_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
	
	    mvc.perform(get("/pets/111/types")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	
	}    
	
	@Test
	public void GetTypes_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/pets/" + pets.getId()+ "/types")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	@Test
	public void GetOwners_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
	
	    mvc.perform(get("/pets/111/owners")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	
	}    
	
	@Test
	public void GetOwners_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/pets/" + pets.getId()+ "/owners")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
    

}
