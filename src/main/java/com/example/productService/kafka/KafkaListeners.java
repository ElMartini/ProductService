package com.example.productService.kafka;

import com.example.productService.dto.FromMainBasketDTO;
import com.example.productService.database.mysql.model.Product;
import com.example.productService.database.mysql.service.ProductServiceImpl;
import com.example.productService.dto.ProductToExecuteDTO;
import com.example.productService.dto.ToOrderBasketDTO;
import com.example.productService.dto.ProductDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service
public class KafkaListeners {
    ObjectMapper objectMapper = new ObjectMapper();
    ProductServiceImpl productService = new ProductServiceImpl();
    private final KafkaProducer kafkaProducer;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public KafkaListeners(KafkaProducer kafkaProducer, KafkaTemplate<String, Object> kafkaTemplate) throws SQLException, ClassNotFoundException {
        this.kafkaProducer=kafkaProducer;
        this.kafkaTemplate=kafkaTemplate;
    }


    @KafkaListener(topics = "kafkaProduct", groupId = "productOrderGroup")
    public void listenToProduct(ConsumerRecord<String, Object> data) throws JsonProcessingException, SQLException, ClassNotFoundException {
        ProductDTO productDTO = objectMapper.readValue(data.value().toString(), ProductDTO.class);
        String action = productDTO.getAction();
        Product product = productDTO.getProduct();

        switch (action) {
            case "ADD" -> productService.addProduct(product);
            case "DELETE" -> productService.deleteProduct(product.getPName());
            case "UPDATE" -> productService.updateProduct(product, product.getPID());
        }

    }


    @KafkaListener(topics = "mainProductBasket", groupId = "mainProductGroup")
    public void createBasket(ConsumerRecord<String, Object> data) throws JsonProcessingException, SQLException, ClassNotFoundException {
        FromMainBasketDTO basketDTO = objectMapper.readValue(data.value().toString(), FromMainBasketDTO.class);
        int productsCount = basketDTO.getpNames().size();
        List<Product> products = productService.selectByName(basketDTO.getpNames());
        for (int i = 0; i < productsCount; i++) {
            products.get(i).setPQuantity(basketDTO.getpQuantity().get(i));
        }
        ToOrderBasketDTO orderSendDTO = new ToOrderBasketDTO();
        orderSendDTO.setProducts(products);
        orderSendDTO.setcID(basketDTO.getcID());

        kafkaProducer.sendBasketToOrderService(orderSendDTO);
    }

    @KafkaListener(topics = "mainProductCheckProducts", groupId = "mainProductGroup")
    public void areProductsAvailable(ConsumerRecord<String, Object> data) throws JsonProcessingException, SQLException, ClassNotFoundException {
        ProductToExecuteDTO productToExecuteDTO = objectMapper.readValue(data.value().toString(), ProductToExecuteDTO.class);
        for(Product p: productToExecuteDTO.getProducts()){
            if(!productService.isProductQuantityInStock(p.getPName(), p.getPQuantity()));
            kafkaProducer.sendAreProductsAvailableResponse(false);
        }
        kafkaProducer.sendAreProductsAvailableResponse(true);
    }




}
