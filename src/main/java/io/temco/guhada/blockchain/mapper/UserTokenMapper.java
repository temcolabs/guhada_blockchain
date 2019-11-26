package io.temco.guhada.blockchain.mapper;

import io.temco.guhada.blockchain.model.LuckyDrawModel;
import io.temco.guhada.framework.model.blockchain.response.UserTokenItemResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shin Han
 * Since 2019-11-25
 */
@Repository
@Mapper
public interface UserTokenMapper {


    @Select("SELECT \n" +
            "   ROUND(UNIX_TIMESTAMP(created_at) * 1000) AS completeTimestamp, \n" +
            "   changed_balance AS changedBalance \n" +
            "   FROM user_token_history\n" +
            "   where user_id = #{userId}\n" +
            "   ORDER BY created_at desc\n"+
            "   LIMIT #{startIndex} , #{unitPerPage};")
    List<UserTokenItemResponse> getMyTokenInfo(@Param("userId") Long userId,
                                               @Param("startIndex") int startIndex,
                                               @Param("unitPerPage")int unitPerPage);

    @Select("SELECT \n" +
            "    COUNT(id)\n" +
            "FROM user_token_history\n" +
            "where user_id = #{userId};")
    int getMyTokenInfoCount(@Param("userId") long userId);
}
