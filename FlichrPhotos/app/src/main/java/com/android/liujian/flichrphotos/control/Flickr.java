package com.android.liujian.flichrphotos.control;

import android.net.Uri;

import com.android.liujian.flichrphotos.model.Comment;
import com.android.liujian.flichrphotos.model.Gallery;
import com.android.liujian.flichrphotos.model.Group;
import com.android.liujian.flichrphotos.model.People;
import com.android.liujian.flichrphotos.model.Photo;
import com.android.liujian.flichrphotos.model.Photoset;

import java.util.List;

/**
 * Created by liujian on 15/9/11.
 *
 * A flickr class for
 */
public class Flickr {

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
    public Flickr(){
        //default constructor
    }

    /**
     * The constructor
     * @param api_key user api_key
     */
    public Flickr(String api_key){
        mApiKey = api_key;
    }


    /**
     * Get interesting photos
     * @return a list photos
     */
    public List<Photo> getInterestingPhotos(){
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
    public List<Photo> searchPhotos(String queryString){
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
    public List<Group> searchGroups(String queryString){
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

    public List<Photo> getGroupPoolPhoto(String groupId){
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
    public List<Photo> getGalleryPhotos(String galleryId){
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
    public List<Gallery> getGalleryList(String userId){
        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", GALLERIES_LIST_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .appendQueryParameter("user_id", userId)
                .appendQueryParameter("primary_photo_extras", EXTRA_SMALL_URL)
                .build().toString();
        return FlickrUtils.fetchGalleries(_url);
    }


    /**
     * Find a people by user name
     * @param username user name
     * @return a people
     */
    public People findPeopleByUsername(String username){

        String _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", SEARCH_PEOPLE_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("username", username)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .build().toString();

        String _userId = FlickrUtils.parsePeopleId(_url);

        _url = Uri.parse(END_POINT).buildUpon()
                .appendQueryParameter("method", PEOPLE_INFO_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("user_id", _userId)
                .appendQueryParameter("format", OUTPUT_FORMAT)
                .build().toString();

        return FlickrUtils.fetchPeople(_url);
    }


    /**
     * A method to get photos from a user
     * @param userId a user id
     * @return a list of photos
     */
    public List<Photo> getPeoplePhotos(String userId){
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
    public List<Photoset> getPhotosetsList(String userId){
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
    public List<Photo> getPhotosetsPhotos(String photosetId, String userId){
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
    public List<Comment> getPhotoComments(String photoId){
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

        return FlickrUtils.fetchPhotoCommentCount(_url);
    }


    /**
     * A method to get a user's favourite photos
     * @param userId the user id
     * @return a list of photos
     */
    public List<Photo> getPeopleFavPhotos(String userId){
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