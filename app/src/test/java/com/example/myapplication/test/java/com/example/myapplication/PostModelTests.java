package com.example.myapplication.test.java.com.example.myapplication;

import com.example.myapplication.models.Like;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * File created by tcarau18
 * <p>
 * Model Tests Class used to test basic functionality for the Post model Class
 **/
public class PostModelTests {
    private Post post;
    private User user;
    private List<Like> likes;

    @Before
    public void createLogHistory() {
        post = new Post();
        user = new User();
    }

    @Test
    public void setLikeUserId() {
        String user_id = "1232superID";
        post.setUserId(user_id);
        assertEquals(user_id, post.getUserId());
    }


    @Test
    public void setPostDescription() {
        String description = "Description";
        post.setmDescription(description);
        assertEquals(description, post.getmDescription());
    }

    @Test
    public void setPost_FoodImageUrl() {
        String foodImageUrl = "FoodImageUrl.com";
        post.setmFoodImgUrl(foodImageUrl);
        assertEquals(foodImageUrl, post.getmFoodImgUrl());
    }

    @Test
    public void setUser_Recipe() {
        String recipe = " Its my great cooking Recipe";
        post.setmRecipe(recipe);
        assertEquals(recipe, post.getmRecipe());
    }

    @Test
    public void setUser_Ingredients() {
        String ingredients = "Ingredients list of stuff";
        post.setmIngredients(ingredients);
        assertEquals(ingredients, post.getmIngredients());
    }

    @Test
    public void setUser_date_created() {
        String dateCreated = "User. My. Name. Is";
        post.setDate_created(dateCreated);
        assertEquals(dateCreated, post.getDate_created());
    }

    @Test
    public void setUser_postId() {
        String post_Id = "this_is_my_post_id";
        post.setPostId(post_Id);
        assertEquals(post_Id, post.getPostId());
    }


    @Test
    public void setUser_Constructor() {
        String user_id = "1232superID";
        String incorrect_uid = "IncorrectUID";
        String description = "Description";
        String foodImageUrl = "FoodImageUrl.com";
        String recipe = " Its my great cooking Recipe";
        String dateCreated = "User. My. Name. Is";
        String post_Id = "this_is_my_post_id";
        String ingredients = "Ingredients list of stuff";

        post = new Post(description, foodImageUrl, recipe, ingredients, user_id, post_Id, dateCreated, likes);
        post.setUser(user);

        assertNotEquals(incorrect_uid, post.getUserId());

        assertEquals(user_id, post.getUserId());
        assertEquals(description, post.getmDescription());
        assertEquals(foodImageUrl, post.getmFoodImgUrl());
        assertEquals(recipe, post.getmRecipe());
        assertEquals(ingredients, post.getmIngredients());
        assertEquals(dateCreated, post.getDate_created());
        assertEquals(post_Id, post.getPostId());
        assertEquals(user, post.getUser());

        assertFalse(post.toString().contains(incorrect_uid));

        //Now changing the currently existing Post's Description
        post.setmDescription("different Description");
        assertNotEquals(post.getmDescription(), description);

    }

    @Test
    public void constructorWithEmptyDescription() {
        String emptyDescription = "";
        String noDescriptionAvailable = "No description available";
        post = new Post(emptyDescription, null, null,
                null, null, null,
                null, null);

        assertNotEquals(emptyDescription, post.getmDescription());
        assertEquals(noDescriptionAvailable, post.getmDescription());
    }

}
