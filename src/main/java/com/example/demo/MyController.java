package com.example.demo;

import com.example.demo.CartItem.CartItemService;
import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryService;
import com.example.demo.ConfirmationToken.ConfirmationToken;
import com.example.demo.ConfirmationToken.ConfirmationTokenService;
import com.example.demo.Description.Description;
import com.example.demo.Order.Order;
import com.example.demo.Order.OrderService;
import com.example.demo.Product.Product;
import com.example.demo.Product.ProductService;
import com.example.demo.Property.Property;
import com.example.demo.Property.PropertyService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.commons.validator.routines.EmailValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Контроллер веб-сервиса
 * @author Maximus
 * @author mike
 */
@Controller
public class MyController implements ErrorController {
    /**
     *
     */
    @Autowired
    private UserService userService;
    /**
     * Репозиторий работы с характеристиками товаров из таблицы БД
     */
    @Autowired
    private PropertyService propertyService;
    /**
     * Сервис для работы с продуктами из таблицы БД
     */
    @Autowired
    private ProductService productService;
    /**
     * Сервис для работы с категориями товаров из таблицы БД
     */
    @Autowired
    private CategoryService categoryService;
    /**
     *
     */
    @Autowired
    private CartItemService cartItemService;
    /**
     *
     */
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    /**
     *
     */
    @Autowired
    private OrderService orderService;
    /**
     *
     */
    @Autowired
    private WishService wishService;
    /**
     * Сервис для работы с отзывами товаров из таблицы БД
     */
    @Autowired
    private ReviewService reviewService;
    /**
     * Сервис для фильтрации объектов по критериям
     */
    @Autowired
    private CriteriaService criteriaService;

    /**
     *
     * @param user
     * @param model
     * @return
     */
    @PostMapping("/sign-up")
    String signUp(User user, Model model) {
        if(userService.uniqueEmail(user.getEmail())){
            model.addAttribute("Problem","sign");
            model.addAttribute("Status","oldemail");
            model.addAttribute("kolvo", size());
            return "login";
        }
        if(!EmailValidator.getInstance().isValid(user.getEmail())){
            model.addAttribute("Problem","sign");
            model.addAttribute("Status","bademail");
            model.addAttribute("kolvo", size());
            return "login";
        }
        if(!user.getPassword().equals(user.getPassword2())){
            model.addAttribute("Problem","sign");
            model.addAttribute("Status","pass1!=pass2");
            model.addAttribute("kolvo", size());
            return "login";
        }
        model.addAttribute("status","noconfirm");
        model.addAttribute("kolvo", size());
        userService.signUpUser(user);
        return "sign-up";
    }

