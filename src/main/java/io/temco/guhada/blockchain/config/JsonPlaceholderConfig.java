package io.temco.guhada.blockchain.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.temco.guhada.blockchain.service.retrofit.BenefitApiService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
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


    @Autowired
    private Interceptor jsonPlaceholderInterceptor;

    @Bean("jsonPlaceholderOkHttpClient")
    public OkHttpClient jsonPlaceholderOkHttpClient() {

        return new OkHttpClient.Builder()
                .addInterceptor(jsonPlaceholderInterceptor)
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
    public Retrofit userApiRetrofit(
            @Qualifier("jsonPlaceholderObjectMapper") ObjectMapper jsonPlaceholderObjectMapper,
            @Qualifier("jsonPlaceholderOkHttpClient") OkHttpClient jsonPlaceholderOkHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(benefitApi)
                .addConverterFactory(JacksonConverterFactory.create(jsonPlaceholderObjectMapper))
                .client(jsonPlaceholderOkHttpClient)
                .build();
    }

    @Bean("BenefitApiService")
    public BenefitApiService userApiService(@Qualifier("BenefitApiRetrofit") Retrofit jsonPlaceHolderRetrofit) {
        return jsonPlaceHolderRetrofit.create(BenefitApiService.class);
    }

}
