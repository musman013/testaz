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
import com.nfinity.ll.testaz.application.types.TypesAppService;
import com.nfinity.ll.testaz.application.types.dto.*;
import com.nfinity.ll.testaz.domain.irepository.ITypesRepository;
import com.nfinity.ll.testaz.domain.model.TypesEntity;
import com.nfinity.ll.testaz.application.pets.PetsAppService;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class TypesControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private ITypesRepository types_repository;
	
	@SpyBean
	private TypesAppService typesAppService;
    
    @SpyBean
	private PetsAppService petsAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private TypesEntity types;

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
		em.createNativeQuery("drop table sample.types CASCADE").executeUpdate();
		em.getTransaction().commit();
	}

	@Autowired 
	private CacheManager cacheManager; 
	
	public void evictAllCaches(){ 
		for(String name : cacheManager.getCacheNames()) {
			cacheManager.getCache(name).clear(); 
		} 
	}

	public TypesEntity createEntity() {
	
		TypesEntity types = new TypesEntity();
		types.setId(1);
  		types.setName("1");
		
		return types;
	}

	public CreateTypesInput createTypesInput() {
	
	    CreateTypesInput types = new CreateTypesInput();
  		types.setName("2");
	    
		
		
		return types;
	}

	public TypesEntity createNewEntity() {
		TypesEntity types = new TypesEntity();
		types.setId(3);
  		types.setName("3");
		return types;
	}
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		evictAllCaches();
		final TypesController typesController = new TypesController(typesAppService,petsAppService,
	logHelper);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(typesController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		types= createEntity();
		List<TypesEntity> list= types_repository.findAll();
		if(!list.contains(types)) {
			types=types_repository.save(types);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/types/" + types.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		mvc.perform(get("/types/111")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    
	@Test
	public void CreateTypes_TypesDoesNotExist_ReturnStatusOk() throws Exception {
		CreateTypesInput types = createTypesInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(types);

		mvc.perform(post("/types").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeleteTypes_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(typesAppService).findById(111);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/types/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a types with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 TypesEntity entity =  createNewEntity();
		
		entity = types_repository.save(entity);

		FindTypesByIdOutput output= new FindTypesByIdOutput();
  		output.setId(entity.getId());
        Mockito.when(typesAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/types/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateTypes_TypesDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(typesAppService).findById(111);

		UpdateTypesInput types = new UpdateTypesInput();
		types.setId(111);
  		types.setName("111");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(types);
		mvc.perform(put("/types/111").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void UpdateTypes_TypesExists_ReturnStatusOk() throws Exception {
		TypesEntity entity =  createNewEntity();
		entity = types_repository.save(entity);
		FindTypesByIdOutput output= new FindTypesByIdOutput();
  		output.setId(entity.getId());
  		output.setName(entity.getName());
        Mockito.when(typesAppService.findById(entity.getId())).thenReturn(output);
        
		UpdateTypesInput types = new UpdateTypesInput();
  		types.setId(entity.getId());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(types);
	
		mvc.perform(put("/types/" + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		TypesEntity de = createEntity();
		de.setId(entity.getId());
		types_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/types?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/types?search=typesid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property typesid not found!"));

	} 
	
	@Test
	public void GetPets_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("typeId", "1");

		Mockito.when(typesAppService.parsePetsJoinColumn("typeId")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/types/1/pets?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetPets_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("typeId", "1");
		
        Mockito.when(typesAppService.parsePetsJoinColumn("typeId")).thenReturn(joinCol);
		mvc.perform(get("/types/1/pets?search=typeId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetPets_searchIsNotEmpty() throws Exception {
	
		Mockito.when(typesAppService.parsePetsJoinColumn(anyString())).thenReturn(null);
		mvc.perform(get("/types/1/pets?search=typeid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
    

}
