package com.revature.services;

import com.revature.models.User;
import com.revature.repos.UserDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServicesTests {
    private UserServices userServices;
    private UserDAO userDAO;
    private User mockedUser;
    private final int userId = 2;
    private final String testName = "John";
    private final String testLastName = "Doe";
    private final String validEmail = "jhdoe@example.com";
    private final String validPassword = "ValidPW88";
    private final String newFirstName = "Jane";
    private final String newLastName = "Austen";
    private final String newEmail = "example.123@outlook.com";
    private final String newPassword = "ABetterPassword123";
    private Answer<Object> updateFunction;


    @Before
    public void setUp(){
        userDAO = Mockito.mock(UserDAO.class);
        userServices = new UserServices(userDAO);
        mockedUser = new User(testName, testLastName, validEmail, validPassword);
        updateFunction = invocation -> {
            User captured = invocation.getArgument(0);
            return new User(captured.getFirstName(), captured.getLastName(), captured.getEmail(), captured.getPassword());
        };
    }

    @Test
    public void createUserWithValidDataShouldReturnUserInstance(){
        when(userDAO.getUserByEmail(validEmail)).thenReturn(null);
        when(userDAO.create(any(User.class))).thenReturn(mockedUser);

        User returnedUser = userServices.createUser(testName, testLastName, validEmail, validPassword);

        Assert.assertEquals(testName, returnedUser.getFirstName());
        Assert.assertEquals(testLastName, returnedUser.getLastName());
        Assert.assertEquals(validEmail, returnedUser.getEmail());
        Assert.assertEquals(validPassword, returnedUser.getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createUserWithNonValidEmailShouldThrowException(){
        String nonValidEmail = "jonhDoe.com";
        when(userDAO.getUserByEmail(nonValidEmail)).thenReturn(null);
        User user = userServices.createUser(testName, testLastName, nonValidEmail, validPassword);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createUserWithRegisteredEmailShouldThrowException(){
        when(userDAO.getUserByEmail(validEmail)).thenReturn(mockedUser);
        User user = userServices.createUser(testName, testLastName, validEmail, validPassword);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createUserWithAShortPasswordShouldThrowException(){
        String shortPassword = "passw";
        when(userDAO.getUserByEmail(validEmail)).thenReturn(null);
        User user = userServices.createUser(testName, testLastName, validEmail, shortPassword);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createUserWithFullCapitalPasswordShouldThrowException(){
        String capitalPassword = "PASSWORD891";
        when(userDAO.getUserByEmail(validEmail)).thenReturn(null);
        User user = userServices.createUser(testName, testLastName, validEmail, capitalPassword);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createUserWithFullLowerCasePasswordShouldThrowException(){
        String lowerCasePassword = "passwordlowercase";
        when(userDAO.getUserByEmail(validEmail)).thenReturn(null);
        User user = userServices.createUser(testName, testLastName, validEmail, lowerCasePassword);
    }

    @Test
    public void loginUserWithAnUnregisteredEmailShouldReturnNull(){
        when(userDAO.getUserByEmail(validEmail)).thenReturn(null);
        User user = userServices.loginUser(validEmail, validPassword);
        Assert.assertNull(user);
    }

    @Test
    public void loginWithNotMatchingPasswordShouldReturnNull(){
        String incorrectPassword = "WontMatch";
        when(userDAO.getUserByEmail(validEmail)).thenReturn(mockedUser);
        User user = userServices.loginUser(validEmail, incorrectPassword);
        Assert.assertNull(user);
    }

    @Test
    public void loginWithValidDataShouldReturnUser(){
        when(userDAO.getUserByEmail(validEmail)).thenReturn(mockedUser);
        User user = userServices.loginUser(validEmail, validPassword);
        Assert.assertEquals(testName, user.getFirstName());
        Assert.assertEquals(testLastName, user.getLastName());
    }

    @Test
    public void updateNameWhenUserIsRegisteredShouldReturnUser(){
        User updatedUser = new User(newFirstName, newLastName, validEmail, validPassword);
        when(userDAO.getById(userId)).thenReturn(mockedUser);
        when(userDAO.update(any(User.class))).thenReturn(updatedUser);

        User returnedUser = userServices.updateName(userId , newFirstName, newLastName);
        Assert.assertEquals(newFirstName, returnedUser.getFirstName());
        Assert.assertEquals(newLastName, returnedUser.getLastName());
    }

    @Test
    public void updateNameWhenUserIsNotRegisteredShouldReturnNull(){
        when(userDAO.getById(userId)).thenReturn(null);
        User returnedUser = userServices.updateName(userId, testName, testLastName);
        Assert.assertNull(returnedUser);
    }

    @Test
    public void updateNameWhenGivenNullAsLastNameItShouldNotBeUpdated(){
        when(userDAO.getById(userId)).thenReturn(mockedUser);
        when(userDAO.update(any(User.class))).thenAnswer(updateFunction);

        User returnedUser = userServices.updateName(userId, newFirstName, null);

        Assert.assertEquals(newFirstName, returnedUser.getFirstName());
        Assert.assertEquals(testLastName, returnedUser.getLastName());
    }

    @Test
    public void updateNameWhenGivenFirstNameNullShouldNotUpdateFirstName(){
        when(userDAO.getById(userId)).thenReturn(mockedUser);
        when(userDAO.update(any(User.class))).thenAnswer(updateFunction);

        User returnedUser = userServices.updateName(userId, null, newLastName);

        Assert.assertEquals(testName, returnedUser.getFirstName());
        Assert.assertEquals(newLastName, returnedUser.getLastName());
    }

    @Test
    public void updateEmailWithWrongPasswordShouldReturnNull(){
        String wrongPassword = "wrongPassword123";
        when(userDAO.getById(userId)).thenReturn(mockedUser);
        User returnedUser = userServices.updateEmail(userId, newEmail, wrongPassword);
        Assert.assertNull(returnedUser);
    }

    @Test
    public void updateEmailWithCorrectPasswordShouldReturnUserWithNewEmail(){
        when(userDAO.getById(userId)).thenReturn(mockedUser);
        when(userDAO.update(any(User.class))).thenAnswer(updateFunction);
        User returnedUser = userServices.updateEmail(userId, newEmail, validPassword);
        Assert.assertEquals(newEmail, returnedUser.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmailWithNotValidEmailShouldThrowAnException(){
        String notValidEmail = "notValidEmail.com";
        when(userDAO.getById(userId)).thenReturn(mockedUser);
        when(userDAO.update(any(User.class))).thenAnswer(updateFunction);
        User returnedUser = userServices.updateEmail(userId, notValidEmail, validPassword);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmailWithARegisteredEmailShouldReturnAnException(){
        String registeredEmail = "example123@yahoo.com";
        when(userDAO.getUserByEmail(registeredEmail)).thenReturn(mockedUser);
        when(userDAO.getById(userId)).thenReturn(mockedUser);
        User returnedUser = userServices.updateEmail(userId, registeredEmail, validPassword);
    }

    @Test
    public void updatePasswordWhenOldPasswordDoNotMatchShouldReturnNull(){
        String oldPassword = "WontMatch123";
        when(userDAO.getById(userId)).thenReturn(mockedUser);
        User returnedUser = userServices.updatePassword(userId, oldPassword, newPassword);
        Assert.assertNull(returnedUser);
    }

    @Test
    public void updatePasswordWithValidPasswordAndMatchingOldPasswordShouldReturnUserWithNewPassword(){
        when(userDAO.getById(userId)).thenReturn(mockedUser);
        when(userDAO.update(any(User.class))).thenAnswer(updateFunction);
        User returnedUser = userServices.updatePassword(userId, validPassword, newPassword);
        Assert.assertEquals(newPassword, returnedUser.getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatePasswordWithNotValidPasswordShouldThrowAnException(){
        String notValidPassword = "notvalid";
        when(userDAO.getById(userId)).thenReturn(mockedUser);
        when(userDAO.update(any(User.class))).thenAnswer(updateFunction);
        User returnedUser = userServices.updatePassword(userId, validPassword, notValidPassword);
    }
}
