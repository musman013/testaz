package com.nfinity.ll.testaz.application.specialties;

import com.nfinity.ll.testaz.application.specialties.dto.*;
import com.nfinity.ll.testaz.domain.specialties.ISpecialtiesManager;
import com.nfinity.ll.testaz.domain.model.QSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
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
public class SpecialtiesAppService implements ISpecialtiesAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private ISpecialtiesManager _specialtiesManager;

	@Autowired
	private SpecialtiesMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateSpecialtiesOutput create(CreateSpecialtiesInput input) {

		SpecialtiesEntity specialties = mapper.createSpecialtiesInputToSpecialtiesEntity(input);
		SpecialtiesEntity createdSpecialties = _specialtiesManager.create(specialties);
		
		return mapper.specialtiesEntityToCreateSpecialtiesOutput(createdSpecialties);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Specialties", key = "#p0")
	public UpdateSpecialtiesOutput update(Integer  specialtiesId, UpdateSpecialtiesInput input) {

		SpecialtiesEntity specialties = mapper.updateSpecialtiesInputToSpecialtiesEntity(input);
		
		SpecialtiesEntity updatedSpecialties = _specialtiesManager.update(specialties);
		
		return mapper.specialtiesEntityToUpdateSpecialtiesOutput(updatedSpecialties);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Specialties", key = "#p0")
	public void delete(Integer specialtiesId) {

		SpecialtiesEntity existing = _specialtiesManager.findById(specialtiesId) ; 
		_specialtiesManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Specialties", key = "#p0")
	public FindSpecialtiesByIdOutput findById(Integer specialtiesId) {

		SpecialtiesEntity foundSpecialties = _specialtiesManager.findById(specialtiesId);
		if (foundSpecialties == null)  
			return null ; 
 	   
 	    FindSpecialtiesByIdOutput output=mapper.specialtiesEntityToFindSpecialtiesByIdOutput(foundSpecialties); 
		return output;
	}
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Specialties")
	public List<FindSpecialtiesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<SpecialtiesEntity> foundSpecialties = _specialtiesManager.findAll(search(search), pageable);
		List<SpecialtiesEntity> specialtiesList = foundSpecialties.getContent();
		Iterator<SpecialtiesEntity> specialtiesIterator = specialtiesList.iterator(); 
		List<FindSpecialtiesByIdOutput> output = new ArrayList<>();

		while (specialtiesIterator.hasNext()) {
			output.add(mapper.specialtiesEntityToFindSpecialtiesByIdOutput(specialtiesIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QSpecialtiesEntity specialties= QSpecialtiesEntity.specialtiesEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(specialties, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("name") ||
				list.get(i).replace("%20","").trim().equals("vetspecialties")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QSpecialtiesEntity specialties, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("name")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(specialties.name.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(specialties.name.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(specialties.name.ne(details.getValue().getSearchValue()));
			}
		}
		return builder;
	}
	
	
	public Map<String,String> parseVetSpecialtiesJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("specialtyId", keysString);
		return joinColumnMap;
	}
    
	
}


