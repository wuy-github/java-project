package com.project.futabuslines.configurations;

import com.project.futabuslines.dtos.OrderDTO;
import com.project.futabuslines.models.Order;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Khai báo typeMap để không bao giờ map id từ OrderDTO -> Order
        mapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(m -> m.skip(Order::setId));

        return mapper;
    }
}
