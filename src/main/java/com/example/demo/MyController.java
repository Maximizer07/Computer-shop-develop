package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
public class MyController {
    private ArrayList<Product>products=new ArrayList<>();
    @RequestMapping(path="/orderinfo")
    public String orderinfo() {
        return "orderinfo";
    }
    @RequestMapping(path="/shoppingcard")
    public String shoppingcard(Model model) {
        products.add(new Product("blabla",1,25));
        products.add(new Product("yeeeee",3,30));
        model.addAttribute("products",products);
        return "shopping_card";
    }
    @RequestMapping(path="/user_info")
    public String userinfo() {
        return "user_info";
    }

}
