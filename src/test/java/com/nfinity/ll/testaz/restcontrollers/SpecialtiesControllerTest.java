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
import com.nfinity.ll.testaz.application.specialties.SpecialtiesAppService;
import com.nfinity.ll.testaz.application.specialties.dto.*;
import com.nfinity.ll.testaz.domain.irepository.ISpecialtiesRepository;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
import com.nfinity.ll.testaz.application.vetspecialties.VetSpecialtiesAppService;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class SpecialtiesControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private ISpecialtiesRepository specialties_repository;
	
	@SpyBean
	private SpecialtiesAppService specialtiesAppService;
    
    @SpyBean
	private VetSpecialtiesAppService vetSpecialtiesAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private SpecialtiesEntity specialties;

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
		em.createNativeQuery("drop table sample.specialties CASCADE").executeUpdate();
		em.getTransaction().commit();
	}

	@Autowired 
	private CacheManager cacheManager; 
	
	public void evictAllCaches(){ 
		for(String name : cacheManager.getCacheNames()) {
			cacheManager.getCache(name).clear(); 
		} 
	}

	public SpecialtiesEntity createEntity() {
	
		SpecialtiesEntity specialties = new SpecialtiesEntity();
		specialties.setId(1);
  		specialties.setName("1");
		
		return specialties;
	}

	public CreateSpecialtiesInput createSpecialtiesInput() {
	
	    CreateSpecialtiesInput specialties = new CreateSpecialtiesInput();
  		specialties.setName("2");
	    
		
		
		return specialties;
	}

	public SpecialtiesEntity createNewEntity() {
		SpecialtiesEntity specialties = new SpecialtiesEntity();
		specialties.setId(3);
  		specialties.setName("3");
		return specialties;
	}
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		evictAllCaches();
		final SpecialtiesController specialtiesController = new SpecialtiesController(specialtiesAppService,vetSpecialtiesAppService,
	logHelper);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(specialtiesController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		specialties= createEntity();
		List<SpecialtiesEntity> list= specialties_repository.findAll();
		if(!list.contains(specialties)) {
			specialties=specialties_repository.save(specialties);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/specialties/" + specialties.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		mvc.perform(get("/specialties/111")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    
	@Test
	public void CreateSpecialties_SpecialtiesDoesNotExist_ReturnStatusOk() throws Exception {
		CreateSpecialtiesInput specialties = createSpecialtiesInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(specialties);

		mvc.perform(post("/specialties").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeleteSpecialties_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(specialtiesAppService).findById(111);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/specialties/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a specialties with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 SpecialtiesEntity entity =  createNewEntity();
		
		entity = specialties_repository.save(entity);

		FindSpecialtiesByIdOutput output= new FindSpecialtiesByIdOutput();
  		output.setId(entity.getId());
        Mockito.when(specialtiesAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/specialties/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateSpecialties_SpecialtiesDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(specialtiesAppService).findById(111);

		UpdateSpecialtiesInput specialties = new UpdateSpecialtiesInput();
		specialties.setId(111);
  		specialties.setName("111");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(specialties);
		mvc.perform(put("/specialties/111").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void UpdateSpecialties_SpecialtiesExists_ReturnStatusOk() throws Exception {
		SpecialtiesEntity entity =  createNewEntity();
		entity = specialties_repository.save(entity);
		FindSpecialtiesByIdOutput output= new FindSpecialtiesByIdOutput();
  		output.setId(entity.getId());
  		output.setName(entity.getName());
        Mockito.when(specialtiesAppService.findById(entity.getId())).thenReturn(output);
        
		UpdateSpecialtiesInput specialties = new UpdateSpecialtiesInput();
  		specialties.setId(entity.getId());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(specialties);
	
		mvc.perform(put("/specialties/" + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		SpecialtiesEntity de = createEntity();
		de.setId(entity.getId());
		specialties_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/specialties?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/specialties?search=specialtiesid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property specialtiesid not found!"));

	} 
	
	@Test
	public void GetVetSpecialties_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("specialtyId", "1");

		Mockito.when(specialtiesAppService.parseVetSpecialtiesJoinColumn("specialtyId")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/specialties/1/vetSpecialties?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetVetSpecialties_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("specialtyId", "1");
		
        Mockito.when(specialtiesAppService.parseVetSpecialtiesJoinColumn("specialtyId")).thenReturn(joinCol);
		mvc.perform(get("/specialties/1/vetSpecialties?search=specialtyId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetVetSpecialties_searchIsNotEmpty() throws Exception {
	
		Mockito.when(specialtiesAppService.parseVetSpecialtiesJoinColumn(anyString())).thenReturn(null);
		mvc.perform(get("/specialties/1/vetSpecialties?search=specialtyid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
    

}
