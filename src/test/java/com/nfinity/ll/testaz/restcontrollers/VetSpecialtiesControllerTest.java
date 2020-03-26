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
import com.nfinity.ll.testaz.application.vetspecialties.VetSpecialtiesAppService;
import com.nfinity.ll.testaz.application.vetspecialties.dto.*;
import com.nfinity.ll.testaz.domain.irepository.IVetSpecialtiesRepository;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.irepository.ISpecialtiesRepository;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
import com.nfinity.ll.testaz.domain.irepository.IVetsRepository;
import com.nfinity.ll.testaz.domain.model.VetsEntity;
import com.nfinity.ll.testaz.application.specialties.SpecialtiesAppService;    
import com.nfinity.ll.testaz.application.vets.VetsAppService;    
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesId;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class VetSpecialtiesControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IVetSpecialtiesRepository vetSpecialties_repository;
	
	@Autowired 
	private ISpecialtiesRepository specialtiesRepository;
	
	@Autowired 
	private IVetsRepository vetsRepository;
	
	@SpyBean
	private VetSpecialtiesAppService vetSpecialtiesAppService;
    
    @SpyBean
	private SpecialtiesAppService specialtiesAppService;
    
    @SpyBean
	private VetsAppService vetsAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private VetSpecialtiesEntity vetSpecialties;

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
		em.createNativeQuery("drop table sample.vet_specialties CASCADE").executeUpdate();
		em.createNativeQuery("drop table sample.specialties CASCADE").executeUpdate();
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

	public VetSpecialtiesEntity createEntity() {
		SpecialtiesEntity specialties = createSpecialtiesEntity();
		if(!specialtiesRepository.findAll().contains(specialties))
		{
			specialties=specialtiesRepository.save(specialties);
		}
		VetsEntity vets = createVetsEntity();
		if(!vetsRepository.findAll().contains(vets))
		{
			vets=vetsRepository.save(vets);
		}
	
		VetSpecialtiesEntity vetSpecialties = new VetSpecialtiesEntity();
		vetSpecialties.setSpecialtyId(1);
		vetSpecialties.setVetId(1);
		vetSpecialties.setSpecialties(specialties);
		vetSpecialties.setVets(vets);
		
		return vetSpecialties;
	}

	public CreateVetSpecialtiesInput createVetSpecialtiesInput() {
	
	    CreateVetSpecialtiesInput vetSpecialties = new CreateVetSpecialtiesInput();
		vetSpecialties.setSpecialtyId(2);
		vetSpecialties.setVetId(2);
	    
		SpecialtiesEntity specialties = new SpecialtiesEntity();
		specialties.setId(2);
  		specialties.setName("2");
		specialties=specialtiesRepository.save(specialties);
		vetSpecialties.setSpecialtyId(specialties.getId());
		
		VetsEntity vets = new VetsEntity();
  		vets.setFirstName("2");
		vets.setId(2);
  		vets.setLastName("2");
		vets=vetsRepository.save(vets);
		vetSpecialties.setVetId(vets.getId());
		
		
		return vetSpecialties;
	}

	public VetSpecialtiesEntity createNewEntity() {
		VetSpecialtiesEntity vetSpecialties = new VetSpecialtiesEntity();
		vetSpecialties.setSpecialtyId(3);
		vetSpecialties.setVetId(3);
		return vetSpecialties;
	}
	
	public SpecialtiesEntity createSpecialtiesEntity() {
		SpecialtiesEntity specialties = new SpecialtiesEntity();
		specialties.setId(1);
  		specialties.setName("1");
		return specialties;
		 
	}
	public VetsEntity createVetsEntity() {
		VetsEntity vets = new VetsEntity();
  		vets.setFirstName("1");
		vets.setId(1);
  		vets.setLastName("1");
		return vets;
		 
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		evictAllCaches();
		final VetSpecialtiesController vetSpecialtiesController = new VetSpecialtiesController(vetSpecialtiesAppService,specialtiesAppService,vetsAppService,
	logHelper);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(vetSpecialtiesController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		vetSpecialties= createEntity();
		List<VetSpecialtiesEntity> list= vetSpecialties_repository.findAll();
		if(!list.contains(vetSpecialties)) {
			vetSpecialties=vetSpecialties_repository.save(vetSpecialties);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/vetSpecialties/specialtyId:" + vetSpecialties.getSpecialtyId() + ",vetId:" + vetSpecialties.getVetId() )
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		mvc.perform(get("/vetSpecialties/specialtyId:111,vetId:111")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    
	@Test
	public void CreateVetSpecialties_VetSpecialtiesDoesNotExist_ReturnStatusOk() throws Exception {
		CreateVetSpecialtiesInput vetSpecialties = createVetSpecialtiesInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vetSpecialties);

		mvc.perform(post("/vetSpecialties").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeleteVetSpecialties_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(vetSpecialtiesAppService).findById(new VetSpecialtiesId(111, 111));
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/vetSpecialties/specialtyId:111,vetId:111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a vetSpecialties with a id=specialtyId:111,vetId:111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 VetSpecialtiesEntity entity =  createNewEntity();
		SpecialtiesEntity specialties = new SpecialtiesEntity();
		specialties.setId(3);
  		specialties.setName("3");
		specialties=specialtiesRepository.save(specialties);
		
		entity.setSpecialtyId(specialties.getId());
		entity.setSpecialties(specialties);
		VetsEntity vets = new VetsEntity();
  		vets.setFirstName("3");
		vets.setId(3);
  		vets.setLastName("3");
		vets=vetsRepository.save(vets);
		
		entity.setVetId(vets.getId());
		entity.setVets(vets);
		
		entity = vetSpecialties_repository.save(entity);

		FindVetSpecialtiesByIdOutput output= new FindVetSpecialtiesByIdOutput();
  		output.setSpecialtyId(entity.getSpecialtyId());
  		output.setVetId(entity.getVetId());
	    Mockito.when(vetSpecialtiesAppService.findById(new VetSpecialtiesId(entity.getSpecialtyId(), entity.getVetId()))).thenReturn(output);
   
		mvc.perform(delete("/vetSpecialties/specialtyId:"+ entity.getSpecialtyId()+ ",vetId:"+ entity.getVetId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateVetSpecialties_VetSpecialtiesDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(vetSpecialtiesAppService).findById(new VetSpecialtiesId(111, 111));

		UpdateVetSpecialtiesInput vetSpecialties = new UpdateVetSpecialtiesInput();
		vetSpecialties.setSpecialtyId(111);
		vetSpecialties.setVetId(111);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vetSpecialties);
		mvc.perform(put("/vetSpecialties/specialtyId:111,vetId:111").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void UpdateVetSpecialties_VetSpecialtiesExists_ReturnStatusOk() throws Exception {
		VetSpecialtiesEntity entity =  createNewEntity();
		SpecialtiesEntity specialties = new SpecialtiesEntity();
		specialties.setId(5);
  		specialties.setName("5");
		specialties=specialtiesRepository.save(specialties);
		entity.setSpecialtyId(specialties.getId());
		entity.setSpecialties(specialties);
		VetsEntity vets = new VetsEntity();
  		vets.setFirstName("5");
		vets.setId(5);
  		vets.setLastName("5");
		vets=vetsRepository.save(vets);
		entity.setVetId(vets.getId());
		entity.setVets(vets);
		entity = vetSpecialties_repository.save(entity);
		FindVetSpecialtiesByIdOutput output= new FindVetSpecialtiesByIdOutput();
  		output.setSpecialtyId(entity.getSpecialtyId());
  		output.setVetId(entity.getVetId());
	    Mockito.when(vetSpecialtiesAppService.findById(new VetSpecialtiesId(entity.getSpecialtyId(), entity.getVetId()))).thenReturn(output);
        
		UpdateVetSpecialtiesInput vetSpecialties = new UpdateVetSpecialtiesInput();
  		vetSpecialties.setSpecialtyId(entity.getSpecialtyId());
  		vetSpecialties.setVetId(entity.getVetId());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vetSpecialties);
	
		mvc.perform(put("/vetSpecialties/specialtyId:"+ entity.getSpecialtyId()+ ",vetId:"+ entity.getVetId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		VetSpecialtiesEntity de = createEntity();
		de.setSpecialtyId(entity.getSpecialtyId());
		de.setVetId(entity.getVetId());
		vetSpecialties_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/vetSpecialties?search=specialtyId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vetSpecialties?search=vetSpecialtiesspecialtyId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property vetSpecialtiesspecialtyId not found!"));

	} 
	
	@Test
	public void GetSpecialties_IdIsNotEmptyAndIdIsNotValid_ThrowException() throws Exception {
	
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vetSpecialties/specialtyId:111/specialties")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=specialtyId:111"));
	
	}    
	@Test
	public void GetSpecialties_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
	
	    mvc.perform(get("/vetSpecialties/specialtyId:111,vetId:111/specialties")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	
	}    
	
	@Test
	public void GetSpecialties_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/vetSpecialties/specialtyId:" + vetSpecialties.getSpecialtyId()+ ",vetId:" + vetSpecialties.getVetId()+ "/specialties")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	@Test
	public void GetVets_IdIsNotEmptyAndIdIsNotValid_ThrowException() throws Exception {
	
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vetSpecialties/specialtyId:111/vets")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=specialtyId:111"));
	
	}    
	@Test
	public void GetVets_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
	
	    mvc.perform(get("/vetSpecialties/specialtyId:111,vetId:111/vets")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	
	}    
	
	@Test
	public void GetVets_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/vetSpecialties/specialtyId:" + vetSpecialties.getSpecialtyId()+ ",vetId:" + vetSpecialties.getVetId()+ "/vets")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
    

}
