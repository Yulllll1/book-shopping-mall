package io.elice.shoppingmall.product.controller;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductViewController {

    @GetMapping("/books")
    public String viewBookList() {
        return "/product-list/product-list.html";
    }

    @GetMapping("/book/{id}")
    public String viewBook() {
        return "/product-detail/product-detail.html";
    }
}
