package com.hugo.commerce.domain.application;

import com.hugo.commerce.domain.application.command.AddCartItem;
import com.hugo.commerce.domain.application.command.ModifyCartItem;
import com.hugo.commerce.domain.model.Cart;
import com.hugo.commerce.domain.model.CartItem;
import com.hugo.commerce.domain.model.CartItemDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartItemFinder cartItemFinder;
    private final CartItemManager cartItemManager;
    private final ProductFinder productFinder;

    public Cart getCart(Long userId) {
        var items = cartItemFinder.findCartItems(userId);

        var productOptionIds = items.stream()
            .map(CartItem::productOptionId)
            .toList();
        var productOptions = productFinder.findProductOptionDetails(productOptionIds)
            .stream()
            .collect(Collectors.toMap(it -> it.option().id(), Function.identity()));

        return new Cart(
            userId,
            items.stream()
                .filter(item -> productOptions.containsKey(item.productOptionId()))
                .map(item -> new CartItemDetail(item, productOptions.get(item.productOptionId())))
                .toList()
        );
    }

    public void addCartItem(Long userId, AddCartItem command) {
        var option = productFinder.findProductOption(command.productOptionId());
        option.validateStock(command.quantity());
        cartItemManager.addCartItem(userId, command);
    }

    public void modifyCartItem(Long userId, ModifyCartItem command) {
        var cartItem = cartItemFinder.findCartItem(command.cartItemId(), userId);
        var option = productFinder.findProductOption(cartItem.productOptionId());
        option.validateStock(command.quantity());
        cartItemManager.modifyCartItem(cartItem, command.quantity());
    }

    public void removeCartItem(Long userId, Long cartItemId) {
        cartItemManager.removeCartItem(cartItemId, userId);
    }

}
