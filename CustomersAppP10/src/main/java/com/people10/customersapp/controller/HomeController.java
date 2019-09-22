package com.people10.customersapp.controller;

import com.people10.customersapp.model.Customer;
import com.people10.customersapp.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private ICustomerService customerService;

    @RequestMapping("/")
    public String home(){
        return "webapps/dashboard";
    }

    @RequestMapping("/home/new-customer")
    public ModelAndView addCustomer(){

        ModelAndView mv = new ModelAndView("webapps/new-customer");
        mv.addObject("customer",new Customer());
        return mv;
    }
    @RequestMapping("/home/new-customer/{customerId}")
    public ModelAndView addCustomer(@PathVariable Integer customerId){
        ModelAndView mv = new ModelAndView("webapps/new-customer");
        mv.addObject("customer",customerService.findById(customerId));
        return mv;
    }

    @PostMapping("/home/savecustomer")
    public String saveCustomer(@Valid @ModelAttribute Customer customer, BindingResult result){
        if (result.hasErrors())
            return "webapps/new-customer";
        customerService.saveCustomer(customer);
        return "redirect:/";
    }
}
