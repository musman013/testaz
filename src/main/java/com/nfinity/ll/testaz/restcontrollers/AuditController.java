package com.nfinity.ll.testaz.restcontrollers;

import com.nfinity.ll.testaz.domain.model.TypesEntity;
import com.nfinity.ll.testaz.domain.model.PetsEntity;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
import com.nfinity.ll.testaz.domain.model.VisitsEntity;
import com.nfinity.ll.testaz.domain.model.VetsEntity;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesEntity;
import com.nfinity.ll.testaz.domain.model.OwnersEntity;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.domain.model.RolepermissionEntity;
import com.nfinity.ll.testaz.domain.model.UserpermissionEntity;
import com.nfinity.ll.testaz.domain.model.UserroleEntity;
import com.nfinity.ll.testaz.commons.search.SearchUtils;

import org.javers.core.Javers;
import org.javers.core.diff.Change;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/audit")
public class AuditController {

    private final Javers javers;
    
    @Autowired
	private Environment env;

    @Autowired
    public AuditController(Javers javers) {
        this.javers = javers;
    }
    @RequestMapping("/user")
    public String getUserChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(UserEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    @RequestMapping("/role")
    public String getRoleChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(RoleEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }

    @RequestMapping("/permission")
    public String getPermissionChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(PermissionEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    
    @RequestMapping("/rolepermission")
    public String getRolepermissionChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(RolepermissionEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    
    @RequestMapping("/userpermission")
    public String getUserpermissionChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(UserpermissionEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    
    @RequestMapping("/userrole")
    public String getUserroleChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(UserroleEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }

    @RequestMapping("/types")
    public String getTypesChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(TypesEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    @RequestMapping("/pets")
    public String getPetsChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(PetsEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    @RequestMapping("/specialties")
    public String getSpecialtiesChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(SpecialtiesEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    @RequestMapping("/visits")
    public String getVisitsChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(VisitsEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    @RequestMapping("/vets")
    public String getVetsChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(VetsEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    @RequestMapping("/vetspecialties")
    public String getVetSpecialtiesChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(VetSpecialtiesEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    @RequestMapping("/owners")
    public String getOwnersChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        QueryBuilder jqlQuery = addPaginationAndFilters(QueryBuilder.byClass(OwnersEntity.class),limit,offset,search);
        List<Change> changes = javers.findChanges(jqlQuery.withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(changes);
    }
    
    @RequestMapping("/changes")
    public String getAllChanges(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) {
        JqlQuery jqlQuery = addPaginationAndFilters(QueryBuilder.anyDomainObject().withNewObjectChanges(),limit,offset,search).build();
        List<Change> changes = javers.findChanges(jqlQuery);
        return javers.getJsonConverter().toJson(changes);
    }
    
    public QueryBuilder addPaginationAndFilters(QueryBuilder query, String limit, String offset,String search)
	{
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		query = query.limit(Integer.parseInt(limit)).skip(Integer.parseInt(offset));
		Map<String, Object> map=parseSearchString(search);
		if(map.containsKey("author") && map.get("author") != null)
		{
			query = query.byAuthor(map.get("author").toString()).from((LocalDateTime)map.get("from")).to((LocalDateTime)map.get("to"));
		}
		else
			query = query.from((LocalDateTime)map.get("from")).to((LocalDateTime)map.get("to"));

		return query;
	}

	public Map<String, Object> parseSearchString(String searchString)
	{
		Map<String, Object> searchMap = new HashMap<>();
		if(searchString != null && searchString.length() > 0) {
			String[] fields = searchString.split(";");

			for(String field: fields) {
				String fieldName = field.substring(0, field.indexOf('=')).toLowerCase();
				String searchValue = field.substring(field.indexOf('=') + 1);
				
				searchMap.put(fieldName,searchValue);
			}

		}
		if(searchMap.containsKey("from") )
		{
			LocalDateTime from=SearchUtils.stringToDate(searchMap.get("from").toString()).toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			searchMap.put("from",from);
		}
		else
		{
			searchMap.put("from",LocalDateTime.of(1970, Month.JANUARY, 1, 10, 10, 30));
		}
		if(searchMap.containsKey("to") )
		{
			LocalDateTime to=SearchUtils.stringToDate(searchMap.get("to").toString()).toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			searchMap.put("to",to);
		}
		else
		{
			searchMap.put("to",LocalDateTime.now());
		}

		return searchMap;
	}

}

