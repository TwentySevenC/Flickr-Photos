package com.android.liujian.flichrphotos.control;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.liujian.flichrphotos.model.Comment;
import com.android.liujian.flichrphotos.model.Gallery;
import com.android.liujian.flichrphotos.model.Group;
import com.android.liujian.flichrphotos.model.People;
import com.android.liujian.flichrphotos.model.Photo;
import com.android.liujian.flichrphotos.model.Photoset;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lj on 2015/8/14.
 * A specific java class to load bytes from website "www.flickr.com"
 */
public class FlickrUtils {
    private static final String TAG = "FlickrUtils";

    /**
     * Medium size Photo url
     */
    private static final String EXTRA_MEDIUM_URL = "url_c";

    /**
     * Xml photo tag
     */
    private static final String XML_PHOTO = "photo";

    /**
     * Xml group tag
     */
    private static final String XML_GROUP = "group";

    /**
     * Xml gallery tag
     */
    private static final String XML_GALLERY = "gallery";

    /**
     * Xml photo tag
     */
    private static final String XML_PHOTOSET = "photoset";

    /**
     * Xml user tag
     */
    private static final String XML_USER = "user";

    /**
     * Xml person tag
     */
    private static final String XML_PERSON = "person";

    /**
     * Xml username tag
     */
    private static final String XML_USERNAME = "username";

    /**
     * Xml real name tag
     */
    private static final String XML_REALNAME = "realname";

    /**
     * Xml location tag
     */
    private static final String XML_LOCATION = "location";

    /**
     * Xml title tag
     */
    private static final String XML_TITLE = "title";

    /**
     * Xml description tag
     */
    private static final String XML_DESCRIPTION = "description";

    /**
     * Xml primary photo extra tag
     */
    private static final String XML_PRIMARY_PHOTO_EXTRA = "primary_photo_extras";

    /**
     * Xml comment tag
     */
    private static final String XML_COMMMENT = "comment";

    /**
     * Xml comments tag
     */
    private static final String XML_COMMENTS = "comments";


    /**
     * Get a list of photo item
     * @param url photo item
     * @return a photo list
     */
    public static ArrayList<Photo> fetchPhotos(String url){
        Log.d(TAG, "fetchPhotos");
        ArrayList<Photo> _photos = new ArrayList<>();
        try{
            String _xmlString = getUrl(url);
//            Log.i(TAG, "Get the items: " + _xmlString);


            /**Create a xmlPullParser*/
            XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
            XmlPullParser _xmlPullParser = _factory.newPullParser();
            _xmlPullParser.setInput(new StringReader(_xmlString));

            parsePhotos(_photos, _xmlPullParser);                       /**Parser the xml String*/


        } catch (XmlPullParserException e){
            e.printStackTrace();
        } catch (IOException e2) {
            // TODO: handle exception
        }

        return _photos;
    }

    /**
     * Get a list of group
     * @param url group url
     * @return a group list
     */
    public static ArrayList<Group> fetchGroups(String url){

        ArrayList<Group> _groups = new ArrayList<>();
        try{
            String _xmlString = getUrl(url);
//            Log.i(TAG, "Get the items: " + _xmlString);


            /**Create a xmlPullParser*/
            XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
            XmlPullParser _xmlPullParser = _factory.newPullParser();
            _xmlPullParser.setInput(new StringReader(_xmlString));

            parseGroups(_groups, _xmlPullParser);                       /**Parser the xml String*/


        } catch (XmlPullParserException e){
            e.printStackTrace();
        } catch (IOException e2) {
            // TODO: handle exception
        }

        return _groups;
    }

    /**
     * Get a list of galleries
     * @param url  url
     * @return gallery list
     */
    public static ArrayList<Gallery> fetchGalleries(String url){
        ArrayList<Gallery> _galleries = new ArrayList<>();
        try{
            String _xmlString = getUrl(url);


            /**Create a xmlPullParser*/
            XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
            XmlPullParser _xmlPullParser = _factory.newPullParser();
            _xmlPullParser.setInput(new StringReader(_xmlString));

            parseGalleries(_galleries, _xmlPullParser);                       /**Parser the xml String*/


        } catch (XmlPullParserException e){
            e.printStackTrace();
        } catch (IOException e2) {
            // TODO: handle exception
        }

        return _galleries;
    }


