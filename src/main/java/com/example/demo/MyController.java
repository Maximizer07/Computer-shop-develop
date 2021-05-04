package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class MyController {
    public ArrayList<Product> products = new ArrayList<>();
    public ArrayList<Order> orders = new ArrayList<>();
    boolean isauth = false;
    @Autowired
    private  UserService userService;
    @Autowired
    private  ConfirmationTokenService confirmationTokenService;
    @PostMapping("/sign-up")
    String signUp(User user) {
        System.out.println("ffff");
        userService.signUpUser(user);
        return "redirect:/login";
    }
    @GetMapping("/sign-up/confirm")
    String confirmMail(@RequestParam("token") String token) {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);
        userService.confirmUser(optionalConfirmationToken.get());
        return "redirect:/login";
    }
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("isauth", isauth);
        model.addAttribute("kolvo", products.size());
        model.addAttribute("users", userService.readAll());
        return "admin";
    }
    @RequestMapping(path="admin/{number}")
    public String Adminuserinfo(@PathVariable(value = "number") int number, Model model) {
        model.addAttribute("user",userService.loadUserById(number));
        model.addAttribute("orders",orders);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        return "user_info";
    }
    @GetMapping("/login")
    public String viewLoginPage(Model model,User user) {
        model.addAttribute("kolvo", products.size());
        model.addAttribute("isauth", isauth);
        return "login";
    }
    @RequestMapping(path = "/")
    public String add() {
        return "redirect:/shoppingcard";
    }

    @RequestMapping(path = "/shoppingcard")
    public String shoppingcard(Model model) {
        int sum = (int) products.stream()
                .mapToDouble(a -> a.getPrice() * a.getQuantity())
                .sum();
        model.addAttribute("sum", sum);
        model.addAttribute("kolvo", products.size());
        model.addAttribute("products", products);
        model.addAttribute("isauth", isauth);
        return "shopping_card";
    }
    @GetMapping("/shoppingcard3")
    public String deleteIncome(@RequestParam(value = "opisanie") String opisanie,@RequestParam(value = "quantity") int quantity,Model model) {
        for(Product p:products){
            if (p.getOpisanie().equals(opisanie)){
                p.setQuantity(quantity);
                if (p.getQuantity()==0){
                    p.setOpisanie("nulll");
                }
            }
        }
        products.removeIf(product -> product.getOpisanie().equals("nulll"));
        model.addAttribute("products",products);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        return "redirect:/shoppingcard";
    }
    @GetMapping("/shoppingcard/delete")
    public String delete(@RequestParam(value = "opisanie") String opisanie,Model model) {
        products.removeIf(product -> product.getOpisanie().equals(opisanie));
        model.addAttribute("products",products);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        return "redirect:/shoppingcard";
    }

    @RequestMapping(path="/user_info")
    public String userinfo(Authentication authentication,Model model) {
        isauth=true;
        products.add(new Product("blabla",1,25));
        products.add(new Product("yeeeee",3,30));
        model.addAttribute("user",userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("orders",orders);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        return "user_info";
    }
    @RequestMapping(path="/orderconfirm")
    public String orderconfirm(Model model) {
        int sum = (int) products.stream()
                .mapToDouble(a -> a.getPrice()*a.getQuantity())
                .sum();
        model.addAttribute("price",sum);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        return "order_confirm";
    }
    @RequestMapping(path="/addorder")
    public String addorder(@RequestParam String data, Model model) {
        int sum = (int) products.stream()
                .mapToDouble(a -> a.getPrice()*a.getQuantity())
                .sum();
        Order o =new Order(123,"12.21.2021",data,sum,products,0);
        orders.add(o);

        return "redirect:/user_info";
    }
    @RequestMapping(path="/{number}")
    public String userinfo(@PathVariable(value = "number") int number, Model model) {
        for(Order o :orders){
            if (o.getNumber()==number){
                System.out.println(o.getNumber());
                model.addAttribute("order",o);
            }
        }
        model.addAttribute("isauth",isauth);
        model.addAttribute("kolvo",products.size());
        return "orderinfo";
    }
    @RequestMapping(path="/deleteinfo/{number}")
    public String deleteinfo(@PathVariable(value = "number") int number,Model model) {
        orders.removeIf(o -> o.getNumber() == number);
        return "redirect:/user_info";
    }
}
