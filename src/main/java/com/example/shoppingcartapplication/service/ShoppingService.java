package com.example.shoppingcartapplication.service;

import com.example.shoppingcartapplication.POJO.CartItemMapper;
import com.example.shoppingcartapplication.model.CartItem;
import com.example.shoppingcartapplication.model.Shop;
import com.example.shoppingcartapplication.repository.CartItemRepository;
import com.example.shoppingcartapplication.repository.ShoppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ShoppingService {

    @Autowired
    private ShoppingRepository shoppingRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    //create an image extension
    public boolean saveShop(MultipartFile file, Shop shop) {

        boolean flag = false;

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        //Extract the image extension
        String ext = fileName.substring(fileName.indexOf(".") + 1);

        //set image extension
        shop.setImageExt(ext);

        if (fileName.isEmpty()) {
            System.out.println("Not a valid file");
        }

        try {

            shop.setImage("data:img/" + ext + ";base64," + Base64.getEncoder().encodeToString(file.getBytes()));

            shoppingRepository.save(shop);

            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }


    //Get list of products
    public List<Shop> getShop() {
        List<Shop> shop = new ArrayList<>();

        try {
            shop = shoppingRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shop;
    }

    //Get operations on product
    public Shop getShopById(Long shopId) {
        Shop shop = null;

        try {
            shop = shoppingRepository.findById(shopId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shop;
    }

    //Create operation on product
    public boolean editShop(MultipartFile file, Shop shop) {
        boolean flag = false;

        try {
            Shop editShop = shoppingRepository.findById(shop.getId()).get();

            editShop.setCategory(shop.getCategory());
            editShop.setPrice(shop.getPrice());
            editShop.setQuantity(shop.getQuantity());
            editShop.setName(shop.getName());
            editShop.setDescription(shop.getDescription());

            //check if image was also edited
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            if (!fileName.isEmpty()) {
                //Extract the image extension
                String ext = fileName.substring(fileName.indexOf(".") + 1);

                //set image extension
                editShop.setImageExt(ext);
                editShop.setImage("data:img/" + ext + ";base64," + Base64.getEncoder().encodeToString(file.getBytes()));
            }

            shoppingRepository.save(editShop);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    // DELETE operation on Product

    public boolean deleteShop(Long shopId) {
        boolean flag = false;

        try {

            cartItemRepository.deleteAllByShopId(shopId);

            shoppingRepository.deleteById(shopId);

            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    //GET operation on Comment

    public List<CartItemMapper> getCartItemOnShopBy(Long customerId) {
        List<CartItemMapper> listOfCartItem = new ArrayList<>();

        try {
            List<CartItem> cartItem = cartItemRepository.findAllByCustomerId(customerId);

            cartItem
                    .forEach(cartItem1 -> {
                        CartItemMapper cartItemMapper = new CartItemMapper();

                        cartItemMapper.setMyCartItem(cartItem1.getMyCartItem());
                        cartItemMapper.setId(cartItem1.getId());

                        Shop shop = shoppingRepository.findById(cartItem1.getShop().getId()).get();

                        cartItemMapper.setShop(shop);

                        Long sum = cartItem1.getMyCartItem() * shop.getPrice();

                        cartItemMapper.setSum(sum);

                        listOfCartItem.add(cartItemMapper);
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listOfCartItem;
    }
}