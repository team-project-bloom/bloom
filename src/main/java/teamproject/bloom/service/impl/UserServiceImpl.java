package teamproject.bloom.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamproject.bloom.model.ShoppingCart;
import teamproject.bloom.model.User;
import teamproject.bloom.repository.ShoppingCartRepository;
import teamproject.bloom.repository.UserRepository;
import teamproject.bloom.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public User saveUser(String userName) {
        User user = new User();
        user.setUserName(userName);
        user = userRepository.save(user);
        createShoppingCart(user);
        return user;
    }

    private void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
