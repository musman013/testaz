package com.nfinity.ll.testaz.application.vets;

import org.mapstruct.Mapper;
import com.nfinity.ll.testaz.application.vets.dto.*;
import com.nfinity.ll.testaz.domain.model.VetsEntity;

@Mapper(componentModel = "spring")
public interface VetsMapper {

   VetsEntity createVetsInputToVetsEntity(CreateVetsInput vetsDto);
   
   CreateVetsOutput vetsEntityToCreateVetsOutput(VetsEntity entity);

   VetsEntity updateVetsInputToVetsEntity(UpdateVetsInput vetsDto);

   UpdateVetsOutput vetsEntityToUpdateVetsOutput(VetsEntity entity);

   FindVetsByIdOutput vetsEntityToFindVetsByIdOutput(VetsEntity entity);


}
