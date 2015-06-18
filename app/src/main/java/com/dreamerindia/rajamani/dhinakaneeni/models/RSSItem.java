package com.dreamerindia.rajamani.dhinakaneeni.models;

/**
 * Created by Rajamani on 6/16/2015.
 */
public class RSSItem {
    // All <item> node name
    String _title;
    String _link;
    String _description;
    String _category;
    String _pubdate;
    String _guid;
    String _feedburnerOrigLink;

    // constructor
    public RSSItem() {

    }

    // constructor with parameters
    public RSSItem(String title, String link, String description, String category, String pubdate,
                   String guid, String feedburnerOrigLink) {
        this._title = title;
        this._link = link;
        this._description = description;
        this._category = category;
        this._pubdate = pubdate;
        this._guid = guid;
        this._feedburnerOrigLink = feedburnerOrigLink;
    }

    /**
     * All SET methods
     */
    public void setTitle(String title) {
        this._title = title;
    }

    public void setLink(String link) {
        this._link = link;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public void set_category(String category) {
        this._category = category;
    }

    public void setPubdate(String pubDate) {
        this._pubdate = pubDate;
    }


    public void setGuid(String guid) {
        this._guid = guid;
    }

    public void set_feedburnerOrigLink(String feedburnerOrigLink) {
        this._feedburnerOrigLink = feedburnerOrigLink;
    }

    /**
     * All GET methods
     */
    public String getTitle() {
        return this._title;
    }

    public String getLink() {
        return this._link;
    }

    public String getDescription() {
        return this._description;
    }

    public String get_category() {
        return this._category;
    }

    public String getPubdate() {
        return this._pubdate;
    }

    public String getGuid() {
        return this._guid;
    }

    public String get_feedburnerOrigLink() {
        return this._feedburnerOrigLink;
    }


}
