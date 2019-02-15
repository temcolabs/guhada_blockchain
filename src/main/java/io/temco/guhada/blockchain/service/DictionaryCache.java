package io.temco.guhada.blockchain.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import io.temco.guhada.blockchain.dao.BrandDAO;
import io.temco.guhada.blockchain.dao.ModelDAO;
import io.temco.guhada.blockchain.dao.SellerDAO;
import io.temco.guhada.blockchain.model.Brand;
import io.temco.guhada.blockchain.model.Model;
import io.temco.guhada.blockchain.model.Seller;
import io.temco.guhada.blockchain.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public final class DictionaryCache {

    private static final long ERROR_ID = 1L;

    private static final Logger log = LogManager.getLogger(DictionaryCache.class);

    @Autowired
    private BrandDAO brandDao;
    @Autowired
    private ModelDAO modelDao;
    @Autowired
    private SellerDAO sellerDao;

    private LoadingCache<String, Long> brandNameIdCache;
    private Map<Long, String> brandIdNameCache;
    private LoadingCache<String, Long> modelNameIdCache;
    private Map<Long, String> modelIdNameCache;
    private LoadingCache<String, Seller> sellerEmailCache;
    private Map<Long, String> sellerIdCache;
    @PostConstruct
    public void init(){
        // init code goes here
        brandNameIdCache = CacheBuilder.newBuilder().
                maximumSize(500).
                expireAfterAccess(10, TimeUnit.DAYS).
                removalListener((RemovalListener<String, Long>) removal->
                    brandIdNameCache.remove(removal.getValue())).
                build(new CacheLoader<String, Long>() {
                    @Override
                    public Long load(@Nonnull String name)  {
                        return getBrandIdFromDatabase(name);
                    }
                });
        brandIdNameCache = new HashMap<>();

        modelNameIdCache = CacheBuilder.newBuilder().
                maximumSize(500).
                expireAfterAccess(10, TimeUnit.DAYS).
                removalListener((RemovalListener<String, Long>) removal->
                        modelIdNameCache.remove(removal.getValue())).
                build(new CacheLoader<String, Long>() {
                    @Override
                    public Long load(@Nonnull String name)  {
                        return getModelIdFromDatabase(name);
                    }
                });
        modelIdNameCache = new HashMap<>();

        sellerEmailCache = CacheBuilder.newBuilder().
                maximumSize(500).
                expireAfterAccess(10, TimeUnit.DAYS).
                removalListener((RemovalListener<String, Seller>) removal->
                        sellerIdCache.remove(removal.getValue().getId())).
                build(new CacheLoader<String, Seller>() {
                    @Override
                    public Seller load(@Nonnull String email)  {
                        return getSellerFromDatabase(email);
                    }
                });
        sellerIdCache = new HashMap<>();
        initLoad();
    }

    private void initLoad() {
        List<Brand> brands = brandDao.findAll(PageRequest.of(0, 10)).getContent();
        brandNameIdCache.invalidateAll();
        brandNameIdCache.putAll(brands.parallelStream().collect(Collectors.toMap(Brand::getName, Brand::getId)));
        brandIdNameCache.putAll(brands.parallelStream().collect(Collectors.toMap(Brand::getId, Brand::getName)));

        List<Model> models = modelDao.findAll(PageRequest.of(0, 10)).getContent();
        modelNameIdCache.invalidateAll();
        modelNameIdCache.putAll(models.parallelStream().collect(Collectors.toMap(Model::getName, Model::getId)));
        modelIdNameCache.putAll(models.parallelStream().collect(Collectors.toMap(Model::getId, Model::getName)));

        List<Seller> sellers = sellerDao.findAll(PageRequest.of(0, 10)).getContent();
        sellerEmailCache.invalidateAll();
        sellerEmailCache.putAll(sellers.parallelStream().collect(Collectors.toMap(Seller::getEmail, Function.identity())));
        sellerIdCache.putAll(sellers.parallelStream().collect(Collectors.toMap(Seller::getId, Seller::getEmail)));

    }

    private Seller getSellerFromDatabase(String email) {
        String _email = StringUtil.noneIfEmpty(email).toLowerCase();
        Optional<Seller> seller = sellerDao.findByEmail(_email);
        return seller.orElseGet(() -> sellerDao.save(new Seller(_email)));
    }

    private Long getModelIdFromDatabase(String name) {
        String _name = StringUtil.noneIfEmpty(name).toLowerCase();
        Optional<Model> model = modelDao.findByName(_name);
        if(model.isPresent()) return model.get().getId();
        return modelDao.save(new Model(_name)).getId();
    }

    private Long getBrandIdFromDatabase(String name) {
        String _name = StringUtil.noneIfEmpty(name).toLowerCase();
        Optional<Brand> brand = brandDao.findByName(_name);
        if(brand.isPresent()) return brand.get().getId();
        return brandDao.save(new Brand(_name)).getId();
    }

    public long getBrandId(String name) {
        try {
            return brandNameIdCache.get(StringUtil.noneIfEmpty(name).toLowerCase());
        } catch (ExecutionException e) {
            log.error("Failed to get brand id for {}", name);
            return ERROR_ID;
        }
    }

    public String getBrandName(Long id){
        return StringUtil.emptyIfNull(brandIdNameCache.get(id));
    }

    public long getModelId(String name) {
        try {
            return modelNameIdCache.get(StringUtil.noneIfEmpty(name).toLowerCase());
        } catch (ExecutionException e) {
            log.error("Failed to get model id for {}", name);
            return ERROR_ID;
        }
    }

    public String getModelName(Long id){
        return StringUtil.emptyIfNull(modelIdNameCache.get(id));
    }

    public long getSellerId(String email) {
        try {
            return sellerEmailCache.get(StringUtil.noneIfEmpty(email).toLowerCase()).getId();
        } catch (ExecutionException e) {
            log.error("Failed to get brand id for {}", email);
            return ERROR_ID;
        }
    }
    public String getSellerEmail(Long id){
        return StringUtil.emptyIfNull(sellerIdCache.get(id));
    }

    public Optional<Seller> getSeller(String email) {
        try {
            return Optional.of(sellerEmailCache.get(StringUtil.noneIfEmpty(email).toLowerCase()));
        } catch (ExecutionException e) {
            log.error("Failed to get brand id for {}", email);
            return Optional.empty();
        }
    }
}
