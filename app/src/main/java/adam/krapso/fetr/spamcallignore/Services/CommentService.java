package adam.krapso.fetr.spamcallignore.Services;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import adam.krapso.fetr.spamcallignore.Models.CommentModel;
import adam.krapso.fetr.spamcallignore.Receivers.IncomingCallReceiver;

/**
 * Created by krapso on 20-Jan-18.
 */

public class CommentService {
    private String number;
    private List<CommentModel> listOfAllComments;
    private Handler mainHandler;
    public CommentService(String number, Handler mainHandler){
        this.number = number;
        this.mainHandler = mainHandler;
    }

    public class InitTask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... arg0) {
            boolean loading = false;
            listOfAllComments = new ArrayList<>();
            String url = String.format("https://www.site.com/%s/",number);
            try {
                Document doc = Jsoup.connect(url).get();
                Elements content = doc.getElementsByClass("komentare_vypis");
                if(content.size()>0){
                    Elements elements = content.first().getElementsByClass("komentar");
                    for(Element element : elements){
                        listOfAllComments.add(new CommentModel(element));
                    }
                }
                loading = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return loading;
        }

        protected void onPostExecute(Boolean result) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString(IncomingCallReceiver.ISLOADED,result.toString());
            msg.setData(bundle);
            mainHandler.sendMessage(msg);
        }
    }

    public CommentModel getMostRatedComment(){
        CommentModel commentModel = null;
        if(listOfAllComments.size()>0){
            for(CommentModel comment : listOfAllComments){
                if(commentModel == null){
                    commentModel = comment;
                }else{
                    if(commentModel.rating<comment.rating){
                        commentModel = comment;
                    }
                }
            }
        }
        return commentModel;
    }

    public List<String> getAllCommentsSortByMostUsedStatuses(){
        List<String> mostUsedStatuses = new ArrayList<>();
        HashMap<String,Integer> hashMap = new HashMap<>();
        if(listOfAllComments.size()>0) {
            for (CommentModel commentModel : listOfAllComments) {
                if (hashMap.containsKey(commentModel.status)) {
                    hashMap.put(commentModel.status, hashMap.get(commentModel.status) + 1);
                } else {
                    hashMap.put(commentModel.status, 1);
                }
            }
            hashMap = new HashMap<>(sortByValue(hashMap));
            mostUsedStatuses.addAll(hashMap.keySet());
            Collections.reverse(mostUsedStatuses);
        }
        return mostUsedStatuses;
    }

    private static <K, V> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                return ((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue()).compareTo(((Map.Entry<K, V>) (o2)).getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext();) {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) it.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}
