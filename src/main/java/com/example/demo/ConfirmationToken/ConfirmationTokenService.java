package com.example.demo.ConfirmationToken;

import com.example.demo.CartItem.Cart_Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {

        confirmationTokenRepository.save(confirmationToken);
    }
    public List<ConfirmationToken> readAll() {
        return confirmationTokenRepository.findAll();
    }
    public void deleteConfirmationToken(Long id) {

        confirmationTokenRepository.deleteById(id);
    }


    public Optional<ConfirmationToken> findConfirmationTokenByToken(String token) {

        return confirmationTokenRepository.findConfirmationTokenByConfirmationToken(token);
    }

}
