package com.nfinity.ll.testaz.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.nfinity.ll.testaz.domain.model.RolepermissionEntity;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.model.UserpermissionEntity;
import com.nfinity.ll.testaz.domain.model.UserroleEntity;
@Component
public class SecurityUtils {

    public List<String> getAllPermissionsFromUserAndRole(UserEntity user) {

		List<String> permissions = new ArrayList<>();
        Set<UserroleEntity> ure = user.getUserroleSet();
        Iterator rIterator = ure.iterator();
		while (rIterator.hasNext()) {
            UserroleEntity re = (UserroleEntity) rIterator.next();
            Set<RolepermissionEntity> srp= re.getRole().getRolepermissionSet();
            for (RolepermissionEntity item : srp) {
				permissions.add(item.getPermission().getName());
            }
		}
		
		Set<UserpermissionEntity> spe = user.getUserpermissionSet();
        Iterator pIterator = spe.iterator();
		while (pIterator.hasNext()) {
            UserpermissionEntity pe = (UserpermissionEntity) pIterator.next();
            
            if(permissions.contains(pe.getPermission().getName()) && (pe.getRevoked() != null && pe.getRevoked()))
            {
            	permissions.remove(pe.getPermission().getName());
            }
            if(!permissions.contains(pe.getPermission().getName()) && (pe.getRevoked()==null || !pe.getRevoked()))
            {
            	permissions.add(pe.getPermission().getName());
			
            }
         
		}
		
		return permissions
				.stream()
				.distinct()
				.collect(Collectors.toList());
	}

}
