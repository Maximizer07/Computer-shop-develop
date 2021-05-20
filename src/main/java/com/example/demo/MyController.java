package com.example.demo;

import com.example.demo.CartItem.CartItemService;
import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryService;
import com.example.demo.ConfirmationToken.ConfirmationToken;
import com.example.demo.ConfirmationToken.ConfirmationTokenService;
import com.example.demo.Description.Description;
import com.example.demo.Description.DescriptionService;
import com.example.demo.Order.Order;
import com.example.demo.Order.OrderService;
import com.example.demo.Product.Product;
import com.example.demo.Product.ProductService;
import com.example.demo.Review.Review;
import com.example.demo.Review.ReviewService;
import com.example.demo.User.User;
import com.example.demo.User.UserService;
import com.example.demo.WishItem.WishItem;
import com.example.demo.WishItem.WishService;
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

@Controller
public class MyController implements ErrorController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DescriptionService descriptionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WishService wishService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CriteriaService criteriaService;
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

    @GetMapping("/auth")
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
        model.addAttribute("kolvo", size());
        return "newcategories";
    }
    private int size(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null){
            return 0;
        }
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
    public String add(Model model) {
        List<String> path = new LinkedList<>();
        path.add("Каталог");
        path.add("Категории");
        model.addAttribute("path", path);
        model.addAttribute("categories", categoryService.readAll());
        model.addAttribute("kolvo", size());
        return "main";
    }
    @RequestMapping(path = "/admin2")
    public String admin2(Model model) {
        model.addAttribute("user", new User());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("User",userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("users", userService.readAll());
        model.addAttribute("orders", orderService.readAll());
        model.addAttribute("products", productService.readAll());
        model.addAttribute("categories", categoryService.readAll());
        model.addAttribute("kolvo", size());
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
    @RequestMapping(path = "/category/add", method = RequestMethod.POST)
    public String addNewCategory(@RequestParam String Name, @RequestParam String Link, Model model) {
        Category  c = new Category();
        c.setName(Name);
        c.setLink(Link);
        categoryService.create(c);
        return "redirect:/admin2";
    }
    @GetMapping("/addproduct")
    public String addProduct(Model model) {
        return "product_add";
    }
    @RequestMapping(path = "/savenewproduct", method = { RequestMethod.GET, RequestMethod.POST })
    public String saveNewProduct(@RequestParam String Name,@RequestParam String Category,
                                 @RequestParam String Price, @RequestParam String Quantity,
                                 @RequestParam String Manufacturer, @RequestParam String Link,
                                 @RequestParam String Description, Model model) {
        Product p = new Product();
        Description description = new Description();
        description.setDescription(Description);
        p.setName(Name);
        p.setManufacturer(Manufacturer);
        p.setLink(Link);
        description.setProduct(p);
        p.setDescription(description);
        p.setCategory(categoryService.findById(Integer.parseInt(Category)));
        p.setPrice(Integer.parseInt(Price));
        p.setQuantity(Integer.parseInt(Quantity));
        productService.save(p);
        return "redirect:/admin2";
    }
    @RequestMapping(path = "/category/change/{id}", method = RequestMethod.POST)
    public String changeCategoryData(@PathVariable(value = "id") int id, @RequestParam String Name, @RequestParam String Link, Model model) {
        Category c = categoryService.findById(id);
        c.setName(Name);
        c.setLink(Link);
        categoryService.change(c);
        return "redirect:/admin2";
    }
    @RequestMapping(path = "/category/search", method = RequestMethod.POST)
    public String categorySearch(@RequestParam String Id, @RequestParam String Name, Model model) {
        model.addAttribute("user", new User());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("User",userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("users", userService.readAll());
        model.addAttribute("orders", orderService.readAll());
        model.addAttribute("products", productService.readAll());
        model.addAttribute("categories", criteriaService.takeCategories(Name, Id));
        model.addAttribute("search_id", Id);
        model.addAttribute("search_name", Name);
        return "admin2";
    }
    @RequestMapping(path = "/product/search", method = RequestMethod.POST)
    public String productSearch(@RequestParam String Id, @RequestParam String Name,@RequestParam String Quantity, @RequestParam String Price, Model model) {
        model.addAttribute("user", new User());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("User",userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("users", userService.readAll());
        model.addAttribute("orders", orderService.readAll());
        model.addAttribute("products", criteriaService.takeProducts(Name, Id, Price, Quantity));
        model.addAttribute("categories", categoryService.readAll());
        model.addAttribute("search_id", Id);
        model.addAttribute("search_name", Name);
        model.addAttribute("search_quantity", Quantity);
        model.addAttribute("search_price", Price);
        return "admin2";
    }
    @RequestMapping(path = "/category/delete/{id}", method = RequestMethod.POST)
    public String deleteCategory(@PathVariable(value = "id") int id, Model model) {
        Category c = categoryService.findById(id);
        categoryService.delete(c);
        return "redirect:/admin2";
    }
    @RequestMapping(path = "/product/delete/{id}", method = RequestMethod.POST)
    public String deleteProduct(@PathVariable(value = "id") int id, Model model) {
        Product p = productService.findById(id);
        productService.delete(p);
        return "redirect:/admin2";
    }
    @RequestMapping(path = "/product/change/{id}", method = RequestMethod.POST)
    public String changeProductData(@PathVariable(value = "id") int id, @RequestParam String Name, @RequestParam String Price, @RequestParam String Quantity, Model model) {
        Product p = productService.findById(id);
        p.setName(Name);
        p.setPrice(Integer.parseInt(Price));
        p.setQuantity(Integer.parseInt(Quantity));
        productService.change(p);
        return "redirect:/admin2";
    }
    @RequestMapping(path = "/product/savechangeinfo/{id}", method = { RequestMethod.GET, RequestMethod.POST })
    public String saveChangeInfo(@PathVariable(value = "id") int id, @RequestParam String Name,
                                 @RequestParam String Price, @RequestParam String Quantity,
                                 @RequestParam String Manufacturer, @RequestParam String Link,
                                 @RequestParam String Category, @RequestParam String Description, Model model) {
        Product p = productService.findById(id);
        Description description = p.getDescription();
        description.setDescription(Description);
        p.setName(Name);
        p.setCategory(categoryService.findById(Integer.parseInt(Category)));
        p.setManufacturer(Manufacturer);
        p.setLink(Link);
        p.setDescription(description);
        p.setPrice(Integer.parseInt(Price));
        p.setQuantity(Integer.parseInt(Quantity));
        productService.change(p);
        return "redirect:/admin2";
    }
    @RequestMapping(path = "/product/changeinfo/{id}", method = RequestMethod.POST)
    public String changeAllProductData(@PathVariable(value = "id") int id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product",product);
        return "product_edit";
    }
    @RequestMapping(path = "/category/delete", method = RequestMethod.POST)
    public String DeleteCategory(@RequestParam String Name, @RequestParam String Link, Model model) {
        Category  c = new Category();
        c.setName(Name);
        c.setLink(Link);
        categoryService.del(c);
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
    @RequestMapping(value = "/makereview", method = RequestMethod.POST)
    public @ResponseBody String addreview(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("fdsfsdf");
        Review review = new Review();
        if(comment != null){
            review.setComment(comment);
            review.setRating(rating);
            review.setProduct(productService.findBynumber(id));
            review.setUser(userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
            review.setCreated(LocalDate.now(ZoneId.of("Europe/Moscow")));
            reviewService.save(review);
        }
        long rat = 0;
        for (Review rev:productService.findBynumber(id).getReviews()) {
            rat += rev.getRating();
        }
        int average = (int) (rat/productService.findBynumber(id).getReviews().size());
        Product product = productService.findBynumber(id);
        product.setRating(average);
        productService.save(product);
        return String.valueOf(id);
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
    @GetMapping("wishadd/{number}")
    String addWishItem(@PathVariable(value = "number") int number, Model model) {
        WishItem wishItem = new WishItem();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        wishItem.setProduct(productService.findBynumber(number));
        wishItem.setUser(userService.loadUserByUsername(authentication.getName()));
        wishService.save(wishItem);
        return "redirect:/user_info";
    }
    @GetMapping("wishdelete/{number}")
    String deleteWishItem(@PathVariable(value = "number") int number, Model model) {
        wishService.delete((long) number);
        return "redirect:/user_info";
    }
    @GetMapping("/shop")
    public String shop(Model model) {
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
        return "shop";
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
        return "shop";
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
        List<Order> orders= new ArrayList<>();
        for( Order order:orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId())){
            if(order.getDateGet()!=null) {
                orders.add(order);
            }
        }
        model.addAttribute("wishlist",userService.loadUserByUsername(authentication.getName()).getWishItems());
        model.addAttribute("orders", orders);
        model.addAttribute("kolvo", size());
        return "user_info";
    }
    @PostMapping(path="/user_info/findNumber")
    public String userinfo(Model model,int number) {
        return "redirect:/user_info/orders/"+number;
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
    public String deleteinfo(@PathVariable(value = "number") String number,Model model) {
        orderService.delete(number);
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
        Product product = productService.findById(id);
        model.addAttribute("product",product);
        List <Product> products = product.getCategory().getProducts();
        if (products.size() >= 3) {
            model.addAttribute("products", products.subList(0, 3));
        } else {
            model.addAttribute("products", products);
        }
        model.addAttribute("description",product.getDescription().getDescription());
        model.addAttribute("category",product.getCategory());
        model.addAttribute("kolvo", size());
        return "product";
    }
}
