package lost.test.quarkus;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FighterMapper {

    static FighterMapper instance = Mappers.getMapper(FighterMapper.class);

    Fighter fromFighterCreate(FighterCreate fighterCreate);
}
