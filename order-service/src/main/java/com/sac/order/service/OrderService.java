package com.sac.order.service;


import com.sac.common.enums.OrderEnums;
import com.sac.common.models.ItemsBean;
import com.sac.common.models.OrderBean;
import com.sac.order.entity.ItemEntity;
import com.sac.order.entity.OrderEntity;
import com.sac.order.events.handlers.EventHandler;
import com.sac.order.repository.OrderRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.Timer;

@Service
public class OrderService {
    @Resource
    private OrderRepository orderRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private EventHandler eventHandler;

    private final Timer myTimer;

    public OrderService(MeterRegistry meterRegistry){
        myTimer = Timer.builder("create.order.latency").
                description("Latency of creating a new order").register(meterRegistry);
    }

    public String createOrder(OrderBean orderBean) {
        long startTime = System.nanoTime();
        try {
            OrderEntity orderEntity = createRequiredOrderDTOFromBean(orderBean);
            orderEntity = orderRepository.save(orderEntity);
            eventHandler.publishEvent(orderEntity);
        }catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        myTimer.record(endTime - startTime, TimeUnit.NANOSECONDS);
        return  "Order created successfully !!!";
    }

    private OrderEntity createRequiredOrderDTOFromBean(OrderBean orderBean) {
        OrderEntity orderEntity = mapper.map(orderBean, OrderEntity.class);
        orderEntity.getItemEntities().clear();
        Set<ItemsBean> itemBeanSet = orderBean.getItems();
        for (ItemsBean itemsBean : itemBeanSet) {
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setItemName(itemsBean.getItemName());
            itemEntity.setOrderEntity(orderEntity);
            orderEntity.getItemEntities().add(itemEntity);
        }
        orderEntity.setStatus(OrderEnums.PENDING);
        return orderEntity;
    }

    public void updateOrderStatus(Long orderId, OrderEnums orderStatus) {
        Optional<OrderEntity> order = orderRepository.findById(orderId);
        order.ifPresent(value -> value.setStatus(orderStatus));
        order.ifPresent(value -> orderRepository.save(value));

    }

}
