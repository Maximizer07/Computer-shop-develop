package com.example.demo;

import com.example.demo.CartItem.CartItemService;
import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Category.CategoryService;
import com.example.demo.ConfirmationToken.ConfirmationToken;
import com.example.demo.ConfirmationToken.ConfirmationTokenService;
import com.example.demo.Order.Order;
import com.example.demo.Order.OrderService;
import com.example.demo.Product.ProductService;
import com.example.demo.User.User;
import com.example.demo.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MyController implements ErrorController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private OrderService orderService;
    @PostMapping("/sign-up")
    String signUp(User user, Model model) {
        if(!user.getPassword().equals(user.getPassword2())){
            model.addAttribute("Problem","sign");
            model.addAttribute("Status","pass1!=pass2");
            model.addAttribute("kolvo", size());
            return "login";
        }
        if(userService.uniqueEmail(user.getEmail())){
            model.addAttribute("Problem","sign");
            model.addAttribute("Status","oldemail");
            model.addAttribute("kolvo", size());
            return "login";
        }
        model.addAttribute("status","noconfirm");
        model.addAttribute("kolvo", size());
        userService.signUpUser(user);
        return "sign-up";
    }
    @GetMapping("sign-up/confirm")
    String confirmMail(@RequestParam("token") String token,Model model) {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);
        userService.confirmUser(optionalConfirmationToken.get());
        model.addAttribute("kolvo", size());
        model.addAttribute("status","confirm");
        return "sign-up";
    }
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("kolvo", size());
        model.addAttribute("users", userService.readAll());
        return "admin";
    }
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("kolvo", size());
        return "about";
    }
    @RequestMapping(path="admin/{number}")
    public String Adminuserinfo(@PathVariable(value = "number") int number, Model model) {
        model.addAttribute("user",userService.loadUserById(number));
        model.addAttribute("orders",1);
        model.addAttribute("kolvo", size());
        model.addAttribute("not_my","not_my");
        return "user_info";
    }

    @GetMapping("/login")
    public String viewLoginPage(Model model, User user) {
        model.addAttribute("Problem","login");
        model.addAttribute("kolvo", size());
        return "login";
    }
    @GetMapping("/categories")
    public String viewCategoriesPage(Model model) {
        List<String> path = new LinkedList<>();
        path.add("Каталог");
        path.add("Категории");
        model.addAttribute("path", path);
        model.addAttribute("categories", categoryService.readAll());

        return "newcategories";
    }
    private int size(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(Objects.equals(authentication.getName(), "anonymousUser"))) {
            if (orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().noneMatch(o -> o.getStatus() == -1)) {
                return 0;
            } else {
                return orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream()
                        .filter(o -> o.getStatus() == -1).findAny().get().getProducts().size();
            }
        }
        return 0;
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
        model.addAttribute("kolvo", size());
        model.addAttribute("users", userService.readAll());
        model.addAttribute("products", productService.readAll());
        model.addAttribute("categories", categoryService.readAll());
        return "admin2";
    }
    @PostMapping ("/changeRole/{number}")
    public String changeRole(@PathVariable(value = "number") long number,Model model,User user) {
        userService.updateUserRole(number,user.getUserRole());
        return "redirect:/admin2";
    }
    @GetMapping ("/deleteUser/{number}")
    public String deleteUser(@PathVariable(value = "number") long number,Model model) {
        userService.deleteUser(number);
        return "redirect:/admin2";
    }
    @RequestMapping(value = "/user_info/changename", method = RequestMethod.POST)
    public @ResponseBody String addname(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String firstName = request.getParameter("firstname");
        String email = request.getParameter("email");
        userService.updateUserName(userService.loadUserByUsername(email).getId(),firstName);
        return firstName;
    }

    @RequestMapping(value = "/user_info/changesurname", method = RequestMethod.POST)
    public @ResponseBody String addsurname(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        userService.updateSurName(userService.loadUserByUsername(email).getId(),surname);
        return surname;
    }
    @GetMapping("shoppingcardadd/{number}")
    String addCartItem(@PathVariable(value = "number") int number, Model model) {
        Cart_Item cart_item = new Cart_Item();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().noneMatch(o->o.getStatus()==-1)) {
            Order o =new Order();
            o.setStatus(-1);
            o.setUserid(userService.loadUserByUsername(authentication.getName()).getId());
            orderService.save(o);
            cart_item.setProduct(productService.findBynumber(number));
            cart_item.setQuantity(1);
            cart_item.setOrder(o);
            cartItemService.create(cart_item);
        }
        else {
            if(orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream()
                    .filter(o -> o.getStatus() == -1).findAny().get().getProducts().
                            stream().anyMatch(b->b.getProduct().getId()==number)){
                cartItemService.findbyid(number).stream().filter(a->a.getOrder()
                        ==orderService.findbyuser(userService.loadUserByUsername
                        (authentication.getName()).getId())
                        .stream().filter(o -> o.getStatus() == -1).findAny().get())
                        .findAny().get().setQuantity(cartItemService.findbyid(number).stream().filter(a->a.getOrder()
                        ==orderService.findbyuser(userService.loadUserByUsername
                        (authentication.getName()).getId())
                        .stream().filter(o -> o.getStatus() == -1).findAny().get())
                        .findAny().get().getQuantity()+1);
                cartItemService.create(cartItemService.findbyid(number).stream().filter(a->a.getOrder()
                        ==orderService.findbyuser(userService.loadUserByUsername
                        (authentication.getName()).getId())
                        .stream().filter(o -> o.getStatus() == -1).findAny().get()).findAny().get());
            }
            else {
                cart_item.setProduct(productService.findBynumber(number));
                cart_item.setQuantity(1);
                cart_item.setOrder(orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().filter(o -> o.getStatus() == -1).findAny().get());
                cartItemService.create(cart_item);
            }
        }
        return "redirect:/shoppingcard";
    }
    @RequestMapping(path = "/shoppingcard")
    public String shoppingcard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().anyMatch(o->o.getStatus()==-1)){
            Order order = orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().filter(o->o.getStatus()==-1).findAny().get();
            int sum = (int) order.getProducts().stream()
                    .mapToDouble(a -> a.getProduct().getPrice()*a.getQuantity())
                    .sum();
            model.addAttribute("sum",sum);
            model.addAttribute("products",order.getProducts());
        }
        else{
            model.addAttribute("products",new ArrayList<>());
        }
        model.addAttribute("kolvo", size());
        return "shopping_card";
    }
    @GetMapping("shoppingcard/change")
    public String deleteIncome(@RequestParam(value = "opisanie") String opisanie,@RequestParam(value = "quantity") int quantity,Model model) {
     //   for(Product p:products){
       //     if (p.getOpisanie().equals(opisanie)){
         //       p.setQuantity(quantity);
         //       if (p.getQuantity()==0){
          //          p.setOpisanie("nulll");
          //      }
          //  }
        //}
        //products.removeIf(product -> product.getOpisanie().equals("nulll"));
        model.addAttribute("kolvo", size());
        return "redirect:/shoppingcard";
    }
    @GetMapping("shoppingcard/delete")
    public String delete(@RequestParam(value = "opisanie") String opisanie,Model model) {
        //products.removeIf(product -> product.getOpisanie().equals(opisanie));
        model.addAttribute("kolvo", size());
        return "redirect:/shoppingcard";
    }

    @RequestMapping(path="/user_info")
    public String userinfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user",userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("orders",orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()));
        model.addAttribute("kolvo", size());
        return "user_info";
    }
    @RequestMapping(path="shoppingcard/orderconfirm")
    public String orderconfirm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().anyMatch(o->o.getStatus()==-1)){
            Order order = orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().filter(o->o.getStatus()==-1).findAny().get();
            int sum = (int) order.getProducts().stream()
                    .mapToDouble(a -> a.getProduct().getPrice()*a.getQuantity())
                    .sum();
            model.addAttribute("price",sum);
        }
        model.addAttribute("kolvo", size());
        return "order_confirm";
    }
    @RequestMapping(path="shoppingcard/addorder")
    public String addorder(@RequestParam String data, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().anyMatch(o->o.getStatus()==-1)) {
            Order o = orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().filter(a->a.getStatus()==-1).findAny().get();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            o.setDateSet(LocalDate.now(ZoneId.of("Europe/Moscow")));
            o.setDateGet(LocalDate.parse(data, formatter));
            int sum = (int) o.getProducts().stream()
                    .mapToDouble(a -> a.getProduct().getPrice()*a.getQuantity())
                    .sum();
            o.setPrise(sum);
            o.setStatus(0);
            o.setNumber(String.format("%06d", new Random().nextInt(999999)));
            o.setUserid(userService.loadUserByUsername(authentication.getName()).getId());
            orderService.save(o);
        }
        return "redirect:/user_info";
    }
    @RequestMapping(path="user_info/orders/{number}")
    public String userinfo(@PathVariable(value = "number") String number, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId());
        model.addAttribute("order",orderService.findbynumber(number));
        model.addAttribute("kolvo", size());
        return "orderinfo";
    }
    @RequestMapping(path="user_info/orders/deleteinfo/{number}")
    public String deleteinfo(@PathVariable(value = "number") int number,Model model) {
        return "redirect:/user_info";
    }
    @RequestMapping("/categories/{category}")
    public String categoryProducts(@PathVariable(value = "category") String engname, Model model){
        model.addAttribute("products", productService.findById_category(categoryService.findByEngname(engname).getId()));
        model.addAttribute("current_category", categoryService.findByEngname(engname).getName());
        model.addAttribute("categories", categoryService.readAll());
        model.addAttribute("kolvo", size());
        return "product_list1";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
    @RequestMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("kolvo", size());
        return "error";
    }
    @GetMapping("/wishlist")
    public String wishlist(Model model) {
        model.addAttribute("kolvo", size());
        return "wishlist";
    }
    @RequestMapping(path="product/{id}")
    public String product_info(@PathVariable(value = "id") int id, Model model) {
        model.addAttribute("kolvo", size());
        model.addAttribute("product",productService.findById(id));
        model.addAttribute("category",categoryService.findById(productService.findById(id).getCategoryId()));
        return "product";
    }
}
