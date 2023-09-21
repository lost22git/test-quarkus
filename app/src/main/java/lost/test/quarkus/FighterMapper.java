package lost.test.quarkus;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FighterMapper {

    FighterMapper instance = Mappers.getMapper(FighterMapper.class);

    Fighter fromFighterCreate(FighterCreate fighterCreate);
}
