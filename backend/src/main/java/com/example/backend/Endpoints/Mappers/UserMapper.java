package com.example.backend.Endpoints.Mappers;


import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Entity.ApplicationUser;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper
public abstract class UserMapper {

    public abstract UserDetailDto entityToUserDetailDto(ApplicationUser applicationUser);
    public abstract List<UserDetailDto> entityListToUserDetailDtoList(List<ApplicationUser> users);


}
