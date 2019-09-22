package com.people10.customersapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomerErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "webapps/error-page";
    }

    @Override
    public String getErrorPath() {
        return "webapps/error-page";
    }
}
