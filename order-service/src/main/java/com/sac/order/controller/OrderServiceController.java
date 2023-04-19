package com.sac.order.controller;


import com.sac.common.models.OrderBean;
import com.sac.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.annotation.Timed;

@RestController
@Timed
@RequestMapping(value = "/v1/api/order")
public class OrderServiceController {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Timed("create.order.latency")
    public String createOrder(@RequestBody OrderBean orderBean) {
        LOG.debug("Going to create order");
        return orderService.createOrder(orderBean);
    }
}
