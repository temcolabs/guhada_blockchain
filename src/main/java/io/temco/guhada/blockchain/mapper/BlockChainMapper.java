package io.temco.guhada.blockchain.mapper;

import io.temco.guhada.blockchain.model.response.UnregisteredDeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shin Han
 * Since 2019-05-16
 */
@Repository
@Mapper
public interface BlockChainMapper {

    @Select("SELECT\n" +
            "deal.id AS dealId,\n" +
            "deal.product_id AS productId,\n" +
            "deal.name AS productName,\n" +
            "user.nickname AS seller,\n" +
            "deal.sell_price AS price,\n" +
            "brand.name_ko AS brandName,\n" +
            "guhada_transact.guhada_transact_id\n" +
            "FROM deal\n" +
            "INNER JOIN product ON product.id = deal.product_id\n" +
            "INNER JOIN brand ON brand.id = product.brand_id\n" +
            "INNER JOIN user ON user.id = deal.seller_id\n" +
            "LEFT JOIN guhada_transact ON deal.id = guhada_transact.deal_id\n" +
            "WHERE guhada_transact.guhada_transact_id is null\n" +
            "LIMIT 25;\n")
    List<UnregisteredDeal> getUnregisteredDeal();

}
