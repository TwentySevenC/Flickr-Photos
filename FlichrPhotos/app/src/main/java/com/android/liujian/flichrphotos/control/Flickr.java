package com.android.liujian.flichrphotos.control;

import android.net.Uri;
import android.util.Log;

import com.android.liujian.flichrphotos.model.Comment;
import com.android.liujian.flichrphotos.model.Gallery;
import com.android.liujian.flichrphotos.model.Group;
import com.android.liujian.flichrphotos.model.People;
import com.android.liujian.flichrphotos.model.Photo;
import com.android.liujian.flichrphotos.model.Photoset;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujian on 15/9/11.
 *
 * A flickr class for
 */
public class Flickr {
    private static final String TAG = "Flickr";

    /**Singleton uniqueInstance*/
    private static Flickr mFlickr = null;

    /**
     * Flickr id
     */
    private static final String mFlickrId = "66956608@N06";

    /**
     * Some necessary information to get photo from the flickr
     */

    /**
     * The flickr endpoint
     */
    private static final String END_POINT = "https://api.flickr.com/services/rest/";

    /**
     * The api key for developer
     */
    private static final String API_KEY = "ec5e798b72197d6b6607e5fcc969b766";

    /**
     * The output format xml(reset)
     */
    private static final String OUTPUT_FORMAT = "rest";

    /**
     * Small photo source url
     */
    private static final String EXTRA_SMALL_URL = "url_s";

    /**
     * The photo source url - size suffixes
     */
    private static final String EXTRA_MEDIUM_URL = "url_c";

    /**
     * Original photo source url
     */
    private static final String EXTRA_ORIGINAL_URL = "url_o";

    /**
     * The method to get the interesting photos from flickr
     */
    private static final String INTERESTING_METHOD = "flickr.interestingness.getList";

    /**
     * The method to search photos
     */
    private static final String SEARCH_PHOTO_METHOD = "flickr.photos.search";


    /**
     * The method to get group pool photos
     */
    private static final String GROUP_POOL_METHOD = "flick.groups.pools.getPhotos";


    /**
     * The method to search groups
     */
    private static final String SEARCH_GROUPS_METHOD = "flickr.groups.search";

    /**
     * The method to search people
     */
    private static final String SEARCH_PEOPLE_METHOD = "fickr.people.findByUsername";

    /**
     * The method to get people info
     */
    private static final String PEOPLE_INFO_METHOD = "flickr.people.getInfo";

    /**
     * The method to get gallery photo
     */
    private static final String GALLERIES_PHOTO_METHOD = "flickr.galleries.getPhotos";

    /**
     * The method to get gallery list
     */
    private static final String GALLERIES_LIST_METHOD = "flickr.galleries.getList";

    /**
     * The Method to get photos from a user
     */
    private static final String PEOPLE_PHOTOS_METHOD = "flickr.people.getPublicPhotos";

    /**
     * The method to get photo set from a user
     */
    private static final String PHOTOSET_LIST_METHOD = "flickr.photosets.getList";

    /**
     * The method to get a photo set's photos
     */
    private static final String PHOTOSET_PHOTOS_METHOD = "flickr.photosets.getPhotos";

    /**
     * The method to get a photo's comments
     */
    private static final String PHOTO_COMMENT_METHOD = "flickr.photos.comments.getList";

    /**
     * The method to get a photo's fav count
     */
    private static final String PHOTO_FAVOURITE_METHOD = "flickr.photos.getFavorites";

    /**
     * The method to get photo's information
     */
    private static final String PHOTO_INFORMATION_METHOD = "flickr.photos.getInfo";

    /**
     * The method to get people's favourite photos
     */
    private static final String FAVOURITE_PHOTO_METHOD = "flickr.favourites.getList";


    /**
     * The user api_key
     */
    private String mApiKey = API_KEY;


    /**
     * The constructor
     */
    private Flickr(){
        //default constructor
    }

    /**
     * The constructor
     * @param api_key user api_key
     */
    private Flickr(String api_key){
        mApiKey = api_key;
    }


    /**
     * A static method to get flickr instance
     */
    public static Flickr getInstance(){
        if(mFlickr == null){
            mFlickr = new Flickr();
        }
        return mFlickr;
    }


    /**
     * A static method with parameters to get flickr instance
     */
    public static Flickr getInstance(String apiKey){
        if(mFlickr == null){
            mFlickr = new Flickr(apiKey);
        }

        return mFlickr;
    }

    /**
     * Get interesting photos
     * @return a list photos
     */
    public ArrayList<Photo> getInterestingPhotos(){
        /**Create a url*/
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", INTERESTING_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("extras", EXTRA_MEDIUM_URL)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .build().toString();

        return FlickrUtils.fetchPhotos(_url);
    }


    /**
     * A method to search photos with a query string
     * @param queryString  query text
     * @return a list photos
     */
    public ArrayList<Photo> searchPhotos(String queryString){
        if(queryString == null) return null;

        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", SEARCH_PHOTO_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("extras", EXTRA_MEDIUM_URL)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .appendQueryParameter("text", queryString)
                .build().toString();

        return FlickrUtils.fetchPhotos(_url);
    }

