package lost.test.quarkus.mapper;

import lost.test.quarkus.entity.Fighter;
import lost.test.quarkus.model.FighterCreateParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FighterMapper {

    FighterMapper instance = Mappers.getMapper(FighterMapper.class);

    Fighter fromFighterCreate(FighterCreateParam fighterCreateParam);

}