    /**
     *
     * @param token
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return
     */
    @GetMapping("sign-up/confirm")
    String confirmMail(@RequestParam("token") String token,Model model) {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);
        userService.confirmUser(optionalConfirmationToken.get());
        model.addAttribute("kolvo", size());
        model.addAttribute("status","confirm");
        return "sign-up";
    }

    /**
     * Метод слушающий все запросы по /about
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу о нас
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("kolvo", size());
        return "about";
    }

    /**
     *
     * @param number
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return
     */
    @RequestMapping(path="admin/{number}")
    public String Adminuserinfo(@PathVariable(value = "number") int number, Model model) {
        model.addAttribute("user",userService.loadUserById(number));
        model.addAttribute("orders",1);
        model.addAttribute("kolvo", size());
        model.addAttribute("not_my","not_my");
        return "user_info";
    }

    /**
     *
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param user
     * @return
     */
    @GetMapping("/auth")
    public String viewLoginPage(Model model, User user) {
        model.addAttribute("Problem","login");
        model.addAttribute("kolvo", size());
        return "login";
    }

    /**
     * Метод слушающий все запросы по /categories
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу категорий товаров
     */
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

    /**
     *
     * @return
     */
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

    /**
     * Метод слушающий все запросы по /
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает главную страницу
     */
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

    /**
     * Метод слушающий все запросы по /admin2
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу админ панели
     */
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
        String message = (String) model.asMap().get("selector");
        if (message==null){
            message = "user";
        }
        System.out.println(message);
        model.addAttribute("message",message);
        return "admin2";
    }

    /**
     *
     * @param number
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param user
     * @param redirectAttrs Flash-атрибуты
     * @return Перенаправление на страницу админ панели
     */
    @PostMapping ("/changeRole/{number}")
    public String changeRole(@PathVariable(value = "number") long number,Model model,
                             User user, final RedirectAttributes redirectAttrs) {
        userService.updateUserRole(number,user.getUserRole());
        redirectAttrs.addFlashAttribute("selector", "user");
        return "redirect:/admin2";
    }

    /**
     *
     * @param number
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param redirectAttrs Flash-атрибуты
     * @return Перенаправление на страницу админ панели
     */
    @GetMapping ("/deleteUser/{number}")
    public String deleteUser(@PathVariable(value = "number") long number,
                             Model model, final RedirectAttributes redirectAttrs) {
        userService.deleteUser(number);
        redirectAttrs.addFlashAttribute("selector", "user");
        return "redirect:/admin2";
    }

    /**
     * Метод слушающий все запросы по /category/add, добавляет новую категорию товаров
     * @param Name Название категории
     * @param Link Ссылка на изображение
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param redirectAttrs Flash-атрибуты
     * @return Перенаправление на страницу админ панели
     */
    @RequestMapping(path = "/category/add", method = RequestMethod.POST)
    public String addNewCategory(@RequestParam String Name, @RequestParam String Link,
                                 Model model, final RedirectAttributes redirectAttrs) {
        Category  c = new Category();
        c.setName(Name);
        c.setLink(Link);
        categoryService.create(c);
        redirectAttrs.addFlashAttribute("selector", "category");
        return "redirect:/admin2";
    }

    /**
     * Метод слушающий все запросы по /addproduct
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу добавления нового товара
     */
    @GetMapping("/addproduct")
    public String addProduct(Model model) {
        model.addAttribute("categories", categoryService.readAll());
        return "product_add";
    }

    /**
     * Метод слушающий все запросы по /savenewproduct, добавляет новый продукт
     * @param Name Название товара
     * @param Price Цена продукта
     * @param Quantity Количество продукта
     * @param Manufacturer Производитель продукта
     * @param Link Ссылка на изображение
     * @param Category Категория товара
     * @param Description Описание товара
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param redirectAttrs Flash-атрибуты
     * @param characteristics Список добавленных характеристик
     * @param values Список значений характеристик
     * @return Перенаправление на страницу админ панели
     */
    @RequestMapping(path = "/savenewproduct", method = { RequestMethod.GET, RequestMethod.POST })
    public String saveNewProduct(@RequestParam String Name,@RequestParam Integer Category,
                                 @RequestParam Integer Price, @RequestParam Integer Quantity,
                                 @RequestParam String Manufacturer, @RequestParam String Link,
                                 @RequestParam String Description, Model model,
                                 final RedirectAttributes redirectAttrs,
                                 String[] characteristics, String[] values) {
        Product p = new Product();
        Description description = new Description();
        description.setDescription(Description);
        p.setName(Name);
        p.setManufacturer(Manufacturer);
        p.setLink(Link);
        description.setProduct(p);
        p.setDescription(description);
        p.setCategory(categoryService.findById(Category));
        p.setPrice(Price);
        p.setQuantity(Quantity);
        productService.save(p);
        for (int i = 0; i < characteristics.length; i++) {
            if (!characteristics[i].isEmpty() & !values[i].isEmpty()){
                Property property = new Property();
                property.setName(characteristics[i]);
                property.setValue(values[i]);
                property.setProduct(p);
                propertyService.save(property);
            }
        }
        redirectAttrs.addFlashAttribute("selector", "product");
        return "redirect:/admin2";
    }

    /**
     * Метод слушающий все запросы по /category/change/{id}, изменяет категорию товаров
     * @param id Идентификатор категории
     * @param Name Название категории
     * @param Link Ссылка на изображение
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param redirectAttrs Flash-атрибуты
     * @return Перенаправление на страницу админ панели
     */
    @RequestMapping(path = "/category/change/{id}", method = RequestMethod.POST)
    public String changeCategoryData(@PathVariable(value = "id") int id, @RequestParam String Name,
                                     @RequestParam String Link, Model model, final RedirectAttributes redirectAttrs) {
        Category c = categoryService.findById(id);
        c.setName(Name);
        c.setLink(Link);
        categoryService.change(c);
        redirectAttrs.addFlashAttribute("selector", "category");
        return "redirect:/admin2";
    }

    /**
     * Метод слушающий все запросы по /category/search, фильтрует категории товаров
     * @param Id Идентификатор категории
     * @param Name Название категории
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу админ панели
     */
    @RequestMapping(path = "/category/search", method = RequestMethod.POST)
    public String categorySearch(@RequestParam(required =false) Integer Id, @RequestParam String Name, Model model) {
        model.addAttribute("user", new User());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("lol " + Id);
        model.addAttribute("User",userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("users", userService.readAll());
        model.addAttribute("orders", orderService.readAll());
        model.addAttribute("products", productService.readAll());
        model.addAttribute("categories", criteriaService.takeCategories(Name, Id));
        model.addAttribute("search_id", Id);
        model.addAttribute("search_name", Name);
        model.addAttribute("message","category");
        return "admin2";
    }

    /**
     * Метод слушающий все запросы по /product/search, фильтрует продукты
     * @param Id Идентификатор продукта
     * @param Name Название товара
     * @param Price Цена продукта
     * @param Quantity Количество продукта
     * @param Idcategory Идентификатор категории
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу админ панели
     */
    @RequestMapping(path = "/product/search", method = RequestMethod.POST)
    public String productSearch(@RequestParam(required =false) Integer Id, @RequestParam String Name,@RequestParam(required =false) Integer Quantity,@RequestParam(required =false) Integer Idcategory, @RequestParam(required =false) Integer Price, Model model) {
        model.addAttribute("user", new User());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("User",userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("users", userService.readAll());
        model.addAttribute("orders", orderService.readAll());
        model.addAttribute("products", criteriaService.takeProducts(Name, Id, Price, Quantity, Idcategory));
        model.addAttribute("categories", categoryService.readAll());
        model.addAttribute("search_id", Id);
        model.addAttribute("search_name", Name);
        model.addAttribute("search_quantity", Quantity);
        model.addAttribute("search_price", Price);
        model.addAttribute("search_idcategory", Idcategory);
        model.addAttribute("message","product");
        return "admin2";
    }

    /**
     * Метод слушающий все запросы по /category/delete/{id}, удаляет категорию товаров
     * @param id Идентификатор категории
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param redirectAttrs Flash-атрибуты
     * @return Перенаправление на страницу админ панели
     */
    @RequestMapping(path = "/category/delete/{id}", method = RequestMethod.POST)
    public String deleteCategory(@PathVariable(value = "id") int id, Model model, final RedirectAttributes redirectAttrs) {
        Category c = categoryService.findById(id);
        categoryService.delete(c);
        redirectAttrs.addFlashAttribute("selector", "category");
        return "redirect:/admin2";
    }

    /**
     * Метод слушающий все запросы по /product/delete/{id}, удаляет продукт
     * @param id Идентификатор продукта
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param redirectAttrs Flash-атрибуты
     * @return Перенаправление на страницу админ панели
     */
    @RequestMapping(path = "/product/delete/{id}", method = RequestMethod.POST)
    public String deleteProduct(@PathVariable(value = "id") int id, Model model, final RedirectAttributes redirectAttrs) {
        Product p = productService.findById(id);
        List<Property> properties = p.getProperties();
        for (Property property : properties) {
            propertyService.delete(property);
        }
        productService.delete(p);
        redirectAttrs.addFlashAttribute("selector", "product");
        return "redirect:/admin2";
    }

    /**
     * Метод слушающий все запросы по /product/change/{id}, изменяет поля продукта
     * @param id Идентификатор продукта
     * @param Name Название товара
     * @param Price Цена продукта
     * @param Quantity Количество продукта
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param redirectAttrs Flash-атрибуты
     * @return Перенаправление на страницу админ панели
     */
    @RequestMapping(path = "/product/change/{id}", method = RequestMethod.POST)
    public String changeProductData(@PathVariable(value = "id") int id, @RequestParam String Name,
                                    @RequestParam int Price, @RequestParam int Quantity, Model model,
                                    final RedirectAttributes redirectAttrs) {
        Product p = productService.findById(id);
        p.setName(Name);
        p.setPrice(Price);
        p.setQuantity(Quantity);
        productService.change(p);
        redirectAttrs.addFlashAttribute("selector", "product");
        return "redirect:/admin2";
    }

    /**
     * Метод слушающий все запросы по /product/savechangeinfo/{id}, изменяет поля продукта
     * @param id Идентификатор продукта
     * @param Name Название товара
     * @param Price Цена продукта
     * @param Quantity Количество продукта
     * @param Manufacturer Производитель продукта
     * @param Link Ссылка на изображение
     * @param Category Категория товара
     * @param Description Описание товара
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param redirectAttrs Flash-атрибуты
     * @param characteristics Список добавленных характеристик
     * @param values Список значений характеристик
     * @return Перенаправление на страницу админ панели
     */
    @RequestMapping(path = "/product/savechangeinfo/{id}", method = { RequestMethod.GET, RequestMethod.POST })
    public String saveChangeInfo(@PathVariable(value = "id") int id, @RequestParam String Name,
                                 @RequestParam int Price, @RequestParam int Quantity,
                                 @RequestParam String Manufacturer, @RequestParam String Link,
                                 @RequestParam int Category, @RequestParam String Description,
                                 Model model, final RedirectAttributes redirectAttrs,
                                 String[] characteristics, String[] values) {
        Product p = productService.findById(id);
        Description description = p.getDescription();
        description.setDescription(Description);
        p.setName(Name);
        p.setCategory(categoryService.findById(Category));
        p.setManufacturer(Manufacturer);
        p.setLink(Link);
        p.setDescription(description);
        p.setPrice(Price);
        p.setQuantity(Quantity);
        productService.change(p);
        List<Property> properties = p.getProperties();
        if (!properties.isEmpty()) {
            for (Property property : properties) {
                propertyService.delete(property);
            }
        }

        for (int i = 0; i < characteristics.length; i++) {
            if (!characteristics[i].isEmpty() & !values[i].isEmpty()){
                Property property = new Property();
                property.setName(characteristics[i]);
                property.setValue(values[i]);
                property.setProduct(p);
                propertyService.save(property);
            }
        }
        redirectAttrs.addFlashAttribute("selector", "product");
        return "redirect:/admin2";
    }

    /**
     * Метод слушающий все запросы по /product/changeinfo/{id}
     * @param id Идентификатор продукта
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу редактирвоания продукта
     */
    @RequestMapping(path = "/product/changeinfo/{id}", method = RequestMethod.POST)
    public String changeAllProductData(@PathVariable(value = "id") int id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("categories", categoryService.readAll());
        model.addAttribute("product",product);
        return "product_edit";
    }

    /**
     * Метод слушающий все запросы по /category/delete, удаляет существующую категорию товара
     * @param Name Название категории
     * @param Link Ссылка на картинку обложки
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param redirectAttrs Flash-атрибуты
     * @return Перенаправление на админ панель
     */
    @RequestMapping(path = "/category/delete", method = RequestMethod.POST)
    public String DeleteCategory(@RequestParam String Name, @RequestParam String Link,
                                 Model model, final RedirectAttributes redirectAttrs) {
        Category  c = new Category();
        c.setName(Name);
        c.setLink(Link);
        categoryService.del(c);
        redirectAttrs.addFlashAttribute("selector", "category");
        return "redirect:/admin2";
    }

    /**
     * Метод слушающий все запросы по /user_info/changename
     * @param request Объект запроса
     * @param response Объект ответа
     * @return Возвращает имя
     * @throws Exception Исключение
     */
    @RequestMapping(value = "/user_info/changename", method = RequestMethod.POST)
    public @ResponseBody String addname(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String firstName = request.getParameter("firstname");
        String email = request.getParameter("email");
        userService.updateUserName(userService.loadUserByUsername(email).getId(),firstName);
        return firstName;
    }

    /**
     * Метод слушающий все запросы по /user_info/changesurname
     * @param request Объект запроса
     * @param response Объект ответа
     * @return Возвращает фамилию
     * @throws Exception Исключение
     */
    @RequestMapping(value = "/user_info/changesurname", method = RequestMethod.POST)
    public @ResponseBody String addsurname(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        userService.updateSurName(userService.loadUserByUsername(email).getId(),surname);
        return surname;
    }

    /**
     * Метод слушающий все запросы по /makereview, добавляет новый отзыв продукту
     * @param request Объект запроса
     * @param response Объект ответа
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает строковое представление идентификатора товара
     * @throws Exception Исключение
     */
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
            review.setProduct(productService.findById(id));
            review.setUser(userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
            review.setCreated(LocalDate.now(ZoneId.of("Europe/Moscow")));
            reviewService.save(review);
        }
        long rat = 0;
        for (Review rev:productService.findById(id).getReviews()) {
            rat += rev.getRating();
        }
        int average = (int) (rat/productService.findById(id).getReviews().size());
        Product product = productService.findById(id);
        product.setRating(average);
        productService.save(product);
        return String.valueOf(id);
    }

    /**
     *
     * @param number
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return
     */
    @GetMapping("shopping_card/add/{number}")
    String addCartItem(@PathVariable(value = "number") int number, Model model) {
        Cart_Item cart_item = new Cart_Item();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().noneMatch(o->o.getStatus()==-1)) {
            Order o =new Order();
            o.setStatus(-1);
            o.setUserid(userService.loadUserByUsername(authentication.getName()).getId());
            orderService.save(o);
            cart_item.setProduct(productService.findById(number));
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
                cart_item.setProduct(productService.findById(number));
                cart_item.setQuantity(1);
                cart_item.setOrder(orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId()).stream().filter(o -> o.getStatus() == -1).findAny().get());
                cartItemService.create(cart_item);
            }
        }
        return "redirect:/shopping_card";
    }

    /**
     *
     * @param number
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Перенаправление на страницу пользователя
     */
    @GetMapping("wishadd/{number}")
    String addWishItem(@PathVariable(value = "number") int number, Model model) {
        WishItem wishItem = new WishItem();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        wishItem.setProduct(productService.findById(number));
        wishItem.setUser(userService.loadUserByUsername(authentication.getName()));
        wishService.save(wishItem);
        return "redirect:/user_info";
    }

    /**
     * Метод слушающий все запросы по /wishdelete/{number}
     * @param number Идентификатор товара
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Перенаправление на страницу пользователя
     */
    @GetMapping("wishdelete/{number}")
    String deleteWishItem(@PathVariable(value = "number") int number, Model model) {
        wishService.delete((long) number);
        return "redirect:/user_info";
    }

    /**
     * Метод слушающий все запросы по /shopping_card
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу корзины
     */
    @RequestMapping(path = "/shopping_card")
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

    /**
     * Метод слушающий все запросы по shopping_card/change, изменяет содержимое корзины
     * @param request Объект запроса
     * @param response Объект ответа
     * @return Возвращает ок
     * @throws Exception Исключение
     */
    @RequestMapping(value = "shopping_card/change", method = RequestMethod.POST)
    public @ResponseBody String ShoppingCardChange(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int new_quantity = Integer.parseInt(request.getParameter("quantity"));
        int id = Integer.parseInt(request.getParameter("id"));
        Cart_Item cart_item = cartItemService.findbyid(id).get(0);
        cart_item.setQuantity(new_quantity);
        cartItemService.create(cart_item);
        return "ok";
    }

    /**
     * Метод слушающий все запросы по /shopping_card/delete, удаляет корзину
     * @param id Идентификатор корзины
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Перенаправление на страницу корзины
     */
    @GetMapping("shopping_card/delete")
    public String delete(@RequestParam(value = "id") int id,Model model) {
        cartItemService.delete(cartItemService.findbyid(id).get(0));
        return "redirect:/shopping_card";
    }

    /**
     * Метод слушающий все запросы по /user_info
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу пользователя
     */
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

    /**
     * Метод слушающий все запросы по /user_info/findNumber
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @param number Идентификатор заказа
     * @return Перенаправление на страницу заказа
     */
    @PostMapping(path="/user_info/findNumber")
    public String userinfo(Model model,int number) {
        return "redirect:/user_info/orders/"+number;
    }

    /**
     * Метод слушающий все запросы по /shopping_card/order_confirm
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу подтверждения заказа
     */
    @RequestMapping(path="shopping_card/order_confirm")
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

    /**
     * Метод слушающий все запросы по /shopping_card/order_confirm/success
     * @param data Дата
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Перенаправление на страницу пользователя
     */
    @RequestMapping(path="shopping_card/order_confirm/success")
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

    /**
     * Метод слушающий все запросы по /user_info/orders/{number}
     * @param number Идентификатор заказа
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу заказа
     */
    @RequestMapping(path="user_info/orders/{number}")
    public String userinfo(@PathVariable(value = "number") String number, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderService.findbyuser(userService.loadUserByUsername(authentication.getName()).getId());
        model.addAttribute("order",orderService.findbynumber(number));
        model.addAttribute("kolvo", size());
        return "orderinfo";
    }

    /**
     * Метод слушающий все запросы по /user_info/orders/deleteinfo/{number}, удаляет заказ
     * @param number Идентификатор заказа
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Перенаправление на страницу пользователя
     */
    @RequestMapping(path="user_info/orders/deleteinfo/{number}")
    public String deleteinfo(@PathVariable(value = "number") String number,Model model) {
        orderService.delete(number);
        return "redirect:/user_info";
    }

    /**
     * Метод слушающий все запросы по /categories/{category}
     * @param engname Английское название категории
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу с запрашиваемой категорией товаров
     */
    @RequestMapping("/categories/{category}")
    public String categoryProducts(@PathVariable(value = "category") String engname, Model model){
        Category category = categoryService.findByEngname(engname);
        List<Product> products = productService.findById_category(category.getId());
        ArrayList<String> manufactures = new ArrayList<String>();
        if (!products.isEmpty()){
            for (Product product:products){
                manufactures.add(product.getManufacturer());
            }
        }
        Set<String> set_manufactures =new LinkedHashSet<>(manufactures);
        if (set_manufactures.contains("")){
            set_manufactures.remove("");
        }
        model.addAttribute("products", products);
        model.addAttribute("current_category", category);
        model.addAttribute("categories", categoryService.readAll());
        model.addAttribute("kolvo", size());
        String [] manufactures_checked = new String[set_manufactures.size()];
        Arrays.fill(manufactures_checked, null);
        model.addAttribute("manufactures_checked", manufactures_checked);

        String[] list_rating = {null,null,null,null,null,null};
        model.addAttribute("list_rating", list_rating);

        model.addAttribute("manufactures", set_manufactures);
        String[] confirm = {"on",null,null};
        model.addAttribute("confirm", confirm);
        return "product_list1";
    }

    /**
     * Метод слушающий все запросы по /categories/filter
     * @param category Объект категории
     * @param rating Массив выбранного рейтинга
     * @param quantity Количество продукта
     * @param manufactures_list Массив выбранных производителей
     * @param filterName Фрагмент названия продукта
     * @param minPrice Минимальная цена продукта
     * @param maxPrice Максимальная цена продукта
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу отфильтрованных товаров
     */
    @RequestMapping("/categories/filter")
    public String categoryFilter(@RequestParam String category,@RequestParam int quantity,
                                 @RequestParam(required =false) int[] rating,
                                 @RequestParam(required =false) String[] manufactures_list,
                                 @RequestParam(required =false) String filterName,
                                 @RequestParam(required =false) Integer minPrice,
                                 @RequestParam(required =false) Integer maxPrice,
                                 Model model)
    {
        Category c = categoryService.findByEngname(category);
        List<Product> products = productService.findById_category(c.getId());
        ArrayList<String> manufactures = new ArrayList<String>();
        if (!products.isEmpty()){
            for (Product product:products){
                manufactures.add(product.getManufacturer());
            }
        }
        Set<String> set_manufactures =new LinkedHashSet<>(manufactures);
        if (set_manufactures.contains("")){
            set_manufactures.remove("");
        }
        model.addAttribute("current_category", c);
        model.addAttribute("categories", categoryService.readAll());
        model.addAttribute("kolvo", size());
        model.addAttribute("manufactures", set_manufactures);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("filterName", filterName);
        List<String> manufactures_set = new ArrayList<>(set_manufactures);
        String [] manufactures_checked = new String[set_manufactures.size()];
        Arrays.fill(manufactures_checked, null);
        if (manufactures_list!=null) {
            for (int i = 0; i < manufactures_list.length; i++) {
                if (manufactures_set.contains(manufactures_list[i])) {
                    manufactures_checked[manufactures_set.indexOf(manufactures_list[i])] = "on";
                }
            }
        }
        model.addAttribute("manufactures_checked", manufactures_checked);


        String[] list_rating = {null,null,null,null,null,null};
        if (rating!=null){
            for (int i=0;i<rating.length;i++){
                switch (rating[i]){
                    case 0: list_rating[0] = "on";
                        break;
                    case 1: list_rating[1] = "on";
                        break;
                    case 2: list_rating[2] = "on";
                        break;
                    case 3: list_rating[3] = "on";
                        break;
                    case 4: list_rating[4] = "on";
                        break;
                    case 5: list_rating[5] = "on";
                        break;
                }
            }
        }
        model.addAttribute("list_rating", list_rating);

        String[] confirm = {null,null,null};
        confirm[quantity-1] = "on";
        model.addAttribute("confirm", confirm);

        model.addAttribute("products", criteriaService.takeProductList(c, rating, quantity, manufactures_list, filterName ,minPrice,maxPrice, manufactures_set));
        return  "product_list1";
    }

    /**
     * Метод ошибки
     * @return Возвращает страницу ошибки
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * Метод слушающий все запросы по /error
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу ошибки
     */
    @RequestMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("kolvo", size());
        return "error";
    }

    /**
     * Метод слушающий все запросы по /wishlist
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу желаемых товаров
     */
    @GetMapping("/wishlist")
    public String wishlist(Model model) {
        model.addAttribute("kolvo", size());
        return "wishlist";
    }

    /**
     * Метод слушающий все запросы по /product/{id}
     * @param id Идентификатор продукта
     * @param model Объект предоставляющий атрибуты, используемые для визуализации представлений
     * @return Возвращает страницу с конкретным продуктом
     */
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
        Collections.shuffle(products);
        model.addAttribute("description",product.getDescription().getDescription());
        model.addAttribute("category",product.getCategory());
        model.addAttribute("kolvo", size());
        return "product";
    }
}
