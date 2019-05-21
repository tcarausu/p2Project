package com.example.myapplication;

import android.os.Parcel;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.myapplication.models.Like;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * File created by tcarau18
 **/
@RunWith(AndroidJUnit4.class)
@SmallTest
public class PostParcelableTestClass {
    private Post post;
    private List<Like> likes;

    @Before
    public void createPostModel() {
        post = new Post();
    }

    /**
     * This Test checks for the correct input present in the Parcel based on the constructor.
     * During the test, creating + testing we realised that the Parcel class/methods,
     * have to be generated again after making changes to the model.
     *
     * This helped us restructure the Parcel correctly.
     */
    @Test
    public void Post_Model_ParcelableWriteRead() {
        //setup variables
        String description = "Description";
        String foodImageUrl = "FoodImageUrl.com";
        String recipe = " Its my great cooking Recipe";
        String ingredients = "Ingredients list of stuff";
        String user_id = "1232superID";
        String post_Id = "this_is_my_post_id";
        String dateCreated = "date created";

        String incorrect_uid = "IncorrectUID";

        post = new Post(description, foodImageUrl, recipe, ingredients, user_id, post_Id, dateCreated, likes);
        // Write the data
        Parcel parcel = Parcel.obtain();
        post.writeToParcel(parcel, post.describeContents());

        // After you're done with writing, you need to reset the parcel for reading.
        parcel.setDataPosition(0);

        // Read the data
        Post createdFromParcel = Post.CREATOR.createFromParcel(parcel);
        // Verify that the received data is correct.
        assertEquals(createdFromParcel.getmDescription(), description);
        assertEquals(createdFromParcel.getmFoodImgUrl(),foodImageUrl);
        assertEquals(createdFromParcel.getmRecipe(),recipe);
        assertEquals( createdFromParcel.getmIngredients(),ingredients);
        assertEquals(createdFromParcel.getUserId(),user_id);
        assertEquals(createdFromParcel.getPostId(), post_Id);
        assertEquals(createdFromParcel.getDate_created(),dateCreated);
        assertEquals(createdFromParcel.getLikes(), likes);
        assertNotEquals(createdFromParcel.getUserId(),incorrect_uid);

    }
}
