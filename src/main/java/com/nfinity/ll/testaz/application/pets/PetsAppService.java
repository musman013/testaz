package com.nfinity.ll.testaz.application.pets;

import com.nfinity.ll.testaz.application.pets.dto.*;
import com.nfinity.ll.testaz.domain.pets.IPetsManager;
import com.nfinity.ll.testaz.domain.model.QPetsEntity;
import com.nfinity.ll.testaz.domain.model.PetsEntity;
import com.nfinity.ll.testaz.domain.types.TypesManager;
import com.nfinity.ll.testaz.domain.model.TypesEntity;
import com.nfinity.ll.testaz.domain.owners.OwnersManager;
import com.nfinity.ll.testaz.domain.model.OwnersEntity;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import org.springframework.cache.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
@Validated
public class PetsAppService implements IPetsAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IPetsManager _petsManager;

    @Autowired
	private TypesManager _typesManager;
    @Autowired
	private OwnersManager _ownersManager;
	@Autowired
	private PetsMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreatePetsOutput create(CreatePetsInput input) {

		PetsEntity pets = mapper.createPetsInputToPetsEntity(input);
	  	if(input.getTypeId()!=null) {
			TypesEntity foundTypes = _typesManager.findById(input.getTypeId());
			if(foundTypes!=null) {
				pets.setTypes(foundTypes);
			}
		}
	  	if(input.getOwnerId()!=null) {
			OwnersEntity foundOwners = _ownersManager.findById(input.getOwnerId());
			if(foundOwners!=null) {
				pets.setOwners(foundOwners);
			}
		}
		PetsEntity createdPets = _petsManager.create(pets);
		
		return mapper.petsEntityToCreatePetsOutput(createdPets);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Pets", key = "#p0")
	public UpdatePetsOutput update(Integer  petsId, UpdatePetsInput input) {

		PetsEntity pets = mapper.updatePetsInputToPetsEntity(input);
	  	if(input.getTypeId()!=null) {
			TypesEntity foundTypes = _typesManager.findById(input.getTypeId());
			if(foundTypes!=null) {
				pets.setTypes(foundTypes);
			}
		}
	  	if(input.getOwnerId()!=null) {
			OwnersEntity foundOwners = _ownersManager.findById(input.getOwnerId());
			if(foundOwners!=null) {
				pets.setOwners(foundOwners);
			}
		}
		
		PetsEntity updatedPets = _petsManager.update(pets);
		
		return mapper.petsEntityToUpdatePetsOutput(updatedPets);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Pets", key = "#p0")
	public void delete(Integer petsId) {

		PetsEntity existing = _petsManager.findById(petsId) ; 
		_petsManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Pets", key = "#p0")
	public FindPetsByIdOutput findById(Integer petsId) {

		PetsEntity foundPets = _petsManager.findById(petsId);
		if (foundPets == null)  
			return null ; 
 	   
 	    FindPetsByIdOutput output=mapper.petsEntityToFindPetsByIdOutput(foundPets); 
		return output;
	}
    //Types
	// ReST API Call - GET /pets/1/types
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Pets", key="#p0")
	public GetTypesOutput getTypes(Integer petsId) {

		PetsEntity foundPets = _petsManager.findById(petsId);
		if (foundPets == null) {
			logHelper.getLogger().error("There does not exist a pets wth a id=%s", petsId);
			return null;
		}
		TypesEntity re = _petsManager.getTypes(petsId);
		return mapper.typesEntityToGetTypesOutput(re, foundPets);
	}
	
    //Owners
	// ReST API Call - GET /pets/1/owners
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Pets", key="#p0")
	public GetOwnersOutput getOwners(Integer petsId) {

		PetsEntity foundPets = _petsManager.findById(petsId);
		if (foundPets == null) {
			logHelper.getLogger().error("There does not exist a pets wth a id=%s", petsId);
			return null;
		}
		OwnersEntity re = _petsManager.getOwners(petsId);
		return mapper.ownersEntityToGetOwnersOutput(re, foundPets);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Pets")
	public List<FindPetsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<PetsEntity> foundPets = _petsManager.findAll(search(search), pageable);
		List<PetsEntity> petsList = foundPets.getContent();
		Iterator<PetsEntity> petsIterator = petsList.iterator(); 
		List<FindPetsByIdOutput> output = new ArrayList<>();

		while (petsIterator.hasNext()) {
			output.add(mapper.petsEntityToFindPetsByIdOutput(petsIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QPetsEntity pets= QPetsEntity.petsEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(pets, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("typeId") ||
				list.get(i).replace("%20","").trim().equals("ownerId") ||
				list.get(i).replace("%20","").trim().equals("birthDate") ||
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("name") ||
				list.get(i).replace("%20","").trim().equals("owners") ||
				list.get(i).replace("%20","").trim().equals("types") ||
				list.get(i).replace("%20","").trim().equals("visits")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QPetsEntity pets, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("birthDate")) {
				if(details.getValue().getOperator().equals("equals") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(pets.birthDate.eq(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(pets.birthDate.ne(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   Date startDate= SearchUtils.stringToDate(details.getValue().getStartingValue());
				   Date endDate= SearchUtils.stringToDate(details.getValue().getEndingValue());
				   if(startDate!=null && endDate!=null)	 
					   builder.and(pets.birthDate.between(startDate,endDate));
				   else if(endDate!=null)
					   builder.and(pets.birthDate.loe(endDate));
                   else if(startDate!=null)
                	   builder.and(pets.birthDate.goe(startDate));  
                 }
                   
			}
            if(details.getKey().replace("%20","").trim().equals("name")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(pets.name.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(pets.name.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(pets.name.ne(details.getValue().getSearchValue()));
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("typeId")) {
		    builder.and(pets.types.id.eq(Integer.parseInt(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("ownerId")) {
		    builder.and(pets.owners.id.eq(Integer.parseInt(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	
	public Map<String,String> parseVisitsJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("petId", keysString);
		return joinColumnMap;
	}
    
	
}


