package io.temco.guhada.blockchain.dao;

import io.temco.guhada.blockchain.model.Goods;
import io.temco.guhada.blockchain.model.client.GoodsItem;
import io.temco.guhada.blockchain.service.DictionaryCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class GoodsDao  {

    private static final Logger log = LoggerFactory.getLogger(GoodsDao.class);

    @Autowired
    JdbcTemplate jdbcTemplate;


    private final String SQL_INSERT = "insert into GOODS (SELLER_ID, BRAND_ID, MODEL_ID, SERIALNUMBER) " +
                                      " values(?,?,?,?)";
    public Goods insert(Goods goods) throws DataAccessException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT,  Statement.RETURN_GENERATED_KEYS);
                int i = 0;
                ps.setLong(++i, goods.getSellerId());
                ps.setLong(++i, goods.getBrandId());
                ps.setLong(++i, goods.getModelId());
                ps.setString(++i, goods.getSerialNumber());
                return ps;
            }, keyHolder);
            goods.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
            return goods;
        } catch (DataAccessException e) {
            log.error("Failed to insert goods", e);
            throw e;
        }
    }

    @Transactional(readOnly=true)
    public List<GoodsItem> findAll(final DictionaryCache dictionaryCache) {
        return jdbcTemplate.query(
                "select SELLER_ID, BRAND_ID, MODEL_ID, SERIALNUMBER from GOODS",
                (rs, rowNum) -> {
                    GoodsItem item = new GoodsItem();
                    int i = 0;
                    item.setEmail(dictionaryCache.getSellerEmail(rs.getLong(++i)));
                    item.setBrand(dictionaryCache.getBrandName(rs.getLong(++i)));
                    item.setModel(dictionaryCache.getModelName(rs.getLong(++i)));
                    item.setSerialNumber(rs.getString(++i));
                    return item;
                });
    }

}
