package com.nfinity.ll.testaz.application.visits;

import com.nfinity.ll.testaz.application.visits.dto.*;
import com.nfinity.ll.testaz.domain.visits.IVisitsManager;
import com.nfinity.ll.testaz.domain.model.QVisitsEntity;
import com.nfinity.ll.testaz.domain.model.VisitsEntity;
import com.nfinity.ll.testaz.domain.pets.PetsManager;
import com.nfinity.ll.testaz.domain.model.PetsEntity;
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
public class VisitsAppService implements IVisitsAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IVisitsManager _visitsManager;

    @Autowired
	private PetsManager _petsManager;
	@Autowired
	private VisitsMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateVisitsOutput create(CreateVisitsInput input) {

		VisitsEntity visits = mapper.createVisitsInputToVisitsEntity(input);
	  	if(input.getPetId()!=null) {
			PetsEntity foundPets = _petsManager.findById(input.getPetId());
			if(foundPets!=null) {
				visits.setPets(foundPets);
			}
		}
		VisitsEntity createdVisits = _visitsManager.create(visits);
		
		return mapper.visitsEntityToCreateVisitsOutput(createdVisits);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Visits", key = "#p0")
	public UpdateVisitsOutput update(Integer  visitsId, UpdateVisitsInput input) {

		VisitsEntity visits = mapper.updateVisitsInputToVisitsEntity(input);
	  	if(input.getPetId()!=null) {
			PetsEntity foundPets = _petsManager.findById(input.getPetId());
			if(foundPets!=null) {
				visits.setPets(foundPets);
			}
		}
		
		VisitsEntity updatedVisits = _visitsManager.update(visits);
		
		return mapper.visitsEntityToUpdateVisitsOutput(updatedVisits);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Visits", key = "#p0")
	public void delete(Integer visitsId) {

		VisitsEntity existing = _visitsManager.findById(visitsId) ; 
		_visitsManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Visits", key = "#p0")
	public FindVisitsByIdOutput findById(Integer visitsId) {

		VisitsEntity foundVisits = _visitsManager.findById(visitsId);
		if (foundVisits == null)  
			return null ; 
 	   
 	    FindVisitsByIdOutput output=mapper.visitsEntityToFindVisitsByIdOutput(foundVisits); 
		return output;
	}
    //Pets
	// ReST API Call - GET /visits/1/pets
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Visits", key="#p0")
	public GetPetsOutput getPets(Integer visitsId) {

		VisitsEntity foundVisits = _visitsManager.findById(visitsId);
		if (foundVisits == null) {
			logHelper.getLogger().error("There does not exist a visits wth a id=%s", visitsId);
			return null;
		}
		PetsEntity re = _visitsManager.getPets(visitsId);
		return mapper.petsEntityToGetPetsOutput(re, foundVisits);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Visits")
	public List<FindVisitsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<VisitsEntity> foundVisits = _visitsManager.findAll(search(search), pageable);
		List<VisitsEntity> visitsList = foundVisits.getContent();
		Iterator<VisitsEntity> visitsIterator = visitsList.iterator(); 
		List<FindVisitsByIdOutput> output = new ArrayList<>();

		while (visitsIterator.hasNext()) {
			output.add(mapper.visitsEntityToFindVisitsByIdOutput(visitsIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QVisitsEntity visits= QVisitsEntity.visitsEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(visits, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("petId") ||
				list.get(i).replace("%20","").trim().equals("description") ||
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("pets") ||
				list.get(i).replace("%20","").trim().equals("visitDate")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QVisitsEntity visits, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("description")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(visits.description.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(visits.description.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(visits.description.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("visitDate")) {
				if(details.getValue().getOperator().equals("equals") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(visits.visitDate.eq(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(visits.visitDate.ne(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   Date startDate= SearchUtils.stringToDate(details.getValue().getStartingValue());
				   Date endDate= SearchUtils.stringToDate(details.getValue().getEndingValue());
				   if(startDate!=null && endDate!=null)	 
					   builder.and(visits.visitDate.between(startDate,endDate));
				   else if(endDate!=null)
					   builder.and(visits.visitDate.loe(endDate));
                   else if(startDate!=null)
                	   builder.and(visits.visitDate.goe(startDate));  
                 }
                   
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("petId")) {
		    builder.and(visits.pets.id.eq(Integer.parseInt(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	
    
	
}


