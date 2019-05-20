package com.example.myapplication.test.java.com.example.myapplication;

import com.example.myapplication.models.Like;
import com.example.myapplication.models.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * File created by tcarau18
 * <p>
 * Model Tests Class used to test basic functionality for the Post model Class
 **/
public class UserModelTests {
    private User user = new User();
    private Like like = new Like();

    @Test
    public void setLikeUserId() {
        String user_id = "This Is My Id";
        like.setUser_id(user_id);
        assertEquals(user_id, like.getUser_id());
    }

    @Test
    public void setLikeConstructorAndToString() {
        String user_id = "This Is My Id";
        String wrong_user_id = "This Is Wrong Id";

        like = new Like(user_id);

        assertEquals(user_id, like.getUser_id());
        assertTrue(like.toString().contains(user_id));
        assertFalse(like.toString().contains(wrong_user_id));

    }

    @Test
    public void setUser_UserName() {
        String userName = "UserName";
        user.setUsername(userName);
        assertEquals(userName, user.getUsername());
    }

    @Test
    public void setUser_About_Information() {
        String about = "About Me";
        user.setAbout(about);
        assertEquals(about, user.getAbout());
    }

    @Test
    public void setUser_DisplayName() {
        String displayName = "User. My. Name. Is";
        user.setDisplay_name(displayName);
        assertEquals(displayName, user.getDisplay_name());
    }

    @Test
    public void setUser_Profile_photo() {
        String profile_photo = "this_is_my_photo";
        user.setProfile_photo(profile_photo);
        assertEquals(profile_photo, user.getProfile_photo());
    }

    @Test
    public void setUser_Website() {
        String website = "website";
        user.setWebsite(website);
        assertEquals(website, user.getWebsite());
    }

    @Test
    public void setUser_Followers() {
        long followers = 24;
        user.setFollowers(followers);
        assertEquals(followers, user.getFollowers());
    }

    @Test
    public void setUser_Following() {
        long following = 0;
        user.setFollowing(following);
        assertEquals(following, user.getFollowing());
    }

    @Test
    public void setUser_Posts() {
        long posts = 28;
        user.setNrPosts(posts);
        assertEquals(posts, user.getPosts());
    }

    @Test
    public void setUser_PhoneNumber() {
        long phoneNumber = 2464564;
        user.setPhone_number(phoneNumber);
        assertEquals(phoneNumber, user.getPhone_number());
    }

    @Test
    public void setUser_Email() {
        String email = "UserName@mail.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void setUser_Constructor() {
        String userName = "UserName";
        String incorrectUserName = "Incorrect UserName";
        String email = "UserName@mail.com";
        long phoneNumber = 2464564;
        long posts = 28;
        long following = 0;
        long followers = 24;
        String website = "website";
        String profile_photo = "this_is_my_photo";
        String displayName = "User. My. Name. Is";
        String about = "About Me";

        user = new User(about, displayName, userName, email, phoneNumber, followers, following, posts, profile_photo, website);

        assertNotEquals(incorrectUserName, user.getUsername());
        assertEquals(displayName, user.getDisplay_name());
        assertEquals(profile_photo, user.getProfile_photo());
        assertEquals(website, user.getWebsite());
        assertEquals(followers, user.getFollowers());
        assertEquals(following, user.getFollowing());
        assertEquals(posts, user.getPosts());
        assertEquals(phoneNumber, user.getPhone_number());
        assertEquals(email, user.getEmail());

        assertFalse(user.toString().contains(incorrectUserName));
        assertTrue(user.toString().contains(userName));

        //Now changing the currently existing User Email
        user.setEmail("differentMail");
        assertNotEquals(user.getEmail(), email);

    }

}
