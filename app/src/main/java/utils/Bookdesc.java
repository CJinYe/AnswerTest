package utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import bean.ExercisesBean;

/**
 * Created by jlfxs on 2016/11/21.
 */

public class Bookdesc {

    public static List<ExercisesBean> readXML(InputStream inputstream, String inputEncoding)
            throws XmlPullParserException, IOException {
        List<ExercisesBean> persons = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputstream, inputEncoding);
        int eventCode = parser.getEventType();

        ExercisesBean person = null;
        while (eventCode != XmlPullParser.END_DOCUMENT) {
            switch (eventCode) {
                case XmlPullParser.START_DOCUMENT:
                    persons = new ArrayList<ExercisesBean>();
                    break;
                case XmlPullParser.START_TAG:

                    if ("item".equals(parser.getName())) {

                        person = new ExercisesBean();
                        if ("name".equals(parser.getName())) {
                            person.name=parser.nextText();
                        }
                        if ("size".equals(parser.getName())) {
                            person.size=Integer.parseInt(parser.nextText());
                        }
                        if ("note".equals(parser.getName())) {
                            person.note=Integer.parseInt(parser.nextText());
                        }

                    }else if(person != null){
                        if ("id".equals(parser.getName())) {
                            person.id=Integer.parseInt(parser.nextText());
                        }else if("subject".equals(parser.getName())){
                            person.subject = parser.nextText();
                        }else if("analysis".equals(parser.getName())){
                            person.analysis=parser.nextText();
                        }
                        else if("score".equals(parser.getName())){
                            person.score=parser.nextText();
                        }
                        else if("type".equals(parser.getName())){
                            person.type=Integer.parseInt(parser.nextText());
                        }
                        else if("answer".equals(parser.getName())){
                            person.answer=parser.nextText();
                        }

                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("item".equals(parser.getName()) && person!= null) {
                        persons.add(person);
                        person =null;
                    }
                    break;
                default:
                    break;
            }
            eventCode = parser.next();
        }

        return persons;
    }
}
