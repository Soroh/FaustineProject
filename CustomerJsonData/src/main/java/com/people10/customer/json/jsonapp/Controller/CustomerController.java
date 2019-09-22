package com.people10.customer.json.jsonapp.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.people10.customer.json.jsonapp.model.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CustomerController {

    @RequestMapping(value = {"/","/dashboard"})
    public String dashboard(){

        return "webapps/index";
    }

    @RequestMapping("/customers/{search}")
    @ResponseBody
    public List<Customer> searchCustomer(@PathVariable String search){
            String localSearch=search.toLowerCase();
            List<Customer> customers=customers();
        if(!(search==null||search.trim().equals("")))
           customers= customers.stream()
                   .filter(x->
                           x.getFirst_name().toLowerCase().contains(localSearch)||
                           x.getLast_name().contains(localSearch)||
                           x.getEmail().contains(localSearch)||
                           x.getIp().contains(localSearch)
                   ).collect(Collectors.toList());
        return customers;
    }
    @RequestMapping("/customer/{id}")
    @ResponseBody
    public Customer getCustomer(@PathVariable Integer id){
        Optional<Customer> matchingObject = customers().stream().
                filter(customer->customer.getId()== id).
                findFirst();
            return matchingObject.orElse(null);
    }


    @RequestMapping("/customers")
    @ResponseBody
    public List<Customer> customers( ) {
        List<Customer> customers= new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Customer>> typeReference = new TypeReference<List<Customer>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/customers.json");
        try {
           customers  = mapper.readValue(inputStream,typeReference);
        } catch (IOException e){
            System.out.println("Error reading form File: " + e.getMessage());
        }
        return customers;
    }

}
