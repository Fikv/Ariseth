package fikv.ariseth.mappers;

import fikv.ariseth.entities.User;
import fikv.ariseth.records.UserRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.Date;

@Mapper(componentModel = "spring",
        imports = {Instant.class, Date.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTimestamp", expression = "java(Date.from(Instant.now()))")
    User toEntity(UserRequestDTO userRequestDTO);
}