    /**
     * A method to get a list of photoset
     * @param url  url
     * @return  a list of photoset
     */
    public static ArrayList<Photoset> fetchPhotosets(String url){
        ArrayList<Photoset> _photosets = new ArrayList<>();
        try{
            String _xmlString = getUrl(url);
//            Log.i(TAG, "Get the items: " + _xmlString);


            /**Create a xmlPullParser*/
            XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
            XmlPullParser _xmlPullParser = _factory.newPullParser();
            _xmlPullParser.setInput(new StringReader(_xmlString));

            parsePhotosets(_photosets, _xmlPullParser);                       /**Parser the xml String*/


        } catch (XmlPullParserException e){
            e.printStackTrace();
        } catch (IOException e2) {
            // TODO: handle exception
        }

        return _photosets;
    }


    /**
     * A method to get a people
     * @param url url
     * @return a people
     */
    public static People fetchPeople(String url){
        People _people = new People();

        try{
            String _xmlString = getUrl(url);
//            Log.i(TAG, "Get the items: " + _xmlString);


            /**Create a xmlPullParser*/
            XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
            XmlPullParser _xmlPullParser = _factory.newPullParser();
            _xmlPullParser.setInput(new StringReader(_xmlString));

            parsePeople(_people, _xmlPullParser);                       /**Parser the xml String*/


        } catch (XmlPullParserException e){
            e.printStackTrace();
        } catch (IOException e2) {
            // TODO: handle exception
        }

        return _people;
    }


    /**
     * A method to a photo's comments
     * @param url url
     * @return a list of comment
     */
    public static ArrayList<Comment> fetchComments(String url){
        ArrayList<Comment> _comments = new ArrayList<>();
        try{
            String _xmlString = getUrl(url);
//            Log.i(TAG, "Get the items: " + _xmlString);


            /**Create a xmlPullParser*/
            XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
            XmlPullParser _xmlPullParser = _factory.newPullParser();
            _xmlPullParser.setInput(new StringReader(_xmlString));

            parseComments(_comments, _xmlPullParser);                       /**Parser the xml String*/


        } catch (XmlPullParserException e){
            e.printStackTrace();
        } catch (IOException e2) {
            // TODO: handle exception
        }

        return _comments;
    }


