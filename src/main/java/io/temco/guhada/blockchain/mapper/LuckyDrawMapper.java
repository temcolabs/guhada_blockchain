package io.temco.guhada.blockchain.mapper;

import io.temco.guhada.blockchain.model.LuckyDrawModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Shin Han
 * Since 2019-11-18
 */
@Repository
@Mapper
public interface LuckyDrawMapper {



    @Select("SELECT\n" +
            "    #{dealId} AS dealId,\n" +
            "    user.id AS userId,\n" +
            "    user.email AS userEmail\n" +
            "FROM user\n" +
            "where user.id = #{userId};")
    LuckyDrawModel getUser(@Param("dealId") long dealId,
                                 @Param("userId") long userId);

}
