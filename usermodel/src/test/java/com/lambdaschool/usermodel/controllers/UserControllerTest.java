package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    List<User> userList = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        Role r3 = new Role("data");
        r3.setRoleid(3);


        // admin, data, user
        User u1 = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@email.local"));
        u1.getUseremails().get(0).setUseremailid(1);
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@mymail.local"));
        u1.getUseremails().get(1).setUseremailid(2);

        u1.setUserid(1);
        userList.add(u1);


        // data, user
        User u2 = new User("cinnamon",
            "1234567",
            "cinnamon@lambdaschool.local");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getRoles().add(new UserRoles(u2, r3));
        u2.getUseremails()
            .add(new Useremail(u2,
                "cinnamon@mymail.local"));
        u2.getUseremails().get(0).setUseremailid(1);
        u2.getUseremails()
            .add(new Useremail(u2,
                "hops@mymail.local"));
        u2.getUseremails().get(1).setUseremailid(2);
        u2.getUseremails()
            .add(new Useremail(u2,
                "bunny@email.local"));
        u2.getUseremails().get(2).setUseremailid(3);

        u2.setUserid(2);
        userList.add(u2);


        // user
        User u3 = new User("barnbarn",
            "ILuvM4th!",
            "barnbarn@lambdaschool.local");
        u3.getRoles().add(new UserRoles(u3, r2));
        u3.getUseremails()
            .add(new Useremail(u3,
                "barnbarn@email.local"));
        u2.getUseremails().get(0).setUseremailid(1);

        u3.setUserid(3);
        userList.add(u3);

        User u4 = new User("puttat",
            "password",
            "puttat@school.lambda");
        u4.getRoles().add(new UserRoles(u4, r2));
       // u4.getUseremails().get(0).setUseremailid(1);

        u4.setUserid(4);
        userList.add(u4);

        User u5 = new User("misskitty",
            "password",
            "misskitty@school.lambda");
        u5.getRoles().add(new UserRoles(u5, r2));
        //u5.getUseremails().get(0).setUseremailid(1);
        u5.setUserid(5);
        userList.add(u5);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void a_listAllUsers() throws Exception
    {
        String urls = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(urls).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String tr = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList);

        assertEquals(er, tr);
    }

    @Test
    public void b_getUserById() throws Exception
    {
        String urls = "/users/user/1";
        Mockito.when(userService.findUserById(1)).thenReturn(userList.get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(urls).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String tr = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void c_getUserByIdNotFound() throws Exception
    {
        String urls = "/users/user/666";
        Mockito.when(userService.findUserById(666)).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(urls).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String tr = result.getResponse().getContentAsString();

        String er = "";

        assertEquals(er, tr);
    }

    @Test
    public void d_getUserByName() throws Exception
    {
        String urls = "/users/user/name/cinnamon";
        Mockito.when(userService.findByName("cinnamon")).thenReturn(userList.get(2));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(urls).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String results = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String reseltss = mapper.writeValueAsString(userList.get(2));

        assertEquals(reseltss, results);
    }

    @Test
    public void f_getUserLikeName() throws Exception
    {
        String urls = "/users/user/name/like/cinnamon";
        List<User> likeNameList = new ArrayList<>();
        likeNameList.add(userList.get(2));
        Mockito.when(userService.findByNameContaining("cinnamon")).thenReturn(likeNameList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(urls).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String results = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String reseltss = mapper.writeValueAsString(likeNameList);

        assertEquals(reseltss, results);
    }

    @Test
    public void g_addNewUser() throws Exception
    {
        String urls = "/users/user";
        User newUser = new User("newuser", "passwd", "emails@email.com");
        newUser.setUserid(75);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(newUser);

        Mockito.when(userService.save(any(User.class))).thenReturn(newUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(urls).accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userString);
        mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void h_updateFullUser() throws Exception
    {
        String urls = "/users/user/3";
        User newUser = new User("newuser", "passwd", "emails@email.com");
        newUser.setUserid(3);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(newUser);

        Mockito.when(userService.save(any(User.class))).thenReturn(newUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(urls).accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userString);
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void i_updateUser() throws Exception
    {
        String urls = "/users/user/4";
        User updatedUser = userList.get(3);
        updatedUser.setUsername("newusername");

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(updatedUser);

        Mockito.when(userService.update(updatedUser, 4)).thenReturn(updatedUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(urls).accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON).content(userString);
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void j_deleteUserById() throws Exception
    {
//        String urls = "/users/user/4";
//        Mockito.when(userService.delete(any(Long.class))); .thenReturns
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(urls).accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        String tr = result.getResponse().getContentAsString();
//
//        ObjectMapper mapper = new ObjectMapper();
//        String er = ""; //mapper.writeValueAsString(userList.get(0));
//
//        assertEquals(er, tr);

    }
}