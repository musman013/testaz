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
import com.nfinity.ll.testaz.application.vets.VetsAppService;
import com.nfinity.ll.testaz.application.vets.dto.*;
import com.nfinity.ll.testaz.domain.irepository.IVetsRepository;
import com.nfinity.ll.testaz.domain.model.VetsEntity;
import com.nfinity.ll.testaz.application.vetspecialties.VetSpecialtiesAppService;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class VetsControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IVetsRepository vets_repository;
	
	@SpyBean
	private VetsAppService vetsAppService;
    
    @SpyBean
	private VetSpecialtiesAppService vetSpecialtiesAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private VetsEntity vets;

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
		em.createNativeQuery("drop table sample.vets CASCADE").executeUpdate();
		em.getTransaction().commit();
	}

	@Autowired 
	private CacheManager cacheManager; 
	
	public void evictAllCaches(){ 
		for(String name : cacheManager.getCacheNames()) {
			cacheManager.getCache(name).clear(); 
		} 
	}

	public VetsEntity createEntity() {
	
		VetsEntity vets = new VetsEntity();
  		vets.setFirstName("1");
		vets.setId(1);
  		vets.setLastName("1");
		
		return vets;
	}

	public CreateVetsInput createVetsInput() {
	
	    CreateVetsInput vets = new CreateVetsInput();
  		vets.setFirstName("2");
  		vets.setLastName("2");
	    
		
		
		return vets;
	}

	public VetsEntity createNewEntity() {
		VetsEntity vets = new VetsEntity();
  		vets.setFirstName("3");
		vets.setId(3);
  		vets.setLastName("3");
		return vets;
	}
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		evictAllCaches();
		final VetsController vetsController = new VetsController(vetsAppService,vetSpecialtiesAppService,
	logHelper);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(vetsController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		vets= createEntity();
		List<VetsEntity> list= vets_repository.findAll();
		if(!list.contains(vets)) {
			vets=vets_repository.save(vets);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/vets/" + vets.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		mvc.perform(get("/vets/111")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    
	@Test
	public void CreateVets_VetsDoesNotExist_ReturnStatusOk() throws Exception {
		CreateVetsInput vets = createVetsInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vets);

		mvc.perform(post("/vets").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeleteVets_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(vetsAppService).findById(111);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/vets/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a vets with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 VetsEntity entity =  createNewEntity();
		
		entity = vets_repository.save(entity);

		FindVetsByIdOutput output= new FindVetsByIdOutput();
  		output.setId(entity.getId());
        Mockito.when(vetsAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/vets/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateVets_VetsDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(vetsAppService).findById(111);

		UpdateVetsInput vets = new UpdateVetsInput();
  		vets.setFirstName("111");
		vets.setId(111);
  		vets.setLastName("111");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vets);
		mvc.perform(put("/vets/111").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void UpdateVets_VetsExists_ReturnStatusOk() throws Exception {
		VetsEntity entity =  createNewEntity();
		entity = vets_repository.save(entity);
		FindVetsByIdOutput output= new FindVetsByIdOutput();
  		output.setFirstName(entity.getFirstName());
  		output.setId(entity.getId());
  		output.setLastName(entity.getLastName());
        Mockito.when(vetsAppService.findById(entity.getId())).thenReturn(output);
        
		UpdateVetsInput vets = new UpdateVetsInput();
  		vets.setId(entity.getId());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vets);
	
		mvc.perform(put("/vets/" + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		VetsEntity de = createEntity();
		de.setId(entity.getId());
		vets_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/vets?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vets?search=vetsid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property vetsid not found!"));

	} 
	
	@Test
	public void GetVetSpecialties_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("vetId", "1");

		Mockito.when(vetsAppService.parseVetSpecialtiesJoinColumn("vetId")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vets/1/vetSpecialties?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetVetSpecialties_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("vetId", "1");
		
        Mockito.when(vetsAppService.parseVetSpecialtiesJoinColumn("vetId")).thenReturn(joinCol);
		mvc.perform(get("/vets/1/vetSpecialties?search=vetId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetVetSpecialties_searchIsNotEmpty() throws Exception {
	
		Mockito.when(vetsAppService.parseVetSpecialtiesJoinColumn(anyString())).thenReturn(null);
		mvc.perform(get("/vets/1/vetSpecialties?search=vetid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
    

}
