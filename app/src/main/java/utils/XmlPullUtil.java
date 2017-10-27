package utils;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import bean.ExercisesBean;
import bean.QuestionAnswer;
import bean.QuestionChoice;
import bean.QuestionFilling;
import bean.QuestionLigature;
import bean.QuestionMoreChoice;
import bean.QuestionTwoChoice;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 13:57
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class XmlPullUtil {
    /**
     * 解析选择题数据
     *
     * @return
     */
    public static List<QuestionChoice> PullParserChoice(InputStream is) {
        try {
            List<QuestionChoice> list = null;
            QuestionChoice questionChoice = null;
            //            InputStream is = getAssets().open("choiceDemo.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("questionsChoice")) {
                            questionChoice = new QuestionChoice();
                        } else if (parser.getName().equals("idChoice")) {
                            eventType = parser.next();
                            questionChoice.idChoice = Integer.parseInt(parser.getText());
                        } else if (parser.getName().equals("questionChoice")) {
                            eventType = parser.next();
                            questionChoice.questionChoice = parser.getText();
                        } else if (parser.getName().equals("answerA")) {
                            eventType = parser.next();
                            questionChoice.answerA = parser.getText();
                        } else if (parser.getName().equals("answerB")) {
                            eventType = parser.next();
                            questionChoice.answerB = parser.getText();
                        } else if (parser.getName().equals("answerC")) {
                            eventType = parser.next();
                            questionChoice.answerC = parser.getText();
                        } else if (parser.getName().equals("answerD")) {
                            eventType = parser.next();
                            questionChoice.answerD = parser.getText();
                        } else if (parser.getName().equals("answerChoice")) {
                            eventType = parser.next();
                            questionChoice.answerChoice = parser.getText();
                        } else if (parser.getName().equals("explainationChoice")) {
                            eventType = parser.next();
                            questionChoice.explainationChoice = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("questionsChoice")) {
                            list.add(questionChoice);
                            questionChoice = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    /**
     * 解析双选题数据
     *
     * @return
     */
    public static List<QuestionTwoChoice> PullParserTwoChoice(InputStream is) {
        try {
            List<QuestionTwoChoice> list = null;
            QuestionTwoChoice questionTwoChoice = null;
            //            InputStream is = getAssets().open("choiceDemo.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("questionsTwoChoice")) {
                            questionTwoChoice = new QuestionTwoChoice();
                        } else if (parser.getName().equals("idTwoChoice")) {
                            eventType = parser.next();
                            questionTwoChoice.idTwoChoice = Integer.parseInt(parser.getText());
                        } else if (parser.getName().equals("questionTwoChoice")) {
                            eventType = parser.next();
                            questionTwoChoice.questionTwoChoice = parser.getText();
                        } else if (parser.getName().equals("answerTwoA")) {
                            eventType = parser.next();
                            questionTwoChoice.answerTwoA = parser.getText();
                        } else if (parser.getName().equals("answerTwoB")) {
                            eventType = parser.next();
                            questionTwoChoice.answerTwoB = parser.getText();
                        } else if (parser.getName().equals("answerTwoC")) {
                            eventType = parser.next();
                            questionTwoChoice.answerTwoC = parser.getText();
                        } else if (parser.getName().equals("answerTwoD")) {
                            eventType = parser.next();
                            questionTwoChoice.answerTwoD = parser.getText();
                        } else if (parser.getName().equals("answerTwoChoice")) {
                            eventType = parser.next();
                            questionTwoChoice.answerTwoChoice = parser.getText();
                        } else if (parser.getName().equals("explainationTwoChoice")) {
                            eventType = parser.next();
                            questionTwoChoice.explainationTwoChoice = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("questionsTwoChoice")) {
                            list.add(questionTwoChoice);
                            questionTwoChoice = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    /**
     * 解析双选题数据
     *
     * @return
     */
    public static List<QuestionMoreChoice> PullParserMoreChoice(InputStream is) {
        try {
            List<QuestionMoreChoice> list = null;
            QuestionMoreChoice questionMoreChoice = null;
            //            InputStream is = getAssets().open("choiceDemo.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("questionsMoreChoice")) {
                            questionMoreChoice = new QuestionMoreChoice();
                        } else if (parser.getName().equals("idMoreChoice")) {
                            eventType = parser.next();
                            questionMoreChoice.idMoreChoice = Integer.parseInt(parser.getText());
                        } else if (parser.getName().equals("questionMoreChoice")) {
                            eventType = parser.next();
                            questionMoreChoice.questionMoreChoice = parser.getText();
                        } else if (parser.getName().equals("answerMoreA")) {
                            eventType = parser.next();
                            questionMoreChoice.answerMoreA = parser.getText();
                        } else if (parser.getName().equals("answerMoreB")) {
                            eventType = parser.next();
                            questionMoreChoice.answerMoreB = parser.getText();
                        } else if (parser.getName().equals("answerMoreC")) {
                            eventType = parser.next();
                            questionMoreChoice.answerMoreC = parser.getText();
                        } else if (parser.getName().equals("answerMoreD")) {
                            eventType = parser.next();
                            questionMoreChoice.answerMoreD = parser.getText();
                        } else if (parser.getName().equals("answerMoreChoice")) {
                            eventType = parser.next();
                            questionMoreChoice.answerMoreChoice = parser.getText();
                        } else if (parser.getName().equals("explainationMoreChoice")) {
                            eventType = parser.next();
                            questionMoreChoice.explainationMoreChoice = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("questionsMoreChoice")) {
                            list.add(questionMoreChoice);
                            questionMoreChoice = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 解析连线题数据
     *
     * @return
     */
    public static List<QuestionLigature> PullParserLigature(InputStream is) {
        try {
            List<QuestionLigature> list = null;
            QuestionLigature questionMoreChoice = null;
            //            InputStream is = getAssets().open("choiceDemo.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("questionsLigature")) {
                            questionMoreChoice = new QuestionLigature();
                        } else if (parser.getName().equals("idLigature")) {
                            eventType = parser.next();
                            questionMoreChoice.idLigature = Integer.parseInt(parser.getText());
                        } else if (parser.getName().equals("questionLigatureLeft")) {
                            eventType = parser.next();
                            questionMoreChoice.questionLigatureLeft = parser.getText();
                        } else if (parser.getName().equals("questionLigatureRight")) {
                            eventType = parser.next();
                            questionMoreChoice.questionLigatureRight = parser.getText();
                        } else if (parser.getName().equals("answerLigature")) {
                            eventType = parser.next();
                            questionMoreChoice.answerLigature = parser.getText();
                        } else if (parser.getName().equals("explainationLigature")) {
                            eventType = parser.next();
                            questionMoreChoice.explainationLigature = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("questionsLigature")) {
                            list.add(questionMoreChoice);
                            questionMoreChoice = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    /**
     * 解析填空题数据
     *
     * @return
     */
    public static List<QuestionFilling> PullParserFilling(InputStream is) {
        try {
            List<QuestionFilling> list = null;
            QuestionFilling questionFilling = null;
            //            InputStream is = getAssets().open("choiceDemo.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("questionsFilling")) {
                            questionFilling = new QuestionFilling();
                        } else if (parser.getName().equals("idFilling")) {
                            eventType = parser.next();
                            questionFilling.idFilling = Integer.parseInt(parser.getText());
                        } else if (parser.getName().equals("questionFilling")) {
                            eventType = parser.next();
                            questionFilling.questionFilling = parser.getText();
                        } else if (parser.getName().equals("answerFilling")) {
                            eventType = parser.next();
                            questionFilling.answerFilling = parser.getText();
                        } else if (parser.getName().equals("explainationFilling")) {
                            eventType = parser.next();
                            questionFilling.explainationFilling = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("questionsFilling")) {
                            list.add(questionFilling);
                            questionFilling = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    /**
     * 解析作答题数据
     *
     * @return
     */
    public static List<QuestionAnswer> PullParserAnswer(InputStream is) {
        try {
            List<QuestionAnswer> list = null;
            QuestionAnswer questionAnswer = null;
            //            InputStream is = getAssets().open("choiceDemo.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("questionsAnswer")) {
                            questionAnswer = new QuestionAnswer();
                        } else if (parser.getName().equals("idAnswer")) {
                            eventType = parser.next();
                            questionAnswer.idAnswer = Integer.parseInt(parser.getText());
                        } else if (parser.getName().equals("questionAnswer")) {
                            eventType = parser.next();
                            questionAnswer.questionAnswer = parser.getText();
                        } else if (parser.getName().equals("answerAnswer")) {
                            eventType = parser.next();
                            questionAnswer.answerAnswer = parser.getText();
                        } else if (parser.getName().equals("explainationAnswer")) {
                            eventType = parser.next();
                            questionAnswer.explainationAnswer = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("questionsAnswer")) {
                            list.add(questionAnswer);
                            questionAnswer = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 解析作答题数据
     *
     * @return
     */
    public static List<ExercisesBean> readXML(InputStream inputstream, String inputEncoding) {
        try {
            List<ExercisesBean> persons = null;
            ExercisesBean person = null;
            //            InputStream is = getAssets().open("choiceDemo.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputstream, "gb2312");
            int eventType = parser.getEventType();
            String name ="";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        persons = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("name")) {
                            name = parser.nextText();
                        }
                        if (parser.getName().equals("item")) {
                            person = new ExercisesBean();
                        } else if (parser.getName().equals("id")) {
                            eventType = parser.next();
                            person.id = Integer.parseInt(parser.getText());
                        } else if (parser.getName().equals("subject")) {
                            eventType = parser.next();
                            person.subject = parser.getText();
                        }else if (parser.getName().equals("alysis1")) {
                            eventType = parser.next();
                            person.alysis1 = parser.getText();
                        } else if (parser.getName().equals("analysis")) {
                            eventType = parser.next();
                            person.analysis = parser.getText();
                        } else if (parser.getName().equals("score")) {
                            eventType = parser.next();
                            person.score = parser.getText();
                        } else if (parser.getName().equals("type")) {
                            eventType = parser.next();
                            person.type = Integer.parseInt(parser.getText());
                        } else if (parser.getName().equals("sound")) {
                            eventType = parser.next();
                            person.sound = parser.getText();
                        } else if (parser.getName().equals("answer")) {
                            eventType = parser.next();
                            person.answer = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            person.name = name;
                            persons.add(person);
                            person = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            return persons;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity111", "Exception = "+e);
            return new ArrayList<>();
        }
    }


    /**
     * 解析作答题数据
     *
     * @return
     */
    public static List<ExercisesBean> readTestXML(InputStream inputstream, String inputEncoding) {
        try {
            List<ExercisesBean> persons = null;
            ExercisesBean person = null;
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputstream, "gb2312");
            int eventType = parser.getEventType();
            String name ="";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        persons = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("name")) {
                            name = parser.nextText();
                        }
                        if (parser.getName().equals("item")) {
                            person = new ExercisesBean();
                        }else if (parser.getName().equals("h1")) {
                            eventType = parser.next();
                            person.h1 = parser.getText();
                        }else if (parser.getName().equals("h2")) {
                            eventType = parser.next();
                            person.h2 = parser.getText();
                        }else if (parser.getName().equals("h3")) {
                            eventType = parser.next();
                            person.h3 = parser.getText();
                        }else if (parser.getName().equals("h4")) {
                            eventType = parser.next();
                            person.h4 = parser.getText();
                        }else if (parser.getName().equals("h5")) {
                            eventType = parser.next();
                            person.h5 = parser.getText();
                        }else if (parser.getName().equals("alysis1")) {
                            eventType = parser.next();
                            person.alysis1 = parser.getText();
                        } else if (parser.getName().equals("id")) {
                            eventType = parser.next();
                            person.id = Integer.parseInt(parser.getText());
                        } else if (parser.getName().equals("subject")) {
                            eventType = parser.next();
                            person.subject = parser.getText();
                        } else if (parser.getName().equals("analysis")) {
                            eventType = parser.next();
                            person.analysis = parser.getText();
                        } else if (parser.getName().equals("score")) {
                            eventType = parser.next();
                            person.score = parser.getText();
                        } else if (parser.getName().equals("type")) {
                            eventType = parser.next();
                            person.type = Integer.parseInt(parser.getText());
                        } else if (parser.getName().equals("sound")) {
                            eventType = parser.next();
                            person.sound = parser.getText();
                        } else if (parser.getName().equals("answer")) {
                            eventType = parser.next();
                            person.answer = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            person.name = name;
                            persons.add(person);
                            person = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            return persons;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity111", "Exception = "+e);
            return new ArrayList<>();
        }
    }
}
