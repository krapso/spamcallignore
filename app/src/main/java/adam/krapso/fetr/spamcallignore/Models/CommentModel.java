package adam.krapso.fetr.spamcallignore.Models;

import org.jsoup.nodes.Element;

/**
 * Created by krapso on 20-Jan-18.
 */

public class CommentModel {
    public String comment;
    public int rating;
    public String status;
    private Element element;

    public CommentModel(Element element){
        this.element = element;
        init();
    }

    private void init(){
        comment = getStringFromFirstElementByClassName("text");
        status = getStringFromFirstElementByClassName("status");
        rating = Integer.parseInt(getStringFromFirstElementByClassName("ratingSum"));
    }

    public String getStringFromFirstElementByClassName(String className){
        Element firstElement = element.getElementsByClass(className).first();
        String outputString = null;
        if(firstElement!=null){
            outputString = firstElement.text();
        }
        return outputString;
    }

}
