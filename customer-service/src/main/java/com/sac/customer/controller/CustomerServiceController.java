package com.sac.customer.controller;

import com.sac.common.models.CustomerDTO;
import com.sac.customer.service.CustomerService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@Timed
@RequestMapping(value = "/v1/api/customer")
public class CustomerServiceController {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Timed("create.customer.latency")
    public String addCustomer(@RequestBody CustomerDTO customer) {
            LOG.debug("==============Customer created============================");
            return customerService.addCustomer(customer);
    }

    @RequestMapping(value = "/delete/all", method = RequestMethod.DELETE)
    public String deleteAllCustomers() {
       return customerService.deleteAllCustomers();
    }
}
