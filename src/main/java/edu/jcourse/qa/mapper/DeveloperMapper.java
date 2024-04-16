package edu.jcourse.qa.mapper;

import edu.jcourse.qa.dto.DeveloperDto;
import edu.jcourse.qa.entity.Developer;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DeveloperMapper {

    DeveloperDto toDto(Developer developer);

    @InheritInverseConfiguration
    Developer toEntity(DeveloperDto developerDto);

    Developer copyDtoToEntity(DeveloperDto developerDto, @MappingTarget Developer developer);

    List<DeveloperDto> toDtoList(List<Developer> developers);
}
