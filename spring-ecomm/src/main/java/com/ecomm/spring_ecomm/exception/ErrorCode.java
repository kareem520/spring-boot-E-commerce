package com.ecomm.spring_ecomm.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
public enum ErrorCode {

    PRODUCT_ALREADY_EXISTS_IN_CART("PRODUCT_ALREADY_EXISTS", "Product with id: '%s' already exists in cart", HttpStatus.CONFLICT),
    USER_Cart_FOUND("USER_Cart_FOUND", "User don't have cart: '%s' not found", NOT_FOUND),
    NO_ORDERS_FOUND("NO_ORDERS_FOUND", "No orders", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("USER_NOT_FOUND", "User by this email: '%s' not found", NOT_FOUND),
    SELLER_MUST_BE_LOGGED_IN("SELLER_MUST_BE_LOGGED_IN", "for only sellers", NOT_FOUND),
    NO_ITEMS_TO_PLACE("NO_ITEMS_TO_PLACE", "Your Cart Is Empty", HttpStatus.NOT_FOUND),
    AUTHORIZED_FOR_ADMINS_ONLY("AUTHORIZED_FOR_ADMINS_ONLY", "Only For Admins!", UNAUTHORIZED),
    PASSWORDS_DO_NOT_MATCH("PASSWORDS_DO_NOT_MATCH", "New password and confirm password do not match", BAD_REQUEST),
    NO_ENOUGH_QUANTITY_FOUND("NO_ENOUGH_QUANTITY_FOUND", "quantity of '%s' available in stock is not enough", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_AVAILABLE_IN_STOCK("PRODUCT_NOT_AVAILABLE_IN_STOCK", "product '%s' not available in stock", HttpStatus.NOT_FOUND),
    NO_CART_ITEM_FOUND("NO_CART_ITEM_FOUND", "No cartItem with product id: '%s' founded ", HttpStatus.NOT_FOUND),
    NO_PRODUCTS_FOUND("NO_PRODUCTS_FOUND", "No products available", HttpStatus.NOT_FOUND),
    NO_Categories_FOUND("NO_Categories_FOUND", "No Categories available", HttpStatus.NOT_FOUND),
    CATEGORY_HAVE_PRODUCTS("CATEGORY_HAVE_PRODUCTS", "can't delete category already have products on it", HttpStatus.CONFLICT),
    CATEGORY_ALREADY_EXISTS("CATEGORY_ALREADY_EXISTS", "Category already exists", HttpStatus.CONFLICT),
    PRODUCT_ALREADY_EXISTS("PRODUCT_ALREADY_EXISTS", "product already exists", HttpStatus.CONFLICT),
    PRODUCT_ALREADY_EXISTS_IN_YOUR_PRODUCTS("PRODUCT_ALREADY_EXISTS_IN_YOUR_PRODUCTS", "Product already exists in your products", HttpStatus.CONFLICT),
    PRODUCT_BELONGS_TO_ANOTHER_CATEGORY("PRODUCT_BELONGS_TO_ANOTHER_CATEGORY", "WRONG CATEGORY", HttpStatus.CONFLICT),
    UNKNOWN_ROLE("UNKNOWN_ROLE", "The specified role does not exist", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", "Requested '%s' with Id: '%s' was not found", HttpStatus.NOT_FOUND),
    NEW_PASSWORD_SAME_AS_OLD("NEW_PASSWORD_SAME_AS_OLD","New password cannot be the same as the current password",BAD_REQUEST),
    BAD_CREDENTIALS("BAD_CREDENTIALS", "Username and / or password is incorrect", UNAUTHORIZED),
    USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "Cannot find user with the provided username", NOT_FOUND),
    EMAIL_ALREADY_EXISTS("ERR_EMAIL_EXISTS", "Email already exists", CONFLICT),
    PHONE_ALREADY_EXISTS("ERR_PHONE_EXISTS", "An account with this phone number already exists", CONFLICT),
    INTERNAL_EXCEPTION("INTERNAL_EXCEPTION", "An internal exception occurred, please try again or contact the admin", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCOUNT_ALREADY_DEACTIVATED("ACCOUNT_ALREADY_DEACTIVATED", "Account has been deactivated", BAD_REQUEST),
    ACCOUNT_ALREADY_ACTIVATED("ACCOUNT_ALREADY_ACTIVATED", "Account is active already", BAD_REQUEST),
    CURRENT_PASSWORD_INVALID("CURRENT_PASSWORD_INVALID", "Current password is incorrect", BAD_REQUEST);

    private final String code;
    private String defaultMessage;
    private final HttpStatus status;

    ErrorCode(String code, String defaultMessage, HttpStatus status) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;
    }

    public ErrorCode changeDefaultMessage(String message) {
        this.defaultMessage = message;
        return this;
    }

}
