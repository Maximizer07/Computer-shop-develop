package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class MyController {
    public ArrayList<Product> products = new ArrayList<>();
    public ArrayList<Order> orders = new ArrayList<>();
    boolean isauth = false;
    public ArrayList<User> users = new ArrayList<>();
    public User user = new User("Mike", "I", "89100036300", "mike7677877@gmail.com", "123456");

    @RequestMapping(path = "/")
    public String add() {
        users.add(user);
        return "redirect:/auth";
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

    @RequestMapping(path = "/auth")
    public String auth(Model model) {
        model.addAttribute("kolvo", products.size());
        model.addAttribute("isauth", isauth);
        return "auth";
    }

    @RequestMapping(path = "/signUperror", method = RequestMethod.POST)
    public String SignUp(@RequestParam String login, String password, String password2, String email, Model model) {
        if (!password.equals(password2)) {
            model.addAttribute("Status", "pass1!=pass2");
            model.addAttribute("kolvo", products.size());
            model.addAttribute("isauth", isauth);
            model.addAttribute("Status2", "sign");
            return "auth";
        } else {
            if (login.equals("Mike")) {
                model.addAttribute("kolvo", products.size());
                model.addAttribute("isauth", isauth);
                model.addAttribute("Status", "user_exists");
                model.addAttribute("Status2", "sign");
                return "auth";
            } else {
                users.add(new User(login, "", "", email, password));
                isauth = true;
                return "redirect:/user_info";
            }
        }
    }
    @RequestMapping(path = "/loginerror", method = RequestMethod.POST)
    public String Login(@RequestParam String username, String password,Model model) {
            if (!username.equals("Mike")) {
                model.addAttribute("kolvo", products.size());
                model.addAttribute("isauth", isauth);
                model.addAttribute("Status", "no_user_exists");
                model.addAttribute("Status2", "login");
                return "auth";
            } else {
                if (!password.equals("123456")) {
                    model.addAttribute("kolvo", products.size());
                    model.addAttribute("isauth", isauth);
                    model.addAttribute("Status", "password_wrong");
                    model.addAttribute("Status2", "login");
                    return "auth";
                } else {
                isauth = true;
                return "redirect:/user_info";
            }
        }
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
    public String userinfo(Model model) {
        isauth=true;
        products.add(new Product("blabla",1,25));
        products.add(new Product("yeeeee",3,30));
        model.addAttribute("user",users.get(users.size()-1));
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
