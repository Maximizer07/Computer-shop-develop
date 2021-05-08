package com.example.demo;

import com.example.demo.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class MyController {
    public ArrayList<Product> products = new ArrayList<>();
    public ArrayList<Order> orders = new ArrayList<>();
    public ArrayList<Category> categories = new ArrayList<>();
    boolean isauth = false;
    @Autowired
    private  UserService userService;
    @Autowired
    private CategoryService cs;
    @Autowired
    private  ConfirmationTokenService confirmationTokenService;
    @PostMapping("/sign-up")
    String signUp(User user,Model model) {
        if(!user.getPassword().equals(user.getPassword2())){
            model.addAttribute("Problem","sign");
            model.addAttribute("Status","pass1!=pass2");
            model.addAttribute("kolvo", products.size());
            model.addAttribute("isauth", isauth);
            System.out.println("ddsdsds2");
            return "login";
        }
        if(userService.uniqueEmail(user.getEmail())){
            model.addAttribute("Problem","sign");
            model.addAttribute("Status","oldemail");
            model.addAttribute("kolvo", products.size());
            System.out.println("ddsdsds1");
            model.addAttribute("isauth", isauth);
            return "login";
        }
        model.addAttribute("status","noconfirm");
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        userService.signUpUser(user);
        return "sign-up";
    }
    @GetMapping("sign-up/confirm")
    String confirmMail(@RequestParam("token") String token,Model model) {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);
        userService.confirmUser(optionalConfirmationToken.get());
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        model.addAttribute("status","confirm");
        return "sign-up";
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
        model.addAttribute("Problem","login");
        model.addAttribute("kolvo", products.size());
        model.addAttribute("isauth", isauth);
        return "login";
    }
    @GetMapping("/categories")
    public String viewCategoriesPage(Model model) {
        List<String> path = new LinkedList<>();
        path.add("Каталог");
        path.add("Категории");
        model.addAttribute("path", path);
        model.addAttribute("isauth", isauth);
        model.addAttribute("categories", cs.readAll());

        return "categories";
    }
    @RequestMapping(path = "/")
    public String add() {
        return "redirect:/user_info";
    }
    @RequestMapping(path = "/admin2")
    public String admin2(Model model) {
        model.addAttribute("user", new User());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("User",userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("isauth", isauth);
        model.addAttribute("kolvo", products.size());
        model.addAttribute("newRole", UserRole.values());
        model.addAttribute("users", userService.readAll());
        return "admin2";
    }
    @PostMapping ("/changeRole/{number}")
    public String process(@PathVariable(value = "number") long number,Model model,User user) {
        userService.updateUserRole(number,user.getUserRole());
        return "redirect:/admin2";
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
    @GetMapping("shoppingcard/change")
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
    @GetMapping("shoppingcard/delete")
    public String delete(@RequestParam(value = "opisanie") String opisanie,Model model) {
        products.removeIf(product -> product.getOpisanie().equals(opisanie));
        model.addAttribute("products",products);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        return "redirect:/shoppingcard";
    }

    @RequestMapping(path="/user_info")
    public String userinfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isauth=true;
        products.add(new Product("blabla",1,25));
        products.add(new Product("yeeeee",3,30));
        model.addAttribute("user",userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("orders",orders);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        return "user_info";
    }
    @RequestMapping(path="shoppingcard/orderconfirm")
    public String orderconfirm(Model model) {
        int sum = (int) products.stream()
                .mapToDouble(a -> a.getPrice()*a.getQuantity())
                .sum();
        model.addAttribute("price",sum);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        return "order_confirm";
    }
    @RequestMapping(path="shoppingcard/addorder")
    public String addorder(@RequestParam String data, Model model) {
        int sum = (int) products.stream()
                .mapToDouble(a -> a.getPrice()*a.getQuantity())
                .sum();
        Order o =new Order(123,"12.21.2021",data,sum,products,0);
        orders.add(o);

        return "redirect:/user_info";
    }
    @RequestMapping(path="user_info/orders/{number}")
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
    @RequestMapping(path="user_info/orders/deleteinfo/{number}")
    public String deleteinfo(@PathVariable(value = "number") int number,Model model) {
        orders.removeIf(o -> o.getNumber() == number);
        return "redirect:/user_info";
    }
}
