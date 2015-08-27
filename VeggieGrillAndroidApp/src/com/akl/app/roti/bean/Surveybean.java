/**
 * 
 */
package com.akl.app.roti.bean;


/**
 * @author MehrozKarim
 * 
 */
public class Surveybean {

    String id;
    String title;
    Questions questions[];

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the questions
     */
    public Questions[] getQuestions() {
        return questions;
    }

    /**
     * @param questions
     *            the questions to set
     */
    public void setQuestions(Questions[] questions) {
        this.questions = questions;
    }


}
