package com.nfinity.ll.testaz.application.authorization.rolepermission;

import java.util.List;

import com.nfinity.ll.testaz.domain.model.RolepermissionId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.application.authorization.rolepermission.dto.*;

@Service
public interface IRolepermissionAppService {

	CreateRolepermissionOutput create(CreateRolepermissionInput rolepermission);

    void delete(RolepermissionId rolepermissionId);

    UpdateRolepermissionOutput update(RolepermissionId rolepermissionId , UpdateRolepermissionInput input);

    FindRolepermissionByIdOutput findById(RolepermissionId rolepermissionId);

    List<FindRolepermissionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
	
	public RolepermissionId parseRolepermissionKey(String keysString);
	
    //Permission
    GetPermissionOutput getPermission(RolepermissionId rolepermissionId);
    
    //Role
    GetRoleOutput getRole(RolepermissionId rolepermissionId);
}
