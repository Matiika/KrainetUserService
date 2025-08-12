package com.Krainet.UserService.mapper;
import com.Krainet.UserService.dto.UserCreateDTO;
import com.Krainet.UserService.dto.UserDTO;
import com.Krainet.UserService.dto.UserUpdateDTO;
import com.Krainet.UserService.model.User;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface UserMapStructMapper {

    UserDTO toDTO(User user);

    User fromCreateDTO(UserCreateDTO dto);

    @AfterMapping
    default void setDefaultRole(UserCreateDTO dto, @MappingTarget User user) {
        user.setRole("USER");
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget User user);

}
