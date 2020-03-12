package com.interview.login;
/**
 * @author Mauricio Lomeli
 * @version March, 2020
 *
 * This program generates a login system for Users to
 * authenticate. Special thanks to Google who has provided the code.
 */

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.interview.data.LoginDataSource;
import com.interview.data.LoginRepository;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
