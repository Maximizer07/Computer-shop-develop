package com.example.demo.WishItem;

import com.example.demo.Order.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * сервис работы с товарами в списке желаемых
 * @author mike
 */
@Service
public class WishService {
    /**
     * репозиторий работы с товарами в списке желаемых
     */
    final
    WishItemRepository wishItemRepository;

    /**
     * конструктор
     * @param wishItemRepository репозиторий работы с товарами в списке желаемых
     */
    public WishService(WishItemRepository wishItemRepository) {
        this.wishItemRepository = wishItemRepository;
    }

    /**
     * получение всех товаров в списке желаемых
     * @return список товаров в списке желаемых
     */
    public List<WishItem> findAll(){
        return wishItemRepository.findAll();
    }

    /**
     * добавление товара в список желаемых
     * @param wishItem товар
     */
    public void save(WishItem wishItem){
        wishItemRepository.save(wishItem);
    }

    /**
     * поиск по id товара
     * @param id id товара
     * @return список товаров
     */
    public List<WishItem> findbyid(long id){
        return wishItemRepository.findById(id);
    }

    /**
     * удаление товара по id
     * @param id id товара
     */
    public void delete(Long id){
        wishItemRepository.deleteById(id);
    }
}
