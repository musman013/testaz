package com.nfinity.ll.testaz.application.authorization.user;

import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchFields;
import com.nfinity.ll.testaz.commons.search.SearchUtils;
import com.nfinity.ll.testaz.application.authorization.user.dto.*;
import com.nfinity.ll.testaz.domain.authorization.user.IUserManager;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.model.QUserEntity;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.*;
import org.apache.commons.lang3.StringUtils;
import java.util.*;

@Service
@Validated
public class UserAppService implements IUserAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IUserManager _userManager;

	@Autowired
	private UserMapper mapper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateUserOutput create(CreateUserInput input) {

		UserEntity user = mapper.createUserInputToUserEntity(input);		
		UserEntity createdUser = _userManager.create(user);
		return mapper.userEntityToCreateUserOutput(createdUser);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="User", key = "#p0")
	public UpdateUserOutput update(Long userId, UpdateUserInput input) {

		UserEntity user = mapper.updateUserInputToUserEntity(input);
		UserEntity updatedUser = _userManager.update(user);
		return mapper.userEntityToUpdateUserOutput(updatedUser);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="User", key = "#p0")
	public void delete(Long userId) {

		UserEntity existing = _userManager.findById(userId) ;
		_userManager.delete(existing);
	
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "User", key = "#p0")
	public FindUserByIdOutput findById(Long userId) {

		UserEntity foundUser = _userManager.findById(userId);
		if (foundUser == null)  
			return null ; 
 	   
 	   FindUserByIdOutput output=mapper.userEntityToFindUserByIdOutput(foundUser); 
		return output;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "User", key = "#p0")
	public FindUserByNameOutput findByUserName(String userName) {

		UserEntity foundUser = _userManager.findByUserName(userName);
		if (foundUser == null) {
			return null;
		}
		return  mapper.userEntityToFindUserByNameOutput(foundUser);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "User", key = "{'allFields', #p0}")
	public FindUserWithAllFieldsByIdOutput findWithAllFieldsById(Long userId) {

		UserEntity foundUser = _userManager.findById(userId);
		if (foundUser == null)  
			return null ; 
 	   
 	    FindUserWithAllFieldsByIdOutput output=mapper.userEntityToFindUserWithAllFieldsByIdOutput(foundUser); 
		return output;
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "User")
	public List<FindUserByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<UserEntity> foundUser = _userManager.findAll(search(search), pageable);
		List<UserEntity> userList = foundUser.getContent();
		Iterator<UserEntity> userIterator = userList.iterator(); 
		List<FindUserByIdOutput> output = new ArrayList<>();

		while (userIterator.hasNext()) {
			output.add(mapper.userEntityToFindUserByIdOutput(userIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QUserEntity user= QUserEntity.userEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(user, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
		if(!(
		 list.get(i).replace("%20","").trim().equals("roleId") ||
		
		 list.get(i).replace("%20","").trim().equals("accessFailedCount") ||
		 list.get(i).replace("%20","").trim().equals("authenticationSource") ||
		 list.get(i).replace("%20","").trim().equals("emailAddress") ||
		 list.get(i).replace("%20","").trim().equals("emailConfirmationCode") ||
		 list.get(i).replace("%20","").trim().equals("firstName") ||
		 list.get(i).replace("%20","").trim().equals("id") ||
		 list.get(i).replace("%20","").trim().equals("isEmailConfirmed") ||
		 list.get(i).replace("%20","").trim().equals("isLockoutEnabled") ||
		 list.get(i).replace("%20","").trim().equals("isPhoneNumberConfirmed") ||
		 list.get(i).replace("%20","").trim().equals("lastLoginTime") ||
		 list.get(i).replace("%20","").trim().equals("lastName") ||
		 list.get(i).replace("%20","").trim().equals("lockoutEndDateUtc") ||
		 list.get(i).replace("%20","").trim().equals("isActive") ||
		 list.get(i).replace("%20","").trim().equals("password") ||
		 list.get(i).replace("%20","").trim().equals("passwordResetCode") ||
		 list.get(i).replace("%20","").trim().equals("phoneNumber") ||
		 list.get(i).replace("%20","").trim().equals("profilePictureId") ||
		 list.get(i).replace("%20","").trim().equals("userrole") ||
		 list.get(i).replace("%20","").trim().equals("isTwoFactorEnabled") ||
		 list.get(i).replace("%20","").trim().equals("userName") ||
		 list.get(i).replace("%20","").trim().equals("userpermission")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QUserEntity user, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("accessFailedCount")) {
				if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(user.accessFailedCount.eq(Integer.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(user.accessFailedCount.ne(Integer.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(user.accessFailedCount.between(Integer.valueOf(details.getValue().getStartingValue()), Long.valueOf(details.getValue().getEndingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getStartingValue()))
                	   builder.and(user.accessFailedCount.goe(Integer.valueOf(details.getValue().getStartingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(user.accessFailedCount.loe(Integer.valueOf(details.getValue().getEndingValue())));
				}
			}
            if(details.getKey().replace("%20","").trim().equals("authenticationSource")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.authenticationSource.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.authenticationSource.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.authenticationSource.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("emailAddress")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.emailAddress.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.emailAddress.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.emailAddress.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("emailConfirmationCode")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.emailConfirmationCode.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.emailConfirmationCode.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.emailConfirmationCode.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("firstName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.firstName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.firstName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.firstName.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("isEmailConfirmed")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isEmailConfirmed.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isEmailConfirmed.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
			if(details.getKey().replace("%20","").trim().equals("isLockoutEnabled")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isLockoutEnabled.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isLockoutEnabled.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
            if(details.getKey().replace("%20","").trim().equals("isPhoneNumberConfirmed")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.isPhoneNumberConfirmed.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.isPhoneNumberConfirmed.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.isPhoneNumberConfirmed.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("lastLoginTime")) {
				if(details.getValue().getOperator().equals("equals") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(user.lastLoginTime.eq(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(user.lastLoginTime.ne(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   Date startDate= SearchUtils.stringToDate(details.getValue().getStartingValue());
				   Date endDate= SearchUtils.stringToDate(details.getValue().getEndingValue());
				   if(startDate!=null && endDate!=null)	 
					   builder.and(user.lastLoginTime.between(startDate,endDate));
				   else if(endDate!=null)
					   builder.and(user.lastLoginTime.loe(endDate));
                   else if(startDate!=null)
                	   builder.and(user.lastLoginTime.goe(startDate));  
                 }
                   
			}
            if(details.getKey().replace("%20","").trim().equals("lastName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.lastName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.lastName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.lastName.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("lockoutEndDateUtc")) {
				if(details.getValue().getOperator().equals("equals") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(user.lockoutEndDateUtc.eq(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(user.lockoutEndDateUtc.ne(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   Date startDate= SearchUtils.stringToDate(details.getValue().getStartingValue());
				   Date endDate= SearchUtils.stringToDate(details.getValue().getEndingValue());
				   if(startDate!=null && endDate!=null)	 
					   builder.and(user.lockoutEndDateUtc.between(startDate,endDate));
				   else if(endDate!=null)
					   builder.and(user.lockoutEndDateUtc.loe(endDate));
                   else if(startDate!=null)
                	   builder.and(user.lockoutEndDateUtc.goe(startDate));  
                 }
                   
			}
			if(details.getKey().replace("%20","").trim().equals("isActive")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isActive.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isActive.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
            if(details.getKey().replace("%20","").trim().equals("password")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.password.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.password.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.password.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("passwordResetCode")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.passwordResetCode.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.passwordResetCode.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.passwordResetCode.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("phoneNumber")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.phoneNumber.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.phoneNumber.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.phoneNumber.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("profilePictureId")) {
				if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(user.profilePictureId.eq(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(user.profilePictureId.ne(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(user.profilePictureId.between(Long.valueOf(details.getValue().getStartingValue()), Long.valueOf(details.getValue().getEndingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getStartingValue()))
                	   builder.and(user.profilePictureId.goe(Long.valueOf(details.getValue().getStartingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(user.profilePictureId.loe(Long.valueOf(details.getValue().getEndingValue())));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("isTwoFactorEnabled")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isTwoFactorEnabled.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isTwoFactorEnabled.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
            if(details.getKey().replace("%20","").trim().equals("userName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.userName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.userName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.userName.ne(details.getValue().getSearchValue()));
			}
		}
	
		return builder;
	}
	
	public Map<String,String> parseUserpermissionJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("userId", keysString);
		return joinColumnMap;
	}
	
	public Map<String,String> parseUserroleJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("userId", keysString);
		return joinColumnMap;
		
	}

}