    /**
     * A method to search groups with a query string
     * @param queryString a topic that what you are searching
     * @return a list groups
     */
    public ArrayList<Group> searchGroups(String queryString){
        if(queryString == null) return null;

        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", SEARCH_GROUPS_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("extras", EXTRA_MEDIUM_URL)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .appendQueryParameter("text", queryString)
                .build().toString();

        return FlickrUtils.fetchGroups(_url);
    }

    public ArrayList<Photo> getGroupPoolPhoto(String groupId){
        if(groupId == null) return null;

        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", GROUP_POOL_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("extras", EXTRA_MEDIUM_URL)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .appendQueryParameter("group_id", groupId)
                .build().toString();

        return FlickrUtils.fetchPhotos(_url);
    }


    /**
     * A method to search galleries with a query string
     * @param galleryId gallery id
     * @return gallery photos
     */
    public ArrayList<Photo> getGalleryPhotos(String galleryId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", GALLERIES_PHOTO_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("extras", EXTRA_ORIGINAL_URL)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .appendQueryParameter("gallery_id", galleryId)
                .build().toString();

        return FlickrUtils.fetchPhotos(_url);
    }


    /**
     * A method to get gallery list
     * @param userId  user id
     * @return gallery list
     */
    public ArrayList<Gallery> getGalleryList(String userId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", GALLERIES_LIST_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("user_id", userId)
                .appendQueryParameter("primary_photo_extras", EXTRA_SMALL_URL)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .build().toString();
        Log.d(TAG, "Get galleries list url: " + _url);
        return FlickrUtils.fetchGalleries(_url);
    }


    /**
     * A method to get flickr's gallery list
     * @return gallery list
     */
    public ArrayList<Gallery> getFlickrGalleries(){
        return getGalleryList(mFlickrId);
    }



    /**
     * Find a people by user name
     * @param username user name
     * @return a people
     */
    public People findPeopleByUsername(String username) throws IOException {

        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", SEARCH_PEOPLE_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("username", username)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .build().toString();


        return findPeopleByUserId(FlickrUtils.parsePeopleId(_url));
    }


    /**
     * Find a people by user id
     * @param userId user id
     * @return a people
     * @throws IOException
     */
    public People findPeopleByUserId(String userId) throws IOException {
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", PEOPLE_INFO_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("user_id", userId)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .build().toString();

        People _people = FlickrUtils.fetchPeople(_url);
        _people.setBuddyicon(FlickrUtils.getBitmapFromUrl(_people.getBuddyiconsUrl()));

        return _people;
    }


    /**
     * A method to get photos from a user
     * @param userId a user id
     * @return a list of photos
     */
    public ArrayList<Photo> getPeoplePhotos(String userId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", PEOPLE_PHOTOS_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .appendQueryParameter("user_id", userId)
                .appendQueryParameter("extras", EXTRA_SMALL_URL)
                .build().toString();
        return FlickrUtils.fetchPhotos(_url);
    }


    /**
     * A method to get photo sets from a user
     * @param userId user id
     * @return a list of photo
     */
    public ArrayList<Photoset> getPhotosetsList(String userId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", PHOTOSET_LIST_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .appendQueryParameter("user_id", userId)
                .appendQueryParameter("primary_photo_extras", EXTRA_SMALL_URL)
                .build().toString();
        return FlickrUtils.fetchPhotosets(_url);
    }


    /**
     * A method to get photoset photos
     * @param photosetId  photo set id
     * @param userId   user id
     * @return a list of photo
     */
    public ArrayList<Photo> getPhotosetsPhotos(String photosetId, String userId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", PHOTOSET_PHOTOS_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .appendQueryParameter("photoset_id", photosetId)
                .appendQueryParameter("user_id", userId)
                .appendQueryParameter("extras", EXTRA_SMALL_URL)
                .build().toString();
        return FlickrUtils.fetchPhotos(_url);
    }


    /**
     * A method to get a photo's comments
     * @param photoId a photo id
     * @return a list of comments
     */
    public ArrayList<Comment> getPhotoComments(String photoId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", PHOTO_COMMENT_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("photo_id", photoId)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .build().toString();
        return FlickrUtils.fetchComments(_url);
    }


    /**
     * A method to a photo's favourite count
     * @param photoId  a photo id
     * @return fav count
     */
    public int getPhotoFavCount(String photoId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", PHOTO_FAVOURITE_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("photo_id", photoId)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .build().toString();
        return FlickrUtils.fetchPhotoFavCount(_url);
    }


    /**
     * A method to get a photo's view count
     * @param photoId a photo id
     * @return view count
     */
    public int getPhotoViewCount(String photoId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", PHOTO_INFORMATION_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("photo_id", photoId)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .build().toString();
        return FlickrUtils.fetchPhotoViewCounts(_url);
    }


    /**
     * A method to get a photo's comment count
     * @param photoId photo count
     * @return comment count
     */
    public int getPhotoCommentCount(String photoId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", PHOTO_INFORMATION_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("photo_id", photoId)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .build().toString();

        Log.d(TAG, "getPhotoCommentCount param : " + _url);
        return FlickrUtils.fetchPhotoCommentCount(_url);
    }


    /**
     * A method to get a user's favourite photos
     * @param userId the user id
     * @return a list of photos
     */
    public ArrayList<Photo> getPeopleFavPhotos(String userId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", FAVOURITE_PHOTO_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("photo_id", userId)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .appendQueryParameter("extras", EXTRA_SMALL_URL)
                .build().toString();
        return FlickrUtils.fetchPhotos(_url);
    }


}