    /**
     * The method to get a photo's favourite count
     * @param url the url
     * @return fav count
     */
    public static int fetchPhotoFavCount(String url){
        int _favCount = 0;
        try {
            String _xmlString = getUrl(url);

            XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = _factory.newPullParser();
            parser.setInput(new StringReader(_xmlString));

            int _endType = parser.next();

            while(XmlPullParser.END_DOCUMENT != _endType){

                /**Parse a photo's fav count  from xml*/
                if(parser.getEventType() == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())){
                    _favCount = Integer.valueOf(parser.getAttributeValue(null, "total"));
                    break;
                }

                _endType = parser.next();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Photo favourite count: " + _favCount);

        return _favCount;
    }


    /**
     * The method to get a photo's view count
     * @param url url
     * @return view count
     */
    public static int fetchPhotoViewCounts(String url){
        int _viewCount = 0;
        try {
            String _xmlString = getUrl(url);

            XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = _factory.newPullParser();
            parser.setInput(new StringReader(_xmlString));

            int _endType = parser.next();

            while(XmlPullParser.END_DOCUMENT != _endType){

                /**Parse a photo's fav count  from xml*/
                if(parser.getEventType() == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())){
                    _viewCount = Integer.valueOf(parser.getAttributeValue(null, "views"));
                    break;
                }

                _endType = parser.next();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return _viewCount;
    }


    /**
     * The method to get a photo's comment count
     * @param url url
     * @return comment count
     */
    public static int fetchPhotoCommentCount(String url){
        int _commentCount = 0;
        try {
            String _xmlString = getUrl(url);

            XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = _factory.newPullParser();
            parser.setInput(new StringReader(_xmlString));

            int _endType = parser.next();

            while(XmlPullParser.END_DOCUMENT != _endType){

                /**Parse a photo's fav count  from xml*/
                if(parser.getEventType() == XmlPullParser.START_TAG && XML_COMMENTS.equals(parser.getName())){
                    String comments = parser.nextText();
                    Log.d(TAG, "Comments: " + comments);
                    if(comments != null){
                        _commentCount = Integer.valueOf(comments);
                    }else{
                        _commentCount = 0;
                    }
                    break;
                }

                _endType = parser.next();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return _commentCount;
    }

    /**
     * A method to parse a people
     * @param people people
     * @param parser a xmlPullParser
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static void parsePeople(People people, XmlPullParser parser) throws IOException, XmlPullParserException {

        int _endType = parser.next();

        while(XmlPullParser.END_DOCUMENT != _endType){

            /**Parse a people from xml*/
            if(parser.getEventType() == XmlPullParser.START_TAG && XML_PERSON.equals(parser.getName())){
                people.setId(parser.getAttributeValue(null, "nsid"));
                people.setBuddyIconsUrl(parser.getAttributeValue(null, "iconfarm"),
                        parser.getAttributeValue(null, "iconserver"), parser.getAttributeValue(null, "nsid"));

            }else if(parser.getEventType() == XmlPullParser.START_TAG && XML_USERNAME.equals(parser.getName())){
                people.setUserName(parser.nextText());
            }else if(parser.getEventType() == XmlPullParser.START_TAG && XML_REALNAME.equals(parser.getName())){
                people.setRealName(parser.nextText());
            }else if(parser.getEventType() == XmlPullParser.START_TAG && XML_LOCATION.equals(parser.getName())){
                people.setLocation(parser.nextText());
                break;
            }

            _endType = parser.next();
        }
    }


    /**
     * A parser method to parse photo
     * @param items a list
     * @param parser a pull parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static void parsePhotos(ArrayList<Photo> items, XmlPullParser parser) throws XmlPullParserException, IOException{
        Log.d(TAG, "parsePhotos()");
        int _endType = parser.next();

        while(XmlPullParser.END_DOCUMENT != _endType){

            /**Parse a photo item from xml*/
            if(parser.getEventType() == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())){
                Photo _item = new Photo();

                _item.setId(parser.getAttributeValue(null, "id"));

                _item.setUrl(parser.getAttributeValue(null, EXTRA_MEDIUM_URL));

                if(parser.getAttributeValue(null, EXTRA_MEDIUM_URL) != null)
                    Log.d(TAG, parser.getAttributeValue(null, EXTRA_MEDIUM_URL));

                _item.setTitle(parser.getAttributeValue(null, "title"));
                _item.setOwnerId(parser.getAttributeValue(null, "owner"));


                items.add(_item);
            }

            _endType = parser.next();
        }

    }



    /**
     * Get a user id
     * @param url url
     * @return user id
     */
    public static String parsePeopleId(String url){
        String _userId = null;
        try {
            String _xmlString = getUrl(url);

            XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = _factory.newPullParser();
            parser.setInput(new StringReader(_xmlString));

            int _endType = parser.next();

            while(XmlPullParser.END_DOCUMENT != _endType){

                /**Parse a usr id  from xml*/
                if(parser.getEventType() == XmlPullParser.START_TAG && XML_USER.equals(parser.getName())){
                    _userId = parser.getAttributeValue(null, "id");
                    break;
                }

                _endType = parser.next();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return _userId;
    }


    /**
     * A method to parse groups
     * @param groups a group list
     * @param parser a xml pull parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static void parseGroups(ArrayList<Group> groups, XmlPullParser parser) throws IOException, XmlPullParserException {
        int _endType = parser.next();

        while(XmlPullParser.END_DOCUMENT != _endType){

            /**Parse a group item from xml*/
            if(parser.getEventType() == XmlPullParser.START_TAG && XML_GROUP.equals(parser.getName())){
                Group _item = new Group();
                _item.setId(parser.getAttributeValue(null, "id"));
                _item.setName(parser.getAttributeValue(null, "name"));
                _item.setMember(parser.getAttributeValue(null, "members"));
                _item.setTopicCount(parser.getAttributeValue(null, "topic_count"));
                _item.setPoolCount(parser.getAttributeValue(null, "pool_count"));
                groups.add(_item);
            }

            _endType = parser.next();
        }
    }


    /**
     * Parse galleries
     * @param galleries  A gallery arrayList
     * @param parser  a pull parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static void parseGalleries(ArrayList<Gallery> galleries, XmlPullParser parser) throws IOException, XmlPullParserException {
        int _endType = parser.next();
        Gallery _item = new Gallery();

        while(XmlPullParser.END_DOCUMENT != _endType){

            /**Parse a gallery item from xml*/
            if(parser.getEventType() == XmlPullParser.START_TAG && XML_GALLERY.equals(parser.getName())){
                _item.setId(parser.getAttributeValue(null, "id"));
//                _item.setUrl(parser.getAttributeValue(null, "url"));
                _item.setOwnerId(parser.getAttributeValue(null, "owner"));
//                _item.setOwnerName(parser.getAttributeValue(null, "username"));
                _item.setPhotoCount(parser.getAttributeValue(null, "count_photos"));
                _item.setCommentCount(parser.getAttributeValue(null, "count_comments"));
                _item.setViewCount(parser.getAttributeValue(null, "count_views"));
                _item.setUpdatedTime(parser.getAttributeValue(null, "date_update"));

            }else if(parser.getEventType() == XmlPullParser.START_TAG && XML_TITLE.equals(parser.getName())){
                _item.setTitle(parser.nextText());
            }else if(parser.getEventType() == XmlPullParser.START_TAG && XML_DESCRIPTION.equals(parser.getName())){
                _item.setDescription(parser.nextText());
            }else if(parser.getEventType() == XmlPullParser.START_TAG && XML_PRIMARY_PHOTO_EXTRA.equals(parser.getName())){
                _item.setPrimaryPhotoUrl(parser.getAttributeValue(null, "url_s"));
                galleries.add(_item);
                _item = new Gallery();
            }

            _endType = parser.next();
        }

    }


    /**
     * Parse a photoset
     * @param photosets  A photoset list
     * @param parser   a xml pull parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static void parsePhotosets(ArrayList<Photoset> photosets, XmlPullParser parser) throws IOException, XmlPullParserException {
        int _endType = parser.next();
        Photoset _item = new Photoset();

        while(XmlPullParser.END_DOCUMENT != _endType){

            /**Parse a photoset item from xml*/
            if(parser.getEventType() == XmlPullParser.START_TAG && XML_PHOTOSET.equals(parser.getName())){
                _item.setId(parser.getAttributeValue(null, "id"));
                _item.setPhotoCount(parser.getAttributeValue(null, "photos"));
                _item.setCommentCount(parser.getAttributeValue(null, "count_comments"));
                _item.setViewCount(parser.getAttributeValue(null, "count_views"));

            }else if(parser.getEventType() == XmlPullParser.START_TAG && XML_TITLE.equals(parser.getName())){
                _item.setTitle(parser.nextText());
            }else if(parser.getEventType() == XmlPullParser.START_TAG && XML_DESCRIPTION.equals(parser.getName())){
                _item.setDescription(parser.nextText());
            }else if(parser.getEventType() == XmlPullParser.START_TAG && XML_PRIMARY_PHOTO_EXTRA.equals(parser.getName())){
                _item.setPrimaryPhotoUrl(parser.getAttributeValue(null, "url_s"));
                photosets.add(_item);
                _item = new Photoset();
            }


            _endType = parser.next();
        }

    }


    /**
     * Parse a photo's comments
     * @param comments an arrayList
     * @param parser a xmlPullParser
     */
    private static void parseComments(ArrayList<Comment> comments, XmlPullParser parser) throws IOException, XmlPullParserException {
        int _endType = parser.next();

        while(XmlPullParser.END_DOCUMENT != _endType){

            /**Parse a group item from xml*/
            if(parser.getEventType() == XmlPullParser.START_TAG && XML_COMMMENT.equals(parser.getName())){
                Comment _item = new Comment();
                _item.setCommentId(parser.getAttributeValue(null, "id"));
                _item.setAuthorId(parser.getAttributeValue(null, "author"));
                _item.setContent(parser.nextText());
                comments.add(_item);
            }

            _endType = parser.next();
        }
    }

    /**
     * Get bytes from a specific url
     * @param urlSpec a url string
     * @return byte array
     * @throws IOException
     */
    private static byte[] getUrlBytes(String urlSpec) throws IOException {
//        Log.d(TAG, "getUrlBytes()");
        URL _url = new URL(urlSpec);
        /**Open a HttpURLConnection*/
        HttpsURLConnection _connection = (HttpsURLConnection)_url.openConnection();
//        Log.d(TAG, "Http ResponseCode: " + _connection.getResponseCode());
        if(_connection.getResponseCode() != HttpsURLConnection.HTTP_OK){
            Log.d(TAG, "HTTP request failed");
            return null;
        }


        ByteArrayOutputStream _out = new ByteArrayOutputStream();
        try {
            InputStream  _in = _connection.getInputStream();

            int _readLength;
            byte[] _bytes = new byte[1024];
            while ((_readLength = _in.read(_bytes)) != -1){
                _out.write(_bytes, 0, _readLength);
            }
            _in.close();
            _out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            _connection.disconnect();
        }

//        Log.d(TAG, "Success to get bytes.");
        return _out.toByteArray();
    }

    /**
     * Get string from a specific url
     * @param urlSpec a specific url
     * @return  a string
     * @throws IOException
     */
    private static String getUrl(String urlSpec) throws IOException{
//        Log.d(TAG, "getUrl()");
        byte[] bytes = getUrlBytes(urlSpec);
        return new String(bytes);
    }


    /**
     * Return a bitmap from a certain url
     * @param url a photo's url
     * @return a bitmap
     * @throws IOException
     */
    public static Bitmap getBitmapFromUrl(String url) throws IOException {
        byte[] _byteArray = getUrlBytes(url);
        return BitmapFactory.decodeByteArray(_byteArray, 0, _byteArray.length);
    }

}
