package io.temco.guhada.blockchain.model;


import io.temco.guhada.blockchain.model.client.Upload;
import io.temco.guhada.blockchain.service.DictionaryCache;
import io.temco.guhada.blockchain.util.HashUtil;
import io.temco.guhada.blockchain.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;

@Getter
@Setter
public class Goods implements Serializable {

    private static final long serialVersionUID = 518122122913416359L;

    private Long id;
    private final long sellerId;
    private final long brandId;
    private final long modelId;
    private final String serialNumber;

    private Goods(long sellerId, long brandId, long modelId, String serialNumber) {
        this.sellerId = sellerId;
        this.brandId = brandId;
        this.modelId = modelId;
        this.serialNumber = serialNumber;
    }


    public static Goods from(Upload upload, DictionaryCache cache) throws IOException {
        return new Goods(cache.getSellerId(upload.getEmail()),
                cache.getBrandId(upload.getBrand()),
                cache.getModelId(upload.getModel()),
                upload.getSerialNumber());
    }

    public String getHash() {
        if (id == null) return "0";
        return HashUtil.sha512(toString());
    }

    public long generateProductId() {
        return StringUtil.getLongHasCode("" + brandId + "" + modelId + "" + serialNumber);
    }

    @Override
    public String toString() {
        return "Goods{id:" + id + ",sellerId:" + sellerId + ",brandId:" + brandId + ",modelId:" + modelId +
                ",serialNumber:" + serialNumber + "}";
    }
}

