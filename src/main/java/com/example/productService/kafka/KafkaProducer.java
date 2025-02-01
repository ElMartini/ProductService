package com.example.productService.kafka;

import com.example.productService.dto.ToOrderBasketDTO;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@NoArgsConstructor
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendBasketToOrderService(ToOrderBasketDTO toOrderBasketDTO){
        kafkaTemplate.send("productOrderBasket", toOrderBasketDTO);
    }

    public void sendAreProductsAvailableResponse(boolean response){
        kafkaTemplate.send("productMainCheckProducts", response);
    }
}
