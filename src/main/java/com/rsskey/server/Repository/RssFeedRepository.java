package com.rsskey.server.Repository;

import com.rsskey.server.DAO.Exception.DAOException;
import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.DatabaseUtils.DbConnect;
import com.rsskey.server.Models.RSSFeed;
import com.rsskey.server.Models.RSSFeedItem;
import com.rsskey.server.Models.User;
import com.rsskey.server.Utils.SQLHelper;
import org.springframework.dao.support.DaoSupport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

public class RssFeedRepository extends ARepository<RSSFeed> {

    public RssFeedRepository(DAOFactory dao) {
        super(dao);
    }

    public RSSFeed add(RSSFeed model) {
        String query = "INSERT INTO public.rssfeed(\"Title\", \"Link\", \"Description\", \"Language\", \"Copyright\", \"Pubdate\") VALUES (?, ?, ?, ?, ?, ?)";
        RSSFeed DBModel = null;
        ArrayList<Long> result = null;
        RssFeedItemRepository repoRssItem = DAOFactory.getInstance().getRssFeedItemRepository();

        try {
            Timestamp ts = new Timestamp(new Date().getTime());

            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,true, "RssID", model.getTitle(), model.getLink(), model.getDescription(), model.getLanguage(), model.getCopyright(), ts);
            System.out.println("Result of the query " + result);
            if (result != null && result.size() > 0 && (DBModel = this.findbyID(result.get(0))) != null) {
                System.out.println("Result of the DBModel " + DBModel);
                for (RSSFeedItem rss: model.getItems()
                     ) {
                    rss.setRssID(DBModel.getID());
                    RSSFeedItem item = repoRssItem.add(rss);
                    DBModel.items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return DBModel;
    }

    @Override
    public Boolean delete(Long ID) throws DAOException {
        Boolean toreturn = false;
        ArrayList<Long>  result = null;
        String query = "DELETE FROM public.rssfeed WHERE \"RssID\"=?";

        try {
            result = SQLHelper.executeNonQuery(this.daoFactory.getConnection(),query,false, null, ID);
            if (result != null && result.size() > 0)
                toreturn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    @Override
    public RSSFeed findbyID(Long ID) throws DAOException {
        RSSFeed feed = new RSSFeed();
        RSSFeed newFeed = null;
        String query = "SELECT * FROM public.rssfeed where \"RssID\"=?";
        System.out.println("Find RssFeed by Id" + ID);
        try {
            newFeed = (RSSFeed)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, feed ,ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("newFeed" + newFeed);
        return newFeed;
    }

    /*public List<RSSFeed> getAllFeeds() throws DAOException {
        RSSFeed feed = new RSSFeed();
        RSSFeed newFeed = null;
        String query = "SELECT * FROM public.rssfeed where \"RssID\"=?";

        try {
            newFeed = (RSSFeed)SQLHelper.executeQuery(this.daoFactory.getConnection(),query,false, feed ,ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFeed;
    }*/

    public RSSFeed update(RSSFeed model) {
        RSSFeed updatedModel = null;

        try {
            /*Statement state = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String query = "UPDATE public.users SET \"Title\"=?, \"Link\"=?, \"Description\"=?, \"Language\"=?, \"Copyright\"=?, \"Pubdate\"=? WHERE \"RssID\"=?";
            //On crée l'objet avec la requête en paramètre
            PreparedStatement prepare = this.connect.prepareStatement(query);

            prepare.setString(1, model.getTitle());
            prepare.setString(2, model.getLink());
            prepare.setString(3, model.getDescription());
            prepare.setString(4, model.getLanguage());
            prepare.setString(5, model.getCopyright());
            Date date= new Date();

            long time = date.getTime();
            System.out.println("Time in Milliseconds: " + time);

            Timestamp ts = new Timestamp(time);

            prepare.setTimestamp(6, ts);
            prepare.setInt(7, model.getID());

            ResultSet res =  prepare.executeQuery();
            while(res.next()){
                updatedModel = this.findbyID(model.getID());
            }

            res.close();
            prepare.close();
            state.close();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedModel;
    }

    public List<RSSFeed> getRSSFeed(long IDUser)
    {
        ArrayList<Long> RssIDS;
        ArrayList<RSSFeed> rssFeeds = new ArrayList<>();
        try {
            RssIDS = DAOFactory.getInstance().getSuscriberRepository().getRSSFeed(IDUser);

            for (Long id: RssIDS) {
                RSSFeed rss = this.findbyID(id);
                if (rss != null)
                    rssFeeds.add(rss);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssFeeds;
    }
}
