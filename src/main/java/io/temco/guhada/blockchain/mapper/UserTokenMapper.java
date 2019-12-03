package io.temco.guhada.blockchain.mapper;

import io.temco.guhada.blockchain.model.AirdropUser;
import io.temco.guhada.framework.model.blockchain.response.UserTokenItemResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
            "   changed_balance AS changedBalance, \n" +
            "   action_type AS tokenActionType \n" +
            "   FROM user_token_history\n" +
            "   where user_id = #{userId} and" +
            "   token_name = #{tokenName} \n" +
            "   ORDER BY created_at desc\n"+
            "   LIMIT #{startIndex} , #{unitPerPage};")
    List<UserTokenItemResponse> getMyTokenInfo(@Param("userId") Long userId,
                                               @Param("tokenName") String tokenName,
                                               @Param("startIndex") int startIndex,
                                               @Param("unitPerPage")int unitPerPage);

    @Select("SELECT \n" +
            "    COUNT(id)\n" +
            "FROM user_token_history\n" +
            "where user_id = #{userId};")
    int getMyTokenInfoCount(@Param("userId") long userId);


    @Select("select\n" +
            "       coinone.id AS id,\n" +
            "       user.id AS userId,\n" +
            "       coinone.phone AS phone,\n" +
            "       coinone.amount AS amount,\n" +
            "       coinone.is_success AS success\n" +
            "from coinone\n" +
            "inner join user on user.mobile = coinone.phone\n" +
            "where coinone.is_success = 0;\n")
    List<AirdropUser> getAirdropUsers();


    @Update("UPDATE \n" +
            "coinone \n" +
            "SET is_success = '1' \n" +
            "WHERE id = #{id}")
    void updateSuccess(@Param("id") int id);

}
