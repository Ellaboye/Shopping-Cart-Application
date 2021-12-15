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

    /**
     * CREATE operation on Product
     * @param file
     * @param shop
     * @return boolean
     * */
    public boolean saveShop(MultipartFile file, Shop shop){

        boolean flag = false;

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        //Extract the image extension
        String ext = fileName.substring(fileName.indexOf(".")+1);

        //set image extension
        shop.setImageExt(ext);

        if(fileName.isEmpty()){
            System.out.println("Not a valid file");
        }

        try {

            shop.setImage("data:image/"+ext+";base64,"+Base64.getEncoder().encodeToString(file.getBytes()));

            shoppingRepository.save(shop);

            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * GET operation on Product
     * @return list of the products
     * */
    public List<Shop> getShop(){
        List<Shop> shop = new ArrayList<>();

        try {
            shop = shoppingRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shop;
    }

    /**
     * GET operation on Product
     * @param shopId
     * @return product by its id
     * */
    public Shop getShopById(Long shopId){
        Shop shop = null;

        try {
            shop = shoppingRepository.findById(shopId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shop;
    }

    /**
     * CREATE operation on Product
     * @param file
     * @return boolean
     * */
    public boolean editShop(MultipartFile file, Shop shop){
        boolean flag = false;

        try{
            Shop editShop = shoppingRepository.findById(shop.getId()).get();

            editShop.setCategory(shop.getCategory());
            editShop.setPrice(shop.getPrice());
            editShop.setQuantity(shop.getQuantity());
            editShop.setName(shop.getName());
            editShop.setDescription(shop.getDescription());

            //check if image was also edited
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            if(!fileName.isEmpty()){
                //Extract the image extension
                String ext = fileName.substring(fileName.indexOf(".")+1);

                //set image extension
                editShop.setImageExt(ext);
                editShop.setImage("data:image/"+ext+";base64,"+Base64.getEncoder().encodeToString(file.getBytes()));
            }

            shoppingRepository.save(editShop);
            flag = true;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * DELETE operation on Product
     * @param shopId
     * @return boolean(true for successful update and false on failure on post)
     * */
    public boolean deleteShop(Long shopId){
        boolean flag = false;

        try {

            cartItemRepository.deleteAllByShopId(shopId);

            shoppingRepository.deleteById(shopId);

            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * GET operation on Comment
     * to get all orders made by a person
     * @param customerId
     * @return list
     * */
    public List<CartItemMapper> getCartItemOnShopBy(Long customerId){
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