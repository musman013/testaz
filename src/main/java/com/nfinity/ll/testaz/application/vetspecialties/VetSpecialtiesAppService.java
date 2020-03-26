package com.nfinity.ll.testaz.application.vetspecialties;

import com.nfinity.ll.testaz.application.vetspecialties.dto.*;
import com.nfinity.ll.testaz.domain.vetspecialties.IVetSpecialtiesManager;
import com.nfinity.ll.testaz.domain.model.QVetSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesId;
import com.nfinity.ll.testaz.domain.specialties.SpecialtiesManager;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
import com.nfinity.ll.testaz.domain.vets.VetsManager;
import com.nfinity.ll.testaz.domain.model.VetsEntity;
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
public class VetSpecialtiesAppService implements IVetSpecialtiesAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IVetSpecialtiesManager _vetSpecialtiesManager;

    @Autowired
	private SpecialtiesManager _specialtiesManager;
    @Autowired
	private VetsManager _vetsManager;
	@Autowired
	private VetSpecialtiesMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateVetSpecialtiesOutput create(CreateVetSpecialtiesInput input) {

		VetSpecialtiesEntity vetSpecialties = mapper.createVetSpecialtiesInputToVetSpecialtiesEntity(input);
	  	if(input.getSpecialtyId()!=null) {
			SpecialtiesEntity foundSpecialties = _specialtiesManager.findById(input.getSpecialtyId());
			if(foundSpecialties!=null) {
				vetSpecialties.setSpecialties(foundSpecialties);
			}
		}
	  	if(input.getVetId()!=null) {
			VetsEntity foundVets = _vetsManager.findById(input.getVetId());
			if(foundVets!=null) {
				vetSpecialties.setVets(foundVets);
			}
		}
		VetSpecialtiesEntity createdVetSpecialties = _vetSpecialtiesManager.create(vetSpecialties);
		
		return mapper.vetSpecialtiesEntityToCreateVetSpecialtiesOutput(createdVetSpecialties);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="VetSpecialties", key = "#p0")
	public UpdateVetSpecialtiesOutput update(VetSpecialtiesId vetSpecialtiesId , UpdateVetSpecialtiesInput input) {

		VetSpecialtiesEntity vetSpecialties = mapper.updateVetSpecialtiesInputToVetSpecialtiesEntity(input);
	  	if(input.getSpecialtyId()!=null) {
			SpecialtiesEntity foundSpecialties = _specialtiesManager.findById(input.getSpecialtyId());
			if(foundSpecialties!=null) {
				vetSpecialties.setSpecialties(foundSpecialties);
			}
		}
	  	if(input.getVetId()!=null) {
			VetsEntity foundVets = _vetsManager.findById(input.getVetId());
			if(foundVets!=null) {
				vetSpecialties.setVets(foundVets);
			}
		}
		
		VetSpecialtiesEntity updatedVetSpecialties = _vetSpecialtiesManager.update(vetSpecialties);
		
		return mapper.vetSpecialtiesEntityToUpdateVetSpecialtiesOutput(updatedVetSpecialties);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="VetSpecialties", key = "#p0")
	public void delete(VetSpecialtiesId vetSpecialtiesId) {

		VetSpecialtiesEntity existing = _vetSpecialtiesManager.findById(vetSpecialtiesId) ; 
		_vetSpecialtiesManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "VetSpecialties", key = "#p0")
	public FindVetSpecialtiesByIdOutput findById(VetSpecialtiesId vetSpecialtiesId) {

		VetSpecialtiesEntity foundVetSpecialties = _vetSpecialtiesManager.findById(vetSpecialtiesId);
		if (foundVetSpecialties == null)  
			return null ; 
 	   
 	    FindVetSpecialtiesByIdOutput output=mapper.vetSpecialtiesEntityToFindVetSpecialtiesByIdOutput(foundVetSpecialties); 
		return output;
	}
    //Specialties
	// ReST API Call - GET /vetSpecialties/1/specialties
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "VetSpecialties", key="#p0")
	public GetSpecialtiesOutput getSpecialties(VetSpecialtiesId vetSpecialtiesId) {

		VetSpecialtiesEntity foundVetSpecialties = _vetSpecialtiesManager.findById(vetSpecialtiesId);
		if (foundVetSpecialties == null) {
			logHelper.getLogger().error("There does not exist a vetSpecialties wth a id=%s", vetSpecialtiesId);
			return null;
		}
		SpecialtiesEntity re = _vetSpecialtiesManager.getSpecialties(vetSpecialtiesId);
		return mapper.specialtiesEntityToGetSpecialtiesOutput(re, foundVetSpecialties);
	}
	
    //Vets
	// ReST API Call - GET /vetSpecialties/1/vets
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "VetSpecialties", key="#p0")
	public GetVetsOutput getVets(VetSpecialtiesId vetSpecialtiesId) {

		VetSpecialtiesEntity foundVetSpecialties = _vetSpecialtiesManager.findById(vetSpecialtiesId);
		if (foundVetSpecialties == null) {
			logHelper.getLogger().error("There does not exist a vetSpecialties wth a id=%s", vetSpecialtiesId);
			return null;
		}
		VetsEntity re = _vetSpecialtiesManager.getVets(vetSpecialtiesId);
		return mapper.vetsEntityToGetVetsOutput(re, foundVetSpecialties);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "VetSpecialties")
	public List<FindVetSpecialtiesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<VetSpecialtiesEntity> foundVetSpecialties = _vetSpecialtiesManager.findAll(search(search), pageable);
		List<VetSpecialtiesEntity> vetSpecialtiesList = foundVetSpecialties.getContent();
		Iterator<VetSpecialtiesEntity> vetSpecialtiesIterator = vetSpecialtiesList.iterator(); 
		List<FindVetSpecialtiesByIdOutput> output = new ArrayList<>();

		while (vetSpecialtiesIterator.hasNext()) {
			output.add(mapper.vetSpecialtiesEntityToFindVetSpecialtiesByIdOutput(vetSpecialtiesIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QVetSpecialtiesEntity vetSpecialties= QVetSpecialtiesEntity.vetSpecialtiesEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(vetSpecialties, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("specialties") ||
				list.get(i).replace("%20","").trim().equals("specialtyId") ||
				list.get(i).replace("%20","").trim().equals("vetId") ||
				list.get(i).replace("%20","").trim().equals("vets")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QVetSpecialtiesEntity vetSpecialties, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("specialtyId")) {
		    builder.and(vetSpecialties.specialties.id.eq(Integer.parseInt(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("vetId")) {
		    builder.and(vetSpecialties.vets.id.eq(Integer.parseInt(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public VetSpecialtiesId parseVetSpecialtiesKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		VetSpecialtiesId vetSpecialtiesId = new VetSpecialtiesId();
		
		Map<String,String> keyMap = new HashMap<String,String>();
		if(keyEntries.length > 1) {
			for(String keyEntry: keyEntries)
			{
				String[] keyEntryArr = keyEntry.split(":");
				if(keyEntryArr.length > 1) {
					keyMap.put(keyEntryArr[0], keyEntryArr[1]);					
				}
				else {
					return null;
				}
			}
		}
		else {
			return null;
		}
		
		vetSpecialtiesId.setSpecialtyId(Integer.valueOf(keyMap.get("specialtyId")));
		vetSpecialtiesId.setVetId(Integer.valueOf(keyMap.get("vetId")));
		return vetSpecialtiesId;
		
	}	
	
    
	
}


