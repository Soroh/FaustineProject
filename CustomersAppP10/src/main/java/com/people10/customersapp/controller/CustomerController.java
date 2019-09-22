package com.people10.customersapp.controller;

import com.opencsv.CSVReader;
import com.people10.customersapp.model.Customer;
import com.people10.customersapp.service.implementation.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerService;

    @GetMapping("customers")
    public List<Customer> customers(){

       return customerService.findAll();
    }

    //Returns a list of all customers
    @GetMapping("customer/{id}")
    public Customer listCustomers(@PathVariable("id") Integer id){

                return customerService.findById(id);
    }
    //Returns a list of all customers based on search criteria
    @GetMapping("/customer/search/{searchTerm}")
    public List<Customer> searchCustomers(@PathVariable("searchTerm") String searchTerm){

               return customerService.searchCustomers(searchTerm);
    }

    //Saves or Updates customer
    @PutMapping(value ="customer")
    public Customer saveOrUpdateCustomer(@RequestBody Customer customer){

        return customerService.saveCustomer(customer);
    }

    @PostMapping(value ="customer")
    public Customer saveCustomer(@RequestBody Customer customer){
    System.out.print("I am here \n\n\n");
       return customerService.saveCustomer(customer);

    }

    //Deletes Customer
    @DeleteMapping("customer/{id}")
    public Customer deleteCustomer(@PathVariable("id") Integer id){
        Customer customer = customerService.findById(id);
        customerService.deleteCustomer(id);
        return customer;
    }

    @GetMapping("/customers/load-data")
    @ResponseBody
    public List<Customer> loadData() {
        List<Customer> customers = new ArrayList<>();
        try {

            //Loading and transforming the first data1
            Reader mapReader = Files.newBufferedReader(Paths.get(
                    ClassLoader.getSystemResource("csv/map1.csv").toURI()));
            Reader  dataReader = Files.newBufferedReader(Paths.get(
                    ClassLoader.getSystemResource("csv/data1.csv").toURI()));
            customers.addAll(transform(mapReader,dataReader));

            //Loading and transforming the first data2
            mapReader = Files.newBufferedReader(Paths.get(
                    ClassLoader.getSystemResource("csv/map2.csv").toURI()));
            dataReader = Files.newBufferedReader(Paths.get(
                    ClassLoader.getSystemResource("csv/data2.csv").toURI()));

            customers.addAll(transform(mapReader,dataReader));

            mapReader.close();
            dataReader.close();

        }catch (URISyntaxException urlE){

            System.out.println("ULI failure");

        }catch (IOException urlE){

            System.out.println("IO failure");
        }


        customerService.saveAll(customers);
        return customers;
    }

    int indexOf(String colName, String[] data) {
        int index = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(colName)) {
                index = i;
                break;
            }
        }
        return index;
    }

    List<Customer> transform(Reader mapReader, Reader dataReader) throws IOException, URISyntaxException {
        List<Customer> customers = new ArrayList<>();
        CSVReader csvReader = new CSVReader(mapReader);
        String[] mapData = csvReader.readNext();
        int [] indices= new  int[7];
        if(mapData!=null){
            indices[0]=indexOf("first_name",mapData);
            indices[1]=indexOf("last_name",mapData);
            indices[2]=indexOf("email",mapData);
            indices[3]=indexOf("ip",mapData);
            indices[4]=indexOf("latitude",mapData);
            indices[5]=indexOf("longitude",mapData);
            indices[6]=indexOf("created_at",mapData);
        }

        csvReader = new CSVReader(dataReader);
        String[] cmData = csvReader.readNext();//reads the headers and ignored at this point

        //Transforming excel rows into Customer object
        while ((cmData = csvReader.readNext()) != null) {
            Customer customer = new Customer();
            customer.setFirstName(cmData[indices[0]]);
            customer.setLastName(cmData[indices[1]]);
            customer.setEmail(cmData[indices[2]]);
            customer.setIp(cmData[indices[3]]);
            customer.setLatitude(Double.parseDouble((cmData[indices[4]].trim().equals(""))?"0.0":cmData[indices[4]]));
            customer.setLongitude(Double.parseDouble((cmData[indices[4]].trim().equals(""))?"0.0":cmData[indices[5]]));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime createdAt = LocalDateTime.parse(cmData[indices[6]], formatter);
            customer.setCreatedAt(createdAt);
            customers.add(customer);
        }
        dataReader.close();
        mapData.clone();
        csvReader.close();
        return customers;
    }




}
