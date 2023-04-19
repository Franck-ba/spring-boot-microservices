package com.sac.customer.service;

import com.sac.common.models.CustomerDTO;
import com.sac.common.models.CustomerOrdersBean;
import com.sac.customer.entity.CustomerEntity;
import com.sac.customer.entity.CustomerOrdersEntity;
import com.sac.customer.repository.CustomerOrderRepository;
import com.sac.customer.repository.CustomerRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

@Service
public class CustomerService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private Mapper mapper;
    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private CustomerOrderRepository customerOrderRepository;

    private final Timer myTimer;

    public CustomerService(MeterRegistry meterRegistry){
        myTimer = Timer.builder("create.customer.latency").
                description("Latency of creating a new customer").register(meterRegistry);
    }


    public String addCustomer(CustomerDTO customerDTO) {
        long startTime = System.nanoTime();
        CustomerEntity customerEntity = mapper.map(customerDTO, CustomerEntity.class);
        try {
            customerEntity = customerRepository.save(customerEntity);
        }catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        myTimer.record(endTime - startTime, TimeUnit.NANOSECONDS);
        return "Customer Created Successfully, Your CustomerId is "+customerEntity.getId();
    }

    public String createCustomerOrder(CustomerOrdersBean customerOrders) {
        LOG.debug("Going to create customer order");
        CustomerOrdersEntity customerOrder = mapper.map(customerOrders, CustomerOrdersEntity.class);
        customerOrderRepository.save(customerOrder);
        return "Order Created Successfully !!!";
    }

    public String deleteAllCustomers() {
        customerRepository.deleteAll();
        return "All customers deleted Successfully !!!";
    }

}
