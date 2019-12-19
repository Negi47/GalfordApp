package com.example.galeford.models;

import com.google.firebase.Timestamp;

import java.util.List;

public class Products {

    public static final String PRODUCT_ITEM_NAME = "productItemName";
    public static final String PRODUCT_ITEM_PRICE = "productItemPrice";
    public static final String PRODUCT_ITEM_DESCRIPTION = "productItemDescription";
    public static final String PRODUCT_IMAGE = "productImage";
    public static final String PRODUCT_ID = "productId";
    public static  final String PRODUCT_GENDER = "productGender";
    public static  final String USER_LIKED_COUNT = "userLikedCount";
    public static final String PRODUCT_TIMESTAMP = "productTimeStamp";

    private String productItemName;
    private String productItemPrice;
    private  String productGender;
    private String productItemDescription;
    private List<String> productImage;
    private String productId;
    private int userLikedCount;
    private Timestamp productTimeStamp;

    public Products() {}

    public String getProductItemName() {
        return productItemName;
    }

    public void setProductItemName(String productItemName) {
        this.productItemName = productItemName;
    }

    public List<String> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<String> productImage) {
        this.productImage = productImage;
    }

    public String _getProductId() {
        return productId;
    }

    public void _setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductItemPrice() {
        return productItemPrice;
    }

    public void setProductItemPrice(String productItemPrice) {
        this.productItemPrice = productItemPrice;
    }

    public String getProductItemDescription() {
        return productItemDescription;
    }

    public void setProductItemDescription(String productItemDescription) {
        this.productItemDescription = productItemDescription;
    }

    public String getProductGender() {
        return productGender;
    }

    public void setProductGender(String productGender) {
        this.productGender = productGender;
    }

    public Timestamp getProductTimeStamp() {
        return productTimeStamp;
    }

    public void setProductTimeStamp(Timestamp productTimeStamp) {
        this.productTimeStamp = productTimeStamp;
    }

    public int getUserLikedCount() {
        return userLikedCount;
    }

    public void setUserLikedCount(int userLikedCount) {
        this.userLikedCount = userLikedCount;
    }
}
