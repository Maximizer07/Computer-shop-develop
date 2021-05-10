package com.example.demo;

import com.example.demo.CartItem.CartItemService;
import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryService;
import com.example.demo.ConfirmationToken.ConfirmationToken;
import com.example.demo.ConfirmationToken.ConfirmationTokenService;
import com.example.demo.Order.Order;
import com.example.demo.Order.OrderService;
import com.example.demo.Product.Product;
import com.example.demo.Product.ProductService;
import com.example.demo.ShoppingCart.ShoppingCartService;
import com.example.demo.ShoppingCart.Shopping_cart;
import com.example.demo.ShoppingCart.ShoppingcartRepository;
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
    public ArrayList<Product> products = new ArrayList<>();
    public ArrayList<Order> orders = new ArrayList<>();
    public ArrayList<Category> categories = new ArrayList<>();
    boolean isauth = false;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private OrderService orderService;
    @PostMapping("/sign-up")
    String signUp(User user, Model model) {
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
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("isauth", isauth);
        model.addAttribute("kolvo", products.size());
        return "about";
    }
    @RequestMapping(path="admin/{number}")
    public String Adminuserinfo(@PathVariable(value = "number") int number, Model model) {
        model.addAttribute("user",userService.loadUserById(number));
        model.addAttribute("orders",orders);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        model.addAttribute("not_my","not_my");
        return "user_info";
    }
    @GetMapping("/login")
    public String viewLoginPage(Model model, User user) {
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
        model.addAttribute("categories", categoryService.readAll());

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
        System.out.println(firstName);
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
        model.addAttribute("isauth", isauth);
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
        model.addAttribute("products",products);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        return "redirect:/shoppingcard";
    }
    @GetMapping("shoppingcard/delete")
    public String delete(@RequestParam(value = "opisanie") String opisanie,Model model) {
        //products.removeIf(product -> product.getOpisanie().equals(opisanie));
        model.addAttribute("products",products);
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
        return "redirect:/shoppingcard";
    }

    @RequestMapping(path="/user_info")
    public String userinfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isauth=true;
        model.addAttribute("user",userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("orders",orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()));
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
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
        model.addAttribute("kolvo",products.size());
        model.addAttribute("isauth",isauth);
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
        model.addAttribute("isauth",isauth);
        model.addAttribute("kolvo",products.size());
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
        model.addAttribute("isauth", isauth);
        return "product_list";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
    @RequestMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("isauth", isauth);
        return "error";
    }
    @GetMapping("/product")
    public String product_page(Model model) {
        model.addAttribute("isauth", isauth);
        return "product";
    }
}
