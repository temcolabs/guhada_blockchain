package io.temco.guhada.blockchain.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.temco.guhada.blockchain.service.retrofit.BenefitApiService;
import io.temco.guhada.blockchain.service.retrofit.ProductApiService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Shin Han
 * Since 2019-03-22
 */
@Configuration
public class JsonPlaceholderConfig {

    @Value("${retrofit.benefit-api}")
    private String benefitApi;


    @Value("${retrofit.product-api}")
    private String productApi;


    @Autowired
    private Interceptor jsonPlaceholderInterceptor;

    @Bean("jsonPlaceholderOkHttpClient")
    public OkHttpClient jsonPlaceholderOkHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(jsonPlaceholderInterceptor)
                .addInterceptor(interceptor)
                .build();
    }

    @Bean("jsonPlaceholderObjectMapper")
    public ObjectMapper jsonPlaceholderObjectMapper() {

        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .modules(new JavaTimeModule())
                .build();
    }


    @Bean("BenefitApiRetrofit")
    public Retrofit benefitApiRetrofit(
            @Qualifier("jsonPlaceholderObjectMapper") ObjectMapper jsonPlaceholderObjectMapper,
            @Qualifier("jsonPlaceholderOkHttpClient") OkHttpClient jsonPlaceholderOkHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(benefitApi)
                .addConverterFactory(JacksonConverterFactory.create(jsonPlaceholderObjectMapper))
                .client(jsonPlaceholderOkHttpClient)
                .build();
    }

    @Bean("ProductApiRetrofit")
    public Retrofit productApiRetrofit(
            @Qualifier("jsonPlaceholderObjectMapper") ObjectMapper jsonPlaceholderObjectMapper,
            @Qualifier("jsonPlaceholderOkHttpClient") OkHttpClient jsonPlaceholderOkHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(productApi)
                .addConverterFactory(JacksonConverterFactory.create(jsonPlaceholderObjectMapper))
                .client(jsonPlaceholderOkHttpClient)
                .build();
    }

    @Bean("BenefitApiService")
    public BenefitApiService benefitApiService(@Qualifier("BenefitApiRetrofit") Retrofit jsonPlaceHolderRetrofit) {
        return jsonPlaceHolderRetrofit.create(BenefitApiService.class);
    }

    @Bean("ProductApiService")
    public ProductApiService productApiService(@Qualifier("ProductApiRetrofit") Retrofit jsonPlaceHolderRetrofit) {
        return jsonPlaceHolderRetrofit.create(ProductApiService.class);
    }

}
