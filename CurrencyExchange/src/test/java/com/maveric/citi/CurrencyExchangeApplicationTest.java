package com.maveric.citi;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Info;

import java.util.List;

import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.internal.InheritingConfiguration;
import org.modelmapper.internal.TypeResolvingList;
import org.modelmapper.spi.NameTokenizer;
import org.modelmapper.spi.ValueReader;
import org.modelmapper.spi.ValueWriter;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Ignore
class CurrencyExchangeApplicationTest {

    /**
     * Method under test: {@link CurrencyExchangeApplication#restTemplate()}
     */
    @Test
    void testRestTemplate() {
        RestTemplate actualRestTemplateResult = (new CurrencyExchangeApplication()).restTemplate();
        List<ClientHttpRequestInitializer> clientHttpRequestInitializers = actualRestTemplateResult
                .getClientHttpRequestInitializers();
        assertTrue(clientHttpRequestInitializers.isEmpty());
        UriTemplateHandler uriTemplateHandler = actualRestTemplateResult.getUriTemplateHandler();
        assertTrue(uriTemplateHandler instanceof DefaultUriBuilderFactory);
        assertTrue(actualRestTemplateResult.getRequestFactory() instanceof SimpleClientHttpRequestFactory);
        assertTrue(actualRestTemplateResult.getErrorHandler() instanceof DefaultResponseErrorHandler);
        List<HttpMessageConverter<?>> messageConverters = actualRestTemplateResult.getMessageConverters();
        assertEquals(6, messageConverters.size());
        assertEquals(clientHttpRequestInitializers, actualRestTemplateResult.getInterceptors());
        assertTrue(((DefaultUriBuilderFactory) uriTemplateHandler).getDefaultUriVariables().isEmpty());
        assertEquals(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT,
                ((DefaultUriBuilderFactory) uriTemplateHandler).getEncodingMode());
        assertEquals(1, messageConverters.get(2).getSupportedMediaTypes().size());
        assertEquals(2, messageConverters.get(1).getSupportedMediaTypes().size());
        assertEquals(5, ((AllEncompassingFormHttpMessageConverter) messageConverters.get(3)).getPartConverters().size());
        assertFalse(((Jaxb2RootElementHttpMessageConverter) messageConverters.get(4)).isSupportDtd());
        assertFalse(((Jaxb2RootElementHttpMessageConverter) messageConverters.get(4)).isProcessExternalEntities());
        ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) messageConverters.get(5)).getObjectMapper();
        assertNull(objectMapper.getPropertyNamingStrategy());
        assertTrue(objectMapper.getPolymorphicTypeValidator() instanceof LaissezFaireSubTypeValidator);
        assertSame(objectMapper.getJsonFactory(), objectMapper.getFactory());
        assertTrue(objectMapper.getDeserializationContext() instanceof DefaultDeserializationContext.Impl);
        assertTrue(objectMapper.getDateFormat() instanceof StdDateFormat);
        assertTrue(objectMapper.getSubtypeResolver() instanceof StdSubtypeResolver);
        assertTrue(objectMapper.getSerializerFactory() instanceof BeanSerializerFactory);
        assertTrue(objectMapper.getSerializerProviderInstance() instanceof DefaultSerializerProvider.Impl);
        assertTrue(objectMapper.getVisibilityChecker() instanceof VisibilityChecker.Std);
        assertTrue(objectMapper.getSerializerProvider() instanceof DefaultSerializerProvider.Impl);
    }

    /**
     * Method under test: {@link CurrencyExchangeApplication#usersMicroserviceOpenAPI()}
     */
    @Test
    void testUsersMicroserviceOpenAPI() {
        OpenAPI actualUsersMicroserviceOpenAPIResult = (new CurrencyExchangeApplication()).usersMicroserviceOpenAPI();
        assertNull(actualUsersMicroserviceOpenAPIResult.getWebhooks());
        assertNull(actualUsersMicroserviceOpenAPIResult.getExtensions());
        assertEquals(1, actualUsersMicroserviceOpenAPIResult.getSecurity().size());
        assertNull(actualUsersMicroserviceOpenAPIResult.getServers());
        assertEquals(SpecVersion.V30, actualUsersMicroserviceOpenAPIResult.getSpecVersion());
        assertNull(actualUsersMicroserviceOpenAPIResult.getTags());
        assertNull(actualUsersMicroserviceOpenAPIResult.getExternalDocs());
        assertEquals("3.0.1", actualUsersMicroserviceOpenAPIResult.getOpenapi());
        assertNull(actualUsersMicroserviceOpenAPIResult.getPaths());
        Info info = actualUsersMicroserviceOpenAPIResult.getInfo();
        assertEquals("CITI Currency Exchange Portal", info.getDescription());
        assertNull(info.getContact());
        Components components = actualUsersMicroserviceOpenAPIResult.getComponents();
        assertNull(components.getRequestBodies());
        assertNull(components.getParameters());
        assertNull(components.getLinks());
        assertNull(components.getHeaders());
        assertNull(components.getExtensions());
        assertNull(components.getExamples());
        assertNull(components.getCallbacks());
        assertEquals("1.0", info.getVersion());
        assertNull(info.getTermsOfService());
        assertNull(components.getSchemas());
        assertNull(components.getResponses());
        assertNull(info.getLicense());
        assertNull(info.getSummary());
        assertEquals("Currency Exchange Portal", info.getTitle());
        assertEquals(1, components.getSecuritySchemes().size());
        assertNull(info.getExtensions());
    }

    /**
     * Method under test: {@link CurrencyExchangeApplication#modelMapper()}
     */
    @Test
    void testModelMapper() {
        ModelMapper actualModelMapperResult = (new CurrencyExchangeApplication()).modelMapper();
        assertTrue(actualModelMapperResult.getTypeMaps().isEmpty());
        Configuration configuration = actualModelMapperResult.getConfiguration();
        assertEquals(15, configuration.getConverters().size());
        assertNull(configuration.getPropertyCondition());
        assertEquals(Configuration.AccessLevel.PUBLIC, configuration.getFieldAccessLevel());
        NameTokenizer expectedSourceNameTokenizer = configuration.getDestinationNameTokenizer();
        assertSame(expectedSourceNameTokenizer, configuration.getSourceNameTokenizer());
        List<ValueWriter<?>> valueWriters = configuration.getValueWriters();
        assertTrue(valueWriters instanceof TypeResolvingList);
        List<ValueReader<?>> valueReaders = configuration.getValueReaders();
        assertTrue(valueReaders instanceof TypeResolvingList);
        assertEquals(Configuration.AccessLevel.PUBLIC, configuration.getMethodAccessLevel());
        assertSame(valueReaders, ((InheritingConfiguration) configuration).valueAccessStore.getValueReaders());
        assertSame(valueWriters, ((InheritingConfiguration) configuration).valueMutateStore.getValueWriters());
    }

}